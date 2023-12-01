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

    // 사용자가 이미지를 선택하였는지 아닌지를 확인하는 전역변수
    private var isImageSelected = false

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

        // 사진 추가 로직
        picButton.setOnClickListener {
            openGallery()
        }

        // 현재 사용자의 정보
        val currentUser = FirebaseAuth.getInstance().currentUser
        val writer = currentUser?.uid ?: "" // 작성자 uid
        var username : String = "" // 작성자 이름
        val postIdToUpdate = arguments?.getString("postID")

        if (postIdToUpdate != null) {
            Log.d("postInfo", postIdToUpdate)
        }
        else{
            Log.d("postInfo","null")
        }


        // 사용자 Id가 있는 경우 수정을 의미 => 이미지를 다시 선택하지 않을 수도 있음
        if (!postIdToUpdate.isNullOrEmpty()) {
            writeButton.text = "수정하기" // 수정하기로 텍스트 내용을 바꾸고
            // 파이어스토어에서 기존 작성된 정보 가져오기
            itemsCollectionRef.document(postIdToUpdate).get()
                .addOnSuccessListener { document ->
                    if (document != null) { // 정보가 존재한다면
                        // 데이터모델로 파싱하고
                        val post = document.toObject(Post::class.java)
                        Log.d("postInfo-in", postIdToUpdate)
                        if (post != null) {
                            Log.d("postInfo-out", post.content.toString())
                        }
                        post?.let { it ->
                            // 이미지가 존재한다면 => post.image에 해당 게시글 이미지의 이름이 담겨있는 상황
                            if(post.image != ""){
//                                downloadImage(post.image)
                                CoroutineScope(Dispatchers.Main).launch {
                                    downloadImage(post.image)

                                    // 화면에 이미지 보여주기
                                    Log.d("image",downloadUri)
                                    val imageUri = Uri.parse(downloadUri)
                                    Glide.with(requireContext())
                                        .load(imageUri)
                                        .into(binding.imageView)
//                                    binding.imageView.setImageURI(imageUri)
                                }
                            }

                            // 이미 작성된 게시글 정보를 얻어온다.(이름,가격,설명,등..)
                            val sale = document.getBoolean("sale")
                            titleEditText.setText(it.title)
                            priceEditText.setText(it.price)
                            contentEditText.setText(it.content)
                            if(sale!=null){
                                sellSwitch.isChecked=sale
                            }

                            // 만약 사용자가 수정하기 버튼을 누른다면,
                            writeButton.setOnClickListener {
                                val updatedTitle = titleEditText.text.toString()
                                val updatedPrice = priceEditText.text.toString()
                                val updatedContent = contentEditText.text.toString()
                                val updatedIsSale = sellSwitch.isChecked
                                var imageName = ""

                                // 선택된 이미지가 있다면 => 사용자가 이미지를 수정하지 않을수도 있음(그 경우, 이미지가 없어지는 문제 발생)
                                // 사용자가 수정하기 모드에서 이미지를 다시 선택한적이 있는지 검사
                                if (isImageSelected && selectedImageUri != null) {
                                    uploadImage(){ uploadedImageName ->
                                        imageName = uploadedImageName
                                        Log.d("upload",imageName)
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
                                } else { // 다시 선택하지 않았다면
                                    imageName = post.image // 이전에 선택한 이미지로 이름 설정
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
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "글을 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
        }
        else { // 글을 새롭게 작성하는 경우
            writeButton.text = "등록하기"
            writeButton.setOnClickListener {
                val title = titleEditText.text.toString()
                val price = priceEditText.text.toString()
                val content = contentEditText.text.toString()
                val isSale = sellSwitch.isChecked
                var imageName = ""

                Log.d("MakeContent", "Check #1")

                // 사용자 정보 얻어오기
                db.collection("users").document(writer).get()
                    .addOnSuccessListener { document ->
                        val user = document.toObject(User::class.java)
                        if (user != null) {
                            Log.d("username-create", user.name)
                            username=user.name
                            if (title.isNotEmpty() && price.isNotEmpty() && content.isNotEmpty()) {
                                if (isImageSelected && selectedImageUri != null) {
                                    uploadImage(){uploadedImageName ->
                                        imageName = uploadedImageName
                                        // 이미지 업로드가 성공했을 경우에만 글을 포스트 하도록 수정

                                        // 랜덤 포스트 아이디 생성하고
                                        val postId = UUID.randomUUID().toString()

                                        Log.d("MakeContent", "Check #3 $postId")

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

                                        Log.d("MakeContent", "Check #4 :${post.toString()}")

                                        itemsCollectionRef.document(postId).set(post)
                                            .addOnSuccessListener {
                                                val navController = findNavController()
                                                Log.d("MakeContent", "Check #5 : success")
                                                navController.navigate(R.id.action_navigation_make_content_to_navigation_home)
                                            }
                                            .addOnFailureListener { e ->
                                                Log.d("MakeContent", "Check #5-2 : error")
                                                Toast.makeText(requireContext(), "글 작성에 실패했습니다.", Toast.LENGTH_SHORT).show()
                                            }
                                    }
                                    Log.d("MakeContent", "Check #2-1 : ${imageName}")
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


    // 사용자가 이미지를 선택하였을때 호출됨
    // 이 부분에 전역변수 플래그를 수정해서, 수정이 되었는지 아닌지를 확인할 수 있을 것
    // 이미지 수정이 발생되었다면, uploadImage()를 호출하여 이미지 이름을 변경하고
    // 이미지 수정이 발생하지 않았다면, 기존의 이미지 이름을 삽입하면됨
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQ_GALLERY && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.data!!
            binding.imageView.setImageURI(selectedImageUri)
            isImageSelected = true // 이미지 선택했음을 알림
        }
    }



    private fun uploadImage(callback: (String) -> Unit){
        // 이미지 파일 경로 (예: content://media/external/images/media/123)
        val imageUri = selectedImageUri!!

        // Firebase Storage 레퍼런스 생성
        val storageReference = FirebaseStorage.getInstance().reference
        val imagesRef = storageReference.child("images") // "images"는 업로드될 폴더 이름

        // 업로드할 이미지 파일의 이름을 정의 (예: image_123.jpg) => 현재 시간으로 정의?
        val imageName = "image_${System.currentTimeMillis()}.jpg"
        Log.d("uploadImage",imageName)

        // 이미지 파일을 Firebase Storage에 업로드
        val imageRef = imagesRef.child(imageName)
        val uploadTask = imageRef.putFile(imageUri)

        // 업로드 성공 시
        uploadTask.addOnSuccessListener { taskSnapshot ->
            // 이미지 업로드 성공 후의 처리
            Log.d("FirebaseStorage", "Upload successful")
            callback(imageName)

//            // 업로드된 이미지의 다운로드 URL 획득
//            imageRef.downloadUrl.addOnSuccessListener { uri ->
//                val downloadUrl = uri.toString()
//                Log.d("FirebaseStorage", "Download URL: $downloadUrl")
//
//                // TODO: 다운로드된 이미지 URL을 사용하여 추가적인 작업 수행
//            }
        }.addOnFailureListener { exception ->
            // 이미지 업로드 실패 시의 처리
            Log.e("FirebaseStorage", "Upload failed", exception)
        }
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
