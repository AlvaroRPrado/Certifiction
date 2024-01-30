package com.prado.android.codelab.userinterface.usernavigation.tab

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.prado.android.R
import com.prado.android.databinding.FragmentTabHostBinding


class TabHostFragment : Fragment(R.layout.fragment_tab_host) {

    private lateinit var binding: FragmentTabHostBinding
    private val viewModel: TabViewModel by viewModels()

    // Outra maneira de definir o view model
     //private lateinit var viewModel: TabViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTabHostBinding.bind(view)

        // outra maneira de inicializar o view model
        // viewModel = ViewModelProvider(this).get(TabViewModel::class.java)

        //setup tabs
        val tabLayout = binding.tabLayout
        val titles = arrayListOf(R.string.tab_label1, R.string.tab_label2, R.string.tab_label3)
        val  pager = binding.pager
        pager.adapter = TabPagerAdapter2(titles.size, this)

        TabLayoutMediator(tabLayout, pager){ tab , position ->
            tab.text = getString(titles[position])
        }.attach()
        //        Old School way before ViewPager2
//        tabLayout.tabGravity = TabLayout.GRAVITY_FILL
//        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_label1))
//        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_label2))
//        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_label3))
//
//
//        pager.adapter = TabPagerAdapter(tabLayout.tabCount, viewModel, childFragmentManager)
//        pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
//        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
//            override fun onTabSelected(tab: TabLayout.Tab?) {
//               tab?.let { pager.currentItem = it.position }
//            }
//            override fun onTabUnselected(tab: TabLayout.Tab?) = Unit
//            override fun onTabReselected(tab: TabLayout.Tab?) = Unit
//        })
//        tabLayout.setupWithViewPager(pager)

    }

    fun getTabViewModel(): TabViewModel = viewModel


}