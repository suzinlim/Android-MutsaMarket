package hansung.ac.mutsamarket.ui.my_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import hansung.ac.mutsamarket.databinding.FragmentMakeContentBinding
import hansung.ac.mutsamarket.databinding.FragmentMyPageBinding

class MyPageFragment : Fragment() {

    private var _binding: FragmentMyPageBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMyPageBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Realtime Database에서 가져온 데이터라 가정하고 임의로 값 설정
        val userName = "아기사자"
        val userBirth = "2000.10.24"
        val userEmail = "mutsa@naver.com"

        // 가져온 데이터를 TextView에 설정
        binding.myName.text = userName
        binding.myBirth.text = userBirth
        binding.myEmail.text = userEmail

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}