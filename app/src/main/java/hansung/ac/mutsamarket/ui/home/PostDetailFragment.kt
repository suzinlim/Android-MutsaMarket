package hansung.ac.mutsamarket.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}