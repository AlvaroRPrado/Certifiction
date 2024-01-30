package com.prado.android.codelab.userinterface.usernavigation.drawer

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.prado.android.R
import com.prado.android.databinding.FragmentDrawerBinding

class DrawerFragment : Fragment(R.layout.fragment_drawer) {

    //implementar o navigation drawer
    private lateinit var binding: FragmentDrawerBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDrawerBinding.bind(view)
    }

}