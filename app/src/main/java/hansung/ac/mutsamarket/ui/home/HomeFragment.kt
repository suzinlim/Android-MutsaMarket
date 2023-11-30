package hansung.ac.mutsamarket.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
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
//        initList()
    }

    @SuppressLint("NotifyDataSetChanged", "ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        initList()
        initPostRecyclerView()
        binding.allPost.setOnClickListener(){
            binding.allPost.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            binding.allPost.setBackgroundResource(R.drawable.item_bg_on)
            binding.isSale.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            binding.isSale.setBackgroundResource(0)
            binding.isNotSale.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            binding.isNotSale.setBackgroundResource(0)
            homeViewModel.updatePostList()
        }
        binding.isSale.setOnClickListener(){
            binding.allPost.setBackgroundResource(0)
            binding.allPost.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            binding.isSale.setBackgroundResource(R.drawable.item_bg_on)
            binding.isSale.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            binding.isNotSale.setBackgroundResource(0)
            binding.isNotSale.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            homeViewModel.updateIsSalePostList(true)
        }
        binding.isNotSale.setOnClickListener(){
            binding.allPost.setBackgroundResource(0)
            binding.allPost.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            binding.isSale.setBackgroundResource(0)
            binding.isSale.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            binding.isNotSale.setBackgroundResource(R.drawable.item_bg_on)
            binding.isNotSale.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            homeViewModel.updateIsSalePostList(false)
        }

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
        adapter = PostRecyclerViewAdapter(requireContext())
        adapter.dataList = postList
        binding.postRecyclerView.adapter = adapter
        binding.postRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun initList(){
        homeViewModel.updatePostList()
//        homeViewModel.updateIsSalePostList(false)
    }
}