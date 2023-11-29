//ChattingFragment.kt
package hansung.ac.mutsamarket.ui.chatting

import ChatRoom
import ChatRoomAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import hansung.ac.mutsamarket.ChatRoomActivity
import hansung.ac.mutsamarket.databinding.FragmentChattingBinding

class ChattingFragment : Fragment() {
    private var _binding: FragmentChattingBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var chatRoomAdapter: ChatRoomAdapter

    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChattingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        recyclerView = binding.recyclerViewChatRooms
        chatRoomAdapter = ChatRoomAdapter(emptyList())
        recyclerView.adapter = chatRoomAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        // 채팅방 클릭 이벤트 처리
        chatRoomAdapter.setOnItemClickListener { chatRoom ->
            navigateToChatRoom(chatRoom)
        }

        loadChatRooms()

        return root
    }
    private fun navigateToChatRoom(chatRoom: ChatRoom) {
        // 채팅방으로 이동하는 코드를 작성
        // 적절한 Intent를 사용하여 다음 화면으로 이동하고 필요한 정보를 전달
        val intent = Intent(context, ChatRoomActivity::class.java)
        firestore.collection("ChatRooms")
//            .whereEqualTo("writer", chatRoom.receiverId)
//            .whereEqualTo("title", chatRoom.title)
//            .whereEqualTo("senderId", chatRoom.senderId)  // Add senderId check
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    intent.putExtra("chatRoomId", document.id)
                    intent.putExtra("senderId", chatRoom.senderId)

                    startActivity(intent)
                    break  // 여러 개의 문서 중 하나만 사용하려면 반복문 종료
                }
            }
            .addOnFailureListener { exception ->
                // 실패 처리
                Log.e("ChattingFragment", "Error getting chat room ID", exception)
            }
    }


    private fun loadChatRooms() {
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if (currentUser != null) {
            val userId = currentUser.uid

            val senderQuery = firestore.collection("ChatRooms")
                .whereEqualTo("senderId", userId)

            val receiverQuery = firestore.collection("ChatRooms")
                .whereEqualTo("receiverId", userId)

            // 두 쿼리의 결과를 결합
            Tasks.whenAllSuccess<QuerySnapshot>(senderQuery.get(), receiverQuery.get())
                .addOnSuccessListener { results ->
                    val chatRooms = mutableListOf<ChatRoom>()

                    for (result in results) {
                        for (document in result.documents) {
                            val title = document.getString("title") ?: ""
                            val senderId = document.getString("senderId") ?: ""
                            val receiverId = document.getString("receiverId") ?: ""

                            val chatRoom = ChatRoom(receiverId, title, senderId)
                            chatRooms.add(chatRoom)
                        }
                    }

                    // 로그로 확인
                    Log.d("ChattingFragment", "로드된 채팅방 수: ${chatRooms.size}")

                    // RecyclerView에 데이터 설정
                    chatRoomAdapter.updateData(chatRooms)
                }
                .addOnFailureListener { exception ->
                    // 실패 처리
                    Log.e("ChattingFragment", "채팅방 로드 중 오류", exception)
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
}
