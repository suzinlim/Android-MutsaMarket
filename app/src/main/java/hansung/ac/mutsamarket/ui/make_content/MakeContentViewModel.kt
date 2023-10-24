package hansung.ac.mutsamarket.ui.make_content

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MakeContentViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Make Content Fragment"
    }
    val text: LiveData<String> = _text
}