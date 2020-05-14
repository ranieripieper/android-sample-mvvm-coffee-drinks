package me.ranieripieper.android.coffee.feature.drinks.di

import me.ranieripieper.android.coffee.feature.drinks.network.CoffeeDrinkApi
import me.ranieripieper.android.coffee.feature.drinks.repository.CoffeeDrinkRepository
import me.ranieripieper.android.coffee.feature.drinks.viewmodel.CoffeeDrinkViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val drinksModule = module {

    fun provideCoffeeDrinkApi(retrofit: Retrofit): CoffeeDrinkApi {
        return retrofit.create(CoffeeDrinkApi::class.java)
    }

    single { provideCoffeeDrinkApi(get()) }
    single { CoffeeDrinkRepository(get()) }

    viewModel {
        CoffeeDrinkViewModel(get(), get())
    }
}
