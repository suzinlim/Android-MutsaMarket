package hansung.ac.mutsamarket.ui.home

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

    private lateinit var adapter: PostRecyclerViewAdapter
    private val postList= mutableListOf<Post>()

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        Log.d("check error :","#1")
//        initList()
        Log.d("check error :","#2")
//        initPostRecyclerView()
        Log.d("check error :","#3")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        initList()
        initPostRecyclerView()
        Log.d("check error :", "#5")
        val homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        Log.d("check error :", "#6")

        Log.d("check error :", "#7")


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

    private fun initList(){ //임의로 데이터 넣어서 만들어봄
        with(postList){
            add(Post("","title1","10,000","me", "안녕하세요\n반갑습니다!", true))
            add(Post("","title2","20,000","me", "안녕하세요\n반갑습니다!", false))
            add(Post("","title3","30,000","me", "안녕하세요\n반갑습니다!", true))
            add(Post("","title4","40,000","me", "안녕하세요\n반갑습니다!", false))
            add(Post("","title5","50,000","me", "안녕하세요\n반갑습니다!", true))
            add(Post("","title6","60,000","me", "안녕하세요\n반갑습니다!", false))
            add(Post("","title7","70,000","me", "안녕하세요\n반갑습니다!", true))
            add(Post("","title8","80,000","me", "안녕하세요\n반갑습니다!", false))
            add(Post("","title9","90,000","me", "안녕하세요\n반갑습니다!", true))
            add(Post("","title10","100,000","me", "안녕하세요\n반갑습니다!", false))
        }
    }

}