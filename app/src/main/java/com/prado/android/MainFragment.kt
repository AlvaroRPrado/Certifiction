package com.prado.android

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.prado.android.databinding.FragmentMainBinding
import com.prado.android.util.navTo

/** Main Menu Study Guide */
class MainFragment : Fragment(R.layout.fragment_main) {

    private lateinit var binding: FragmentMainBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // +-------------------------------------------------------------------------------------+
        // | Orientação do app: Portrait(em pé) or Landscape(deitado) or Unspecified (os dois)   |
        // +-------------------------------------------------------------------------------------+
        // if you not define it in the manifest, you could also do it that way programmatically
        //requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        binding = FragmentMainBinding.bind(view)
        binding.codelabToastSnake.setOnClickListener { navTo(R.id.toastSnakeFragment) }
        binding.codelabNotification.setOnClickListener { navTo(R.id.notificationFragment) }
        binding.codelabWorkManager.setOnClickListener { navTo(R.id.selectImageFragment) }
        binding.codelabMaterialComponents.setOnClickListener { navTo(R.id.materialComponentsFragment) }
        binding.codelabInteractiveUi.setOnClickListener { navTo(R.id.interactiveUiFragment) }
        binding.codelabActivitiesIntents.setOnClickListener { navTo(R.id.sendFragment) }
        binding.codelabRecyclerview.setOnClickListener{ navTo(R.id.recyclerViewFragment) }
        binding.codelabRecyclerviewWithPaging.setOnClickListener { navTo(R.id.recyclerViewPagingFragment) }
        binding.codelabAccessibility.setOnClickListener { navTo(R.id.accessibilityFragment) }

    }
}
