package projects.ferrari.rene.amalgamate.core

import android.app.Application
import android.util.Log
import io.reactivex.rxjava3.exceptions.UndeliverableException
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import okhttp3.OkHttpClient

val client = OkHttpClient.Builder().build()

// Global indicator if phone has a network connection
var isConnected = false
    private set

class App : Application(),
    ConnectionStateListener {

    override fun onCreate() {
        super.onCreate()
        AppThemer.theme(this)
        setRxErrorHandler()
        ConnectionStateReceiver.register(
            this,
            this
        )
    }

    override fun onTerminate() {
        super.onTerminate()
        ConnectionStateReceiver.unregister(
            this,
            this
        )
    }

    override fun onConnected() {
        isConnected = true
    }

    override fun onDisconnected() {
        isConnected = false
    }

    private fun setRxErrorHandler() {
        RxJavaPlugins.setErrorHandler { error ->
            if (error is UndeliverableException) {
                Log.w(
                    "Undeliverable",
                    "Undeliverable exception received, not sure what to do ${error.cause}"
                )
            }
        }
    }
}
