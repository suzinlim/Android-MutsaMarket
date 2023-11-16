package hansung.ac.mutsamarket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import hansung.ac.mutsamarket.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignupBinding
    lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 인증 객체 초기화
        auth = Firebase.auth

        // 데이터베이스 객체 초기화
        database = Firebase.database.reference

        binding.signupButton.setOnClickListener {
            val name = binding.userName.text.toString().trim()
            val email = binding.userEmail.text.toString().trim()
            val password = binding.password.text.toString().trim()
            val checkPassword = binding.checkPassword.text.toString().trim()

            if (password.length < 6 || password.length > 15 || !password.matches(Regex(".*[a-zA-Z].*"))) {
                // 비밀번호 조건에 맞지 않을 경우
                Toast.makeText(this, "비밀번호는 6~15자의 영문 조합이어야 합니다.", Toast.LENGTH_SHORT).show()
            } else if (password != checkPassword) {
                // 비밀번호와 확인 비밀번호가 일치하지 않을 경우
                Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            } else {
                // 비밀번호 조건이 충족될 경우, 회원가입 시도
                signUp(name, email, password)
            }
        }

        binding.checkButton.setOnClickListener {
            val verifiedEmail = binding.userEmail.text.toString().trim()

            val usersRef = Firebase.database.reference.child("user")
            val query = usersRef.orderByChild("email").equalTo(verifiedEmail)

            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // 이메일이 이미 존재하는 경우
                        Toast.makeText(this@SignupActivity, "이미 사용 중인 이메일입니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        // 이메일이 존재하지 않는 경우
                        Toast.makeText(this@SignupActivity, "사용 가능한 이메일입니다.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // 쿼리 중 오류가 발생한 경우
                    Log.e("Firebase", "Error: ${databaseError.message}")
                }
            })
        }
    }

    // 회원가입 함수
    private fun signUp(name: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // 회원가입 성공할 경우
                    Log.d("SignUp","createUserWithEmail:success")
                    val intent: Intent = Intent(this@SignupActivity, LoginActivity::class.java)
                    startActivity(intent)
                    addUserToDatabase(name, email, auth.currentUser?.uid!!)
                } else {
                    // 회원가입 실패할 경우
                    Log.w("SignUp", "Error: ${task.exception}")
                }
            }
    }

    private fun addUserToDatabase(name: String, email: String, uid: String) {
        database.child("user").child(uid).setValue(User(name, email, uid))
    }
}