package hansung.ac.mutsamarket.ui.my_page

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import hansung.ac.mutsamarket.LoginActivity
import hansung.ac.mutsamarket.databinding.FragmentMyPageBinding
import hansung.ac.mutsamarket.vo.User
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

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
        // FirebaseAuth에서 현재 로그인한 사용자 가져오기
        val user = FirebaseAuth.getInstance().currentUser

        // 현재 사용자의 uid 가져오기
        val userId = user?.uid ?: ""

        _binding = FragmentMyPageBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val userReference = FirebaseDatabase.getInstance().reference.child("user").child(userId)

        userReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val user = dataSnapshot.getValue(User::class.java) // 사용자 정보를 담은 User 모델
                    Log.d("사용자 정보", "이름: ${user?.name}, 생년월일: ${user?.birth}, 이메일: ${user?.email}")
                    binding.myName.text = user?.name
                    val birthDate = user?.birth
                    val originalFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())

                    try {
                        val formattedDate = originalFormat.parse(birthDate)?.let { // 생년월일 포맷팅
                            val targetFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
                            targetFormat.format(it)
                        }
                        Log.d("생년월일", "${formattedDate}")
                        binding.myBirth.text = formattedDate
                    } catch (e: ParseException) {
                        // 생년월일 포맷 변환 실패할 경우
                        Log.e("ParseException", "날짜 변환 실패: ${e.message}")
                        binding.myBirth.text = user?.birth // 원래 데이터 그대로 사용
                    }
                    binding.myEmail.text = user?.email
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                val code = databaseError.code
                val message = databaseError.message
                Log.e("TAG_DB", "onCancelled by $code : $message")
            }
        })

        // 회원 탈퇴 처리
        binding.userDeleteButton.setOnClickListener {
            showUserDeleteConfirmationDialog()
        }

        // 로그아웃 처리
        binding.logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            // 로그아웃이 되면 로그인 화면으로 이동
            val intent: Intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }
        return root
    }

    // 회원 탈퇴하기
    private fun showUserDeleteConfirmationDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("회원 탈퇴")
        builder.setMessage("탈퇴하시려면 '회원 탈퇴'를 입력하세요.")

        // 사용자로부터 회원 탈퇴를 입력 받는 EditText 추가
        val input = EditText(requireContext())
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        input.layoutParams = layoutParams
        builder.setView(input)

        builder.setPositiveButton("탈퇴") { _, _ ->
            val userInput = input.text.toString()
            if (userInput == "회원 탈퇴") {
                val user = FirebaseAuth.getInstance().currentUser
                val userReference =
                    FirebaseDatabase.getInstance().reference.child("user").child(user?.uid ?: "")

                user?.delete()?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Firebase에서 사용자 정보 삭제
                        userReference.removeValue().addOnCompleteListener { databaseTask ->
                            if (databaseTask.isSuccessful) {
                                // 사용자 정보 삭제 성공할 경우
                                Toast.makeText(
                                    requireContext(),
                                    "회원 탈퇴가 완료되었습니다.",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                val intent = Intent(requireContext(), LoginActivity::class.java)
                                startActivity(intent)
                            } else {
                                // 사용자 정보 삭제 실패할 경우
                                Toast.makeText(
                                    requireContext(),
                                    "회원 탈퇴에 실패하였습니다.",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }
                    } else {
                        // 회원 탈퇴 실패
                        Toast.makeText(requireContext(), "회원 탈퇴에 실패하였습니다.", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            } else {
                // 회원 탈퇴를 입력하지 않은 경우
                Toast.makeText(requireContext(), "탈퇴를 다시 진행해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        builder.setNegativeButton("취소") { dialog, _ ->
            // 사용자가 취소를 선택한 경우, 다이얼로그 닫기
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}