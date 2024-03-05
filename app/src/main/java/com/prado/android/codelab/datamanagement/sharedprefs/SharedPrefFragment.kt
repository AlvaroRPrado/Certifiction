package com.prado.android.codelab.datamanagement.sharedprefs

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.annotation.ColorRes
import androidx.fragment.app.Fragment
import com.prado.android.R
import com.prado.android.databinding.FragmentSharedPrefBinding


class SharedPrefFragment : Fragment(R.layout.fragment_shared_pref) {

    companion object {
        const val COUNT_KEY = "COUNT_KEY"
        const val COLOR_KEY = "COLOR_KEY"
        const val SHARED_PREFS = "com.softsuit.codelab.datamanagement.sharedprefs"
    }

    private var mCount = 0
    @ColorRes private var mColor = R.color.black_text_color
    private val mPreferences: SharedPreferences by  lazy { requireActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE) }

    private lateinit var bindind: FragmentSharedPrefBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindind = FragmentSharedPrefBinding.bind(view)

        mPreferences.getInt(COUNT_KEY, 0).let { mCount = it }
        mPreferences.getInt(COLOR_KEY, 0).let { mColor = it }

        if (mCount != 0) bindind.countText.text = String.format("%s", mCount)
        if (mColor != 0) bindind.countText.setTextColor(getColorFromResourceId(mColor))

        bindind.reset.setOnClickListener { reset() }

        bindind.blackButton.setOnClickListener {
            mColor = R.color.black
            updateUi(R.color.black)
        }

        bindind.greenButton.setOnClickListener {
            mColor = R.color.green
            updateUi(R.color.green)
        }
        bindind.blueButton.setOnClickListener {
            mColor = R.color.blue
            updateUi(R.color.blue)
        }
        bindind.redButton.setOnClickListener {
            mColor = R.color.red
            updateUi(R.color.red)
        }

        bindind.count.setOnClickListener {
            bindind.countText.text = (++mCount).toString()
        }
    }

    override fun onPause() {
        super.onPause()
        // NA HORA DO "APERREIO" DA PROVA, DA UM SWITCH PRA CÁ, LEMBRA E VOLTA! :)
        mPreferences.let {
            val editor = it.edit()
            editor.putInt(COUNT_KEY, mCount)
            editor.putInt(COLOR_KEY, mColor)
            editor.apply()
        }
    }
    private fun reset(){
        mCount = 0
        bindind.countText.text = String.format("%s", mCount)

        mColor = android.R.color.white
        bindind.countText.setBackgroundColor(getColorFromResourceId(mColor))

        //NA HORA DO "APERREIO" DA PROVA , DA UM SWITCH PRA CÁ, LEMBRA E VOLATA!
        mPreferences.let {
            val editor = it.edit()
            editor.clear()
            editor.apply()
        }
    }

    private fun updateUi(@ColorRes colorId: Int){
        bindind.countText.setTextColor(getColorFromResourceId(colorId))
    }

    private fun getColorFromResourceId(@ColorRes colorId: Int): Int {
        return if (colorId != 0) requireContext().getColor(colorId) else R.color.black_text_color

    }

}