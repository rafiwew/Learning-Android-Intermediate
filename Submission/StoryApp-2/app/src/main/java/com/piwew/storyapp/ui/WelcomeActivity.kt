package com.piwew.storyapp.ui

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Property
import android.view.View
import com.piwew.storyapp.databinding.ActivityWelcomeBinding
import com.piwew.storyapp.ui.login.LoginActivity
import com.piwew.storyapp.ui.register.RegisterActivity

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        playAnimation()
    }

    private fun createTranslationAnimator(
        view: View,
        property: Property<View, Float>,
        fromValue: Float,
        toValue: Float
    ) = ObjectAnimator.ofFloat(view, property, fromValue, toValue).apply {
        duration = 6000
        repeatCount = ObjectAnimator.INFINITE
        repeatMode = ObjectAnimator.REVERSE
    }

    private fun playAnimation() {
        val illustration2Animator =
            createTranslationAnimator(binding.ivIllustrationWelcome2, View.TRANSLATION_X, -30f, 30f)
        val illustration1Animator =
            createTranslationAnimator(binding.ivIllustrationWelcome1, View.TRANSLATION_Y, -50f, 0f)

        illustration2Animator.start()
        illustration1Animator.start()
    }
}