package com.squad34.aclockworkorange.activities

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.squad34.aclockworkorange.R
import com.squad34.aclockworkorange.adapters.SchedulesAdapter
import com.squad34.aclockworkorange.databinding.ActivitySchedulingBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SchedulingActivity : BaseActivity() {

    private lateinit var mBinding: ActivitySchedulingBinding
    private var mSelectedDates = ArrayList<String>()
    private var mSelectedDateMilliseconds: Long = 0
    private var recurrent: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivitySchedulingBinding.inflate(layoutInflater)
        setContentView(mBinding.root)


        setupActionBar()

        setupDropDownMenus()

        mBinding.etDate.setOnClickListener {
            showDatePicker()
        }




    }

    private fun setupDropDownMenus() {
        val textFieldUnits = mBinding.actvUnit as? AutoCompleteTextView
        val units = arrayListOf("São Paulo", "Santos")
        val adapterUnit = ArrayAdapter(this, R.layout.list_items, R.id.tv_item, units)
        textFieldUnits?.setAdapter(adapterUnit)

        val textFieldWorkStation = mBinding.actvWorkStation as? AutoCompleteTextView
        val workStation = arrayListOf("Estação de trabalho", "Sala de reunião")
        val adapterWork = ArrayAdapter(this, R.layout.list_items, R.id.tv_item, workStation)
        textFieldWorkStation?.setAdapter(adapterWork)

        val textFieldSchedulingType = mBinding.actvSchedulingType as? AutoCompleteTextView
        val schedulingType = arrayListOf("Recorrente", "Normal")
        val adapterSchedulingType =
            ArrayAdapter(this, R.layout.list_items, R.id.tv_item, schedulingType)
        textFieldSchedulingType?.setAdapter(adapterSchedulingType)

        textFieldSchedulingType?.onItemSelectedListener= object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val result = schedulingType[p2]
                if (result == "Recorrente") {
                    recurrent = true
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }


        val textFieldShift = mBinding.actvShift as? AutoCompleteTextView
        val shift = arrayListOf("Manhã", "Tarde", "Integral")
        val adapterShift = ArrayAdapter(this, R.layout.list_items, R.id.tv_item, shift)
        textFieldShift?.setAdapter(adapterShift)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.logout, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun setupActionBar() {
        setSupportActionBar(mBinding.toolbarSchedulingActivity)

        supportActionBar?.title = ""


        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        mBinding.toolbarSchedulingActivity.setNavigationOnClickListener {
            doubleBackToExit()
        }

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_corner_up_left)

    }

    private fun showDatePicker() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(
            this,
            { view, year, monthOfYear, dayOfMonth ->

                if (!recurrent) {
                    val sDayOfMonth = if (dayOfMonth < 10) "0$dayOfMonth" else "$dayOfMonth"
                    val sMonthOfYear =
                        if ((monthOfYear + 1) < 10) "0${monthOfYear + 1}" else "${monthOfYear + 1}"
                    val selectedDate = "$sDayOfMonth/$sMonthOfYear/$year"

                    mSelectedDates.add(selectedDate)
                    populateDatesList(mSelectedDates)
                } else {

                    val sDayOfMonth = if (dayOfMonth < 10) "0$dayOfMonth" else "$dayOfMonth"
                    val sMonthOfYear =
                        if ((monthOfYear + 1) < 10) "0${monthOfYear + 1}" else "${monthOfYear + 1}"
                    val selectedDate = "$sDayOfMonth/$sMonthOfYear/$year"

                    mSelectedDates.add(selectedDate)
                    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.US)
                    val theDate = sdf.parse(selectedDate)
                    mSelectedDateMilliseconds = theDate!!.time


                    var i = 1
                    var day = 8.64e+7
                    while (i <= 4) {
                       val dateInMili: Long = mSelectedDateMilliseconds + (day * i).toLong()
                        val calendar = Calendar.getInstance()
                        calendar.timeInMillis = dateInMili
                        sdf.format(calendar.time)
                        val year2 = calendar.get(Calendar.YEAR)
                        val month2 = calendar.get(Calendar.MONTH)
                        val day2 = calendar.get(Calendar.DAY_OF_MONTH)
                        val sDayOfMonth2 = if (day2 < 10) "0$day2" else "$day2"
                        val sMonthOfYear2 =
                            if ((month2 + 1) < 10) "0${month2 + 1}" else "${month2 + 1}"
                        val selectedDate2 = "$sDayOfMonth2/$sMonthOfYear2/$year2"
                        mSelectedDates.add(selectedDate2)
                        i++
                        populateDatesList(mSelectedDates)
                    }
                }
            },
            year,
            month,
            day
        )
        dpd.show() // It is used to show the datePicker Dialog.
    }

    private fun populateDatesList(date: ArrayList<String>) {
        mBinding.rvDatesSelected.layoutManager = LinearLayoutManager(this)
        mBinding.rvDatesSelected.setHasFixedSize(true)
        val adapter = SchedulesAdapter(this,mSelectedDates)
        mBinding.rvDatesSelected.adapter = adapter

        when (mSelectedDates.size) {
            0 -> {
                mBinding.tvSelectedDate.text = ""
            }
            1 -> {
                mBinding.tvSelectedDate.text = "Data selecionada"
            }
            else -> {
                mBinding.tvSelectedDate.text = "Datas selecionadas"
            }
        }

        /*adapter.setOnClickListener(object : SchedulesAdapter.OnClickListener {
            override fun onClick(position: Int) {
                mSelectedDates.removeAt(position)
            }

        })*/
    }



}