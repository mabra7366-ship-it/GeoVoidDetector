package com.example.geovoiddetector

import android.app.Activity
import android.os.Bundle
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.widget.TextView
import android.widget.LinearLayout
import android.graphics.Color
import android.view.Gravity
import kotlin.math.sqrt
import java.util.Locale

class MainActivity : Activity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private lateinit var display: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        display = TextView(this)
        display.textSize = 22f
        display.gravity = Gravity.CENTER
        display.setTextColor(Color.BLACK)
        display.text = "جاري تشغيل حساس المجال المغناطيسي..."

        val layout = LinearLayout(this)
        layout.gravity = Gravity.CENTER
        layout.setBackgroundColor(Color.WHITE)
        layout.addView(display)
        setContentView(layout)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

        if (magneticSensor != null) {
            sensorManager.registerListener(
                this,
                magneticSensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        } else {
            display.text = "الهاتف لا يحتوي على حساس مغناطيسي"
        }
    }

    override fun onSensorChanged(event: SensorEvent) {
        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]

        val total = sqrt(x*x + y*y + z*z)

        display.text = String.format(
            Locale.US,
            "كاشف المجال المغناطيسي\n\nX: %.2f µT\nY: %.2f µT\nZ: %.2f µT\n\nالمجموع: %.2f µT",
            x, y, z, total
        )
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    
    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
    }
}
