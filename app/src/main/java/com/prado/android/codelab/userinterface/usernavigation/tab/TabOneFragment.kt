package com.prado.android.codelab.userinterface.usernavigation.tab

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.prado.android.R
import com.prado.android.databinding.FragmentTabOneBinding


class TabOneFragment(val viewModel: TabViewModel) : Fragment(R.layout.fragment_tab_one) {

    private lateinit var binding: FragmentTabOneBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTabOneBinding.bind(view)

        //Criar um observador que atualiza a UI
        val tabContentObserver = Observer<String> { newContent ->
            binding.contentTitle.text = viewModel.getCurrent(newContent)
        }

        viewModel.getContentObserver().observe(viewLifecycleOwner, tabContentObserver)

        binding.btnTabOne.setOnClickListener {
            viewModel.getContentObserver().setValue("Tab one!")
        }
    }

}