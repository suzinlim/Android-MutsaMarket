package hansung.ac.mutsamarket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import hansung.ac.mutsamarket.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 인증 초기화
        auth = Firebase.auth

        // 로그인 버튼 이벤트
        binding.loginButton.setOnClickListener {
            val email = binding.userEmail.text.toString()
            val password = binding.password.text.toString()

            login(email, password)
        }

        // 회원가입 버튼 이벤트
        binding.signupButton.setOnClickListener {
            val intent: Intent = Intent(this@LoginActivity, SignupActivity::class.java)
            startActivity(intent) // startActivity 안에 intent를 넣으면 SingupActivity로 이동
        }
    }

    // 로그인 함수
    private fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // 로그인 성공할 경우
                    Log.d("Login", "signInWithEmail:success")
                    val intent: Intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // 로그인 실패할 경우
                    Log.w("Login", "Error: ${task.exception}")
                }
            }
    }
}