package hansung.ac.mutsamarket.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hansung.ac.mutsamarket.databinding.ItemPostBinding
import hansung.ac.mutsamarket.vo.Post

class PostRecyclerViewAdapter: RecyclerView.Adapter<PostRecyclerViewAdapter.PostViewHolder>() {

    var dataList = mutableListOf<Post>()

    inner class PostViewHolder(private val binding: ItemPostBinding): RecyclerView.ViewHolder(binding.root){
       fun bind(post: Post){
//           binding.itemPostImage = post.image
           binding.itemPostTitle.text = post.title
           binding.itemPostPrice.text = post.price.toString()
           binding.itemPostIsSale.text = post.isSale
           binding.itemPostWriter.text = post.title
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