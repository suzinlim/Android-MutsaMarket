package hansung.ac.mutsamarket.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import hansung.ac.mutsamarket.R
import hansung.ac.mutsamarket.databinding.FragmentPostDetailBinding
import hansung.ac.mutsamarket.vo.Post

class PostDetailFragment: Fragment() {
    private var _binding: FragmentPostDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var post: Post

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let{
            post = it.getSerializable("post") as Post
            Log.d("postdetail","showPost: ${post.toString()}")
        }


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPostDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val backBtn = binding.backBtn
        backBtn.setOnClickListener{
            val navController = findNavController()
            navController.navigate(R.id.action_navigation_post_detail_to_navigation_home)
        }
        initView()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("SetTextI18n")
    private fun initView(){
        // 작성자 이름
        val writerTextView = binding.writerText
        writerTextView.text = post.writer

        // 판매 여부
        val isSaleTextView = binding.isSaleText
        if(post.isSale){
            isSaleTextView.text = getString(R.string.post_detail_isSale)
            isSaleTextView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.is_sale))
        }
        else{
            isSaleTextView.text = getString(R.string.post_detail_doneSale)
            isSaleTextView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.done_sale))
        }

        // 게시글 이름
        val titleTextView = binding.titleText
        titleTextView.text = post.title

        // 가격
        val priceTextView = binding.priceText
        priceTextView.text = post.price


        // 내용
        val contentTextView = binding.contentText
        contentTextView.text = post.content
    }
}