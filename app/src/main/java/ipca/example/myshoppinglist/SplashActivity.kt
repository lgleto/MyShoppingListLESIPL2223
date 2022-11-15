package ipca.example.myshoppinglist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        lifecycleScope.launch(Dispatchers.Main) {
            delay(1000)
            val currentUser = Firebase.auth.currentUser
            if(currentUser != null){
                startActivity(Intent(this@SplashActivity,MainActivity::class.java))
            }else{
                startActivity(Intent(this@SplashActivity,LoginActivity::class.java))
            }
        }


    }
}