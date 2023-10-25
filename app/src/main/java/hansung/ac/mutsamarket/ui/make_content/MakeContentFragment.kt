package hansung.ac.mutsamarket.ui.make_content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import hansung.ac.mutsamarket.databinding.FragmentMakeContentBinding

class MakeContentFragment : Fragment() {

    private var _binding: FragmentMakeContentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val makeContentViewModel =
            ViewModelProvider(this).get(MakeContentViewModel::class.java)

        _binding = FragmentMakeContentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //val textView: TextView = binding.textMakeContent
        //makeContentViewModel.text.observe(viewLifecycleOwner) {
            //textView.text = it
        //}
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}