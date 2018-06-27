package es.elb4t.apirestfullrp3

import android.content.ContentValues.TAG
import android.util.Log
import com.google.android.things.pio.Gpio
import com.google.android.things.pio.PeripheralManager
import java.io.IOException


class LEDModel {

    var service: PeripheralManager? = null
    private var mLedGpio: Gpio? = null
    private val PIN_LED = "BCM18"


    constructor() {
        service = PeripheralManager.getInstance()
        try {
            mLedGpio = service?.openGpio(PIN_LED)
            mLedGpio?.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)
        } catch (e: Exception) {
            Log.e(TAG, "Error en el API PeripheralIO", e)
        }
    }

    companion object {
        private var instance: LEDModel? = null

        fun getInstance(): LEDModel {
            if (instance == null) {
                instance = LEDModel()
            }
            return instance as LEDModel
        }

        fun setState(state: Boolean): Boolean {
            try {
                getInstance().mLedGpio?.value = state
                return true
            } catch (e: IOException) {
                Log.e(TAG, "Error en el API PeripheralIO", e)
                return false
            }
        }

        fun getState(): Boolean {
            var value = false
            try {
                value = getInstance().mLedGpio?.value!!
            } catch (e: IOException) {
                Log.e(TAG, "Error en el API PeripheralIO", e)
            }
            return value
        }
    }
}