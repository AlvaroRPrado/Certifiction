package com.prado.android.codelab.datamanagement.roomwithview

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.prado.android.R
import com.prado.android.databinding.FragmentNewWordBinding


class NewWordFragment : Fragment(R.layout.fragment_new_word) {

    private lateinit var binding: FragmentNewWordBinding

    companion object {
        const val BUNDLE_KEY_WORD = "word"
        const val BUNDLE_REQUEST_KEY = "requestKey"
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNewWordBinding.bind(view)

        binding.buttonSave.setOnClickListener {
            // BOM SABER QUE EXISTE ESSA CLASSE UTILITÁRIA PARA MANIPULAR  OU VALIDAR STRING
            if (TextUtils.isEmpty(binding.editWord.text)){
                parentFragmentManager.setFragmentResult(BUNDLE_REQUEST_KEY, bundleOf(BUNDLE_KEY_WORD to "vazio"))
            }else{
                val word = binding.editWord.text.toString()
                parentFragmentManager.setFragmentResult(BUNDLE_REQUEST_KEY, bundleOf(BUNDLE_KEY_WORD to word))
            }
            //NAVEGA PARA A TELA ANTERIOR  - FUNÇÃO AO BOTÃO DE BACK
             findNavController().popBackStack()
        }



    }

}