package com.example.eventcreater.home.fragments

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.eventcreater.R
import com.example.eventcreater.auth.AuthActivity
import com.example.eventcreater.home.HomeActivity
import com.example.eventcreater.models.notification.NotificationModel
import com.example.eventcreater.network.Api
import com.example.eventcreater.network.BaseClient
import com.example.eventcreater.utils.validation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response


class CreateEventFragment : Fragment() {
    lateinit var token: String
    lateinit var btDatepicker: Button
    lateinit var btCreateEvnt: Button
    lateinit var etEvent: EditText
    lateinit var etPlace: EditText

    private lateinit var datePickerDialog: DatePickerDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_create_event, container, false)
        btDatepicker = view.findViewById(R.id.btDatepicker)
        btCreateEvnt = view.findViewById(R.id.btCreateEvnt)
        etEvent = view.findViewById(R.id.etEvent)
        etPlace = view.findViewById(R.id.etPlace)

        (activity as HomeActivity).supportActionBar?.title = "Create Event"
        generateToken()


        btDatepicker.setOnClickListener({
            initeDatePicker()
        })

        btCreateEvnt.setOnClickListener({
            createEvent()
        })
        return view
    }

    suspend fun sendEventNotification(
        body: String,
        title: String,
        creater: String,
        place: String,
        date: String
    ): Response<NotificationModel> {

        return withContext(Dispatchers.IO) {
            BaseClient.buildApi(Api::class.java).sendNotification(
                token, body, title, creater,
                place, date
            )
        }

    }

    fun createEvent() {

        val event = etEvent.text.toString().trim()
        val place = etPlace.text.toString().trim()
        val date = btDatepicker.text.toString().trim()

        CoroutineScope(Dispatchers.Main).launch {


            if (validation(event) and validation(place) and validation(date)) {

                try {

                    val response = sendEventNotification("$event sheduled on $date",event,"1",place,date)

                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "success", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(requireContext(), response.message(), Toast.LENGTH_LONG)
                            .show()

                    }
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Error $e", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(requireContext(), "please fill all fields", Toast.LENGTH_LONG).show()

            }


        }
    }

    private fun generateToken() {

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            // Get new FCM registration token
            token = task.result!!

            Log.d("fcmToken", "fcm $token")


        })

    }

    fun initeDatePicker() {

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        val dpd = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                // Display Selected date in textbox
                btDatepicker.setText(makeDateString(dayOfMonth, monthOfYear, year))

            },
            year,
            month,
            day
        )

        dpd.show()


    }

    private fun makeDateString(dayOfMonth: Int, month: Int, year: Int): String {

        return "$month / $dayOfMonth / $year"
    }

}