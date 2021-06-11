package com.example.eventcreater.home.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eventcreater.R
import com.example.eventcreater.adaptor.ImageListAdapter
import com.example.eventcreater.home.HomeActivity
import com.example.eventcreater.models.imagelist.ImageListModel
import com.example.eventcreater.network.Api
import com.example.eventcreater.network.BaseClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.lang.Exception

class EventImageListFragment : Fragment() {

lateinit var rvImageList:RecyclerView
lateinit var adapter:ImageListAdapter
lateinit var  event:String
 val args: EventImageListFragmentArgs by navArgs()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_event_image_list, container, false)
        rvImageList=view.findViewById(R.id.rvImageList)
        rvImageList.layoutManager=GridLayoutManager(requireContext(),2)
        (activity as HomeActivity).supportActionBar?.setTitle("Event Images")

        event=args.eventId!!
        getImageList()
        return view
    }

    suspend fun getImages(): Response<ImageListModel> {

        return withContext(Dispatchers.IO){
            BaseClient.buildApi(Api::class.java).getImageList(event)

        }

    }
    fun getImageList(){

        CoroutineScope(Dispatchers.Main).launch {

            try {

                val response=getImages()

                if (response.isSuccessful and response.body()?.status.equals("1")){

                    adapter= ImageListAdapter(response.body()!!.data)
                    rvImageList.adapter=adapter


                }else{
                    Toast.makeText(requireContext(),"something went wrong",Toast.LENGTH_LONG).show()

                }
            }catch (e:Exception){
                Toast.makeText(requireContext(),"Error $e",Toast.LENGTH_LONG).show()

            }
        }
    }

}