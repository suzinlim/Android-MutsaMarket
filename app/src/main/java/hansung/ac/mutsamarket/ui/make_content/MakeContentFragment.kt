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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import hansung.ac.mutsamarket.R
import hansung.ac.mutsamarket.databinding.FragmentMakeContentBinding
import hansung.ac.mutsamarket.vo.Post
import java.util.*

class MakeContentFragment : Fragment() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val itemsCollectionRef = db.collection("items")
    private var selectedImageUri: Uri? = null
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
        val postIdToUpdate = arguments?.getString("postID")
        if (postIdToUpdate != null) {
            Log.d("PostId",postIdToUpdate)
        }
        else{
            Log.d("postId","null")
        }
        if (!postIdToUpdate.isNullOrEmpty()) {

            writeButton.text = "수정하기"
            itemsCollectionRef.document(postIdToUpdate).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val post = document.toObject(Post::class.java)
                        post?.let {
                            if (it.image != null) {
                                val imageUri = Uri.parse(it.image)
                                binding.imageView.setImageURI(imageUri)
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

                                if (updatedTitle.isNotEmpty() && updatedPrice.isNotEmpty() && updatedContent.isNotEmpty()) {
                                    val updatedPost = Post(
                                        postID = postIdToUpdate,
                                        image = selectedImageUri.toString(),
                                        title = updatedTitle,
                                        price = updatedPrice,
                                        writer = writer,
                                        content = updatedContent,
                                        isSale = updatedIsSale
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

                if (title.isNotEmpty() && price.isNotEmpty() && content.isNotEmpty()) {
                    val postId = UUID.randomUUID().toString()

                    val post = Post(
                        postID = postId,
                        image = selectedImageUri.toString(),
                        title = title,
                        price = price,
                        writer = writer,
                        content = content,
                        isSale = isSale
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
}
