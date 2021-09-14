package com.squad34.aclockworkorange.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squad34.aclockworkorange.R
import kotlinx.android.synthetic.main.item_date.view.*
import android.widget.AdapterView.OnItemClickListener
import com.squad34.aclockworkorange.activities.MainActivity
import com.squad34.aclockworkorange.models.DateSelected
import com.squad34.aclockworkorange.models.Schedulingdata
import kotlinx.android.synthetic.main.item_date_main_activity.view.*

open class SchedulesMainAdapter(
    private val context: Context,
    private var list: ArrayList<Schedulingdata.DateScheduling>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_date_main_activity,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {
            holder.itemView.tv_unit_in_main_recycler.text = model.location
            holder.itemView.tv_shift_in_main_recycler.text = model.shift
            holder.itemView.tv_date_in_main_recycler.text = model.date
            holder.itemView.ib_edit_date_in_main_recycler.setOnClickListener {

                onClick(position)

            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private fun onClick(position: Int)
    {
        if (context is MainActivity) {
            context.editSchedule(position)
        }
    }

    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}