package me.ranieripieper.android.coffee.feature.drinks.repository

import me.ranieripieper.android.coffee.core.service.ServiceResult
import me.ranieripieper.android.coffee.feature.drinks.data.CoffeeDrink
import me.ranieripieper.android.coffee.feature.drinks.network.CoffeeDrinkApi

class CoffeeDrinkRepository constructor(private val coffeeDrinkApi: CoffeeDrinkApi) {

    suspend fun getCoffeeDrinks(): ServiceResult<List<CoffeeDrink>> {
        return try {
            val result = coffeeDrinkApi.getCoofeeDrinks().await()
            ServiceResult.Success(result)
        } catch (ex: Exception) {
            ServiceResult.Error(ex)
        }
    }
}