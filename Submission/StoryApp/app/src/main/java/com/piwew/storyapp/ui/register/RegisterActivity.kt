package com.piwew.storyapp.ui.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
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
                val name = nameEditText.text.toString()
                val email = emailEditText.text.toString()
                val password = passwordEditText.text.toString()

                when {
                    name.isEmpty() -> {
                        binding.nameEditText.error = "Masih kosong"
                    }

                    email.isEmpty() -> {
                        binding.emailEditText.error = "Masih kosong"
                    }

                    password.isEmpty() -> {
                        binding.passwordEditText.error = "Masih kosong"
                    }

                    password.length < 8 -> {
                        binding.passwordEditText.error = "Password tidak boleh kurang dari 8 karakter"
                    }

                    else -> viewModel.register(name, email, password)
                }

                viewModel.registerStatusResponse.observe(this@RegisterActivity) { isSuccess ->
                    if (isSuccess) {
                        showAlertDialog("Success", viewModel.messageResponse.value!!, "Login")
                    } else {
                        showAlertDialog("Failed", viewModel.messageResponse.value!!, "Close")
                    }
                }

                viewModel.isLoading.observe(this@RegisterActivity) {
                    showLoading(it)
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