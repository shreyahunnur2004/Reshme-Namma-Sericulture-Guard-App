package com.example.reshmenammapride.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "batches")
data class Batch(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val batchName: String,
    val breed: String,
    val rearingHouse: String,
    val startDate: Long,
    val isActive: Boolean = true
)