//ChatRoomActivity.kt
package hansung.ac.mutsamarket

import MessageAdapter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import hansung.ac.mutsamarket.vo.Message

class ChatRoomActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var etMessage: EditText

    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        recyclerView = findViewById(R.id.recycler)
        etMessage = findViewById(R.id.et)

        // "roomId"는 ChattingFragment에서 전달한 채팅방의 고유 ID입니다.
        val roomId = intent.getStringExtra("roomId") ?: ""
        val chatRoomId = intent.getStringExtra("chatRoomId") ?: ""


        messageAdapter = MessageAdapter(emptyList())
        recyclerView.adapter = messageAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // 채팅 메시지 불러오기
        loadMessages(roomId)

        // 메시지 전송 버튼 클릭 이벤트 처리
        findViewById<View>(R.id.btn).setOnClickListener {
            val messageContent = etMessage.text.toString().trim()

            if (messageContent.isNotEmpty()) {
                sendMessage(chatRoomId, messageContent)
            }
        }
    }

    private fun loadMessages(chatRoomId: String) {
        firestore.collection("messages")
            .whereEqualTo("chatRoomId", chatRoomId)
            .orderBy("timestamp")
            .get()
            .addOnSuccessListener { result ->
                val messages = mutableListOf<Message>()

                for (document in result) {
                    val senderId = document.getString("senderId") ?: ""
                    val content = document.getString("content") ?: ""
                    val timestamp = document.getLong("timestamp") ?: 0
                    val chatRoomId = document.getString("chatRoomId") ?: ""

                    val message = Message(senderId, content, timestamp, chatRoomId)
                    messages.add(message)
                }

                messageAdapter.updateData(messages)
                scrollToBottom()
            }
            .addOnFailureListener { exception ->
                // 실패 처리
                Log.e("ChatRoomActivity", "Error loading messages", exception)
            }
    }

    private fun sendMessage(chatRoomId: String, content: String) {
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if (currentUser != null) {
            // 현재 사용자의 정보를 가져옵니다.
            val userId = currentUser.uid
            // 여기에 필요한 사용자 정보를 가져오는 코드를 추가할 수 있습니다.

            // 예시로 가져온 사용자 정보를 Message 모델에 적용
            val message = Message(
                senderId = userId,
                content = content,
                timestamp = System.currentTimeMillis(),
                chatRoomId = chatRoomId // 채팅방 ID 추가

            )

            // Firestore에 Message 모델을 저장합니다.
            firestore.collection("messages")
                .add(message)
                .addOnSuccessListener {
                    // 메시지 전송 성공
                    etMessage.text.clear()
                }
                .addOnFailureListener { e ->
                    // 메시지 전송 실패 처리
                    // 에러 로그 출력 등
                }
        } else {
            // 사용자가 로그인되어 있지 않은 경우에 대한 처리
            // 로그인 화면으로 이동하거나 사용자에게 로그인을 요청하는 등의 작업을 수행할 수 있습니다.
        }
    }
    private fun scrollToBottom() {
        recyclerView.scrollToPosition(messageAdapter.itemCount - 1)
    }
}