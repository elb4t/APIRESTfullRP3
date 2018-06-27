package es.elb4t.apirestfullrp3

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.util.Log
import org.restlet.Component
import org.restlet.data.Protocol
import org.restlet.engine.Engine
import org.restlet.engine.adapter.HttpServerHelper
import org.restlet.routing.Router


class RESTfulService() : IntentService("RESTfulService") {
    private var mComponent: Component? = null

    init {
        Engine.getInstance().registeredServers.clear()
        Engine.getInstance().registeredServers.add(HttpServerHelper(null))
        mComponent = Component()
        val router = Router(mComponent?.context?.createChildContext())
        // Configuración del webserver
        mComponent?.servers?.add(Protocol.HTTP, 8080)
        mComponent?.defaultHost?.attach("/rest", router)
        router.attach("/led", LEDResource::class.java)
    }

    companion object {
        private val ACTION_START = "es.elb4t.apirestfullrp3.START"
        private val ACTION_STOP = "es.elb4t.apirestfullrp3.STOP"

        fun startServer(context: Context) {
            val intent = Intent(context, RESTfulService::class.java)
            intent.action = ACTION_START
            context.startService(intent)
        }

        fun stopServer(context: Context) {
            val intent = Intent(context, RESTfulService::class.java)
            intent.action = ACTION_STOP
            context.startService(intent)
        }
    }

    override fun onHandleIntent(intent: Intent?) {
        if (intent != null) {
            val action = intent.action
            if (ACTION_START.equals(action)) {
                handleStart()
            } else if (ACTION_STOP.equals(action)) {
                handleStop()
            }
        }
    }

    private fun handleStart() {
        try {
            mComponent?.start()
        } catch (e: Exception) {
            Log.e(javaClass.simpleName, e.toString())
        }
    }

    private fun handleStop() {
        try {
            mComponent?.stop()
        } catch (e: Exception) {
            Log.e(javaClass.simpleName, e.toString())
        }
    }
}