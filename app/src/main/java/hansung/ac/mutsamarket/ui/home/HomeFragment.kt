package hansung.ac.mutsamarket.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import hansung.ac.mutsamarket.R
import hansung.ac.mutsamarket.databinding.FragmentHomeBinding
import hansung.ac.mutsamarket.vo.Post


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var adapter: PostRecyclerViewAdapter
    private val postList= mutableListOf<Post>()

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        initList()
        initPostRecyclerView()

        homeViewModel.postList.observe(viewLifecycleOwner) { postList ->
            Log.d("fbPostList", postList.toString())
            adapter.dataList = postList.toMutableList()
            adapter.notifyDataSetChanged()
//            updateUI(postList)
        }

        adapter.setOnItemClickListener(object : PostRecyclerViewAdapter.OnItemClickListener {
            override fun onItemClick(post: Post) {
                // 클릭된 항목(post)에 대한 처리를 수행
                // PostDetailFragment로 이동하면서 데이터를 전달
                val navController = findNavController()
                var bundle = Bundle()
                bundle.putSerializable("post", post)
                navController.navigate(R.id.postDetailDestination,bundle)
            }
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun initPostRecyclerView(){
        adapter = PostRecyclerViewAdapter()
        adapter.dataList = postList
        binding.postRecyclerView.adapter = adapter
        binding.postRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun initList(){
        homeViewModel.updatePostList()
//        homeViewModel.updateIsSalePostList(false)
    }

}