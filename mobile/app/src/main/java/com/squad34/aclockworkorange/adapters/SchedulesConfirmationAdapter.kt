package com.squad34.aclockworkorange.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squad34.aclockworkorange.R
import com.squad34.aclockworkorange.models.DateSelected
import kotlinx.android.synthetic.main.item_date_in_confirmation.view.*

open class SchedulesConfirmationAdapter(
    private val context: Context,
    private var list: ArrayList<DateSelected>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_date_in_confirmation,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {
            holder.itemView.tv_date_in_recycler_confirmation.text = model.date
            holder.itemView.tv_day_of_week_in_recycler_confirmation.text = model.dayOfWeek
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}