package com.example.reshmenammapride

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reshmenammapride.data.AppDatabase
import kotlinx.coroutines.launch

class BatchListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_batch_list)

        val recycler = findViewById<RecyclerView>(R.id.recyclerBatches)
        recycler.layoutManager = LinearLayoutManager(this)

        val db = AppDatabase.getDatabase(this)
        lifecycleScope.launch {
            val batches = db.batchDao().getAllBatches()
            if (batches.isEmpty()) {
                runOnUiThread {
                    Toast.makeText(
                        this@BatchListActivity,
                        "No batches found. Create one first!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } else {
                runOnUiThread {
                    recycler.adapter = BatchAdapter(batches) { batch ->
                        val intent = Intent(
                            this@BatchListActivity,
                            BatchDetailActivity::class.java
                        )
                        intent.putExtra("BATCH_ID", batch.id)
                        startActivity(intent)
                    }
                }
            }
        }
    }
}