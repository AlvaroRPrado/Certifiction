package com.prado.android.codelab.userinterface.usernavigation.oldschool

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.prado.android.databinding.ActivityUserNavigationBinding

class UserNavigationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserNavigationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserNavigationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setSupportActionBar(binding.toolbar)


        binding.fab.setOnClickListener {
            Snackbar.make(view, "Replece with own action", Snackbar.LENGTH_LONG).setAction(
                "Action", null
            ).show()
        }
    }
}