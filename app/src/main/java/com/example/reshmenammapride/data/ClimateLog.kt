package com.example.reshmenammapride.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "climate_logs")
data class ClimateLog(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val batchId: Int,
    val temperature: Float,
    val humidity: Float,
    val timestamp: Long,
    val advice: String,
    val status: String
)