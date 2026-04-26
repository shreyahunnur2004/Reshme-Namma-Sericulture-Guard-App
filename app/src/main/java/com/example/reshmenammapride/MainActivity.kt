package com.example.reshmenammapride

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.reshmenammapride.data.AppDatabase
import com.example.reshmenammapride.logic.ClimateEngine
import kotlinx.coroutines.launch
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var tvBatchInfo: TextView
    private lateinit var tvInstarInfo: TextView
    private lateinit var tvNotice: TextView
    private lateinit var tvTip: TextView
    private lateinit var tvLastTemp: TextView
    private lateinit var tvLastHumidity: TextView
    private lateinit var tvLastStatus: TextView
    private lateinit var cardLastLog: View
    private lateinit var progressBar: ProgressBar
    private lateinit var tvProgressLabel: TextView

    private val tips = listOf(
        "Keep rearing trays clean to prevent disease spread.",
        "Always wash hands before handling silkworms.",
        "Fresh mulberry leaves give better growth in Instar 3–5.",
        "Avoid sudden temperature changes — silkworms are sensitive.",
        "Remove uneaten leaves daily to keep humidity stable.",
        "Good ventilation reduces fungal infections in the rearing house.",
        "Silkworms in Instar 5 eat 70% of their total food intake.",
        "Check humidity every morning for best results.",
        "Maintain rearing house cleanliness to improve cocoon quality.",
        "Dim lighting helps silkworms stay calm and feed better."
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvBatchInfo     = findViewById(R.id.tvActiveBatchInfo)
        tvInstarInfo    = findViewById(R.id.tvInstarInfo)
        tvNotice        = findViewById(R.id.tvClimateNotice)
        tvTip           = findViewById(R.id.tvTipOfDay)
        tvLastTemp      = findViewById(R.id.tvLastTemp)
        tvLastHumidity  = findViewById(R.id.tvLastHumidity)
        tvLastStatus    = findViewById(R.id.tvLastStatus)
        cardLastLog     = findViewById(R.id.cardLastLog)
        progressBar     = findViewById(R.id.progressBatch)
        tvProgressLabel = findViewById(R.id.tvProgressLabel)

        val btnNewBatch    = findViewById<LinearLayout>(R.id.btnNewBatch)
        val btnViewBatches = findViewById<LinearLayout>(R.id.btnViewBatches)

        btnNewBatch.setOnClickListener {
            startActivity(Intent(this, NewBatchActivity::class.java))
        }
        btnViewBatches.setOnClickListener {
            startActivity(Intent(this, BatchListActivity::class.java))
        }

        // Show daily tip based on day of year
        val dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        tvTip.text = tips[dayOfYear % tips.size]
    }

    override fun onResume() {
        super.onResume()
        loadActiveBatch()
    }

    private fun loadActiveBatch() {
        val db = AppDatabase.getDatabase(this)
        lifecycleScope.launch {
            val activeBatch = db.batchDao().getActiveBatch()
            val lastLog     = activeBatch?.let {
                db.climateLogDao().getLogsForBatch(it.id).lastOrNull()
            }

            runOnUiThread {
                if (activeBatch != null) {
                    val days   = ClimateEngine.getDaysElapsed(activeBatch.startDate)
                    val instar = ClimateEngine.getCurrentInstar(days)

                    tvBatchInfo.text    = "${activeBatch.batchName} — Day $days"
                    tvInstarInfo.text   = "Currently in Instar $instar"
                    tvNotice.visibility = View.GONE

                    // Progress bar
                    val progress = ((days.toFloat() / 20f) * 100).toInt().coerceAtMost(100)
                    progressBar.progress    = progress
                    tvProgressLabel.text    = "Day $days of 20 — ${progress}% complete"
                    findViewById<View>(R.id.cardProgress).visibility = View.VISIBLE

                    // Last log card
                    if (lastLog != null) {
                        tvLastTemp.text     = "${lastLog.temperature}°C"
                        tvLastHumidity.text = "${lastLog.humidity}%"
                        tvLastStatus.text   = when (lastLog.status) {
                            "GREEN"  -> "Safe ✓"
                            "ORANGE" -> "Caution !"
                            else     -> "Danger ✕"
                        }
                        tvLastStatus.setTextColor(
                            when (lastLog.status) {
                                "GREEN"  -> android.graphics.Color.parseColor("#3D6B50")
                                "ORANGE" -> android.graphics.Color.parseColor("#C4825A")
                                else     -> android.graphics.Color.parseColor("#C0392B")
                            }
                        )
                        cardLastLog.visibility = View.VISIBLE
                    } else {
                        cardLastLog.visibility = View.GONE
                        tvNotice.visibility    = View.VISIBLE
                        tvNotice.text          = "⚠️  No climate logged yet for this batch"
                    }

                } else {
                    tvBatchInfo.text    = "No active batch"
                    tvInstarInfo.text   = "Tap below to start your first batch"
                    tvNotice.visibility = View.GONE
                    cardLastLog.visibility              = View.GONE
                    findViewById<View>(R.id.cardProgress).visibility = View.GONE
                }
            }
        }
    }
}