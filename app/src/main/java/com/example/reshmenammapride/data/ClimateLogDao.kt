package com.example.reshmenammapride.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ClimateLogDao {
    @Insert
    suspend fun insertLog(log: ClimateLog)

    @Query("SELECT * FROM climate_logs WHERE batchId = :batchId ORDER BY timestamp DESC")
    suspend fun getLogsForBatch(batchId: Int): List<ClimateLog>

    @Query("SELECT * FROM climate_logs WHERE batchId = :batchId ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatestLog(batchId: Int): ClimateLog?
}