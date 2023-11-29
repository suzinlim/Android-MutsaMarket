//ChatRoomAdapter.kt
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hansung.ac.mutsamarket.R

class ChatRoomAdapter(private var chatRooms: List<ChatRoom>) :
    RecyclerView.Adapter<ChatRoomAdapter.ChatRoomViewHolder>() {

    private var onItemClickListener: ((ChatRoom) -> Unit)? = null

    inner class ChatRoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val writerTextView: TextView = itemView.findViewById(R.id.writerTextView)
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)

        init {
            itemView.setOnClickListener {
                Log.d("ChatRoomAdapter", "포지션 $adapterPosition 에서 아이템이 클릭되었습니다.")
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
        holder.titleTextView.text = chatRoom.title
        holder.writerTextView.text = "대화를 시작해보세요!"

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
