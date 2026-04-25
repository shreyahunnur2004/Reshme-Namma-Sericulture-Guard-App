package com.example.reshmenammapride

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.reshmenammapride.data.Batch

class BatchAdapter(
    private val batches: List<Batch>,
    private val onItemClick: (Batch) -> Unit
) : RecyclerView.Adapter<BatchAdapter.BatchViewHolder>() {

    inner class BatchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName   : TextView = itemView.findViewById(R.id.tvBatchName)
        val tvBreed  : TextView = itemView.findViewById(R.id.tvBreed)
        val tvStatus : TextView = itemView.findViewById(R.id.tvStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BatchViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_batch, parent, false)
        return BatchViewHolder(view)
    }

    override fun onBindViewHolder(holder: BatchViewHolder, position: Int) {
        val batch = batches[position]
        holder.tvName.text  = batch.batchName
        holder.tvBreed.text = "Breed: ${batch.breed}"

        if (batch.isActive) {
            holder.tvStatus.text = "ACTIVE"
            holder.tvStatus.setBackgroundColor(0xFF2E7D32.toInt())
        } else {
            holder.tvStatus.text = "COMPLETED"
            holder.tvStatus.setBackgroundColor(0xFF888888.toInt())
        }

        holder.itemView.setOnClickListener { onItemClick(batch) }
    }

    override fun getItemCount() = batches.size
}