package com.prado.android.codelab.userinterface.themes

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.prado.android.R
import com.prado.android.databinding.FragmentThemeBinding

class ThemeFragment : Fragment(R.layout.fragment_theme) {

    private var scoreTeam1 = 0
    private var scoreTeam2 = 0
    private var stateScore1 = "Team 1 Score"
    private var stateScore2 = "Team 2 Score"

    private lateinit var binding: FragmentThemeBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentThemeBinding.bind(view)

        // possibilitar mudanças de orientações de tela
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

        setHasOptionsMenu(true)

        //Recuperar valores em rotações ou quando o tema recriar o app
        if (savedInstanceState != null){
            scoreTeam1 = savedInstanceState.getInt(stateScore1)
            scoreTeam2 = savedInstanceState.getInt(stateScore2)
            binding.score1.text = scoreTeam1.toString()
            binding.score2.text = scoreTeam2.toString()
        }

        binding.decreaseButtonTeam1.setOnClickListener {
            binding.score1.text = (--scoreTeam1).toString()
        }
        binding.decreaseButtonTeam2.setOnClickListener {
            binding.score2.text = (--scoreTeam2).toString()
        }
        binding.increaseButtonTeam1.setOnClickListener {
            binding.score1.text = (++scoreTeam1).toString()
        }
        binding.increaseButtonTeam2.setOnClickListener {
            binding.score2.text = (++scoreTeam2).toString()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        requireActivity().menuInflater.inflate(R.menu.theme_menu, menu)
        val nightMode = AppCompatDelegate.getDefaultNightMode()
        if (nightMode == AppCompatDelegate.MODE_NIGHT_YES){
            menu.findItem(R.id.night_mode).setTitle(R.string.day_mode)
        }else{
            menu.findItem(R.id.night_mode).setTitle(R.string.night_mode)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.night_mode){
            val nightMode = AppCompatDelegate.getDefaultNightMode()
            if (nightMode == AppCompatDelegate.MODE_NIGHT_YES){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            // Recreate the activity for the theme change to take effect.
            requireActivity().recreate()
        }
        return true
    }

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onDetach() {
        super.onDetach()
        // assegurar que tudo esta como era antes
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }
}