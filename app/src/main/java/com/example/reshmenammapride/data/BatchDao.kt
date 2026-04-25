package com.example.reshmenammapride.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BatchDao {
    @Insert
    suspend fun insertBatch(batch: Batch): Long

    @Query("SELECT * FROM batches ORDER BY startDate DESC")
    suspend fun getAllBatches(): List<Batch>

    @Query("SELECT * FROM batches WHERE isActive = 1 ORDER BY id DESC LIMIT 1")
    suspend fun getActiveBatch(): Batch?

    @Query("UPDATE batches SET isActive = 0 WHERE id = :batchId")
    suspend fun deactivateBatch(batchId: Int)
}