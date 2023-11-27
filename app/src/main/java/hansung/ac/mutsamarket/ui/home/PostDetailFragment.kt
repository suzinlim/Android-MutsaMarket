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
import androidx.core.view.isVisible
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
    val db = FirebaseFirestore.getInstance()

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
            val newChatRoomId = getChatRoomId()
            createChatRoom(newChatRoomId)
            val intent = Intent(requireContext(), ChatRoomActivity::class.java)
            intent.putExtra("chatRoomId", newChatRoomId)
            startActivity(intent)
        }

        return root
    }
    private fun getChatRoomId(): String {
        return UUID.randomUUID().toString()
    }
    private fun createChatRoom(newChatRoomId: String) {
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if (currentUser != null) {
            val userId = currentUser.uid
            val userEmail = currentUser.email

            val chatRoomData = hashMapOf(
                "writer" to userId,
                "email" to userEmail
            )

            // Firestore에 새로운 채팅방 추가
            FirebaseFirestore.getInstance().collection("ChatRooms")
                .document(newChatRoomId)
                .set(chatRoomData)
                .addOnSuccessListener {
                    // 채팅방이 성공적으로 추가되면 해당 문서의 ID를 받아옵니다.
                    Log.d("ChatRoomActivity", "Chat room added with ID: $newChatRoomId")

                    // 이제 추가된 채팅방 ID(newChatRoomId)를 사용하여 메시지 전송 등의 작업을 수행할 수 있습니다.
                }
                .addOnFailureListener { e ->
                    // 실패 처리 코드 작성
                    Log.e("ChatRoomActivity", "Error adding chat room", e)
                }
        } else {
            // 사용자가 로그인되어 있지 않은 경우에 대한 처리
            // 로그인 화면으로 이동하거나 사용자에게 로그인을 요청하는 등의 작업을 수행
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("SetTextI18n")
    private fun initView(){
        var myUser = FirebaseAuth.getInstance().currentUser?.uid
        Log.d("detail", myUser.toString())

        // 채팅하기 버튼 / 수정하기 버튼
        if(myUser == post.writer){
            binding.chatButton.isVisible = false
            binding.modifyButton.isVisible = true
        }
        else{
            binding.chatButton.isVisible = true
            binding.modifyButton.isVisible= false
        }

        // 작성자 이름
        val writerTextView = binding.writerText
        var userName: String = "Annonymous User"
        db.collection("users")
            .whereEqualTo("uid", post.writer)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    // 문서에서 데이터를 가져오기
                    userName = document.getString("name").toString()
                }
            }
            .addOnFailureListener { e ->
                Log.d("firebaseLog","Error getting documents: $e")
            }
        writerTextView.text = userName

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