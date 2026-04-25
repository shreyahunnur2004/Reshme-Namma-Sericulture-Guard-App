package com.example.reshmenammapride

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.reshmenammapride.data.AppDatabase
import com.example.reshmenammapride.logic.ClimateEngine
import kotlinx.coroutines.launch

class BatchDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_batch_detail)

        val batchId = intent.getIntExtra("BATCH_ID", -1)
        if (batchId == -1) { finish(); return }

        val db = AppDatabase.getDatabase(this)
        lifecycleScope.launch {
            val batch = db.batchDao().getAllBatches().find { it.id == batchId }
            if (batch == null) { finish(); return@launch }

            val days   = ClimateEngine.getDaysElapsed(batch.startDate)
            val instar = ClimateEngine.getCurrentInstar(days)
            val idealTemp = when (instar) {
                1 -> "26–28°C"
                2 -> "25–27°C"
                3 -> "24–26°C"
                4 -> "23–25°C"
                else -> "22–24°C"
            }
            val idealHumidity = when (instar) {
                1 -> "85–90%"
                2 -> "80–85%"
                3 -> "75–80%"
                4 -> "70–75%"
                else -> "65–70%"
            }

            runOnUiThread {
                findViewById<TextView>(R.id.tvBatchName).text    = batch.batchName
                findViewById<TextView>(R.id.tvBreed).text        = "Breed: ${batch.breed}"
                findViewById<TextView>(R.id.tvDayCount).text     = "Day $days"
                findViewById<TextView>(R.id.tvInstar).text       = "Instar $instar"
                findViewById<TextView>(R.id.tvIdealTemp).text    = "Ideal Temp: $idealTemp"
                findViewById<TextView>(R.id.tvIdealHumidity).text = "Ideal Humidity: $idealHumidity"

                // Show harvest banner if day 20+
                if (ClimateEngine.isHarvestTime(days)) {
                    findViewById<TextView>(R.id.tvHarvestBanner).visibility =
                        android.view.View.VISIBLE
                }

                findViewById<Button>(R.id.btnLogClimate).setOnClickListener {
                    val i = Intent(this@BatchDetailActivity, ClimateEntryActivity::class.java)
                    i.putExtra("BATCH_ID", batchId)
                    i.putExtra("INSTAR", instar)
                    startActivity(i)
                }
            }
        }
    }
}