package com.example.reshmenammapride

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AdviceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_advice)

        val status   = intent.getStringExtra("STATUS") ?: "GREEN"
        val advice   = intent.getStringExtra("ADVICE") ?: ""
        val temp     = intent.getFloatExtra("TEMP", 0f)
        val humidity = intent.getFloatExtra("HUMIDITY", 0f)
        val instar   = intent.getIntExtra("INSTAR", 1)

        val tvCircle   = findViewById<TextView>(R.id.tvStatusCircle)
        val tvLabel    = findViewById<TextView>(R.id.tvStatusLabel)
        val tvInstar   = findViewById<TextView>(R.id.tvInstarLabel)
        val tvTemp     = findViewById<TextView>(R.id.tvTempReading)
        val tvHumidity = findViewById<TextView>(R.id.tvHumidityReading)
        val tvAdvice   = findViewById<TextView>(R.id.tvAdvice)
        val btnBack    = findViewById<Button>(R.id.btnBackHome)

        // Set readings
        tvTemp.text     = "${temp}°C"
        tvHumidity.text = "${humidity}%"
        tvInstar.text   = "Instar $instar"
        tvAdvice.text   = advice

        // Set color and label based on status
        when (status) {
            "GREEN" -> {
                tvCircle.text  = "✓"
                tvLabel.text   = "SAFE"
                tvLabel.setTextColor(Color.parseColor("#2E7D32"))
                tvCircle.background.setTint(Color.parseColor("#2E7D32"))
            }
            "ORANGE" -> {
                tvCircle.text  = "!"
                tvLabel.text   = "CAUTION"
                tvLabel.setTextColor(Color.parseColor("#E65100"))
                tvCircle.background.setTint(Color.parseColor("#FF6F00"))
            }
            "RED" -> {
                tvCircle.text  = "✕"
                tvLabel.text   = "DANGER"
                tvLabel.setTextColor(Color.parseColor("#B71C1C"))
                tvCircle.background.setTint(Color.parseColor("#C62828"))
            }
        }

        btnBack.setOnClickListener {
            val i = Intent(this, MainActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(i)
            finish()
        }
    }
}