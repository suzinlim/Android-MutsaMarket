package hansung.ac.mutsamarket.ui.home

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import hansung.ac.mutsamarket.databinding.ItemPostBinding
import hansung.ac.mutsamarket.vo.Post

class PostRecyclerViewAdapter: RecyclerView.Adapter<PostRecyclerViewAdapter.PostViewHolder>() {

    var dataList = mutableListOf<Post>()
    val db = FirebaseFirestore.getInstance()
    interface OnItemClickListener {
        fun onItemClick(post: Post)
    }
    private var itemClickListener: OnItemClickListener? = null
    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    inner class PostViewHolder(private val binding: ItemPostBinding): RecyclerView.ViewHolder(binding.root){
       fun bind(post: Post){
//           binding.itemPostImage = post.image
           binding.itemPostTitle.text = post.title
           binding.itemPostPrice.text = post.price
           if(post.isSale){ binding.itemPostIsSale.text = "판매 중"}
           else{ binding.itemPostIsSale.text = "판매 완료"}

           binding.itemPostWriter.text = post.writerName
//           val contentUri = Uri.parse("content://com.google.android.apps.photos.contentprovider/-1/1/content%3A%2F%2Fmedia%2Fexternal%2Fimages%2Fmedia%2F1000000033/ORIGINAL/NONE/image%2Fjpeg/2129791021")
//           binding.itemPostImage.setImageURI(contentUri)
           binding.root.setOnClickListener {
               itemClickListener?.onItemClick(post)
           }
       }
    }

    //만들어진 뷰홀더 없을때 뷰홀더(레이아웃) 생성하는 함수
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding=ItemPostBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PostViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(dataList[position])
    }
}