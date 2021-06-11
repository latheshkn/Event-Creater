package com.example.eventcreater.home.fragments

import android.os.Bundle
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
import com.example.eventcreater.home.HomeActivity
import com.example.eventcreater.models.upcomingEvent.UpcomingEventList
import com.example.eventcreater.models.upcomingEvent.upComingEventListModel
import com.example.eventcreater.network.Api
import com.example.eventcreater.network.BaseClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response


class DashBoardFragment : Fragment(), UpcomingListAdapter.SendOnclikLisner {
    lateinit var rvUpEvent: RecyclerView
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
        val view = inflater.inflate(R.layout.fragment_dash_board, container, false)
        rvUpEvent = view.findViewById(R.id.rvUpEvent)

        rvUpEvent.layoutManager = LinearLayoutManager(requireContext())

        (activity as HomeActivity).supportActionBar?.setTitle("Completed Events")

        UpcomingList()
        return view
    }

    suspend fun getUpcomingEvnet(): Response<UpcomingEventList> {

        return withContext(Dispatchers.IO) {
            BaseClient.buildApi(Api::class.java).upComingEventList()
        }

    }

    fun UpcomingList() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = getUpcomingEvnet()

                if (response.isSuccessful) {


                    eventlist = response.body()!!.data
                    upcomingListAdapter =
                        UpcomingListAdapter(response.body()!!.data, this@DashBoardFragment)
                    rvUpEvent.adapter = upcomingListAdapter
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
        val action =
            DashBoardFragmentDirections.actionDashBoardFragment2ToEventImageListFragment(evn_id)
        findNavController().navigate(action)

    }

}