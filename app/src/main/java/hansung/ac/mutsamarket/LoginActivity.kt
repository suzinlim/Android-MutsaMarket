package hansung.ac.mutsamarket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hansung.ac.mutsamarket.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 회원가입 버튼 누르면 회원가입 화면으로 이동하는 이벤트
        binding.signupButton.setOnClickListener {
            val intent: Intent = Intent(this@LoginActivity, SignupActivity::class.java)
            startActivity(intent) // startActivity 안에 intent를 넣으면 SingupActivity로 이동
        }
    }
}