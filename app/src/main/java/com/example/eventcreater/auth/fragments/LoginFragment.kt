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
import com.example.eventcreater.network.Api
import com.example.eventcreater.network.BaseClient
import com.example.eventcreater.preference.SharedPreferenceaManager
import com.example.eventcreater.utils.validation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response


class LoginFragment : Fragment() {
    lateinit var emailEt: EditText
    lateinit var passwordEt: EditText
    lateinit var signInBtn: Button
    lateinit var signUpTxt: TextView
    lateinit var preference: SharedPreferenceaManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_login, container, false)



        emailEt = view.findViewById(R.id.emailEt)
        passwordEt = view.findViewById(R.id.passwordEt)
        signInBtn = view.findViewById(R.id.signInBtn)
        signUpTxt = view.findViewById(R.id.signUpTxt)


        (activity as AuthActivity).supportActionBar?.title = "Login"
        preference = SharedPreferenceaManager(requireContext())
        if (preference.getEmail() != null && !preference.getEmail().equals("")) {
            val intent = Intent(requireContext(), HomeActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        signInBtn.setOnClickListener({
            userLogin()
        })

        signUpTxt.setOnClickListener({

            val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            findNavController().navigate(action)


        })

        return view
    }


    suspend fun login(email: String, password: String): Response<CommonModel> {

        return withContext(Dispatchers.IO) {

            BaseClient.buildApi(Api::class.java).login(email, password)
        }

    }

    fun userLogin() {

        val email = emailEt.text.toString().trim()
        val password = passwordEt.text.toString().trim()

        val emailValidate = validation(email)
        val passwordValidate = validation(password)


        CoroutineScope(Dispatchers.Main).launch {
            try {
                if (emailValidate and passwordValidate) {
                    var response = login(email, password)

                    if (response.isSuccessful and response.body()?.status.equals("1")) {
                        val intent = Intent(requireContext(), HomeActivity::class.java)
                        startActivity(intent)
                        activity?.finish()


                    } else {
                        Toast.makeText(requireContext(), "something went wrong", Toast.LENGTH_LONG)
                            .show()

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