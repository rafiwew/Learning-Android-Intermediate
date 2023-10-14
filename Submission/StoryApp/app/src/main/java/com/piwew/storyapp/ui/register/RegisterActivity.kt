package com.piwew.storyapp.ui.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.piwew.storyapp.data.ResultState
import com.piwew.storyapp.databinding.ActivityRegisterBinding
import com.piwew.storyapp.ui.ViewModelFactory

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            signupButton.setOnClickListener {
                when {
                    binding.nameEditText.text.toString().isEmpty() -> {
                        binding.nameEditText.error = "Masih kosong"
                    }

                    binding.emailEditText.text.toString().isEmpty() -> {
                        binding.emailEditText.error = "Masih kosong"
                    }

                    binding.passwordEditText.text.toString().isEmpty() -> {
                        binding.passwordEditText.error = "Masih kosong"
                    }

                    binding.passwordEditText.text.toString().length < 8 -> {
                        binding.passwordEditText.error = "Password tidak boleh kurang dari 8 karakter"
                    }

                    else -> register()
                }
            }
        }
    }

    private fun register() {
        val name = binding.nameEditText.text.toString()
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()

        viewModel.register(name, email, password)
            .observe(this@RegisterActivity) { result ->
                if (result != null) {
                    when (result) {
                        is ResultState.Loading -> {
                            showLoading(true)
                        }

                        is ResultState.Success -> {
                            showAlertDialog("Success", result.data.toString(), "Login")
                            showLoading(false)
                        }

                        is ResultState.Error -> {
                            showAlertDialog("Failed", result.error, "Close")
                            showLoading(false)
                        }
                    }
                }
            }
    }

    private fun showAlertDialog(title: String, message: String, textButton: String) {
        AlertDialog.Builder(this@RegisterActivity).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton(textButton) { _, _ ->
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