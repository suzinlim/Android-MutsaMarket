package hansung.ac.mutsamarket.ui.chatting
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hansung.ac.mutsamarket.ChatRoom
import hansung.ac.mutsamarket.R // R은 프로젝트에 따라 다를 수 있습니다.

class ChatRoomAdapter(private var chatRooms: List<ChatRoom>) :
    RecyclerView.Adapter<ChatRoomAdapter.ChatRoomViewHolder>() {

    class ChatRoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val writerTextView: TextView = itemView.findViewById(R.id.textViewWriter)
        val lastMessageTextView: TextView = itemView.findViewById(R.id.textViewLastMessage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatRoomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat_room, parent, false)
        return ChatRoomViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatRoomViewHolder, position: Int) {
        val chatRoom = chatRooms[position]
        holder.writerTextView.text = chatRoom.writer
        holder.lastMessageTextView.text = chatRoom.lastMessage
    }

    override fun getItemCount(): Int {
        return chatRooms.size
    }

    fun updateData(newChatRooms: List<ChatRoom>) {
        chatRooms = newChatRooms
        notifyDataSetChanged()
    }

}
