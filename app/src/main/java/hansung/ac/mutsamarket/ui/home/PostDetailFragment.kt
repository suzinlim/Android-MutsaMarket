package hansung.ac.mutsamarket.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import hansung.ac.mutsamarket.ChatRoomActivity
import hansung.ac.mutsamarket.R
import hansung.ac.mutsamarket.databinding.FragmentPostDetailBinding
import hansung.ac.mutsamarket.vo.ChatRoom
import hansung.ac.mutsamarket.vo.Post

class PostDetailFragment: Fragment() {
    private val firestore = FirebaseFirestore.getInstance()

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

        binding.chatButton.setOnClickListener {
            createChatRoom()
            val intent = Intent(requireContext(), ChatRoomActivity::class.java)
            startActivity(intent)
        }

        return root
    }
    private fun createChatRoom() {
        val chatRoom = ChatRoom(writer=post.writer, "대화를 시작해보세요!")

        firestore.collection("ChatRooms")
            .document(chatRoom.chatRoomId)
            .set(chatRoom)
            .addOnSuccessListener {
                Log.d("ChattingFragment", "Chat room created with ID: ${chatRoom.chatRoomId}")

                // 여기에 ChatRoomActivity로 이동하는 코드를 추가
                // ChatRoomActivity에 chatRoomId를 넘겨야 합니다.
            }
            .addOnFailureListener { e ->
                Log.e("ChattingFragment", "Error creating chat room", e)
            }
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