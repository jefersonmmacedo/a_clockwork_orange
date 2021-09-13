package com.squad34.aclockworkorange.activities

import android.R.attr
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
import android.R.attr.maxDate
import android.content.DialogInterface
import android.net.sip.SipSession
import android.widget.DatePicker
import androidx.recyclerview.widget.RecyclerView
import com.squad34.aclockworkorange.adapters.SchedulesConfirmationAdapter
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import kotlinx.android.synthetic.main.confirm_multiple_scheduling_dialog.*
import kotlinx.android.synthetic.main.confirm_recurrent_scheduling_dialog.*
import java.lang.reflect.Array.newInstance
import java.util.Calendar.getInstance
import kotlin.properties.Delegates


open class SchedulingActivity : BaseActivity(), DatePickerDialog.OnDateSetListener {

    private lateinit var mBinding: ActivitySchedulingBinding
    private var mSelectedDates = ArrayList<DateSelected>()
    private var mSelecetdUnit = ""
    private var mSelectedType = ""
    private var mWorkOrMeet = ""
    private var mShift = ""
    private lateinit var datePickerDialog: DatePickerDialog
    private var year = 0
    private var month = 0
    private var day = 0
    private lateinit var calendar: Calendar
    private val dateFake = arrayListOf("13/09/2021", "14/09/2021", "15/09/2021")
    private var datesToDisable = ArrayList<Calendar>()


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivitySchedulingBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        showProgressDialog()

        setupActionBar()

        setupDropDownMenus()

        setupDatePicker()

        mBinding.tilSchedulingType.isEnabled = false
        mBinding.tilShift.isEnabled = false
        mBinding.tilStationOrMeeting.isEnabled = false
        mBinding.tilDate.isEnabled = false

        mBinding.actvUnit.onItemClickListener =
            AdapterView.OnItemClickListener { p0, p1, p2, p3 ->
                mSelecetdUnit = p0?.getItemAtPosition(p2).toString()
                mBinding.tilUnit.isEnabled = false
                mBinding.tilStationOrMeeting.isEnabled = true
            }

        mBinding.actvWorkStation.onItemClickListener =
            AdapterView.OnItemClickListener { p0, p1, p2, p3 ->
                mWorkOrMeet = p0?.getItemAtPosition(p2).toString()
                mBinding.tilStationOrMeeting.isEnabled = false
                mBinding.tilSchedulingType.isEnabled = true
            }

        mBinding.actvSchedulingType.onItemClickListener =
            AdapterView.OnItemClickListener { p0, p1, p2, p3 ->
                mSelectedType = p0?.getItemAtPosition(p2).toString()
                if (mSelectedType == "Normal") {
                    mBinding.tvDate.text = "Escolha uma ou mais datas"
                }
                mBinding.tilSchedulingType.isEnabled = false
                mBinding.tilShift.isEnabled = true
            }


        mBinding.actvShift.onItemClickListener =
            AdapterView.OnItemClickListener { p0, p1, p2, p3 ->
                mShift = p0?.getItemAtPosition(p2).toString()
                mBinding.tilShift.isEnabled = false
                mBinding.tilDate.isEnabled = true
            }

        mBinding.etDate.setOnClickListener {
            datePickerDialog.show(getFragmentManager(), "DatePickerDialog")
        }

