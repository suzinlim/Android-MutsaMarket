package hansung.ac.mutsamarket

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import hansung.ac.mutsamarket.databinding.ActivitySignupBinding
import hansung.ac.mutsamarket.vo.User
import java.util.Calendar

class SignupActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignupBinding
    lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    var isEmailChecked = false // 이메일 중복 확인 여부
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 인증 객체 초기화
        auth = Firebase.auth

        // 데이터베이스 객체 초기화
        database = Firebase.database.reference

        binding.checkButton.setOnClickListener {
            val verifiedEmail = binding.userEmail.text.toString().trim()

            val usersRef = Firebase.database.reference.child("user")
            val query = usersRef.orderByChild("email").equalTo(verifiedEmail)

            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // 이메일이 이미 존재하는 경우
                        Toast.makeText(this@SignupActivity, "이미 사용 중인 이메일입니다.", Toast.LENGTH_SHORT).show()
                        binding.signupButton.isEnabled = false // 회원가입 버튼 비활성화
                    } else {
                        // 이메일이 존재하지 않는 경우
                        Toast.makeText(this@SignupActivity, "사용 가능한 이메일입니다.", Toast.LENGTH_SHORT).show()
                        isEmailChecked = true // 이메일 중복 확인 성공
                        binding.signupButton.isEnabled = true // 회원가입 버튼 활성화
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // 쿼리 중 오류가 발생한 경우
                    Log.e("Firebase", "Error: ${databaseError.message}")
                    binding.signupButton.isEnabled = false // 회원가입 버튼 비활성화
                }
            })
        }

        binding.signupButton.setOnClickListener {
            val name = binding.userName.text.toString().trim()
            val birth = binding.userBirth.text.toString().trim()
            val email = binding.userEmail.text.toString().trim()
            val password = binding.password.text.toString().trim()
            val checkPassword = binding.checkPassword.text.toString().trim()

            // 비밀번호 조건에 맞지 않을 경우
            if (password.length < 6 || password.length > 15 || !password.matches(Regex(".*[a-zA-Z].*"))) {
                Toast.makeText(this, "비밀번호는 6~15자의 영문 조합이어야 합니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener // 회원가입 함수 호출하지 않음
            }
            // 비밀번호와 확인 비밀번호가 일치하지 않을 경우
            if (password != checkPassword) {
                Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener // 회원가입 함수 호출하지 않음
            }
            // 이메일 중복 확인을 하지 않은 경우
            if (!isEmailChecked) {
                Toast.makeText(this, "이메일 중복 확인을 해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener // 회원가입 함수 호출하지 않음
            }
            // 비밀번호 조건이 충족되고 이메일 중복 검사를 한 경우, 회원가입 시도
            signUp(name, birth, email, password)
        }
    }

    // 회원가입 함수
    private fun signUp(name: String, birth: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // 회원가입 성공할 경우
                    Toast.makeText(this, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                    val intent: Intent = Intent(this@SignupActivity, LoginActivity::class.java)
                    startActivity(intent)
                    addUserToDatabase(name, birth, email, auth.currentUser?.uid!!)
                } else {
                    // 회원가입 실패할 경우
                    Toast.makeText(this, "회원가입에 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                    Log.w("SignUp", "Error: ${task.exception}")
                }
            }
    }

    private fun addUserToDatabase(name: String, birth: String, email: String, uid: String) {
        database.child("user").child(uid).setValue(User(name, birth, email, uid))
    }
}