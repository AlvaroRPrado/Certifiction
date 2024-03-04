package com.prado.android.codelab.datamanagement.roomwithview

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.prado.android.MainApplication
import com.prado.android.R
import com.prado.android.codelab.datamanagement.roomwithview.NewWordFragment.Companion.BUNDLE_KEY_WORD
import com.prado.android.codelab.datamanagement.roomwithview.NewWordFragment.Companion.BUNDLE_REQUEST_KEY
import com.prado.android.databinding.FragmentWordBinding
import com.prado.android.util.navTo


class WordFragment : Fragment(R.layout.fragment_word) {

    private lateinit var binding: FragmentWordBinding

    // OBTER VIEW MODEL ATRAVÉS DE EXTENCÕES DE KOTLIN
    private val wordViewModel: WordViewModel by viewModels {
        // FACTORY CRIA NOSSO MODELO INJETANDO O REPOSITÓRIO QUE CRIAMOS
        WordViewModelFactory((requireActivity().application as MainApplication).repository)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWordBinding.bind(view)

        val adapter = WordListAdapter()
        binding.recyclerview.adapter = adapter

        // PODE SER DEFINIDO NO XML TAMBEM SE PREFERIR, MAS QUERIA TE MOSTAR MAIS UMA FORMA DE FAZER
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())

        binding.fab.setOnClickListener { navTo(R.id.newWordFragment) }

        wordViewModel.allWords.observe(requireActivity(), { words ->
            // Atualize a cópia em cache das palavras no adaptador.
            words?.let { adapter.submitList(it) }
        })

        // DEFINIR O QUE SERA TETORNADO COMO PARAMETRO, QUANDO ESTA TELA FOR FECHADA
        setFragmentResultListener(BUNDLE_REQUEST_KEY) { _, bundle ->
            val result = bundle.getString(BUNDLE_KEY_WORD, "vazio")
            wordViewModel.insert(Word(result))
        }
    }

}