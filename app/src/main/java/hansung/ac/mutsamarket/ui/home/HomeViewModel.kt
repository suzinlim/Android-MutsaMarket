package hansung.ac.mutsamarket.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import hansung.ac.mutsamarket.vo.Post
class HomeViewModel : ViewModel() {

    // MutableLiveData를 사용하여 데이터 변경을 감지할 수 있도록 합니다.
    private val _postList = MutableLiveData<List<Post>>()
    val db = FirebaseFirestore.getInstance()
    val collectionRef = db.collection("items")

    // postList를 외부에서 읽을 수 있도록 LiveData로 노출합니다.
    val postList: LiveData<List<Post>> get() = _postList

    // 데이터를 업데이트하는 메서드를 정의합니다.
    fun updatePostList() {
        val postList = mutableListOf<Post>()
        // 컬렉션의 모든 문서를 가져오는 코드
        collectionRef.get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    // 각 문서의 데이터를 가져옵니다.
                    val data = document.data
                    val post = Post(
                        data["image"].toString(),
                        data["title"].toString(),
                        data["price"].toString(),
                        data["writer"].toString(),
                        data["content"].toString(),
                        data["sale"] as Boolean,
                        data["postID"].toString())
                    postList.add(post)
                }
                _postList.value = postList
            }
            .addOnFailureListener {
            }
    }

    fun updateIsSalePostList(saleState : Boolean){
        val postList = mutableListOf<Post>()
        // 컬렉션의 모든 문서를 가져오는 코드
        collectionRef.get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    // 각 문서의 데이터를 가져옵니다.
                    val data = document.data
                    if(data["sale"] as Boolean == saleState){
                        val post = Post(
                            data["image"].toString(),
                            data["title"].toString(),
                            data["price"].toString(),
                            data["writer"].toString(),
                            data["content"].toString(),
                            data["sale"] as Boolean,
                            data["postID"].toString())
                        postList.add(post)
                    }
                }
                _postList.value = postList
            }
            .addOnFailureListener {
            }
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
}