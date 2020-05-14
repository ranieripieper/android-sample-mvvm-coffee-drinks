package me.ranieripieper.android.coffee

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import me.ranieripieper.android.coffee.feature.drinks.view.CoffeeDrinkActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed(Runnable {
            startActivity(Intent(baseContext, CoffeeDrinkActivity::class.java))
            finish()
        }, 2000);
    }
}