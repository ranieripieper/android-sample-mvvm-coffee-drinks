package me.ranieripieper.android.coffee.feature.drinks.viewmodel

import androidx.lifecycle.Observer
import me.ranieripieper.android.coffee.R
import me.ranieripieper.android.coffee.BaseUnitTest
import me.ranieripieper.android.coffee.MockitoHelper
import me.ranieripieper.android.coffee.core.service.ServiceResult
import me.ranieripieper.android.coffee.testObserver
import me.ranieripieper.android.coffee.core.viewmodel.ResourceManager
import me.ranieripieper.android.coffee.core.viewmodel.ViewState
import me.ranieripieper.android.coffee.feature.drinks.data.CoffeeDrink
import me.ranieripieper.android.coffee.feature.drinks.repository.CoffeeDrinkRepository
import org.hamcrest.CoreMatchers
import org.junit.Assert
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

    private val errorMsg = "errorMsg"

    @Test
    fun `loading coffee drinks`() {
        createViewModel(true)

        testCoroutineRule.runBlockingTest {

            val argResult = MockitoHelper.argumentCaptor<List<CoffeeDrink>>()

            Mockito.verify(coffeeDrinkListObserver, Mockito.times(1))
                .onChanged(argResult.capture())

            Assert.assertEquals(1, argResult.allValues.size)
            Assert.assertEquals(getCoffeeDrinkList(), argResult.allValues[0])
        }
    }

    @Test
    fun `error test when loading coffee drinks`() {
        createViewModel(false)

        Assert.assertEquals(1, viewModel.viewState.testObserver().observedValues.size)

        Assert.assertThat(
            viewModel.viewState.testObserver().observedValues[0],
            CoreMatchers.instanceOf(ViewState.Error::class.java)
        )
    }

    @Test
    fun `fetching a coffee drink by id`() {
        createViewModel(true)

        testCoroutineRule.runBlockingTest {
            val coffeeDrinks = getCoffeeDrinkList()

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
        createViewModel(false)
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

    private fun getCoffeeDrinkList(): List<CoffeeDrink> {
        val coffeeDrink = CoffeeDrink(
            1, "Name", "image",
            "description", "ratio", "cup"
        )
        return listOf(coffeeDrink)
    }

    private fun createViewModel(successTest: Boolean) {

        testCoroutineRule.runBlockingTest {
            Mockito.`when`(repository.getCoffeeDrinks()).thenReturn(
                if (successTest) ServiceResult.Success(getCoffeeDrinkList())
                else ServiceResult.Error(Exception(errorMsg))
            )
        }

        viewModel = CoffeeDrinkViewModel(repository, resourceManager).apply {
            viewState.observeForever(viewStateObserver)
            viewStateDetail.observeForever(viewStateDetailObserver)
            coffeeDrinkList.observeForever(coffeeDrinkListObserver)
            coffeeDrinkDetail.observeForever(coffeeDrinkDetailObserver)
        }
    }

}