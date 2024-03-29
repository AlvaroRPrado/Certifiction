package com.prado.android.codelab.userinterface.activityintents

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.prado.android.R
import com.prado.android.databinding.FragmentSendBinding
import com.prado.android.util.navTo


class SendFragment : Fragment(R.layout.fragment_send) {

    companion object{
        const val REPLY = "REPLY"
    }
     private lateinit var binding: FragmentSendBinding
     private var reply: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSendBinding.bind(view)

        binding.buttonSend.setOnClickListener {
            val args = Bundle()
            args.putString(ReplyFragment.SEND, binding.inputSend.text.toString())
            navTo(R.id.action_sendFragment_to_replyFragment)
        }

        // obtem a mensagem enviada pelo fragment
        arguments?.let {
            reply = it.getString(REPLY, null)
        }

        // exibe a mensagem obtida
        reply.let {
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