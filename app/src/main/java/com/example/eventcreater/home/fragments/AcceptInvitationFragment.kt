package com.example.eventcreater.home.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.eventcreater.R
import com.example.eventcreater.home.HomeActivity
import com.example.eventcreater.models.CommonModel
import com.example.eventcreater.network.Api
import com.example.eventcreater.network.BaseClient
import com.example.eventcreater.preference.SharedPreferenceaManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response


class AcceptInvitationFragment : Fragment() {

    lateinit var btAcccept: Button
    lateinit var btDecline: Button
    lateinit var preference: SharedPreferenceaManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_accept_invitation, container, false)
        btAcccept = view.findViewById(R.id.btAcccept)
        btDecline = view.findViewById(R.id.btDecline)

        btDecline.setOnClickListener({
            declineInvite()
        })
        btAcccept.setOnClickListener({
            acceptInvite()
        })

        preference = SharedPreferenceaManager(requireContext())
        return view
    }

    suspend fun postDecline(): Response<CommonModel> {

        return withContext(Dispatchers.IO) {

            BaseClient.buildApi(Api::class.java).postDecline("3", preference.getid())

        }

    }

    fun declineInvite() {

        CoroutineScope(Dispatchers.Main).launch {
            val response = postDecline()

            if (response.isSuccessful and response.body()?.status.equals("1")) {
                val intent = Intent(requireContext(), HomeActivity::class.java)
                startActivity(intent)
                activity?.finish()


            } else {
                Toast.makeText(requireContext(), "fail", Toast.LENGTH_LONG).show()
            }
        }
    }

    suspend fun postAccept(): Response<CommonModel> {

        return withContext(Dispatchers.IO) {

            BaseClient.buildApi(Api::class.java).postAccepted("3",preference.getid())

        }

    }

    fun acceptInvite() {

        CoroutineScope(Dispatchers.Main).launch {
            val response = postAccept()

            if (response.isSuccessful and response.body()?.status.equals("1")) {
                val intent = Intent(requireContext(), HomeActivity::class.java)
                startActivity(intent)
                activity?.finish()


            } else {
                Toast.makeText(requireContext(), "fail", Toast.LENGTH_LONG).show()
            }
        }
    }


}