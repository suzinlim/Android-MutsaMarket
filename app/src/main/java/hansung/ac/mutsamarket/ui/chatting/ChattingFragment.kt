package hansung.ac.mutsamarket.ui.chatting

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import hansung.ac.mutsamarket.ChatRoom
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

        loadChatRooms()

        return root
    }

    private fun loadChatRooms() {
        firestore.collection("ChatRooms")
            .get()
            .addOnSuccessListener { result ->
                val chatRooms = mutableListOf<ChatRoom>()

                for (document in result) {
                    val writer = document.getString("writer") ?: ""
                    val lastMessage = document.getString("lastMessage") ?: ""

                    val chatRoom = ChatRoom(writer, lastMessage)
                    chatRooms.add(chatRoom)
                }

                // 로그로 확인
                Log.d("ChattingFragment", "Loaded ${chatRooms.size} chat rooms")

                // RecyclerView에 데이터 설정
                chatRoomAdapter.updateData(chatRooms)
            }
            .addOnFailureListener { exception ->
                // 실패 처리
                Log.e("ChattingFragment", "Error loading chat rooms", exception)
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
