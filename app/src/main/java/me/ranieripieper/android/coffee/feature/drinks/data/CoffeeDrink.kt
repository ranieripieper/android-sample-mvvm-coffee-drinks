package me.ranieripieper.android.coffee.feature.drinks.data

data class CoffeeDrink(
    val id: Long,
    val name: String,
    val image: String,
    val description: String,
    val ratio: String,
    val cup: String
)