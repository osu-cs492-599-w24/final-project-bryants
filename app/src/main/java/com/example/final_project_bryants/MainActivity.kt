package com.example.final_project_bryants

import android.os.Bundle
import android.util.Log
import android.view.Menu
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.final_project_bryants.databinding.ActivityMainBinding
import com.example.final_project_bryants.ui.calendar.DateDisplayFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar) // Use the toolbar you've set in your layout

        // Retrieve NavHostFragment and NavController
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        val navController = navHostFragment.navController

        // Defining top-level destinations in the AppBarConfiguration
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, // Use the actual IDs from your navigation graph
                R.id.nav_add_item,
                R.id.nav_gallery,
                R.id.nav_slideshow // Make sure these IDs are correctly referenced
            )
        )

        // Setting up ActionBar with NavController and AppBarConfiguration
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Setup BottomNavigationView with NavController
        val bottomNavigationView: BottomNavigationView = binding.bottomNavigation
        bottomNavigationView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        // Ensure the NavHostFragment is correctly handling the support navigate up
        return findNavController(R.id.nav_host_fragment_content_main).navigateUp() || super.onSupportNavigateUp()
    }

    fun changeFragment(year: Int, month: Int, dayOfMonth: Int) {
        try {
            // Create a formatted date string
            val selectedDate = "$year-$month-$dayOfMonth"

            // Create a Bundle to hold the arguments
            val bundle = Bundle().apply {
                putString("selectedDate", selectedDate)
            }

            // Navigate to the DateDisplayFragment with arguments
            findNavController(R.id.nav_host_fragment_content_main)
                .navigate(R.id.nav_calendar_date, bundle)
        } catch (e: Exception) {
            Log.e("changeFragment", "Error navigating to DateDisplayFragment: ${e.message}")
        }
    }
}

