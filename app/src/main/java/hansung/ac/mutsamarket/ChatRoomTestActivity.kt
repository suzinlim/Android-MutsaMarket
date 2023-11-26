package hansung.ac.mutsamarket

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
// import hansung.ac.mutsamarket.databinding.ActivityChatBinding
import hansung.ac.mutsamarket.databinding.ActivityChatroomBinding
// import hansung.ac.mutsamarket.databinding.ActivityMainBinding
import hansung.ac.mutsamarket.ui.chatting.UserAdapter
import hansung.ac.mutsamarket.vo.User

class ChatRoomTestActivity : AppCompatActivity() {

    lateinit var binding: ActivityChatroomBinding
    private lateinit var adapter: UserAdapter

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference
    
    private lateinit var userList: ArrayList<User>

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityChatroomBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // 인증 초기화
        mAuth = Firebase.auth
        // db 초기화
        mDbRef = Firebase.database.reference
        // 리스트 초기화
        userList = ArrayList()
        // 어댑터 초기화
        adapter = UserAdapter(this,userList)

        binding.recyclerViewChatRooms.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewChatRooms.adapter = adapter

        mDbRef.child("user").addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(postSnapshot in snapshot.children){
                    // 유저 정보
                    val currentUser = postSnapshot.getValue(User::class.java)

                    if(mAuth.currentUser?.uid != currentUser?.uid){
                        userList.add(currentUser!!)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // 실패 시 실행
            }

        })
    }
}