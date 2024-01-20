package com.prado.android.codelab.userinterface.activityintents

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.prado.android.R
import com.prado.android.databinding.FragmentReplyBinding
import com.prado.android.util.navTo


class ReplyFragment : Fragment(R.layout.fragment_reply) {

    companion object{
        const val SEND = "SEND"
    }

    private lateinit var binding: FragmentReplyBinding
    private var sent: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentReplyBinding.bind(view)

        binding.buttonReply.setOnClickListener {
            //navegação por fragment
            val args = Bundle()
            args.putString(SendFragment.REPLY, binding.inputReply.text.toString())
            navTo(R.id.action_replyFragment_to_sendFragment)


            // naveganção por activity
            //startActivity(UserNavigationActivity::class.java)
        }

        //obtem a mensagem enviada pelo fragment
        arguments?.let {
            sent = it.getString(SEND, null)
        }

        //exibe a mensagem obtida
        sent?.let {
            binding.textMessage.text = it
        }


    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity).supportActionBar?.show()
    }

}