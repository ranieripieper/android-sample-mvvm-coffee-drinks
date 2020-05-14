package me.ranieripieper.android.coffee.feature.drinks.repository

import me.ranieripieper.android.coffee.core.BaseUnitTest
import me.ranieripieper.android.coffee.core.service.ServiceResult
import me.ranieripieper.android.coffee.core.toDeferred
import me.ranieripieper.android.coffee.feature.drinks.data.CoffeeDrink
import me.ranieripieper.android.coffee.feature.drinks.network.CoffeeDrinkApi
import org.hamcrest.CoreMatchers
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CoffeeDrinkRepositoryTest : BaseUnitTest() {

    @Mock
    lateinit var coffeeDrinkApi: CoffeeDrinkApi

    @Test
    fun `getting a empty list of coffee drinks`() {
        testCoroutineRule.runBlockingTest {
            val coffeeDrinks: List<CoffeeDrink> = emptyList()

            Mockito.`when`(coffeeDrinkApi.getCoofeeDrinks()).thenReturn(coffeeDrinks.toDeferred())

            val coffeeDrinkRepository = CoffeeDrinkRepository(coffeeDrinkApi)

            val serviceResult = coffeeDrinkRepository.getCoffeeDrinks()
            assertThat(serviceResult, CoreMatchers.instanceOf(ServiceResult.Success::class.java))
            assertEquals(emptyList<CoffeeDrink>(), (serviceResult as ServiceResult.Success).data)
        }
    }

    @Test
    fun `getting a list of coffee drinks`() {
        testCoroutineRule.runBlockingTest {
            val coffeeDrinks = getCoffeeDrinkList()

            Mockito.`when`(coffeeDrinkApi.getCoofeeDrinks()).thenReturn(coffeeDrinks.toDeferred())

            val coffeeDrinkRepository = CoffeeDrinkRepository(coffeeDrinkApi)

            val serviceResult = coffeeDrinkRepository.getCoffeeDrinks()
            assertThat(serviceResult, CoreMatchers.instanceOf(ServiceResult.Success::class.java))
            assertEquals(coffeeDrinks, (serviceResult as ServiceResult.Success).data)
        }
    }

    @Test
    fun `error test when getting a list of coffee drinks`() {
        testCoroutineRule.runBlockingTest {
            Mockito.`when`(coffeeDrinkApi.getCoofeeDrinks()).thenThrow(RuntimeException())

            val coffeeDrinkRepository = CoffeeDrinkRepository(coffeeDrinkApi)

            val serviceResult = coffeeDrinkRepository.getCoffeeDrinks()
            assertThat(serviceResult, CoreMatchers.instanceOf(ServiceResult.Error::class.java))
        }
    }

    private fun getCoffeeDrinkList(): List<CoffeeDrink> {
        val coffeeDrink = CoffeeDrink(
            1, "Name", "image",
            "description", "ratio", "cup"
        )
        return listOf(coffeeDrink)
    }
}