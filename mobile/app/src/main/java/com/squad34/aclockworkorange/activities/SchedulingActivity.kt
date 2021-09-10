package com.squad34.aclockworkorange.activities

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.support.v4.os.IResultReceiver
import android.text.TextUtils
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
import androidx.annotation.RequiresApi
import androidx.core.view.get
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Calendar.LONG_FORMAT


class SchedulingActivity : BaseActivity() {

    private lateinit var mBinding: ActivitySchedulingBinding
    private var mSelectedDates = ArrayList<DateSelected>()
    private var mSelecetdUnit = ""
    private var mSelectedType = ""
    private var mWorkOrMeet = ""
    private var mShift = ""


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivitySchedulingBinding.inflate(layoutInflater)
        setContentView(mBinding.root)


        setupActionBar()

        setupDropDownMenus()

        mBinding.etDate.setOnClickListener {
            showDatePicker()
        }

        mBinding.actvSchedulingType.onItemSelectedListener = object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                mSelectedType = p0?.getItemAtPosition(p2).toString()
                Toast.makeText(this@SchedulingActivity, "$mSelectedType", Toast.LENGTH_LONG).show()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }


        }



        mBinding.btnConfirm.setOnClickListener {

            mSelecetdUnit = mBinding.actvUnit.text.toString()
            mWorkOrMeet = mBinding.actvWorkStation.text.toString()
            mSelectedType = mBinding.actvSchedulingType.text.toString()
            mShift = mBinding.actvShift.text.toString()

            when {
                TextUtils.isEmpty(mSelecetdUnit) -> {
                    Toast.makeText(
                        this,
                        "Você deve selecionar uma unidade!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                TextUtils.isEmpty(mWorkOrMeet) -> {
                    Toast.makeText(
                        this,
                        "Você deve selecionar oque você quer agendar!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                TextUtils.isEmpty(mSelectedType) -> {
                    Toast.makeText(
                        this,
                        "Você deve selecionar o tipo de agendamento!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                TextUtils.isEmpty(mShift) -> {
                    Toast.makeText(
                        this,
                        "Você deve selecionar um turno!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                mSelectedDates.isEmpty() -> {
                    Toast.makeText(
                        this,
                        "Você deve selecionar uma data!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    showDialog()
                }
            }


        }


    }

    private fun setupDropDownMenus() {
        val textFieldUnits: AutoCompleteTextView = mBinding.actvUnit
        val units = arrayListOf("São Paulo", "Santos")
        val adapterUnit = ArrayAdapter(this, R.layout.list_items, R.id.tv_item, units)
        textFieldUnits.setAdapter(adapterUnit)

        mSelecetdUnit = textFieldUnits?.text.toString()

        val textFieldWorkStation = mBinding.actvWorkStation as? AutoCompleteTextView
        val workStation = arrayListOf("Estação de trabalho", "Sala de reunião")
        val adapterWork = ArrayAdapter(this, R.layout.list_items, R.id.tv_item, workStation)
        textFieldWorkStation?.setAdapter(adapterWork)

        val textFieldSchedulingType = mBinding.actvSchedulingType as? AutoCompleteTextView
        val schedulingType = arrayListOf("Recorrente", "Normal")
        val adapterSchedulingType =
            ArrayAdapter(this, R.layout.list_items, R.id.tv_item, schedulingType)
        textFieldSchedulingType?.setAdapter(adapterSchedulingType)

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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showDatePicker() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(
            this,
            { view, year, monthOfYear, dayOfMonth ->


                mBinding.tilSchedulingType.isEnabled = false
                mBinding.tilShift.isEnabled = false
                mBinding.tilUnit.isEnabled = false
                mBinding.tilStationOrMeeting.isEnabled = false


                if (mSelectedType == "Normal") {
                    val sDayOfMonth = if (dayOfMonth < 10) "0$dayOfMonth" else "$dayOfMonth"
                    val sMonthOfYear =
                        if ((monthOfYear + 1) < 10) "0${monthOfYear + 1}" else "${monthOfYear + 1}"
                    val selectedDate = "$sDayOfMonth/$sMonthOfYear/$year"

                    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.US)
                    val theDate = sdf.parse(selectedDate)
                    val format = SimpleDateFormat("EEEE", Locale.getDefault())
                    var dow = format.format(theDate)


                    var contain = false


                    for (i in mSelectedDates.indices) {
                        if (mSelectedDates[i].date.contains(selectedDate)) {
                            Toast.makeText(this, "Esta data já foi selecionada!", Toast.LENGTH_LONG)
                                .show()
                            contain = true
                        }
                    }

                    when (dow) {
                        "segunda-feira" -> {
                            dow = "Segunda"
                        }
                        "terça-feira" -> {
                            dow = "Terça"
                        }
                        "quarta-feira" -> {
                            dow = "Quarta"
                        }
                        "quinta-feira" -> {
                            dow = "Quinta"
                        }
                        "sexta-feira" -> {
                            dow = "Sexta"
                        }
                    }


                    if (!contain) {
                        var mSelectedDateFormater = DateSelected(selectedDate, dow)

                        mSelectedDates.add(mSelectedDateFormater)

                        populateDatesList(mSelectedDates)
                    }


                } else {

                    val sDayOfMonth = if (dayOfMonth < 10) "0$dayOfMonth" else "$dayOfMonth"
                    val sMonthOfYear =
                        if ((monthOfYear + 1) < 10) "0${monthOfYear + 1}" else "${monthOfYear + 1}"
                    val selectedDate2 = "$sDayOfMonth/$sMonthOfYear/$year"

                    val dom = sDayOfMonth.toInt()
                    val moy = sMonthOfYear.toInt()

                    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.US)
                    val theDate = sdf.parse(selectedDate2)
                    val format = SimpleDateFormat("EEEE", Locale.getDefault())
                    var dow = format.format(theDate)

                    when (dow) {
                        "segunda-feira" -> {
                            dow = "Segunda"
                        }
                        "terça-feira" -> {
                            dow = "Terça"
                        }
                        "quarta-feira" -> {
                            dow = "Quarta"
                        }
                        "quinta-feira" -> {
                            dow = "Quinta"
                        }
                        "sexta-feira" -> {
                            dow = "Sexta"
                        }
                    }

                    var mSelectedDateFormater = DateSelected(selectedDate2, dow)
                    mSelectedDates.add(mSelectedDateFormater)


                    var i = 1
                    while (i <= 3) {

                        val days = (7*i).toLong()

                        val date = LocalDate.of(year,moy,dom).plusDays(days)
                        val dateWrong = SimpleDateFormat("yyyy-MM-dd")
                        val dateNew = dateWrong.parse(date.toString())
                        val dateNewFormat = SimpleDateFormat("dd/MM/yyyy")
                        val dateFinal = dateNewFormat.format(dateNew)
                        Toast.makeText(this,"$dateFinal", Toast.LENGTH_LONG).show()

                        var mSelectedDateFormater = DateSelected(dateFinal, dow)
                        mSelectedDates.add(mSelectedDateFormater)

                        populateDatesList(mSelectedDates)
                        i++
                    }

                    mBinding.tilDate.isEnabled = false

                }
            },
            year,
            month,
            day
        )
        dpd.show()
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
        unit.text = " " + mSelecetdUnit

        val type = dialog.findViewById(R.id.tv_selected_type_recurrent_confirmation) as TextView
        type.text = " " + mSelectedType

        val dayOfWeek = mSelectedDates[0].dayOfWeek
        val dayOfWeekText =
            dialog.findViewById(R.id.tv_selected_day_of_week_recurrent_confirmation) as TextView
        dayOfWeekText.text = " " + dayOfWeek

        val dates = dialog.findViewById(R.id.tv_dates_selected_recurrent_confirmation) as TextView
        if (mSelectedDates.size > 1) {
            var createDates = "Datas:"
            for (i in mSelectedDates.indices) {
                if (i == mSelectedDates.size - 1) {
                    createDates += " e " + mSelectedDates[i].date
                } else if (i == 0) {
                    createDates += " " + mSelectedDates[i].date
                } else {
                    createDates += ", " + mSelectedDates[i].date
                }
            }
            dates.text = createDates
        }

        val shift = dialog.findViewById(R.id.tv_shift_selected_recurrent_confirmation) as TextView
        shift.text = mShift

        val confirmationButton =
            dialog.findViewById(R.id.btn_confirm_recurrent_confirmation) as Button
        val cancelButton = dialog.findViewById(R.id.btn_cancel_recurrent_confirmation) as Button
        confirmationButton.setOnClickListener {
            //TODO Fazer a implementação para criar o objeto Scheduling, que será enviado ao banco de dados
            dialog.dismiss()
        }
        cancelButton.setOnClickListener { dialog.dismiss() }
        dialog.show()

    }

}