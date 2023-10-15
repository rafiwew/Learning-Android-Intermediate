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
                    binding.edRegisterName.text.toString().isEmpty() -> {
                        binding.edRegisterName.error = "Masih kosong"
                    }

                    binding.edRegisterEmail.text.toString().isEmpty() -> {
                        binding.edRegisterEmail.error = "Masih kosong"
                    }

                    binding.edRegisterPassword.text.toString().isEmpty() -> {
                        binding.edRegisterPassword.error = "Masih kosong"
                    }

                    binding.edRegisterPassword.text.toString().length < 8 -> {
                        binding.edRegisterPassword.error = "Password tidak boleh kurang dari 8 karakter"
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