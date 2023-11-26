//ChatRoomAdapter.kt
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hansung.ac.mutsamarket.R
import hansung.ac.mutsamarket.vo.ChatRoom

class ChatRoomAdapter(private var chatRooms: List<ChatRoom>) :
    RecyclerView.Adapter<ChatRoomAdapter.ChatRoomViewHolder>() {

    private var onItemClickListener: ((ChatRoom) -> Unit)? = null

    inner class ChatRoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val writerTextView: TextView = itemView.findViewById(R.id.writerTextView)
        val emailTextView: TextView = itemView.findViewById(R.id.emailTextView)

        init {
            itemView.setOnClickListener {
                onItemClickListener?.invoke(chatRooms[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatRoomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat_room, parent, false)
        return ChatRoomViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatRoomViewHolder, position: Int) {
        val chatRoom = chatRooms[position]
        holder.writerTextView.text = chatRoom.writer
        holder.emailTextView.text = chatRoom.email
    }

    override fun getItemCount(): Int {
        return chatRooms.size
    }

    fun setOnItemClickListener(listener: (ChatRoom) -> Unit) {
        onItemClickListener = listener
    }

    fun updateData(newChatRooms: List<ChatRoom>) {
        chatRooms = newChatRooms
        notifyDataSetChanged()
    }
}
