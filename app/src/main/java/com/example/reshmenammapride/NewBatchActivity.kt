package com.example.reshmenammapride

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.reshmenammapride.data.AppDatabase
import com.example.reshmenammapride.data.Batch
import kotlinx.coroutines.launch
import java.util.Calendar

class NewBatchActivity : AppCompatActivity() {

    private var selectedDateMillis: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_batch)

        val etBatchName  = findViewById<EditText>(R.id.etBatchName)
        val etBreed      = findViewById<EditText>(R.id.etBreed)
        val btnPickDate  = findViewById<Button>(R.id.btnPickDate)
        val btnSave      = findViewById<Button>(R.id.btnSaveBatch)

        // Date Picker
        btnPickDate.setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(
                this,
                { _, year, month, day ->
                    cal.set(year, month, day, 0, 0, 0)
                    cal.set(Calendar.MILLISECOND, 0)
                    selectedDateMillis = cal.timeInMillis
                    btnPickDate.text = "$day/${month + 1}/$year"
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // Save Batch
        btnSave.setOnClickListener {
            val name  = etBatchName.text.toString().trim()
            val breed = etBreed.text.toString().trim()

            // Validation
            if (name.isEmpty()) {
                etBatchName.error = "Please enter a batch name"
                return@setOnClickListener
            }
            if (breed.isEmpty()) {
                etBreed.error = "Please enter the breed"
                return@setOnClickListener
            }
            if (selectedDateMillis == 0L) {
                Toast.makeText(this, "Please select a start date", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Save to database
            val db = AppDatabase.getDatabase(this)
            lifecycleScope.launch {
                val batch = Batch(
                    batchName    = name,
                    breed        = breed,
                    rearingHouse = name,
                    startDate    = selectedDateMillis,
                    isActive     = true
                )
                db.batchDao().insertBatch(batch)
                runOnUiThread {
                    Toast.makeText(
                        this@NewBatchActivity,
                        "Batch saved successfully!",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish() // go back to Home
                }
            }
        }
    }
}