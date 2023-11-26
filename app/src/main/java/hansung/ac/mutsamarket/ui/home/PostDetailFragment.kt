//PostDetailFragment.kt
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import hansung.ac.mutsamarket.ChatRoomActivity
import hansung.ac.mutsamarket.R
import hansung.ac.mutsamarket.databinding.FragmentPostDetailBinding
import hansung.ac.mutsamarket.vo.ChatRoom
import hansung.ac.mutsamarket.vo.Post
import java.util.UUID

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
            val chatRoomId = getChatRoomId()
            createChatRoom()
            val intent = Intent(requireContext(), ChatRoomActivity::class.java)
            intent.putExtra("chatRoomId",chatRoomId)
            startActivity(intent)
        }

        return root
    }
    private fun getChatRoomId(): String {
        return UUID.randomUUID().toString()
    }
    private fun createChatRoom() {
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if (currentUser != null) {
            val userId = currentUser.uid
            val userEmail = currentUser.email

            val newChatRoomId = UUID.randomUUID().toString()

            val chatRoomData = hashMapOf(
                "writer" to userId,
                "email" to userEmail
            )

            FirebaseFirestore.getInstance().collection("ChatRooms")
                .document(newChatRoomId)
                .set(chatRoomData)
                .addOnSuccessListener {
                    addWelcomeMessage(newChatRoomId, userId) // 변경된 부분
                }
                .addOnFailureListener { e ->
                    // 실패 처리 코드 작성
                }
        } else {
            // 사용자가 로그인되어 있지 않은 경우에 대한 처리
            // 로그인 화면으로 이동하거나 사용자에게 로그인을 요청하는 등의 작업을 수행
        }
    }
    private fun addWelcomeMessage(chatRoomId: String, senderId: String) {
        val messageData = hashMapOf(
            "sender" to senderId,
            "content" to "채팅방에 오신 것을 환영합니다!",
            "timestamp" to FieldValue.serverTimestamp()
        )

        FirebaseFirestore.getInstance().collection("ChatRooms")
            .document(chatRoomId)
            .collection("Messages")
            .add(messageData)
            .addOnSuccessListener {
                // 메시지 추가 성공
                // 여기에서 필요한 처리 코드를 작성
            }
            .addOnFailureListener { e ->
                // 메시지 추가 실패
                // 실패 처리 코드 작성
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