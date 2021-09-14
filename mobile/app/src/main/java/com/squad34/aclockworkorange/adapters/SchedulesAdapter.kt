package com.squad34.aclockworkorange.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squad34.aclockworkorange.R
import kotlinx.android.synthetic.main.item_date.view.*
import android.widget.AdapterView.OnItemClickListener
import com.squad34.aclockworkorange.activities.SchedulingActivity
import com.squad34.aclockworkorange.models.DateSelected

open class SchedulesAdapter(
    private val context: Context,
    private var list: ArrayList<DateSelected>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_date,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]


        if (holder is MyViewHolder) {
            holder.itemView.tv_date_in_recycler.text = model.date
            holder.itemView.ib_cancel_date_schedule.setOnClickListener {

                onClick(position)

            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private fun onClick(position: Int)
    {
        list.removeAt(position)
        notifyDataSetChanged()
    }

    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}