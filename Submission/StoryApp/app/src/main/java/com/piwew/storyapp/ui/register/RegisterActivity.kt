package com.piwew.storyapp.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.piwew.storyapp.data.ResultState
import com.piwew.storyapp.databinding.ActivityRegisterBinding
import com.piwew.storyapp.ui.ViewModelFactory
import com.piwew.storyapp.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            signupButton.setOnClickListener {
                when {
                    binding.edRegisterName.text.toString().isEmpty() -> {
                        binding.edRegisterName.error = "Can not be empty"
                    }

                    binding.edRegisterEmail.text.toString().isEmpty() -> {
                        binding.edRegisterEmail.error = "Can not be empty"
                    }

                    binding.edRegisterPassword.text.toString().isEmpty() -> {
                        binding.edRegisterPassword.error = "Can not be empty"
                    }

                    binding.edRegisterPassword.text.toString().length < 8 -> {
                        binding.edRegisterPassword.error = "Password must not be less than 8 characters"
                    }

                    else -> register()
                }
            }
        }
    }

    private fun register() {
        val name = binding.edRegisterName.text.toString()
        val email = binding.edRegisterEmail.text.toString()
        val password = binding.edRegisterPassword.text.toString()

        viewModel.register(name, email, password)
            .observe(this@RegisterActivity) { result ->
                if (result != null) {
                    when (result) {
                        is ResultState.Loading -> {
                            showLoading(true)
                        }

                        is ResultState.Success -> {
                            showAlertDialog(
                                "Success",
                                result.data.toString(),
                                "Login",
                                LoginActivity::class.java
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
        targetActivity: Class<*>? = RegisterActivity::class.java
    ) {
        AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton(textButton) { _, _ ->
                val intent = Intent(this@RegisterActivity, targetActivity)
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