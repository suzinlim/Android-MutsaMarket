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

class MakeContentFragment : Fragment() {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val itemsCollectionRef = db.collection("items")
    private lateinit var selectedImageUri: Uri

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

        writeButton.setOnClickListener {
            val currentUser = FirebaseAuth.getInstance().currentUser
            if (currentUser != null) {
                val writer = currentUser.displayName ?: "Anonymous"

                val title = titleEditText.text.toString()
                val price = priceEditText.text.toString()
                val content = contentEditText.text.toString()
                val isSale = sellSwitch.isChecked

                val post = Post(selectedImageUri.toString(), title, price, writer, content, isSale)

                itemsCollectionRef.add(post)
                    .addOnSuccessListener { documentReference ->
                        val navController = findNavController()
                        navController.navigate(R.id.action_navigation_make_content_to_navigation_home)
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(requireContext(), "글 작성에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(requireContext(), "사용자가 로그인되어 있지 않습니다.", Toast.LENGTH_SHORT).show()
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
