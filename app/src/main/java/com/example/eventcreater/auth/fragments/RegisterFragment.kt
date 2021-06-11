package com.example.eventcreater.auth.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.eventcreater.R
import com.example.eventcreater.auth.AuthActivity
import com.example.eventcreater.home.HomeActivity
import com.example.eventcreater.models.CommonModel
import com.example.eventcreater.models.register.RegisterModel
import com.example.eventcreater.network.Api
import com.example.eventcreater.network.BaseClient
import com.example.eventcreater.preference.SharedPreferenceaManager
import com.example.eventcreater.utils.validation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class RegisterFragment : Fragment() {

    lateinit var emailEt: EditText
    lateinit var passwordEt: EditText
    lateinit var signUpCPasswordEt: EditText
    lateinit var signUpBtn: Button
    lateinit var loginTxt: TextView
    lateinit var preference: SharedPreferenceaManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_register, container, false)
        emailEt = view.findViewById(R.id.signUpEmailEt)
        passwordEt = view.findViewById(R.id.signUpPasswordEt)
        signUpCPasswordEt = view.findViewById(R.id.signUpCPasswordEt)
        signUpBtn = view.findViewById(R.id.signUpBtn)
        loginTxt = view.findViewById(R.id.loginTxt)

        (activity as AuthActivity?)?.getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        (activity as AuthActivity).supportActionBar?.title = "Register"

        signUpBtn.setOnClickListener({
            userRegister()
        })

        loginTxt.setOnClickListener({
            val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
            findNavController().navigate(action)
        })
        preference= SharedPreferenceaManager(requireContext())


        return view

    }

    suspend fun register(email: String, password: String): Response<RegisterModel> {

        return withContext(Dispatchers.IO) {

            BaseClient.buildApi(Api::class.java).signUp(email, password)
        }

    }


    fun userRegister() {


        val email = emailEt.text.toString().trim()
        val password = passwordEt.text.toString().trim()
        val cPassword = signUpCPasswordEt.text.toString().trim()


        val emailValidate = validation(email)
        val passwordValidate = validation(password)


        CoroutineScope(Dispatchers.Main).launch {
            try {
                if (emailValidate and passwordValidate and validation(cPassword)) {


                    var response = register(email, password)

                    if (response.isSuccessful and response.body()?.status.equals("1")) {

                        preference.saveUser(response.body()?.user!!.id,response.body()?.user!!.email)

                        val intent = Intent(requireContext(), HomeActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                    } else {
                        Toast.makeText(requireContext(), "fail", Toast.LENGTH_LONG).show()

                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "please enter email and password",
                        Toast.LENGTH_LONG
                    ).show()

                }


            } catch (e: Exception) {
                Toast.makeText(requireContext(), "error ${e.message}", Toast.LENGTH_LONG).show()

            }

        }
    }
}