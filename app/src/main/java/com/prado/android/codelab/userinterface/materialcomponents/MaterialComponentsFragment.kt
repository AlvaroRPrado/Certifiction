package com.prado.android.codelab.userinterface.materialcomponents

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.prado.android.R
import com.prado.android.databinding.FragmentMaterialComponentsBinding
import com.prado.android.util.navTo


class MaterialComponentsFragment : Fragment(R.layout.fragment_material_components) {

    private lateinit var binding: FragmentMaterialComponentsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMaterialComponentsBinding.bind(view)

        binding.nextButton.setOnClickListener {
            if (!isPasswordValid(binding.passwordEditText.text.toString())){
                binding.passwordTextInput.error = getString(R.string.shr_error_password)
            }else{
                binding.passwordTextInput.error = null
                navTo(R.id.productGridFragment)
            }
        }
        // Clear the error once more than 8 characters are typed.
        binding.passwordEditText.setOnKeyListener { _, _, _ ->
            if (isPasswordValid(binding.passwordEditText.text.toString())){
                binding.passwordTextInput.error = null
            }
            false

        }

        binding.cancelButton.setOnClickListener {
            binding.passwordEditText.setText("")
            binding.username.setText("")
        }

    }


    private fun isPasswordValid(text: String): Boolean = text.length >= 8
}