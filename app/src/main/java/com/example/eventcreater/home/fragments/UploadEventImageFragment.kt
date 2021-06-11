package com.example.eventcreater.home.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.eventcreater.R
import com.example.eventcreater.models.CommonModel
import com.example.eventcreater.network.Api
import com.example.eventcreater.network.BaseClient
import com.example.eventcreater.utils.UploadImageRequestBody
import com.example.eventcreater.utils.getFileName
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class UploadEventImageFragment : Fragment(), UploadImageRequestBody.UploadCallback {
    val REQUEST_CODE: Int = 102
    lateinit var photoFile: File
    lateinit var currentPhotoPath: String
    var uri: Uri? = null
    lateinit var IvImage: ImageView
    lateinit var bnPickImage: Button
    var event_id: String? = null
    var description: String? = "good time"
    val args: UploadEventImageFragmentArgs by navArgs()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_upload_event_image, container, false)
        IvImage = view.findViewById(R.id.IvImage)
        bnPickImage = view.findViewById(R.id.bnPickImage)

        event_id = args.eventId
        IvImage.setOnClickListener({
            takeSelife()
        })
        bnPickImage.setOnClickListener({
            uploadSelfie()
        })
        return view
    }

    private fun createImageFile(): File? {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "SELFI_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE) {
            IvImage.setImageURI(uri)

        } else {
            Toast.makeText(requireContext(), "no data", Toast.LENGTH_LONG).show()
        }
    }

    private fun uploadSelfie() {

//        SimpleArcLoader(requireContext(), "Uploading..", false)

        val parcelFileDescriptor = requireContext().contentResolver.openFileDescriptor(
            uri!!, "r", null
        ) ?: return


        val file =
            File(requireContext().cacheDir, requireContext().contentResolver.getFileName(uri!!))
        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)


        val body = UploadImageRequestBody(file, "image", this)
        val fileName = file.name

        viewLifecycleOwner.lifecycleScope.launch {

            try {
                val response = uploadSelfieApiCall(fileName, body)

                if (response.isSuccessful) {

                    val responseData = response.body()
                    Toast.makeText(
                        requireContext(),
                        "Sucess  ${responseData!!.message}",
                        Toast.LENGTH_SHORT
                    ).show()

                    val action =
                        UploadEventImageFragmentDirections.actionUploadEventImageFragmentToDashBoardFragment2()
                    findNavController().navigate(action)


                } else {
                    Toast.makeText(
                        requireContext(),
                        "Response Code ${response.code()} Response Message ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    "Exception Occured ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun takeSelife() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(requireContext().packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (e: Exception) {
                    // Error occurred while creating the File
                    Toast.makeText(requireContext(), "null", Toast.LENGTH_LONG)
                        .show()
                    null
                }

                // Continue only if the File was successfully created
                photoFile?.also { file ->
                    val photoUri: Uri = FileProvider.getUriForFile(
                        requireContext(),
                        "com.example.eventcreater.provider",
                        file
                    )
                    uri = photoUri

                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                    Toast.makeText(requireContext(), photoUri.toString(), Toast.LENGTH_LONG)
                        .show()

                    startActivityForResult(takePictureIntent, REQUEST_CODE)

                }
            }

        }
    }

    private suspend fun uploadSelfieApiCall(
        fileName: String,
        body: UploadImageRequestBody
    ): Response<CommonModel> {


        return withContext(Dispatchers.IO) {
            BaseClient.buildApi(Api::class.java).uploadImage(
                MultipartBody.Part.createFormData("profileImg", fileName, body),

                event_id!!.toRequestBody(
                    "multipart/form-data".toMediaTypeOrNull()
                ),
                description!!.toRequestBody(
                    "multipart/form-data".toMediaTypeOrNull()
                )
            )
        }

    }

    override fun onProgressUpdate(percentage: Int) {


    }


}