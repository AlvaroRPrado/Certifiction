package com.prado.android.codelab.userinterface.menu

import android.app.AlertDialog
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import com.prado.android.R
import com.prado.android.databinding.FragmentOrderBinding
import com.prado.android.util.navTo
import com.prado.android.util.snake
import com.prado.android.util.toast

class OrderFragment : Fragment(R.layout.fragment_order) {

    private lateinit var binding: FragmentOrderBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentOrderBinding.bind(view)

        setHasOptionsMenu(true)
        registerForContextMenu(binding.title)

        binding.alertButton.setOnClickListener {
            AlertDialog
                .Builder(requireContext())
                .setTitle("Alerta")
                .setMessage("Olá")
                .setPositiveButton("Ok") { _, _ -> toast("Pressed Ok") }
                .setPositiveButton("Cancel") { _, _ -> snake(it, "Pressed Cancel")}
                .show()
        }
        binding.datePickerButton.setOnClickListener {
            DatePickerFragment { result -> binding.selectedDateText.text = result  }
                .show(childFragmentManager, "datePicker")
        }
        binding.timePickerButton.setOnClickListener {
            TimePickerFragment { result -> binding.selectedTimeText.text = result  }
                .show(childFragmentManager, "TimePicker")
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        requireActivity().menuInflater.inflate(R.menu.menu_main, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.action_status -> {
                toast(getString(R.string.action_status_message))
                return true
            }
            R.id.action_favorites -> {
                toast(getString(R.string.action_favorites_message))
                return true
            }
            R.id.action_contact -> {
                toast(getString(R.string.action_contact_message))
            }
            else -> Unit
        }

        return super.onContextItemSelected(item)
    }

}