package com.squad34.aclockworkorange.activities

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.squad34.aclockworkorange.R
import com.squad34.aclockworkorange.adapters.SchedulesConfirmationAdapter
import com.squad34.aclockworkorange.databinding.*
import com.squad34.aclockworkorange.models.DateSelected
import com.squad34.aclockworkorange.models.Schedulingdata
import com.squad34.aclockworkorange.network.ClockworkService
import com.squad34.aclockworkorange.utils.Constants
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

class EditScheduleActivity : BaseActivity(), DatePickerDialog.OnDateSetListener {
    private lateinit var mBinding: ActivityEditScheduleBinding
    private var position = 0
    private var mSelecetdUnit = ""
    private var mSelectedWork = ""
    private var mSelectedShift = ""
    private var mSelectedDate = ""
    private var mSelectedDay = ""
    private var year = 0
    private var month = 0
    private var day = 0
    private lateinit var calendar: Calendar
    private var datesToDisable = ArrayList<String>()
    private lateinit var datePickerDialog: DatePickerDialog
    private lateinit var userSchedules: ArrayList<Schedulingdata.DateScheduling>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_schedule)

        setupDatePicker()

        mBinding = ActivityEditScheduleBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        if (intent.hasExtra(MainActivity.POSITION)) {
            position = intent.getIntExtra(MainActivity.POSITION, 0)
        }
        if (intent.hasExtra(MainActivity.EDITSCHEDULE)) {
            userSchedules = intent.getParcelableArrayListExtra(MainActivity.EDITSCHEDULE)!!

            mSelecetdUnit = userSchedules[position].location.toString()
            mSelectedWork = userSchedules[position].type.toString()
            mSelectedShift = userSchedules[position].shift.toString()
            mSelectedDate = userSchedules[position].date.toString()
            mSelectedDay = userSchedules[position].day.toString()

            mBinding.tvSelectedUnitEdit.text = mSelecetdUnit
            mBinding.tvSelectedStationEdit.text = mSelectedWork
            mBinding.tvSelectedShiftEdit.text = mSelectedShift
            mBinding.actvDateEdit.setText(userSchedules[position].date)
        }

        setupDropDownMenus()

        mBinding.tvSelectedUnitEdit.setOnClickListener {
            mBinding.tvSelectedUnitEdit.visibility = View.GONE
            mBinding.tilUnitEdit.visibility = View.VISIBLE
        }

        mBinding.actvUnitEdit.onItemClickListener =
            AdapterView.OnItemClickListener { p0, p1, p2, p3 ->
                mSelecetdUnit = p0?.getItemAtPosition(p2).toString()
            }

        mBinding.actvWorkStationEdit.onItemClickListener =
            AdapterView.OnItemClickListener { p0, p1, p2, p3 ->
                mSelectedWork = p0?.getItemAtPosition(p2).toString()
            }

        mBinding.actvShiftEdit.onItemClickListener =
            AdapterView.OnItemClickListener { p0, p1, p2, p3 ->
                mSelectedShift = p0?.getItemAtPosition(p2).toString()
            }

        mBinding.actvDateEdit.setOnClickListener {
            datePickerDialog.show(getFragmentManager(), "DatePickerDialog")
        }

        mBinding.btnConfirmEdit.setOnClickListener {
            showDialogEdit()
        }

        mBinding.btnCancelEdit.setOnClickListener {
            val intent = Intent()
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
        mBinding.btnDeleteEdit.setOnClickListener {
            showDialogDelete()
        }
    }

    private fun showDialogDelete() {
        val dialog = Dialog(this)
        val bindingDialogDelete: DialogDeleteSchedulingBinding =
            DialogDeleteSchedulingBinding.inflate(layoutInflater)
        dialog.setContentView(bindingDialogDelete.root)

        bindingDialogDelete.tvSelectedUnitConfirmationDelete.text = " " + mSelecetdUnit

        bindingDialogDelete.tvDayOfWeekConfirmationDelete.text = "Dia da semana: " + mSelectedDay

        bindingDialogDelete.tvDateSelectedConfirmationDelete.text = "Data: " + mSelectedDate

        bindingDialogDelete.tvShiftSelectedConfirmationDelete.text = " " + mSelectedShift

        bindingDialogDelete.btnCancelDelete.setOnClickListener {
            dialog.dismiss()
        }
        bindingDialogDelete.ibCancelDialogDelete.setOnClickListener {
            dialog.dismiss()
        }
        bindingDialogDelete.btnConfirmationDelete.setOnClickListener {
            dialog.dismiss()
            deleteSchedule()
        }


        dialog.show()
    }

    private fun showDialogEdit() {
        val dialog = Dialog(this)
        val bindingDialogEdit: DialogConfirmEditSchedulingBinding =
            DialogConfirmEditSchedulingBinding.inflate(layoutInflater)
        dialog.setContentView(bindingDialogEdit.root)

        bindingDialogEdit.tvSelectedUnitConfirmationEdit.text = " " + mSelecetdUnit

        bindingDialogEdit.tvDayOfWeekConfirmationEdit.text = "Dia da semana: " + mSelectedDay

        bindingDialogEdit.tvDateSelectedConfirmationEdit.text = "Data: " + mSelectedDate

        bindingDialogEdit.tvShiftSelectedConfirmationEdit.text = " " + mSelectedShift

        bindingDialogEdit.btnCancelConfirmationEdi.setOnClickListener {
            dialog.dismiss()
        }
        bindingDialogEdit.ibCancelDialogEdit.setOnClickListener {
            dialog.dismiss()
        }
        bindingDialogEdit.btnConfirmationEdit.setOnClickListener {
            dialog.dismiss()
            scheduleToBD()
        }


        dialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {

        val sDayOfMonth = if (dayOfMonth < 10) "0$dayOfMonth" else "$dayOfMonth"
        val sMonthOfYear = if ((monthOfYear + 1) < 10) "0${monthOfYear + 1}" else "${monthOfYear + 1}"
        val selectedDate = "$sDayOfMonth/$sMonthOfYear/$year"
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.US)
        val theDate = sdf.parse(selectedDate)
        val format = SimpleDateFormat("EEEE", Locale.getDefault())
        var dow = format.format(theDate)
        dow.replace("f", "F", false)
        mSelectedDate = selectedDate
        mSelectedDay = dow.capitalize(Locale("BR"))
        mBinding.actvDateEdit.setText(mSelectedDate)
    }

    fun setupDatePicker() {

        calendar = Calendar.getInstance()
        year = calendar.get(Calendar.YEAR)
        month = calendar.get(Calendar.MONTH)
        day = calendar.get(Calendar.DAY_OF_MONTH)

        datePickerDialog = DatePickerDialog.newInstance(this@EditScheduleActivity, year, month, day)
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
    }

    private fun setupDropDownMenus() {
        val textFieldUnits: AutoCompleteTextView = mBinding.actvUnitEdit
        val units = arrayListOf("São Paulo", "Santos")
        val adapterUnit = ArrayAdapter(this, R.layout.list_items, R.id.tv_item, units)
        textFieldUnits.setAdapter(adapterUnit)

        val textFieldWorkStation = mBinding.actvWorkStationEdit as? AutoCompleteTextView
        val workStation = arrayListOf("Estação de trabalho", "Sala de reunião")
        val adapterWork = ArrayAdapter(this, R.layout.list_items, R.id.tv_item, workStation)
        textFieldWorkStation?.setAdapter(adapterWork)

        val textFieldShift = mBinding.actvShiftEdit as? AutoCompleteTextView
        val shift = arrayListOf("Manhã", "Tarde", "Dia Inteiro")
        val adapterShift = ArrayAdapter(this, R.layout.list_items, R.id.tv_item, shift)
        textFieldShift?.setAdapter(adapterShift)
    }

    private fun scheduleToBD() {

        if (Constants.isNetworkAvailable(this)) {
            val retrofit: Retrofit = Retrofit.Builder().baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val service: ClockworkService = retrofit.create(ClockworkService::class.java)
            val listCall = service.updateSchedule(
                userSchedules[position]._id!!,
                mSelecetdUnit,
                mSelectedShift,
                mSelectedWork,
                mSelectedDate,
                mSelectedDay,
                userSchedules[position]._idUser!!,
                userSchedules[position].name!!,
                userSchedules[position].lastname!!,
                userSchedules[position].email!!,
                userSchedules[position].role!!
            )

            println(listCall)
            listCall.enqueue(object : Callback<Schedulingdata.DateScheduling> {
                override fun onResponse(
                    call: Call<Schedulingdata.DateScheduling>,
                    response: Response<Schedulingdata.DateScheduling>
                ) {
                    if (response.isSuccessful) {
                        println("Mensagem de retorno : ${response.body().toString()}")
                        Toast.makeText(
                            this@EditScheduleActivity,
                            "Datas editada com sucesso!",
                            Toast.LENGTH_LONG
                        ).show()
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

    private fun deleteSchedule() {

        println(userSchedules[position]._id!!)

        if (Constants.isNetworkAvailable(this)) {
            val retrofit: Retrofit = Retrofit.Builder().baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val service: ClockworkService = retrofit.create(ClockworkService::class.java)
            val listCall = service.deleteSchedule(
                userSchedules[position]._id!!
            )
            println(listCall)
            listCall.enqueue(object : Callback<Schedulingdata.DateScheduling> {
                override fun onResponse(
                    call: Call<Schedulingdata.DateScheduling>,
                    response: Response<Schedulingdata.DateScheduling>
                ) {
                    if (response.isSuccessful) {
                        println("Mensagem de retorno : ${response.body().toString()}")
                        Toast.makeText(
                            this@EditScheduleActivity,
                            "Data apagada com sucesso!",
                            Toast.LENGTH_LONG
                        ).show()
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
}