package hansung.ac.mutsamarket.ui.make_content

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.MultiAutoCompleteTextView
import android.widget.Switch
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import hansung.ac.mutsamarket.R
import hansung.ac.mutsamarket.databinding.FragmentMakeContentBinding
import hansung.ac.mutsamarket.vo.Post
import hansung.ac.mutsamarket.vo.User
import java.util.*
// Firebase Storage 라이브러리 import
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class MakeContentFragment : Fragment() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val itemsCollectionRef = db.collection("items")
    private var selectedImageUri: Uri? = null
    private var downloadUri: String = ""
    private var _binding: FragmentMakeContentBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val REQ_GALLERY = 1
        private const val TAG = "MakeContentFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMakeContentBinding.inflate(inflater, container, false)
        val post = arguments?.getString("postID")
        Log.d("postInfo-onCreateView", post.toString())
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val writeButton = view.findViewById<Button>(R.id.write_button)
        val picButton = view.findViewById<Button>(R.id.pic_button)
        val titleEditText = view.findViewById<EditText>(R.id.edit_name)
        val priceEditText = view.findViewById<EditText>(R.id.edit_price)
        val contentEditText = view.findViewById<MultiAutoCompleteTextView>(R.id.edit_content)
        val sellSwitch = view.findViewById<Switch>(R.id.sell_switch)

        picButton.setOnClickListener {
            openGallery()
        }

        val currentUser = FirebaseAuth.getInstance().currentUser
        val writer = currentUser?.uid ?: ""
        var username : String = ""
        val postIdToUpdate = arguments?.getString("postID")
        if (postIdToUpdate != null) {
            Log.d("postInfo", postIdToUpdate)
        }
        else{
            Log.d("postInfo","null")
        }
        if (!postIdToUpdate.isNullOrEmpty()) {

            writeButton.text = "수정하기"
            itemsCollectionRef.document(postIdToUpdate).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val post = document.toObject(Post::class.java)
                        Log.d("postInfo-in", postIdToUpdate)
                        if (post != null) {
                            Log.d("postInfo-out", post.content.toString())
                        }
                        post?.let {
                            if(post.image != ""){
//                                downloadImage(post.image)
                                CoroutineScope(Dispatchers.Main).launch {
                                    downloadImage(post.image)
                                    Log.d("image",downloadUri)
                                    val imageUri = Uri.parse(downloadUri)
                                    Glide.with(requireContext())
                                        .load(imageUri)
                                        .into(binding.imageView)
//                                    binding.imageView.setImageURI(imageUri)
                                }
                            }
                            titleEditText.setText(it.title)
                            priceEditText.setText(it.price)
                            contentEditText.setText(it.content)
                            sellSwitch.isChecked = it.isSale
                            writeButton.setOnClickListener {
                                val updatedTitle = titleEditText.text.toString()
                                val updatedPrice = priceEditText.text.toString()
                                val updatedContent = contentEditText.text.toString()
                                val updatedIsSale = sellSwitch.isChecked
                                var imageName = ""
                                if(selectedImageUri != null){
                                    imageName = uploadImage()
                                }
                                db.collection("users").document(writer).get()
                                    .addOnSuccessListener { document ->
                                        val user = document.toObject(User::class.java)
                                        if (user != null) {
                                            Log.d("username-create", user.name)
                                            username = user.name
                                            if (updatedTitle.isNotEmpty() && updatedPrice.isNotEmpty() && updatedContent.isNotEmpty()) {
                                                val updatedPost = Post(
                                                    postID = postIdToUpdate,
                                                    image = imageName,
                                                    title = updatedTitle,
                                                    price = updatedPrice,
                                                    writer = writer,
                                                    content = updatedContent,
                                                    isSale = updatedIsSale,
                                                    writerName = username
                                                )

                                                itemsCollectionRef.document(postIdToUpdate).set(updatedPost)
                                                    .addOnSuccessListener {
                                                        val navController = findNavController()
                                                        navController.navigate(R.id.action_navigation_make_content_to_navigation_home)
                                                    }
                                                    .addOnFailureListener { e ->
                                                        Toast.makeText(requireContext(), "글 수정에 실패했습니다.", Toast.LENGTH_SHORT).show()
                                                    }
                                            } else {
                                                Toast.makeText(requireContext(), "모든 항목을 입력하세요.", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    }
                            }
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "글을 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
        } else {
            writeButton.text = "등록하기"
            writeButton.setOnClickListener {
                val title = titleEditText.text.toString()
                val price = priceEditText.text.toString()
                val content = contentEditText.text.toString()
                val isSale = sellSwitch.isChecked
                var imageName = ""
                db.collection("users").document(writer).get()
                    .addOnSuccessListener { document ->
                        val user = document.toObject(User::class.java)
                        if (user != null) {
                            Log.d("username-create", user.name)
                            username=user.name
                            if (title.isNotEmpty() && price.isNotEmpty() && content.isNotEmpty()) {
                                if(selectedImageUri != null){
                                    imageName = uploadImage()
                                }

                                val postId = UUID.randomUUID().toString()

                                val post = Post(
                                    postID = postId,
                                    image = imageName,
                                    title = title,
                                    price = price,
                                    writer = writer,
                                    content = content,
                                    isSale = isSale,
                                    writerName = username
                                )

                                itemsCollectionRef.document(postId).set(post)
                                    .addOnSuccessListener {
                                        val navController = findNavController()
                                        navController.navigate(R.id.action_navigation_make_content_to_navigation_home)
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(requireContext(), "글 작성에 실패했습니다.", Toast.LENGTH_SHORT).show()
                                    }
                            } else {
                                Toast.makeText(requireContext(), "모든 항목을 입력하세요.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.d("firebaseLog","Error getting documents: $e")
                    }
                Log.d("username-create2", username)
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        startActivityForResult(intent, REQ_GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQ_GALLERY && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.data!!
            binding.imageView.setImageURI(selectedImageUri)
        }
    }

    private fun uploadImage() : String{
        // 이미지 파일 경로 (예: content://media/external/images/media/123)
        val imageUri = selectedImageUri!!

        // Firebase Storage 레퍼런스 생성
        val storageReference = FirebaseStorage.getInstance().reference
        val imagesRef = storageReference.child("images") // "images"는 업로드될 폴더 이름

        // 업로드할 이미지 파일의 이름을 정의 (예: image_123.jpg)
        val imageName = "image_${System.currentTimeMillis()}.jpg"

        // 이미지 파일을 Firebase Storage에 업로드
        val imageRef = imagesRef.child(imageName)
        val uploadTask = imageRef.putFile(imageUri)

        // 업로드 성공 시
        uploadTask.addOnSuccessListener { taskSnapshot ->
            // 이미지 업로드 성공 후의 처리
            Log.d("FirebaseStorage", "Upload successful")

            // 업로드된 이미지의 다운로드 URL 획득
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                val downloadUrl = uri.toString()
                Log.d("FirebaseStorage", "Download URL: $downloadUrl")

                // TODO: 다운로드된 이미지 URL을 사용하여 추가적인 작업 수행
            }
        }.addOnFailureListener { exception ->
            // 이미지 업로드 실패 시의 처리
            Log.e("FirebaseStorage", "Upload failed", exception)
        }
        return imageName
    }
    private suspend fun downloadImage(imageName: String): String = withContext(Dispatchers.IO) {
        return@withContext suspendCoroutine { continuation ->
            // Firebase Storage 레퍼런스 생성
            val storageReference = FirebaseStorage.getInstance().reference
            val imagesRef = storageReference.child("images") // "images"는 업로드될 폴더 이름
            // 다운로드할 이미지 파일의 이름으로 StorageReference 생성
            val imageRef = imagesRef.child(imageName)

            // 이미지 다운로드
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                downloadUri = uri.toString()
                Log.d("image-download", downloadUri)
                // TODO: 이미지 다운로드 성공 시의 처리
                // downloadUrl을 사용하여 이미지를 표시하거나 필요한 작업을 수행합니다.
                continuation.resume(downloadUri)
            }.addOnFailureListener { exception ->
                // 이미지 다운로드 실패 시의 처리
                // exception을 사용하여 실패 이유를 확인할 수 있습니다.
                continuation.resumeWithException(exception)
            }
        }
    }
}
