package com.example.reshmenammapride

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.reshmenammapride.data.AppDatabase
import com.example.reshmenammapride.data.ClimateLog
import com.example.reshmenammapride.logic.ClimateEngine
import kotlinx.coroutines.launch

class ClimateEntryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_climate_entry)

        val batchId = intent.getIntExtra("BATCH_ID", -1)
        val instar  = intent.getIntExtra("INSTAR", 1)

        val etTemp     = findViewById<EditText>(R.id.etTemperature)
        val etHumidity = findViewById<EditText>(R.id.etHumidity)
        val btnSubmit  = findViewById<Button>(R.id.btnSubmitClimate)

        btnSubmit.setOnClickListener {
            val tempStr     = etTemp.text.toString().trim()
            val humidityStr = etHumidity.text.toString().trim()

            // Validation
            if (tempStr.isEmpty()) {
                etTemp.error = "Please enter temperature"
                return@setOnClickListener
            }
            if (humidityStr.isEmpty()) {
                etHumidity.error = "Please enter humidity"
                return@setOnClickListener
            }

            val temp     = tempStr.toFloatOrNull()
            val humidity = humidityStr.toFloatOrNull()

            if (temp == null || temp < 10 || temp > 45) {
                etTemp.error = "Please enter a value between 10 and 45"
                return@setOnClickListener
            }
            if (humidity == null || humidity < 50 || humidity > 95) {
                etHumidity.error = "Please enter a value between 50 and 95"
                return@setOnClickListener
            }

            // Run climate logic
            val result = ClimateEngine.evaluate(temp, humidity, instar)

            // Save to database
            val db = AppDatabase.getDatabase(this)
            lifecycleScope.launch {
                val log = ClimateLog(
                    batchId     = batchId,
                    temperature = temp,
                    humidity    = humidity,
                    timestamp   = System.currentTimeMillis(),
                    status      = result.status.name,
                    advice      = result.advice
                )
                db.climateLogDao().insertLog(log)

                runOnUiThread {
                    val i = Intent(this@ClimateEntryActivity, AdviceActivity::class.java)
                    i.putExtra("STATUS", result.status.name)
                    i.putExtra("ADVICE", result.advice)
                    i.putExtra("TEMP", temp)
                    i.putExtra("HUMIDITY", humidity)
                    i.putExtra("INSTAR", instar)
                    startActivity(i)
                    finish()
                }
            }
        }
    }
}