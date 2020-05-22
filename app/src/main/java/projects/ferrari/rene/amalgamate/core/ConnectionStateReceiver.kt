package projects.ferrari.rene.amalgamate.core

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest

object ConnectionStateReceiver : NetworkCallback() {
    private var listener: ConnectionStateListener? = null
    private val networkRequest: NetworkRequest = NetworkRequest.Builder()
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .build()
    private val connectedNetworksList = mutableListOf<Int>()

    fun register(context: Context, listener: ConnectionStateListener) {
        getConnectivityManager(
            context
        )
            .registerNetworkCallback(networkRequest, this)
        ConnectionStateReceiver.listener = listener
    }

    fun unregister(context: Context, listener: ConnectionStateListener) {
        getConnectivityManager(
            context
        ).unregisterNetworkCallback(this)
        ConnectionStateReceiver.listener = listener
    }

    private fun getConnectivityManager(context: Context): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        connectedNetworksList.add(network.hashCode())
        listener?.onConnected()
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        connectedNetworksList.remove(network.hashCode())

        if (connectedNetworksList.size == 0) {
            listener?.onDisconnected()
        }
    }
}

interface ConnectionStateListener {
    fun onConnected()
    fun onDisconnected()
}
