package com.prado.android.codelab.datamanagement.appsettings

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import com.prado.android.R
import com.prado.android.databinding.FragmentAppSettingsBinding
import com.prado.android.util.navTo
import com.prado.android.util.toast


class AppSettingsFragment : Fragment(R.layout.fragment_app_settings) {

    lateinit var binding: FragmentAppSettingsBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAppSettingsBinding.bind(view)


        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
       inflater.inflate(R.menu.menu_settings, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {


        when (item.itemId){
            R.id.action_settings -> {
                navTo(R.id.settingsFragment)
                return true
            }
            else -> Unit
        }

        return super.onOptionsItemSelected(item)

    }

    //OBTENDO VALOR DAS CONFIGURAÇÕES FEITAS
    override fun onResume() {
        super.onResume()
        //IMPORTANTE
        val shaerePref = android.preference.PreferenceManager.getDefaultSharedPreferences(requireContext())

        val assinatura = shaerePref.getString(KEY_PREF_SIGNATURE, "!") ?: ""
        if (assinatura.isNotEmpty() && assinatura != "Não definida")toast(assinatura)

        val resposta = shaerePref.getString(KEY_PREF_REPLY, "!") ?: ""
        if (resposta.isNotEmpty()) toast(resposta)

        val sincronizacao = shaerePref.getBoolean(KEY_PREF_SYNC, false)
        if (sincronizacao) toast("sincronizacao ativada")

        val anexos = shaerePref.getBoolean(KEY_PREF_ATTACHMENT, false)
        if (anexos) toast("anexos ativados")
    }
}