        mBinding.btnConfirm.setOnClickListener {

            if (mSelectedDates.size == 0) {
                mBinding.tilSchedulingType.isEnabled = true
                mBinding.tilShift.isEnabled = true
                mBinding.tilUnit.isEnabled = true
                mBinding.tilStationOrMeeting.isEnabled = true
                mBinding.tilDate.isEnabled = true
            }
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
                    if (mSelectedType == "Recorrente" || mSelectedDates.size == 1) {
                        showDialogRecurrent()
                    } else {
                        showDialogNormal()
                    }

                }
            }
        }
        mBinding.btnCancel.setOnClickListener {
            finish()
        }
    }

    private fun setupDropDownMenus() {
        val textFieldUnits: AutoCompleteTextView = mBinding.actvUnit
        val units = arrayListOf("São Paulo", "Santos")
        val adapterUnit = ArrayAdapter(this, R.layout.list_items, R.id.tv_item, units)
        textFieldUnits.setAdapter(adapterUnit)

        val textFieldWorkStation = mBinding.actvWorkStation as? AutoCompleteTextView
        val workStation = arrayListOf("Estação de trabalho", "Sala de reunião")
        val adapterWork = ArrayAdapter(this, R.layout.list_items, R.id.tv_item, workStation)
        textFieldWorkStation?.setAdapter(adapterWork)

        val textFieldSchedulingType = mBinding.actvSchedulingType as? AutoCompleteTextView
        val schedulingType = mutableListOf<String>("Recorrente", "Normal")
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

    private fun showDialogRecurrent() {
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
            dialog.findViewById(R.id.tv_day_of_week_recurrent_confirmation) as TextView
        dayOfWeekText.text = "Dia da semana: " + dayOfWeek

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
        } else {
            var createDates = "Data: " + mSelectedDates[0].date
            dates.text = createDates

        }

        val shift = dialog.findViewById(R.id.tv_shift_selected_recurrent_confirmation) as TextView
        shift.text = mShift

        val confirmationButton =
            dialog.findViewById(R.id.btn_confirm_recurrent_confirmation) as Button
        val cancelButton = dialog.findViewById(R.id.btn_cancel_recurrent_confirmation) as Button

        val dismissButton = dialog.findViewById(R.id.ib_cancel_dialog_recurrent) as ImageButton
        dismissButton.setOnClickListener { dialog.dismiss() }
        confirmationButton.setOnClickListener {
            //TODO Fazer a implementação para criar o objeto Scheduling, que será enviado ao banco de dados
            dialog.dismiss()
        }
        cancelButton.setOnClickListener { dialog.dismiss() }

        dialog.show()
    }

    private fun showDialogNormal() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.confirm_multiple_scheduling_dialog)

        val unit = dialog.findViewById(R.id.tv_selected_unit_multiple_confirmation) as TextView
        unit.text = " " + mSelecetdUnit

        val rvDates =
            dialog.findViewById(R.id.rv_dates_selected_multiple_confirmation) as RecyclerView
        rvDates.layoutManager = LinearLayoutManager(this)
        rvDates.setHasFixedSize(true)
        val adapter = SchedulesConfirmationAdapter(this, mSelectedDates)
        rvDates.adapter = adapter

        val shift = dialog.findViewById(R.id.tv_shift_selected_multiple_confirmation) as TextView
        shift.text = mShift

        val confirmationButton =
            dialog.findViewById(R.id.btn_confirm_multiple_confirmation) as Button
        val cancelButton = dialog.findViewById(R.id.btn_cancel_multiple_confirmation) as Button
        val dismissButton = dialog.findViewById(R.id.ib_cancel_normal_confirmation) as ImageButton
        dismissButton.setOnClickListener { dialog.dismiss() }
        confirmationButton.setOnClickListener {
            //TODO Fazer a implementação para criar o objeto Scheduling, que será enviado ao banco de dados
            dialog.dismiss()
        }
        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {

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


            var mSelectedDateFormater = DateSelected(selectedDate2, dow)
            mSelectedDates.add(mSelectedDateFormater)
            var i = 1
            while (i <= 3) {
                val days = (7 * i).toLong()
                val date = LocalDate.of(year, moy, dom).plusDays(days)
                val dateWrong = SimpleDateFormat("yyyy-MM-dd")
                val dateNew = dateWrong.parse(date.toString())
                val dateNewFormat = SimpleDateFormat("dd/MM/yyyy")
                val dateFinal = dateNewFormat.format(dateNew)
                var mSelectedDateFormater = DateSelected(dateFinal, dow)
                mSelectedDates.add(mSelectedDateFormater)

                populateDatesList(mSelectedDates)
                i++
            }
            mBinding.tilDate.isEnabled = false
        }
    }

    fun setupDatePicker() {

        calendar = getInstance()
        year = calendar.get(Calendar.YEAR)
        month = calendar.get(Calendar.MONTH)
        day = calendar.get(Calendar.DAY_OF_MONTH)

        datePickerDialog = DatePickerDialog.newInstance(this, year, month, day)
        datePickerDialog.isThemeDark = false
        datePickerDialog.showYearPickerFirst(false)
        datePickerDialog.setTitle("")
        val minDate = Calendar.getInstance()
        val maxDate = Calendar.getInstance()
        maxDate.set(Calendar.YEAR, year + 1)

        datePickerDialog.minDate = minDate
        datePickerDialog.maxDate = maxDate

        var loopdate: Calendar = minDate

        while (minDate.before(maxDate)) {
            val dayOfWeek = loopdate[Calendar.DAY_OF_WEEK]
            if (dayOfWeek == Calendar.SUNDAY || dayOfWeek == Calendar.SATURDAY) {
                val disabledDays: Array<Calendar?> = arrayOfNulls<Calendar>(1)
                disabledDays[0] = loopdate
                datePickerDialog.disabledDays = disabledDays

            }
            minDate.add(Calendar.DATE, 1)
            loopdate = minDate
        }

        for (i in dateFake.indices) {
            val full = Calendar.getInstance()
            val fullDayString = dateFake[i]
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            full.time = sdf.parse(fullDayString)
            val disabledDays: Array<Calendar?> = arrayOfNulls<Calendar>(1)
            disabledDays[0] = full
            datePickerDialog.disabledDays = disabledDays
        }

        hideProgressDialog()
    }
}