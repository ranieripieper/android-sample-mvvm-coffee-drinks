package me.ranieripieper.android.coffee

import android.app.Application
import me.ranieripieper.android.coffee.di.applicationModule
import me.ranieripieper.android.coffee.di.courotinesModule
import me.ranieripieper.android.coffee.di.networkModule
import me.ranieripieper.android.coffee.feature.drinks.di.drinksModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class CoffeeApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.DEBUG else Level.INFO)
            androidContext(this@CoffeeApplication)
            modules(
                listOf(
                    applicationModule,
                    networkModule,
                    courotinesModule,
                    drinksModule
                )
            )
        }
    }
}