package com.prado.android.codelab.userinterface.usernavigation.tab

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TabViewModel : ViewModel() {

    var currentTabContent: String = "Current on Tab one!"
    var currentTab: String = "Tab one!"
    private val currentTabObserver: MutableLiveData<String> = MutableLiveData()

    // lógica é programada no view model a view geralmente so é atualizada
    // separando responsabilidades CoC (cerparation of concern)
    fun getCurrent(tab: String): String {
        if (currentTab != tab){
            currentTabContent = "Toque anterior: $currentTab\nùltimo toque: $tab"
            currentTab = tab
        }
        return currentTabContent
    }

    fun getContentObserver(): MutableLiveData<String> = currentTabObserver

}