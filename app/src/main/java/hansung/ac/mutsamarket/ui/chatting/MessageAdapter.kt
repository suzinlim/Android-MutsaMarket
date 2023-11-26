// MessageAdapter.kt

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import hansung.ac.mutsamarket.databinding.ItemMyMessageBinding
import hansung.ac.mutsamarket.databinding.ItemOtherMessageBinding
import hansung.ac.mutsamarket.vo.Message

class MessageAdapter(
    private var messages: List<Message>,
    private val currentUserId: String
) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    fun updateData(newMessages: List<Message>) {
        messages = newMessages
        notifyDataSetChanged()
    }
    fun addMessage(message: Message) {
        messages = messages + listOf(message)
        notifyItemInserted(messages.size - 1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_MY_MESSAGE -> {
                val binding = ItemMyMessageBinding.inflate(inflater, parent, false)
                MyMessageViewHolder(binding)
            }
            VIEW_TYPE_OTHER_MESSAGE -> {
                val binding = ItemOtherMessageBinding.inflate(inflater, parent, false)
                OtherMessageViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        when (holder) {
            is MyMessageViewHolder -> holder.bindMyMessage(message)
            is OtherMessageViewHolder -> holder.bindOtherMessage(message)
        }
    }

    override fun getItemCount(): Int = messages.size

    override fun getItemViewType(position: Int): Int {
        val message = messages[position]
        return if (message.senderId == currentUserId) {
            VIEW_TYPE_MY_MESSAGE
        } else {
            VIEW_TYPE_OTHER_MESSAGE
        }
    }

    abstract class MessageViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)

    inner class MyMessageViewHolder(private val binding: ItemMyMessageBinding) :
        MessageViewHolder(binding) {
        fun bindMyMessage(message: Message) {
            binding.textMessageContentMy.text = message.content
            binding.textMessageContentOther.visibility = View.GONE
            // 추가적인 내 메시지 레이아웃 설정 작업 수행
        }
    }

    inner class OtherMessageViewHolder(private val binding: ItemOtherMessageBinding) :
        MessageViewHolder(binding) {
        fun bindOtherMessage(message: Message) {
            binding.textMessageContentOther.text = message.content
            binding.textMessageContentMy.visibility = View.GONE
            // 추가적인 상대방 메시지 레이아웃 설정 작업 수행
        }
    }

    companion object {
        private const val VIEW_TYPE_MY_MESSAGE = 1
        private const val VIEW_TYPE_OTHER_MESSAGE = 2
    }
}
