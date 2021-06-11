package com.example.eventcreater.home.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eventcreater.R
import com.example.eventcreater.adaptor.UpcomingListAdapter
import com.example.eventcreater.auth.AuthActivity
import com.example.eventcreater.home.HomeActivity
import com.example.eventcreater.models.upcomingEvent.UpcomingEventList
import com.example.eventcreater.models.upcomingEvent.upComingEventListModel
import com.example.eventcreater.network.Api
import com.example.eventcreater.network.BaseClient
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.*
import retrofit2.Response
import kotlin.coroutines.CoroutineContext


class PendingEventListFragment : Fragment(), UpcomingListAdapter.SendOnclikLisner {
    lateinit var rvPendingEvent: RecyclerView
    lateinit var upcomingListAdapter: UpcomingListAdapter
    lateinit var evn_id: String
    lateinit var eventlist: ArrayList<upComingEventListModel>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_pending_event_list, container, false)

        rvPendingEvent = view.findViewById(R.id.rvPendingEvent)



        (activity as HomeActivity).supportActionBar?.title = "Upcoming Event"
        rvPendingEvent.layoutManager = LinearLayoutManager(requireContext())

        pendingEventList()
        return view

    }

    suspend fun pendingEvent(): Response<UpcomingEventList> {
        return withContext(Dispatchers.IO) {

            BaseClient.buildApi(Api::class.java).getPendingEvnts()


        }

    }

    fun pendingEventList() {

        CoroutineScope(Dispatchers.Main).launch {
            try {

                val response = pendingEvent()

                if (response.isSuccessful) {

                    Toast.makeText(requireContext(), "success", Toast.LENGTH_LONG).show()
                    eventlist = response.body()!!.data
                    upcomingListAdapter =
                        UpcomingListAdapter(response.body()!!.data, this@PendingEventListFragment)
                    rvPendingEvent.adapter = upcomingListAdapter
                } else {

                    Toast.makeText(requireContext(), "fail", Toast.LENGTH_LONG).show()

                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error $e", Toast.LENGTH_LONG).show()

            }

        }
    }

    override fun onClickFromAdapter(position: Int) {
        evn_id = eventlist.get(position).id
        val action =PendingEventListFragmentDirections.actionPendingEventListFragmentToUploadEventImageFragment(evn_id)
        findNavController().navigate(action)

    }
}