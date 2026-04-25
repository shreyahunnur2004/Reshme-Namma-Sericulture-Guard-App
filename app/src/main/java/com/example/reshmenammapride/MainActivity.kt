package com.example.reshmenammapride

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.reshmenammapride.data.AppDatabase
import com.example.reshmenammapride.logic.ClimateEngine
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var tvBatchInfo: TextView
    private lateinit var tvInstarInfo: TextView
    private lateinit var tvNotice: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvBatchInfo  = findViewById(R.id.tvActiveBatchInfo)
        tvInstarInfo = findViewById(R.id.tvInstarInfo)
        tvNotice     = findViewById(R.id.tvClimateNotice)

        val btnNewBatch    = findViewById<Button>(R.id.btnNewBatch)
        val btnViewBatches = findViewById<Button>(R.id.btnViewBatches)

        btnNewBatch.setOnClickListener {
            startActivity(Intent(this, NewBatchActivity::class.java))
        }

        btnViewBatches.setOnClickListener {
            startActivity(Intent(this, BatchListActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        loadActiveBatch()
    }

    private fun loadActiveBatch() {
        val db = AppDatabase.getDatabase(this)
        lifecycleScope.launch {
            val activeBatch = db.batchDao().getActiveBatch()
            runOnUiThread {
                if (activeBatch != null) {
                    val days   = ClimateEngine.getDaysElapsed(activeBatch.startDate)
                    val instar = ClimateEngine.getCurrentInstar(days)
                    tvBatchInfo.text  = "${activeBatch.batchName} — Day $days"
                    tvInstarInfo.text = "Currently in Instar $instar"
                    tvNotice.visibility = View.VISIBLE
                } else {
                    tvBatchInfo.text    = "No active batch"
                    tvInstarInfo.text   = ""
                    tvNotice.visibility = View.GONE
                }
            }
        }
    }
}