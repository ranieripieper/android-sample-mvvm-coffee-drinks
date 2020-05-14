package me.ranieripieper.android.coffee.feature.drinks.network

import kotlinx.coroutines.Deferred
import me.ranieripieper.android.coffee.feature.drinks.data.CoffeeDrink
import retrofit2.http.GET

interface CoffeeDrinkApi {

    @GET("ranieripieper/android-sample-coffee-mvvm/master/sample_api_coffee.json")
    fun getCoofeeDrinks(
    ): Deferred<List<CoffeeDrink>>
}