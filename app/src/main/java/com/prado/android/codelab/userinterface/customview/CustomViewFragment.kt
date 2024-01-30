package com.prado.android.codelab.userinterface.customview

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.prado.android.R
import com.prado.android.databinding.FragmentCustomViewBinding
import com.prado.android.util.snake

class CustomViewFragment : Fragment(R.layout.fragment_custom_view) {

    private lateinit var binding: FragmentCustomViewBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCustomViewBinding.bind(view)

        binding.pwd.passwordForgottenButton?.setOnClickListener {
            snake(it, "Esqueci minha senha!")
        }

        binding.pwdOutlined.passwordForgottenButton?.setOnClickListener {
            snake(it, "Esqueci minha senha!")
        }
    }
}