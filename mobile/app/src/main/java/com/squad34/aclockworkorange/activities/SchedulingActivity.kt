package com.squad34.aclockworkorange.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.Menu
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.squad34.aclockworkorange.R
import com.squad34.aclockworkorange.adapters.SchedulesAdapter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import android.widget.AutoCompleteTextView
import androidx.annotation.RequiresApi
import java.time.LocalDate
import android.util.Log
import android.view.MenuItem
import com.squad34.aclockworkorange.adapters.SchedulesConfirmationAdapter
import com.squad34.aclockworkorange.databinding.*
import com.squad34.aclockworkorange.models.*
import com.squad34.aclockworkorange.network.ClockworkService
import com.squad34.aclockworkorange.utils.Constants
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Calendar.getInstance


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
    private lateinit var mTotal: DateTotalPerDay
    private lateinit var calendar: Calendar
    private var datesToDisable = ArrayList<String>()
    private var datesToDisableMeet = ArrayList<String>()
    private lateinit var mUser: UserFromValidator

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivitySchedulingBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mUser = intent.getParcelableExtra(MainActivity.USERSCHEDULE)!!
        datesToDisable = intent.getStringArrayListExtra(MainActivity.DATES_TO_EXCLUDE)!!

        if (intent.hasExtra(MainActivity.MEET_SCHEDULE)) {
            datesToDisableMeet = intent.getStringArrayListExtra(MainActivity.MEET_SCHEDULE)!!
        }

        setupActionBar()

        setupDropDownMenus()

        mBinding.actvUnit.onItemClickListener =
            AdapterView.OnItemClickListener { p0, p1, p2, p3 ->
                mSelecetdUnit = p0?.getItemAtPosition(p2).toString()
            }

        mBinding.actvWorkStation.onItemClickListener =
            AdapterView.OnItemClickListener { p0, p1, p2, p3 ->
                mWorkOrMeet = p0?.getItemAtPosition(p2).toString()

                setupDropdownForMeeting(mWorkOrMeet)
                setupDatePicker(mWorkOrMeet)
            }

        mBinding.actvSchedulingType.onItemClickListener =
            AdapterView.OnItemClickListener { p0, p1, p2, p3 ->
                mSelectedType = p0?.getItemAtPosition(p2).toString()
                if (mSelectedType == "Normal") {
                    mBinding.tvDate.text = "Escolha uma ou mais datas"
                }
            }

        mBinding.actvShift.onItemClickListener =
            AdapterView.OnItemClickListener { p0, p1, p2, p3 ->
                mShift = p0?.getItemAtPosition(p2).toString()
            }

        mBinding.tvDateToSelect.setOnClickListener {
            when {
                TextUtils.isEmpty(mSelecetdUnit) -> {
                    showToastAlert("Você deve selecionar uma unidade!")
                }
                TextUtils.isEmpty(mWorkOrMeet) -> {
                    showToastAlert("Você deve selecionar oque você quer agendar!")
                }
                TextUtils.isEmpty(mSelectedType) -> {
                    showToastAlert("Você deve selecionar o tipo de agendamento!")
                }
                TextUtils.isEmpty(mShift) -> {
                    showToastAlert("Você deve selecionar um turno!")
                }
                else -> {
                    mBinding.tilUnit.isEnabled = false
                    mBinding.tilStationOrMeeting.isEnabled = false
                    mBinding.tilShift.isEnabled = false
                    mBinding.tilSchedulingType.isEnabled = false
                    datePickerDialog.show(getFragmentManager(), "DatePickerDialog")
                }
            }
        }

        mBinding.btnConfirm.setOnClickListener {

            when {
                mSelectedDates.isEmpty() -> {
                    showToastAlert("Você deve selecionar uma data!")
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
            val intent = Intent()
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    private fun setupDropDownMenus() {
        val textFieldUnits: AutoCompleteTextView = mBinding.actvUnit
        val units = arrayListOf("São Paulo", "Santos")
        val adapterUnit = ArrayAdapter(this, R.layout.list_items, R.id.tv_item, units)
        textFieldUnits.setAdapter(adapterUnit)

        val textFieldWorkStation = mBinding.actvWorkStation as? AutoCompleteTextView
        val workStation = arrayListOf("Estação de trabalho", "Sala de Reuniões")
        val adapterWork = ArrayAdapter(this, R.layout.list_items, R.id.tv_item, workStation)
        textFieldWorkStation?.setAdapter(adapterWork)

        val textFieldSchedulingType = mBinding.actvSchedulingType as? AutoCompleteTextView
        val schedulingType = mutableListOf<String>("Recorrente", "Normal")
        val adapterSchedulingType =
            ArrayAdapter(this, R.layout.list_items, R.id.tv_item, schedulingType)
        textFieldSchedulingType?.setAdapter(adapterSchedulingType)
    }

    private fun getTotalPerDay(
        location: String,
        type: String,
        shift: String,
        date: String,
        day: String,
        normal: Boolean
    ) {
        if (Constants.isNetworkAvailable(this)) {
            val retrofit: Retrofit = Retrofit.Builder().baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val service: ClockworkService = retrofit.create(ClockworkService::class.java)
            val listCall = service.totalPerDay(
                location,
                type,
                shift,
                date
            )
            listCall.enqueue(object : Callback<DateTotalPerDay> {
                override fun onResponse(
                    call: Call<DateTotalPerDay>,
                    response: Response<DateTotalPerDay>
                ) {
                    mTotal = response.body()!!

                    if (type == "Estação de trabalho") {
                        if (normal) {
                            if (location == "Santos") {
                                checkStatusOfDateNormal(
                                    Constants.SANTOS_MAX_QUANTITY,
                                    mTotal.length,
                                    date,
                                    day
                                )
                            } else {
                                checkStatusOfDateNormal(
                                    Constants.SAO_PAULO_MAX_QUANTITY,
                                    mTotal.length,
                                    date,
                                    day
                                )
                            }

                            println("$location, $type, $shift, $date = ${mTotal.length}")
                        } else {
                            if (location == "Santos") {
                                checkStatusOfDateRecurrent(
                                    Constants.SANTOS_MAX_QUANTITY,
                                    mTotal.length,
                                    date,
                                    day
                                )
                            } else {
                                checkStatusOfDateRecurrent(
                                    Constants.SAO_PAULO_MAX_QUANTITY,
                                    mTotal.length,
                                    date,
                                    day
                                )
                            }
                        }
                    } else {
                        var mSelectedDateFormater = DateSelected(date, day)

                        mSelectedDates.add(mSelectedDateFormater)

                        populateDatesList(mSelectedDates)
                    }

                }

                override fun onFailure(
                    call: Call<DateTotalPerDay>,
                    t: Throwable
                ) {
                }
            })
        } else {
            showToastError("Não foi possível baixar os dados, tente mais tarde!")
        }
    }

    private fun setupDropdownForMeeting(work: String) {

        if (work == "Sala de Reuniões") {
            val textFieldShift = mBinding.actvShift as? AutoCompleteTextView
            val shift =
                arrayListOf("08h às 10h", "10h às 12h", "12h às 14h", "14h às 16h", "16h às 18h")
            val adapterShift = ArrayAdapter(this, R.layout.list_items, R.id.tv_item, shift)
            textFieldShift?.setAdapter(adapterShift)
            mBinding.llHideWhenMeeting.visibility = View.GONE
            mBinding.tvShift.text = "Escolha um horário"
            mBinding.tvDate.text = "Escolha uma data"
            mSelectedType = "Recorrente"
        } else {
            mBinding.llHideWhenMeeting.visibility = View.VISIBLE
            mBinding.tvShift.text = "Escolha um turno"
            mBinding.tvDate.text = "Escolha uma data ou mais datas"
            val textFieldShift = mBinding.actvShift as? AutoCompleteTextView
            val shift = arrayListOf("Manhã", "Tarde", "Dia Inteiro")
            val adapterShift = ArrayAdapter(this, R.layout.list_items, R.id.tv_item, shift)
            textFieldShift?.setAdapter(adapterShift)
        }
    }

    fun seutpOnDatesEmpty() {
        mBinding.tvDateToSelect.isEnabled = true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.logout) {
            var alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setTitle("A Workclock Orange")
            alertDialogBuilder
                .setMessage("Você realmente deseja sair?")
                .setCancelable(false)
                .setPositiveButton(
                    "SIM"
                ) { dialogInterface, i ->
                    finishAffinity();
                    System.exit(0)
                }
                .setNegativeButton(
                    "NÃO"
                ) { dialogInterface, i ->
                    dialogInterface.dismiss()
                }

            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.logout, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun setupActionBar() {
        setSupportActionBar(mBinding.toolbarSchedulingActivity)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_corner_up_left)

        supportActionBar?.title = ""

        mBinding.toolbarSchedulingActivity.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
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
        val bindingRecurrent: DialogConfirmRecurrentSchedulingBinding =
            DialogConfirmRecurrentSchedulingBinding.inflate(layoutInflater)
        dialog.setCancelable(false)
        dialog.setContentView(bindingRecurrent.root)
        bindingRecurrent.tvSelectedUnitRecurrentConfirmation.text = " " + mSelecetdUnit
        if (mWorkOrMeet == "Sala de Reuniões") {
            bindingRecurrent.llHideRecurrent.visibility = View.GONE
        } else {
            bindingRecurrent.tvSelectedTypeRecurrentConfirmation.text = " " + mSelectedType
        }
        bindingRecurrent.tvDayOfWeekRecurrentConfirmation.text =
            "Dia da semana: " + mSelectedDates[0].dayOfWeek
        val dates = bindingRecurrent.tvDatesSelectedRecurrentConfirmation
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
            val createDates = "Data: " + mSelectedDates[0].date
            dates.text = createDates
        }
        bindingRecurrent.tvShiftSelectedRecurrentConfirmation.text = mShift
        bindingRecurrent.btnConfirmRecurrentConfirmation.setOnClickListener {
            showProgressDialog()
            dialog.dismiss()
            for (i in mSelectedDates.indices) {
                if (i == mSelectedDates.size - 1) {
                    scheduleToBD(mSelectedDates[i], true)
                } else {
                    scheduleToBD(mSelectedDates[i], false)
                }
            }
        }

        bindingRecurrent.ibCancelDialogRecurrent.setOnClickListener {
            dialog.dismiss()
        }

        bindingRecurrent.btnCancelRecurrentConfirmation.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showDialogNormal() {
        val dialog = Dialog(this)
        val bindingDialogNormal: DialogConfirmMultipleSchedulingBinding =
            DialogConfirmMultipleSchedulingBinding.inflate(layoutInflater)
        dialog.setContentView(bindingDialogNormal.root)

        bindingDialogNormal.tvSelectedUnitMultipleConfirmation.text = " " + mSelecetdUnit

        val rvDates = bindingDialogNormal.rvDatesSelectedMultipleConfirmation
        rvDates.layoutManager = LinearLayoutManager(this)
        rvDates.setHasFixedSize(true)
        val adapter = SchedulesConfirmationAdapter(this, mSelectedDates)
        rvDates.adapter = adapter

        bindingDialogNormal.tvShiftSelectedMultipleConfirmation.text = mShift
        bindingDialogNormal.btnCancelMultipleConfirmation.setOnClickListener {
            dialog.dismiss()
        }
        bindingDialogNormal.ibCancelNormalConfirmation.setOnClickListener {
            dialog.dismiss()
        }
        bindingDialogNormal.btnConfirmMultipleConfirmation.setOnClickListener {
            showProgressDialog()
            dialog.dismiss()
            for (i in mSelectedDates.indices) {
                if (i == mSelectedDates.size - 1) {
                    scheduleToBD(mSelectedDates[i], true)
                } else {
                    scheduleToBD(mSelectedDates[i], false)
                }
            }
        }
        dialog.show()
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {

        mBinding.tilSchedulingType.isEnabled = false
        mBinding.tilShift.isEnabled = false
        mBinding.tilUnit.isEnabled = false
        mBinding.tilStationOrMeeting.isEnabled = false

        if (mSelectedType == "Normal" || mWorkOrMeet == "Sala de Reuniões") {
            val sDayOfMonth = if (dayOfMonth < 10) "0$dayOfMonth" else "$dayOfMonth"
            val sMonthOfYear =
                if ((monthOfYear + 1) < 10) "0${monthOfYear + 1}" else "${monthOfYear + 1}"
            val selectedDate = "$sDayOfMonth/$sMonthOfYear/$year"
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.US)
            val theDate = sdf.parse(selectedDate)
            val format = SimpleDateFormat("EEEE", Locale.getDefault())
            val dow = (format.format(theDate).replace("f", "F", false)).capitalize()

            getTotalPerDay(mSelecetdUnit, mWorkOrMeet, mShift, selectedDate, dow, true)

            mBinding.tvDateToSelect.isEnabled = false

        } else {
            val sDayOfMonth = if (dayOfMonth < 10) "0$dayOfMonth" else "$dayOfMonth"
            val sMonthOfYear =
                if ((monthOfYear + 1) < 10) "0${monthOfYear + 1}" else "${monthOfYear + 1}"
            val selectedDate = "$sDayOfMonth/$sMonthOfYear/$year"
            val dom = sDayOfMonth.toInt()
            val moy = sMonthOfYear.toInt()
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.US)
            val theDate = sdf.parse(selectedDate)
            val format = SimpleDateFormat("EEEE", Locale.getDefault())
            val dow = (format.format(theDate).replace("f", "F", false)).capitalize()
            val listError = ArrayList<String>()
            val mSelectedDateFormater = DateSelected(selectedDate, dow)
            mSelectedDates.add(mSelectedDateFormater)
            var i = 1
            while (i <= 3) {
                val days = (7 * i).toLong()
                val date = LocalDate.of(year, moy, dom).plusDays(days)
                val dateWrong = SimpleDateFormat("yyyy-MM-dd")
                val dateNew = dateWrong.parse(date.toString())
                val dateNewFormat = SimpleDateFormat("dd/MM/yyyy")
                val dateFinal = dateNewFormat.format(dateNew)
                val mSelectedDateFormater = DateSelected(dateFinal, dow)
                getTotalPerDay(mSelecetdUnit, mWorkOrMeet, mShift, dateFinal, dow, false)
                i++
            }
        }
        mBinding.tvDateToSelect.isEnabled = false
    }

    fun checkStatusOfDateNormal(max: Int, total: Int, date: String, day: String) {
        var contain = false
        if (total < max) {
            for (i in mSelectedDates.indices) {
                if (mSelectedDates[i].date.contains(date)) {
                    showToastAlert("Esta data já foi selecionada!")
                    contain = true
                }
            }
            if (!contain) {
                var mSelectedDateFormater = DateSelected(date, day)

                mSelectedDates.add(mSelectedDateFormater)

                populateDatesList(mSelectedDates)
            }
        } else {
            showToastAlert("Nesta data o escritório ja está com a capacidade máxima agendada")
        }
    }

    fun checkStatusOfDateRecurrent(max: Int, total: Int, date: String, day: String) {
        val listError = ArrayList<String>()
        val mSelectedDateFormater = DateSelected(date, day)
        if (total < max) {
            if (datesToDisable.contains(date)) {
                listError.add(date)
            } else {
                mSelectedDates.add(mSelectedDateFormater)
                populateDatesList(mSelectedDates)
            }
        } else {
            listError.add(date)
        }
        if (listError.isNotEmpty()) {
            var dateInfo = ""
            val max = listError.size - 1
            if (listError.size > 1) {
                dateInfo += "As datas "
                for (i in listError.indices) {
                    if (i == max) {
                        dateInfo += "e ${listError[i]} não foram adicionadas pois você já possui agendamentos para estes dias ou o escritório já está com a lotação máxima."
                    } else {
                        dateInfo += "${listError[i]}, "
                    }
                }
            } else {
                dateInfo += "A data ${listError[0]} não foi adicionada pois você já possui agendamento para este dia ou o escritório já está com a lotação máxima."
            }
            showToastAlert(dateInfo)
        }
    }

    fun setupDatePicker(work: String) {

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

        if (!datesToDisable.isNullOrEmpty()) {

            if (work == "Estação de trabalho") {
                for (i in datesToDisable.indices) {
                    val full = Calendar.getInstance()
                    val fullDayString = datesToDisable[i]
                    val sdf = SimpleDateFormat("dd/MM/yyyy")
                    full.time = sdf.parse(fullDayString)
                    val disabledDays: Array<Calendar?> = arrayOfNulls<Calendar>(1)
                    disabledDays[0] = full
                    datePickerDialog.disabledDays = disabledDays
                }
            } else {
                for (i in datesToDisableMeet.indices) {
                    val full = Calendar.getInstance()
                    val fullDayString = datesToDisableMeet[i]
                    val sdf = SimpleDateFormat("dd/MM/yyyy")
                    full.time = sdf.parse(fullDayString)
                    val disabledDays: Array<Calendar?> = arrayOfNulls<Calendar>(1)
                    disabledDays[0] = full
                    datePickerDialog.disabledDays = disabledDays
                }
            }
        }
    }

    fun scheduleToBD(date: DateSelected, last: Boolean) {

        if (Constants.isNetworkAvailable(this)) {
            val retrofit: Retrofit = Retrofit.Builder().baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val service: ClockworkService =
                retrofit.create(ClockworkService::class.java)
            val listCall = service.scheduleDate(
                mSelecetdUnit,
                mShift,
                mWorkOrMeet,
                date.date,
                date.dayOfWeek,
                mUser._id!!,
                mUser.name!!,
                mUser.lastname!!,
                mUser.email!!,
                mUser.role!!
            )

            println(listCall)
            listCall.enqueue(object : Callback<Schedulingdata.DateScheduling> {
                override fun onResponse(
                    call: Call<Schedulingdata.DateScheduling>,
                    response: Response<Schedulingdata.DateScheduling>
                ) {
                    if (response.isSuccessful) {

                        if (last) {
                            showToastSuccess("Datas agendadas com sucesso!")
                            val intent = Intent()
                            setResult(Activity.RESULT_OK, intent)
                            hideProgressDialog()
                            finish()
                        }

                    }
                }

                override fun onFailure(
                    call: Call<Schedulingdata.DateScheduling>,
                    t: Throwable
                ) {
                    Log.e("Erro", t.message.toString())
                }
            })
        }
    }
}
