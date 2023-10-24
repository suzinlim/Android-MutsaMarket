package hansung.ac.mutsamarket.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hansung.ac.mutsamarket.databinding.FragmentHomeBinding
import hansung.ac.mutsamarket.vo.Post

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private lateinit var adapter: PostRecyclerViewAdapter
    val postList= mutableListOf<Post>()

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textHome
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }

        val recyclerView: RecyclerView = binding.postRecyclerView

        initList()
        initPostRecyclerView()

        return root
    }

//    override fun onDestroyView() {
////        super.onDestroyView()
////        _binding = null
//    }
    fun initPostRecyclerView(){
        adapter = PostRecyclerViewAdapter()
        adapter.dataList = postList
        binding.postRecyclerView.adapter = adapter
        binding.postRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    fun initList(){ //임의로 데이터 넣어서 만들어봄
        with(postList){
            add(Post("","title1",10000,"me", "판매중"))
            add(Post("","title2",20000,"me", "판매 완료"))
            add(Post("","title3",30000,"me", "판매중"))
            add(Post("","title4",40000,"me", "판매 완료"))
            add(Post("","title5",50000,"me", "판매중"))
            add(Post("","title6",60000,"me", "판매 완료"))
            add(Post("","title7",70000,"me", "판매중"))
            add(Post("","title8",80000,"me", "판매 완료"))
            add(Post("","title9",90000,"me", "판매중"))
            add(Post("","title10",100000,"me", "판매 완료"))
        }
    }

}