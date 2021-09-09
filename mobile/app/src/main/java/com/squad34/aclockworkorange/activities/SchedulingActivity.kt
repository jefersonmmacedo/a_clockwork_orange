package com.squad34.aclockworkorange.activities

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.Window
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.squad34.aclockworkorange.R
import com.squad34.aclockworkorange.adapters.SchedulesAdapter
import com.squad34.aclockworkorange.databinding.ActivitySchedulingBinding
import com.squad34.aclockworkorange.models.DateSelected
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import android.widget.AutoCompleteTextView


class SchedulingActivity : BaseActivity() {

    private lateinit var mBinding: ActivitySchedulingBinding
    private var mSelectedDates = ArrayList<DateSelected>()
    private var mSelectedDateMilliseconds: Long = 0
    private var recurrent: Boolean = false
    private var mSelecetdUnit = ""
    private var mSelectedType = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivitySchedulingBinding.inflate(layoutInflater)
        setContentView(mBinding.root)


        setupActionBar()

        setupDropDownMenus()

        mBinding.etDate.setOnClickListener {
            showDatePicker()
        }

        mBinding.btnConfirm.setOnClickListener {

            mSelecetdUnit = " " + mBinding.actvUnit.text.toString()
            mSelectedType = " " + mBinding.actvSchedulingType.toString()
            




            showDialog()
        }


    }

    private fun setupDropDownMenus() {
        val textFieldUnits: AutoCompleteTextView = mBinding.actvUnit
        val units = arrayListOf("São Paulo", "Santos")
        val adapterUnit = ArrayAdapter(this, R.layout.list_items, R.id.tv_item, units)
        textFieldUnits.setAdapter(adapterUnit)

        mSelecetdUnit = textFieldUnits?.text.toString()
        Toast.makeText(this, "$mSelecetdUnit", Toast.LENGTH_LONG).show()

        val textFieldWorkStation = mBinding.actvWorkStation as? AutoCompleteTextView
        val workStation = arrayListOf("Estação de trabalho", "Sala de reunião")
        val adapterWork = ArrayAdapter(this, R.layout.list_items, R.id.tv_item, workStation)
        textFieldWorkStation?.setAdapter(adapterWork)

        val textFieldSchedulingType = mBinding.actvSchedulingType as? AutoCompleteTextView
        val schedulingType = arrayListOf("Recorrente", "Normal")
        val adapterSchedulingType =
            ArrayAdapter(this, R.layout.list_items, R.id.tv_item, schedulingType)
        textFieldSchedulingType?.setAdapter(adapterSchedulingType)

        textFieldSchedulingType?.onItemSelectedListener = object :
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
        val dayOfWeek = c.get(Calendar.DAY_OF_WEEK)
        val dpd = DatePickerDialog(
            this,
            { view, year, monthOfYear, dayOfMonth ->

                if (!recurrent) {
                    val sDayOfMonth = if (dayOfMonth < 10) "0$dayOfMonth" else "$dayOfMonth"
                    val sMonthOfYear =
                        if ((monthOfYear + 1) < 10) "0${monthOfYear + 1}" else "${monthOfYear + 1}"
                    val selectedDate = "$sDayOfMonth/$sMonthOfYear/$year"

                    var contain = false
                    val wdf = SimpleDateFormat("EEEEEE", Locale("PT-BR"))
                    val dow = wdf.format(dayOfWeek)
                    for (i in mSelectedDates.indices) {
                        if (mSelectedDates[i].date.contains(selectedDate)) {
                            Toast.makeText(this, "Esta data já foi selecionada!", Toast.LENGTH_LONG)
                                .show()
                            contain = true
                        }
                    }
                    if (!contain) {
                        var mSelectedDateFormater: DateSelected = DateSelected(selectedDate, dow)

                        mSelectedDates.add(mSelectedDateFormater)

                        populateDatesList(mSelectedDates)
                    }


                } else {

                    /* val sDayOfMonth = if (dayOfMonth < 10) "0$dayOfMonth" else "$dayOfMonth"
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
                         populateDatesList(mSelectedDates) */

                }
            },
            year,
            month,
            day
        )
        dpd.show() // It is used to show the datePicker Dialog.
    }

    private fun populateDatesList(date: ArrayList<DateSelected>) {
        mBinding.rvDatesSelected.layoutManager = LinearLayoutManager(this)
        mBinding.rvDatesSelected.setHasFixedSize(true)
        val adapter = SchedulesAdapter(this, date)
        mBinding.rvDatesSelected.adapter = adapter


        when (mSelectedDates.size) {
            0 -> {
                mBinding.llSelectedDates.visibility = View.GONE
                mBinding.tvSelectedDate.text = "Nenhuma data selecionada"
            }
            1 -> {
                mBinding.llSelectedDates.visibility = View.VISIBLE
                mBinding.tvSelectedDate.text = "Data selecionada"
            }
            else -> {
                mBinding.llSelectedDates.visibility = View.VISIBLE
                mBinding.tvSelectedDate.text = "Datas selecionadas"
            }
        }
    }

    private fun showDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.confirm_recurrent_scheduling_dialog)
        val unit = dialog.findViewById(R.id.tv_selected_unit_recurrent_confirmation) as TextView
        unit.text = mSelecetdUnit
        val type = dialog.findViewById(R.id.tv_selected_type_recurrent_confirmation) as TextView
        type.text = if (recurrent) {
            "Recorrente"
        } else {
            "Normal"
        }
        val dayOfWeek = mSelectedDates[0].dayOfWeek

        val yesBtn = dialog.findViewById(R.id.btn_confirm_recurrent_confirmation) as Button
        val noBtn = dialog.findViewById(R.id.btn_cancel_recurrent_confirmation) as Button
        yesBtn.setOnClickListener {
            dialog.dismiss()
        }
        noBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()

    }

}