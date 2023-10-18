package com.piwew.storyapp.ui.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.piwew.storyapp.R
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
            btnRegister.setOnClickListener {
                when {
                    binding.edRegisterName.text.toString().isEmpty() -> {
                        binding.edRegisterName.error = getString(R.string.error_empty_field)
                    }

                    binding.edRegisterEmail.text.toString().isEmpty() -> {
                        binding.edRegisterEmail.error = getString(R.string.error_empty_field)
                    }

                    binding.edRegisterPassword.text.toString().isEmpty() -> {
                        binding.edRegisterPassword.error = getString(R.string.error_empty_field)
                    }

                    binding.edRegisterPassword.text.toString().length < 8 -> {
                        binding.edRegisterPassword.error = getString(R.string.error_short_password)
                    }

                    else -> register()
                }
            }

            tvLogin.setOnClickListener {
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            }
        }
        playAnimation()
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

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.ivIllustration, View.TRANSLATION_X, 1f, 60f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.tvTitleRegister, View.ALPHA, 1f).setDuration(100)
        val subtitle = ObjectAnimator.ofFloat(binding.tvSubtitleRegister, View.ALPHA, 1f).setDuration(100)
        val nameTextLayout = ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val nameEditText = ObjectAnimator.ofFloat(binding.edRegisterName, View.ALPHA, 1f).setDuration(100)
        val emailTextLayout = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val emailEditText = ObjectAnimator.ofFloat(binding.edRegisterEmail, View.ALPHA, 1f).setDuration(100)
        val passwordTextLayout = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordEditText = ObjectAnimator.ofFloat(binding.edRegisterPassword, View.ALPHA, 1f).setDuration(100)
        val buttonLogin = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(
                title,
                subtitle,
                nameTextLayout,
                nameEditText,
                emailTextLayout,
                emailEditText,
                passwordTextLayout,
                passwordEditText,
                buttonLogin
            )
            startDelay = 300
        }.start()
    }
}