package hansung.ac.mutsamarket.ui.my_page

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyPageViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is My Page Fragment"
    }
    val text: LiveData<String> = _text
}