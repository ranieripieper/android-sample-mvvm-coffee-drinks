package me.ranieripieper.android.coffee.feature.drinks.viewmodel

import androidx.lifecycle.Observer
import me.ranieripieper.android.coffee.R
import me.ranieripieper.android.coffee.core.BaseUnitTest
import me.ranieripieper.android.coffee.core.MockitoHelper
import me.ranieripieper.android.coffee.core.service.ServiceResult
import me.ranieripieper.android.coffee.core.viewmodel.ResourceManager
import me.ranieripieper.android.coffee.core.viewmodel.ViewState
import me.ranieripieper.android.coffee.feature.drinks.data.CoffeeDrink
import me.ranieripieper.android.coffee.feature.drinks.repository.CoffeeDrinkRepository
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CoffeeDrinkViewModelTest : BaseUnitTest() {

    @Mock
    lateinit var repository: CoffeeDrinkRepository

    @Mock
    lateinit var resourceManager: ResourceManager

    @Mock
    lateinit var viewStateDetailObserver: Observer<ViewState>

    @Mock
    lateinit var viewStateObserver: Observer<ViewState>

    @Mock
    lateinit var coffeeDrinkListObserver: Observer<List<CoffeeDrink>>

    @Mock
    lateinit var coffeeDrinkDetailObserver: Observer<CoffeeDrink>

    lateinit var viewModel: CoffeeDrinkViewModel

    @Before
    fun setUp() {
        viewModel = CoffeeDrinkViewModel(repository, resourceManager)
        viewModel.apply {
            viewState.observeForever(viewStateObserver)
            viewStateDetail.observeForever(viewStateDetailObserver)
            coffeeDrinkList.observeForever(coffeeDrinkListObserver)
            coffeeDrinkDetail.observeForever(coffeeDrinkDetailObserver)
        }
    }

    @Test
    fun `loading coffee drinks`() {
        testCoroutineRule.runBlockingTest {
            val coffeeDrinks = getCoffeeDrinkList()

            val serviceResult = ServiceResult.Success(coffeeDrinks)
            Mockito.`when`(repository.getCoffeeDrinks()).thenReturn(serviceResult)

            viewModel.loadCoffeeDrinks()

            val argViewState = MockitoHelper.argumentCaptor<ViewState>()
            val argResult = MockitoHelper.argumentCaptor<List<CoffeeDrink>>()

            Mockito.verify(viewStateObserver, Mockito.times(2))
                .onChanged(argViewState.capture())
            Mockito.verify(coffeeDrinkListObserver, Mockito.times(1))
                .onChanged(argResult.capture())

            Assert.assertEquals(2, argViewState.allValues.size)

            Assert.assertThat(
                argViewState.allValues[0],
                CoreMatchers.instanceOf(ViewState.Loading::class.java)
            )
            Assert.assertThat(
                argViewState.allValues[1],
                CoreMatchers.instanceOf(ViewState.Loading::class.java)
            )

            Assert.assertEquals(
                true,
                (argViewState.allValues[0] as ViewState.Loading).loading
            )
            Assert.assertEquals(
                false,
                (argViewState.allValues[1] as ViewState.Loading).loading
            )

            Assert.assertEquals(1, argResult.allValues.size)
            Assert.assertEquals(coffeeDrinks, argResult.allValues[0])
        }
    }

    @Test
    fun `error test when loading coffee drinks`() {
        val msg = "exception msg"
        testCoroutineRule.runBlockingTest {
            val serviceResult = ServiceResult.Error(Exception(msg))
            Mockito.`when`(repository.getCoffeeDrinks()).thenReturn(serviceResult)

            viewModel.loadCoffeeDrinks()

            val argViewState = MockitoHelper.argumentCaptor<ViewState>()
            Mockito.verify(viewStateObserver, Mockito.times(3))
                .onChanged(argViewState.capture())

            Assert.assertEquals(3, argViewState.allValues.size)
            Assert.assertThat(
                argViewState.allValues[0],
                CoreMatchers.instanceOf(ViewState.Loading::class.java)
            )
            Assert.assertThat(
                argViewState.allValues[1],
                CoreMatchers.instanceOf(ViewState.Loading::class.java)
            )

            Assert.assertThat(
                argViewState.allValues[2],
                CoreMatchers.instanceOf(ViewState.Error::class.java)
            )

            Assert.assertEquals(
                true,
                (argViewState.allValues[0] as ViewState.Loading).loading
            )
            Assert.assertEquals(
                false,
                (argViewState.allValues[1] as ViewState.Loading).loading
            )
            Assert.assertEquals(
                msg,
                (argViewState.allValues[2] as ViewState.Error).error
            )
        }
    }

    @Test
    fun `fetching a coffee drink by id`() {
        testCoroutineRule.runBlockingTest {
            val coffeeDrinks = getCoffeeDrinkList()
            viewModel.coffeeDrinkList.value = coffeeDrinks

            viewModel.fetchCoffeeDrink(1L)

            val argViewState = MockitoHelper.argumentCaptor<ViewState>()
            val argResult = MockitoHelper.argumentCaptor<CoffeeDrink>()

            Mockito.verify(viewStateDetailObserver, Mockito.times(2))
                .onChanged(argViewState.capture())

            Mockito.verify(coffeeDrinkDetailObserver, Mockito.times(1))
                .onChanged(argResult.capture())

            Assert.assertEquals(2, argViewState.allValues.size)

            Assert.assertThat(
                argViewState.allValues[0],
                CoreMatchers.instanceOf(ViewState.Loading::class.java)
            )
            Assert.assertThat(
                argViewState.allValues[1],
                CoreMatchers.instanceOf(ViewState.Loading::class.java)
            )

            Assert.assertEquals(
                true,
                (argViewState.allValues[0] as ViewState.Loading).loading
            )
            Assert.assertEquals(
                false,
                (argViewState.allValues[1] as ViewState.Loading).loading
            )

            Assert.assertEquals(coffeeDrinks[0], argResult.allValues[0])
        }
    }

    @Test
    fun `error test when fetching a coffee drink by id`() {
        testCoroutineRule.runBlockingTest {

            val errorMsg = "errorMsg"
            Mockito.`when`(resourceManager.getString(R.string.error_coffee_drink_not_found))
                .thenReturn(errorMsg)

            viewModel.fetchCoffeeDrink(1L)

            val argViewState = MockitoHelper.argumentCaptor<ViewState>()

            Mockito.verify(viewStateDetailObserver, Mockito.times(3))
                .onChanged(argViewState.capture())

            Assert.assertThat(
                argViewState.allValues[0],
                CoreMatchers.instanceOf(ViewState.Loading::class.java)
            )
            Assert.assertThat(
                argViewState.allValues[1],
                CoreMatchers.instanceOf(ViewState.Loading::class.java)
            )

            Assert.assertThat(
                argViewState.allValues[2],
                CoreMatchers.instanceOf(ViewState.Error::class.java)
            )

            Assert.assertEquals(
                true,
                (argViewState.allValues[0] as ViewState.Loading).loading
            )
            Assert.assertEquals(
                false,
                (argViewState.allValues[1] as ViewState.Loading).loading
            )
            Assert.assertEquals(
                errorMsg,
                (argViewState.allValues[2] as ViewState.Error).error
            )
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