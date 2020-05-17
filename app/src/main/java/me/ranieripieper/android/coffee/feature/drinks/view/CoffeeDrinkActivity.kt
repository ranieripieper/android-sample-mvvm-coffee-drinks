package me.ranieripieper.android.coffee.feature.drinks.view

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import kotlinx.android.synthetic.main.activity_coffee_drink.*
import me.ranieripieper.android.coffee.R

class CoffeeDrinkActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_coffee_drink)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        setupActionBarAndNavController()
    }

    fun setupActionBarAndNavController() {
        appBarConfiguration = AppBarConfiguration(setOf(R.id.coffee_drink_list))
        setupActionBarWithNavController(navHostFragment.findNavController(), appBarConfiguration)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            navHostFragment.findNavController().popBackStack()
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}
