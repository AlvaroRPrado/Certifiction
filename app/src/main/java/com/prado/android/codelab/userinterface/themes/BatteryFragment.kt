package com.prado.android.codelab.userinterface.themes

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.prado.android.R
import com.prado.android.databinding.FragmentBatteryBinding


class BatteryFragment : Fragment(R.layout.fragment_battery) {

    private lateinit var binding: FragmentBatteryBinding
    private var imageLevel = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBatteryBinding.bind(view)

        binding.minusButton.setOnClickListener {
            --imageLevel
            if (imageLevel < 0) imageLevel = 0
            // alterar ícones dinâmicamente SUPER SIMPLES
            // exibir score de um jogo, quantidade ou barra de vidas
            // trocar îcones dependendo da estação do ano etc.
            binding.batteryLevelImage.setImageLevel(imageLevel)
        }

        binding.plusButton.setOnClickListener {
            ++imageLevel
            if (imageLevel > 3) imageLevel = 3
            binding.batteryLevelImage.setImageLevel(imageLevel)
        }
    }

}