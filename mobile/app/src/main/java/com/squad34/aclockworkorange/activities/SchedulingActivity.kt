package com.squad34.aclockworkorange.activities

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.View
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
import java.time.LocalDate
import android.util.Log
import com.squad34.aclockworkorange.adapters.SchedulesConfirmationAdapter
import com.squad34.aclockworkorange.databinding.DialogConfirmMultipleSchedulingBinding
import com.squad34.aclockworkorange.databinding.DialogConfirmRecurrentSchedulingBinding
import com.squad34.aclockworkorange.models.Schedulingdata
import com.squad34.aclockworkorange.models.UserFromValidator
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
    private lateinit var calendar: Calendar
    private var datesToDisable = ArrayList<String>()
    private lateinit var mUser: UserFromValidator


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        mUser = intent.getParcelableExtra(MainActivity.USERSCHEDULE)!!
        datesToDisable = intent.getStringArrayListExtra(MainActivity.DATES_TO_EXCLUDE)!!



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
                        Toast.makeText(this, "Recurrent yyy", Toast.LENGTH_LONG).show()
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
        val workStation = arrayListOf("Estação de trabalho", "Sala de reunião")
        val adapterWork = ArrayAdapter(this, R.layout.list_items, R.id.tv_item, workStation)
        textFieldWorkStation?.setAdapter(adapterWork)

        val textFieldSchedulingType = mBinding.actvSchedulingType as? AutoCompleteTextView
        val schedulingType = mutableListOf<String>("Recorrente", "Normal")
        val adapterSchedulingType =
            ArrayAdapter(this, R.layout.list_items, R.id.tv_item, schedulingType)
        textFieldSchedulingType?.setAdapter(adapterSchedulingType)

        val textFieldShift = mBinding.actvShift as? AutoCompleteTextView
        val shift = arrayListOf("Manhã", "Tarde", "Dia Inteiro")
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
        val bindingRecurrent: DialogConfirmRecurrentSchedulingBinding = DialogConfirmRecurrentSchedulingBinding.inflate(layoutInflater)
        dialog.setCancelable(false)
        dialog.setContentView(bindingRecurrent.root)

        bindingRecurrent.tvSelectedUnitRecurrentConfirmation.text = " " + mSelecetdUnit

        bindingRecurrent.tvSelectedTypeRecurrentConfirmation.text = " " + mSelectedType

        bindingRecurrent.tvDayOfWeekRecurrentConfirmation.text = "Dia da semana: " + mSelectedDates[0].dayOfWeek

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
            for (i in mSelectedDates.indices) {
                scheduleToBD(mSelectedDates[i])
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
            for (i in mSelectedDates.indices) {
                scheduleToBD(mSelectedDates[i])
            }
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
            val sDayOfMonth = "$dayOfMonth"
            val sMonthOfYear =
                if ((monthOfYear + 1) < 10) "0${monthOfYear + 1}" else "${monthOfYear + 1}"
            val selectedDate = "$sDayOfMonth/$sMonthOfYear/$year"
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.US)
            val theDate = sdf.parse(selectedDate)
            val format = SimpleDateFormat("EEEE", Locale.getDefault())
            var dow = format.format(theDate)
            dow.capitalize()
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

        if (!datesToDisable.isNullOrEmpty()) {
            for (i in datesToDisable.indices) {
                val full = Calendar.getInstance()
                val fullDayString = datesToDisable[i]
                val sdf = SimpleDateFormat("dd/MM/yyyy")
                full.time = sdf.parse(fullDayString)
                val disabledDays: Array<Calendar?> = arrayOfNulls<Calendar>(1)
                disabledDays[0] = full
                datePickerDialog.disabledDays = disabledDays
            }
        }


        hideProgressDialog()
    }

    fun scheduleToBD(date: DateSelected) {

        if (Constants.isNetworkAvailable(this)) {
            val retrofit: Retrofit = Retrofit.Builder().baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val service: ClockworkService = retrofit.create(ClockworkService::class.java)
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
                        println("Mensagem de retorno : ${response.body().toString()}")
                        Toast.makeText(this@SchedulingActivity, "Datas agendadas com sucesso!", Toast.LENGTH_LONG).show()
                        val intent = Intent()
                        setResult(Activity.RESULT_OK, intent)
                        finish()
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
    companion object{
        const val USERFROMSCHEDULE: String = "userFromSchedule"
    }
}
