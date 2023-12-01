package hansung.ac.mutsamarket.ui.home

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import hansung.ac.mutsamarket.databinding.ItemPostBinding
import hansung.ac.mutsamarket.vo.Post
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class PostRecyclerViewAdapter(private val requireContext: Context) : RecyclerView.Adapter<PostRecyclerViewAdapter.PostViewHolder>() {

    var dataList = mutableListOf<Post>()
    val db = FirebaseFirestore.getInstance()
    private var downloadUri : String = ""
    private lateinit var homeViewModel: HomeViewModel
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

           // 이미지
           if(post.image != ""){
               CoroutineScope(Dispatchers.Main).launch {
                   val downloadImage = downloadImage(post.image)
//                   Log.d("image",downloadUri)
//                   val imageUri = Uri.parse(downloadUri)
                   Glide.with(requireContext)
                       .load(downloadImage)
                       .into(binding.itemPostImage)
//                                    binding.imageView.setImageURI(imageUri)
               }
           }

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
    private suspend fun downloadImage(imageName: String): Uri = withContext(Dispatchers.IO) {
        return@withContext suspendCoroutine { continuation ->
            // Firebase Storage 레퍼런스 생성
            val storageReference = FirebaseStorage.getInstance().reference
            val imagesRef = storageReference.child("images") // "images"는 업로드될 폴더 이름
            // 다운로드할 이미지 파일의 이름으로 StorageReference 생성
            val imageRef = imagesRef.child(imageName)

            // 이미지 다운로드
            imageRef.downloadUrl.addOnSuccessListener { uri ->
//                val downloadUri2 = uri.toString()
                Log.d("image-download", uri.toString())
                // TODO: 이미지 다운로드 성공 시의 처리
                // downloadUrl을 사용하여 이미지를 표시하거나 필요한 작업을 수행합니다.
                continuation.resume(uri)
            }.addOnFailureListener { exception ->
                // 이미지 다운로드 실패 시의 처리
                // exception을 사용하여 실패 이유를 확인할 수 있습니다.
                continuation.resumeWithException(exception)
            }
        }
    }
}