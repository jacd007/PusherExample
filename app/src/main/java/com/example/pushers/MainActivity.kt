package com.example.pushers
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import com.pusher.client.channel.PusherEvent
import com.pusher.client.channel.SubscriptionEventListener
import com.pusher.client.connection.ConnectionEventListener
import com.pusher.client.connection.ConnectionState
import com.pusher.client.connection.ConnectionStateChange
import java.lang.Exception
import org.json.JSONObject




private const val TAG = "PusherTask"

private const val cluster = "mt1"
private const val apiKey = "ee7fa51c0ac2f040bda4"
private const val channelName = "noticias"
private const val eventName = "nuevos"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var tv: TextView = findViewById(R.id.textview)


        val options = PusherOptions()
        options.setCluster(cluster)
        val pusher = Pusher(apiKey, options)
        pusher.connect(object : ConnectionEventListener {
            override fun onConnectionStateChange(change: ConnectionStateChange) {
                Log.i(
                    "Pusher", "State changed from " + change.previousState +
                            " to " + change.currentState
                )
            }

            override fun onError(message: String, code: String, e: Exception) {
                Log.i(
                    "Pusher", """
     There was a problem connecting! 
     code: $code
     message: $message
     Exception: $e
     """.trimIndent()
                )
            }
        }, ConnectionState.ALL)
        val channel = pusher.subscribe(channelName)
        channel.bind(
            eventName
        ) { event ->
            Log.i("Pusher", "Received event with data: $event")
            try {
                val data:String = event.data
                //TODO: aqui haces lo que te de la gana XD

                // si quieres actualizar algo en el hilo principal por ejemplo este textview
                runOnUiThread {
                    tv.text = data
                }

                // Mi clase abstracta para notificaciones push automaticas de casi cualquier tipo
                NotificationsTask.createNotificacions(this, "TITLE", data, "CONT_INFO", 0)

            }catch (e: Exception){}


        }
    }
}

