package com.example.eventcreater.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.eventcreater.R
import com.example.eventcreater.home.fragments.AcceptInvitationFragment
import com.example.eventcreater.preference.SharedPreferenceaManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var fabButton: FloatingActionButton



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        fabButton = findViewById(R.id.fabButton)
//        navController = Navigation.findNavController(this, R.id.fragmentContainerView2)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) as NavHostFragment
        val navController = navHostFragment.navController
        bottomNavigationView.setupWithNavController(navController)

        val title = intent.extras?.getString("Title")
        if (title != null) {
            navController.navigate(R.id.acceptInvitationFragment2)
        }

        fabButton.setOnClickListener({
            navController.navigate(R.id.dashBoardFragment2)

        })



    }
}