package com.ragulan.shakeapp.shakeapp

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Vibrator
import android.support.v7.app.AppCompatActivity
import android.widget.Toast

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() , SensorEventListener {
    private var sensor: Sensor? = null
    private var sensorManager: SensorManager? = null
    private var xOld = 0.0
    private var yOld = 0.0
    private var zOld = 0.0
    private var threadShould = 1000.0
    private var oldTime: Long = 0

    override fun onAccuracyChanged(sensor: Sensor? , accuracy: Int) {

    }

    override fun onSensorChanged(event: SensorEvent?) {
        val x = event!!.values[0]
        val y = event.values[1]
        val z = event.values[2]
        val currentTime = System.currentTimeMillis()
        if ((currentTime - oldTime) > 100) {
            val timeDiff = currentTime - oldTime
            oldTime = currentTime
            val speed = Math.abs(x + y + z - xOld - yOld - zOld) / timeDiff * 10000
            if (speed > threadShould) {
                val v = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                v.vibrate(100)
                Toast.makeText(this , "VIBRATOR_SERVICE" , Toast.LENGTH_SHORT).show()
            }

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    override fun onResume() {
        super.onResume()
        sensorManager!!.registerListener(this , sensor , SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager!!.unregisterListener(this)
    }
}
