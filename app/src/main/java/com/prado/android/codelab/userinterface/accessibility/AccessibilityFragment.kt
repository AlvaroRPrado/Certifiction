package com.prado.android.codelab.userinterface.accessibility

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.prado.android.R
import com.prado.android.databinding.FragmentAccessibilityBinding

class AccessibilityFragment : Fragment(R.layout.fragment_accessibility) {

    private lateinit var binding: FragmentAccessibilityBinding
    private var count = 0

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAccessibilityBinding.bind(view)


        binding.btnAdd.setOnClickListener {
            count++
            binding.textAdd.text = "Apertou $count vez!"
        }
    }

}