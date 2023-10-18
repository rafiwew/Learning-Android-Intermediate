package com.piwew.storyapp.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.piwew.storyapp.R
import com.piwew.storyapp.data.ResultState
import com.piwew.storyapp.data.pref.UserModel
import com.piwew.storyapp.databinding.ActivityLoginBinding
import com.piwew.storyapp.ui.ViewModelFactory
import com.piwew.storyapp.ui.main.MainActivity
import com.piwew.storyapp.ui.register.RegisterActivity

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
            btnLogin.setOnClickListener {
                when {
                    binding.edLoginEmail.text.toString().isEmpty() -> {
                        binding.edLoginEmail.error = getString(R.string.error_empty_field)
                    }

                    binding.edLoginPassword.text.toString().isEmpty() -> {
                        binding.edLoginPassword.error = getString(R.string.error_empty_field)
                    }

                    binding.edLoginPassword.text.toString().length < 8 -> {
                        binding.edLoginPassword.error = getString(R.string.error_short_password)
                    }

                    else -> login()
                }
            }

            tvSignup.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
        }

        playAnimation()
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
                        viewModel.saveSession(UserModel(email, result.data.loginResult.token))
                        Log.d("TOKEN", "login: ${result.data.loginResult.token}")
                        showAlertDialog(
                            "Success",
                            result.data.message,
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
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
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

        val title = ObjectAnimator.ofFloat(binding.tvTitleLogin, View.ALPHA, 1f).setDuration(100)
        val subtitle = ObjectAnimator.ofFloat(binding.tvSubtitleLogin, View.ALPHA, 1f).setDuration(100)
        val emailTextLayout = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val emailEditText = ObjectAnimator.ofFloat(binding.edLoginEmail, View.ALPHA, 1f).setDuration(100)
        val passwordTextLayout = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordEditText = ObjectAnimator.ofFloat(binding.edLoginPassword, View.ALPHA, 1f).setDuration(100)
        val buttonLogin = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(
                title,
                subtitle,
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