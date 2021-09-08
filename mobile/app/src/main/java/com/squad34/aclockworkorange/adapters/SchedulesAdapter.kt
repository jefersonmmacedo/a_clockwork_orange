package com.squad34.aclockworkorange.adapters

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squad34.aclockworkorange.R
import com.squad34.aclockworkorange.activities.MainActivity
import com.squad34.aclockworkorange.activities.SchedulingActivity
import kotlinx.android.synthetic.main.item_date.view.*

open class SchedulesAdapter(
    private val context: Context,
    private var list: ArrayList<String>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener: OnClickListener? = null


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
            holder.itemView.tv_date_in_recycler.text = model
            /*holder.itemView.setOnClickListener {

                if (onClickListener != null) {
                    onClickListener!!.onClick(position)
                }
            }*/
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }



    interface OnClickListener {
        fun onClick(position: Int)
    }


    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}