package com.example.reshmenammapride

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.reshmenammapride.data.Batch
import com.example.reshmenammapride.logic.ClimateEngine

class BatchAdapter(
    private val batches: List<Batch>,
    private val onItemClick: (Batch) -> Unit
) : RecyclerView.Adapter<BatchAdapter.BatchViewHolder>() {

    inner class BatchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName   : TextView = itemView.findViewById(R.id.tvBatchName)
        val tvBreed  : TextView = itemView.findViewById(R.id.tvBreed)
        val tvDays   : TextView = itemView.findViewById(R.id.tvDaysInfo)
        val tvStatus : TextView = itemView.findViewById(R.id.tvStatus)
        val viewStrip: View     = itemView.findViewById(R.id.viewStrip)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BatchViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_batch, parent, false)
        return BatchViewHolder(view)
    }

    override fun onBindViewHolder(holder: BatchViewHolder, position: Int) {
        val batch  = batches[position]
        val days   = ClimateEngine.getDaysElapsed(batch.startDate)
        val instar = ClimateEngine.getCurrentInstar(days)

        holder.tvName.text  = batch.batchName
        holder.tvBreed.text = batch.breed
        holder.tvDays.text  = "Day $days  ·  Instar $instar"

        if (batch.isActive) {
            holder.tvStatus.text = "Active"
            holder.tvStatus.setBackgroundResource(R.drawable.badge_active)
            holder.tvStatus.setTextColor(Color.parseColor("#7A3A22"))
            holder.viewStrip.setBackgroundColor(Color.parseColor("#F4A58A"))
        } else {
            holder.tvStatus.text = "Completed"
            holder.tvStatus.setBackgroundResource(R.drawable.badge_done)
            holder.tvStatus.setTextColor(Color.parseColor("#A06040"))
            holder.viewStrip.setBackgroundColor(Color.parseColor("#F2C9B8"))
        }

        holder.itemView.setOnClickListener { onItemClick(batch) }
    }

    override fun getItemCount() = batches.size
}