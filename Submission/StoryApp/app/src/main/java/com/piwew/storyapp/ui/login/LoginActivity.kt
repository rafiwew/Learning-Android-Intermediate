package com.piwew.storyapp.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.piwew.storyapp.data.pref.UserModel
import com.piwew.storyapp.databinding.ActivityLoginBinding
import com.piwew.storyapp.ui.main.MainActivity
import com.piwew.storyapp.ui.ViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            loginButton.setOnClickListener {
                when {
                    binding.edLoginEmail.text.toString().isEmpty() -> {
                        binding.edLoginEmail.error = "Masih kosong"
                    }

                    binding.edLoginPassword.text.toString().isEmpty() -> {
                        binding.edLoginPassword.error = "Masih kosong"
                    }

                    binding.edLoginPassword.text.toString().length < 8 -> {
                        binding.edLoginPassword.error =
                            "Password tidak boleh kurang dari 8 karakter"
                    }

                    else -> login()
                }
            }
        }
    }

    private fun login() {
        val email = binding.edLoginEmail.text.toString()
        viewModel.saveSession(UserModel(email, "sample_token"))
        AlertDialog.Builder(this).apply {
            setTitle("Yeah!")
            setMessage("Anda berhasil login")
            setPositiveButton("Lanjut") { _, _ ->
                val intent = Intent(context, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
            create()
            show()
        }
    }

}