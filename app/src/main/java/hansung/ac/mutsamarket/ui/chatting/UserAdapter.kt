package hansung.ac.mutsamarket.ui.chatting

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hansung.ac.mutsamarket.ChatActivity
import hansung.ac.mutsamarket.R
import hansung.ac.mutsamarket.vo.User

class UserAdapter(val context: Context, private val userList: ArrayList<User>):
RecyclerView.Adapter<UserAdapter.UserViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_chat_room, parent, false)

        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        // 데이터 담기
        val currentUser = userList[position]
        // 화면에 데이터 보여주기
        holder.nameText.text = currentUser.name
        // 아이템 클릭 이벤트
        holder.itemView.setOnClickListener{
            val intent = Intent(context, ChatActivity::class.java)

            // 넘길 데이터 담기
            intent.putExtra("name", currentUser.name)
            intent.putExtra("uid", currentUser.uid)

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class UserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val nameText: TextView = itemView.findViewById(R.id.writerTextView)
        val emailTextView: TextView = itemView.findViewById(R.id.emailTextView)

    }
}