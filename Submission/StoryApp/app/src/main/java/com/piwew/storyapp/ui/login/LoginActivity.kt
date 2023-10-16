package com.piwew.storyapp.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.piwew.storyapp.data.ResultState
import com.piwew.storyapp.data.pref.UserModel
import com.piwew.storyapp.databinding.ActivityLoginBinding
import com.piwew.storyapp.ui.ViewModelFactory
import com.piwew.storyapp.ui.main.MainActivity

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
                        binding.edLoginEmail.error = "Can not be empty"
                    }

                    binding.edLoginPassword.text.toString().isEmpty() -> {
                        binding.edLoginPassword.error = "Can not be empty"
                    }

                    binding.edLoginPassword.text.toString().length < 8 -> {
                        binding.edLoginPassword.error = "Password must not be less than 8 characters"
                    }

                    else -> login()
                }
            }
        }
    }

    private fun login() {
        val email = binding.edLoginEmail.text.toString()
        val password = binding.edLoginPassword.text.toString()

        viewModel.login(email, password).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is ResultState.Loading -> {
                        showLoading(true)
                    }

                    is ResultState.Success -> {
                        viewModel.saveSession(UserModel(email, result.data.loginResult?.token!!))
                        showAlertDialog(
                            "Success",
                            result.data.message!!,
                            "Continue",
                            MainActivity::class.java,
                            result.data.loginResult.name
                        )
                        showLoading(false)
                    }

                    is ResultState.Error -> {
                        showAlertDialog("Failed", result.error, "Try again")
                        showLoading(false)
                    }
                }
            }
        }
    }

    private fun showAlertDialog(
        title: String,
        message: String,
        textButton: String,
        targetActivity: Class<*>? = LoginActivity::class.java,
        extra: String? = null
    ) {
        AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton(textButton) { _, _ ->
                val intent = Intent(this@LoginActivity, targetActivity)
                intent.putExtra("name", extra)
                startActivity(intent)
                finish()
            }
            create()
            show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}