package com.prado.android.codelab.notifiction

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import com.prado.android.R
import com.prado.android.databinding.FragmentNotificationBinding


private const val NOTIFICATION_ID = 0
private const val PRIMARY_CHANNEL_ID = "primary_notification_channel"
private const val ACTION_UPDATE = "ACTION_UPDATE_NOTIFICATION"
private const val ACTION_CANCEL = "ACTION_CANCEL_NOTIFICATION"
private const val ACTION_DELETE_ALL = "ACTION_DELETED_NOTIFICATIONS"

class NotificationFragment : Fragment(R.layout.fragment_notification) {

    private lateinit var binding: FragmentNotificationBinding


    private lateinit var notificationManager: NotificationManager
    private val notificationReceiver = NotificationReceiver()
    private val dynamicReceiver = DynamicReceiver()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNotificationBinding.bind(view)
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        binding = FragmentNotificationBinding.bind(view)

        setupUiButtonListeners()
        setupUiButtonStates(enableNotify = true, enableUpdate = false, enableCancel = false)
        createNotificationChannel()
        registerNotificationReceiver()
        registerDynamicReceiver(dynamicReceiver)
    }

    private fun setupUiButtonListeners() {
         binding.notify.setOnClickListener { sendNotification()  } // enviar notificação padrão
         binding.update.setOnClickListener { updateNotification()  }// customizar / personalizar
         binding.cancel.setOnClickListener { cancelNotification() }// remover da barra de status

    }

    private fun setupUiButtonStates( // assegurar o estado inicial dos botões
        enableNotify: Boolean,
        enableUpdate: Boolean,
        enableCancel: Boolean
    ) {
        binding.notify.isEnabled = enableNotify
        binding.update.isEnabled = enableUpdate
        binding.cancel.isEnabled = enableCancel

    }

    // A partir do android 8.0 (api 26) temos que definir o canal para que o usuário possa
    // eventualmente desabilitar as notificações do aplicativo através das configurações
    private fun createNotificationChannel() {
        notificationManager =
            requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            // Isso aqui é que aparece nas configurações do aparelho la no seu aplicativo
            val notificationChannel = NotificationChannel(
                PRIMARY_CHANNEL_ID,
                "Mascot Notification", NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Notification from Mascot"
            // serão exibidas se o telefone der suporte a essas coisas
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            // criar o canal com as propriedades definidas
            notificationManager.createNotificationChannel(notificationChannel)
        } else {
            // sua tarefinha de casa :) caso vc tenha um aparelho inferior a api 26
        }
    }

    private fun cancelNotification() {
        notificationManager.cancel(NOTIFICATION_ID)
        setupUiButtonStates(enableNotify = true, enableUpdate = false, enableCancel = false)
    }

    private fun updateNotification() {
        // personalização dinamica da notificação adicionando um icone
        val androidImage = BitmapFactory.decodeResource(resources, R.drawable.ic_notification)
        // atualizando o estilo e o titulo
        val notification = getNotificationBuilder()
            .setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(androidImage)
                    .setBigContentTitle("Notificação atualizada!")
            )
        // atualizar a notificação atual
        notificationManager.notify(NOTIFICATION_ID, notification.build())
        // e habilitar o botão de cancelamento
        setupUiButtonStates(enableNotify = false, enableUpdate = false, enableCancel = true)
    }

    private fun sendNotification() {
        val builder = getNotificationBuilder()

        createNotificationAction(builder, NOTIFICATION_ID, ACTION_UPDATE, "Atualize")
        createNotificationAction(builder, NOTIFICATION_ID, ACTION_CANCEL, "Remover")

        val deleteAllAction = Intent(ACTION_DELETE_ALL) // remove com slide left/right ou lixeira
        val deletedAction = PendingIntent.getBroadcast(
            requireContext(),
            NOTIFICATION_ID,
            deleteAllAction,
            PendingIntent.FLAG_MUTABLE
        )
        builder.setDeleteIntent(deletedAction)

        notificationManager.notify(NOTIFICATION_ID, builder.build())
        // como neste passo aqui a notificação foi enviada, eu deshabilito o botão de enviar
        // e habilito os botões de customização e cancelamento
        setupUiButtonStates(enableNotify = false, enableUpdate = true, enableCancel = true)
    }

    private fun createNotificationAction(
        builder: NotificationCompat.Builder,
        notificationId: Int,
        actionId: String,
        actionTitle: String
    ) {
        val updateActionFilter = Intent(actionId) // for broadcast receiver
        val updateAction = PendingIntent.getBroadcast(
            requireContext(),
            notificationId,
            updateActionFilter,
            PendingIntent.FLAG_MUTABLE
        )
        builder.addAction(
            // mudanças nas notificação desde o Android N
            // esse icone nao aparece mais e esta presente apenas para manter compatibilidade
            // em aparelhos antigos. Em compensação se ganhou mais espaço para os titulos
            // // https://android-developers.googleblog.com/2016/06/notifications-in-android-n.html
            R.drawable.ic_android,
            actionTitle,
            updateAction
        )
    }

    private fun getNotificationBuilder(): NotificationCompat.Builder {
        val notificationIntent = Intent(requireContext(), NotificationFragment::class.java)
        val notificationPendingIntent = PendingIntent.getActivity(
            requireContext(),
            NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_MUTABLE
        )
        return NotificationCompat.Builder(requireContext(), PRIMARY_CHANNEL_ID)
            .setContentTitle("Você recebeu uma notificação!")
            .setContentText("Valeu, já vou me inscrever no canal!")
            .setSmallIcon(R.drawable.ic_notification_update)
            .setContentIntent(notificationPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setAutoCancel(false)
    }




    private fun registerNotificationReceiver() {
        val notificationActionFilters = IntentFilter()
        notificationActionFilters.addAction(ACTION_UPDATE)
        notificationActionFilters.addAction(ACTION_DELETE_ALL)
        notificationActionFilters.addAction(ACTION_CANCEL)
        requireActivity().registerReceiver(notificationReceiver, notificationActionFilters)
    }

    // for broadcast receiver
    override fun onDestroy() {
        requireActivity().unregisterReceiver(notificationReceiver)
        super.onDestroy()
    }

    inner class NotificationReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // Update the notification
            when (intent.action) {
                ACTION_UPDATE -> updateNotification()
                ACTION_CANCEL -> {
                    notificationManager.cancel(NOTIFICATION_ID)
                    setupUiButtonStates(
                        enableNotify = true,
                        enableUpdate = false,
                        enableCancel = false
                    )
                }
                ACTION_DELETE_ALL -> setupUiButtonStates(
                    enableNotify = true,
                    enableUpdate = false,
                    enableCancel = false
                )
            }
        }
    }

    // API level 26 a maioria dos broadcastreceiver sao declarados dinamicamente

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    private fun registerDynamicReceiver(dynamicReceiver: BroadcastReceiver) {
        IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED).also {
            requireActivity().registerReceiver(dynamicReceiver, it)
        }
    }

    private fun unregisterDynamicReceiver() {
        requireActivity().unregisterReceiver(dynamicReceiver)
    }

    override fun onStop() {
        super.onStop()
        unregisterDynamicReceiver()
    }
}