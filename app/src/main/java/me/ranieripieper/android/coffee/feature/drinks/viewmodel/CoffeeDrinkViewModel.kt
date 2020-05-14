package me.ranieripieper.android.coffee.feature.drinks.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.ranieripieper.android.coffee.R
import me.ranieripieper.android.coffee.core.service.ServiceResult
import me.ranieripieper.android.coffee.core.viewmodel.BaseViewModel
import me.ranieripieper.android.coffee.core.viewmodel.ResourceManager
import me.ranieripieper.android.coffee.core.viewmodel.ViewState
import me.ranieripieper.android.coffee.feature.drinks.data.CoffeeDrink
import me.ranieripieper.android.coffee.feature.drinks.repository.CoffeeDrinkRepository

class CoffeeDrinkViewModel(
    private val coffeeDrinkRepository: CoffeeDrinkRepository,
    private val resourceManager: ResourceManager
) : BaseViewModel() {

    private val _coffeeDrinkList = MutableLiveData<List<CoffeeDrink>>()
    private val _coffeeDrinkDetail = MutableLiveData<CoffeeDrink>()
    private val _viewStateDetail = MutableLiveData<ViewState>()
    private val _viewState = MutableLiveData<ViewState>()

    val coffeeDrinkList: LiveData<List<CoffeeDrink>> = _coffeeDrinkList
    val coffeeDrinkDetail: LiveData<CoffeeDrink> = _coffeeDrinkDetail
    val viewStateDetail: LiveData<ViewState> = _viewStateDetail
    val viewState: LiveData<ViewState> = _viewState

    init {
        loadCoffeeDrinks()
    }

    fun loadCoffeeDrinks() {
        _viewState.value = ViewState.Loading(true)

        viewModelScope.launch {
            val result = withContext(contextPool.IO) {
                coffeeDrinkRepository.getCoffeeDrinks()
            }

            _viewState.value = ViewState.Loading(false)

            when (result) {
                is ServiceResult.Success -> {
                    _coffeeDrinkList.value = result.data
                }
                is ServiceResult.Error -> {
                    _viewState.value = ViewState.Error(result.exception.message!!)
                }
            }
        }
    }

    fun fetchCoffeeDrink(id: Long) {
        _viewStateDetail.value = ViewState.Loading(true)

        viewModelScope.launch {
            val result = withContext(contextPool.Default) {
                findCoffeeDrink(id)
            }

            _viewStateDetail.value = ViewState.Loading(false)

            if (result != null) {
                _coffeeDrinkDetail.value = result
            } else {
                _viewStateDetail.value =
                    ViewState.Error(resourceManager.getString(R.string.error_coffee_drink_not_found))
            }
        }
    }

    private fun findCoffeeDrink(id: Long): CoffeeDrink? {
        coffeeDrinkList.value?.forEach { coffeeDrink ->
            if (coffeeDrink.id == id) {
                return coffeeDrink
            }
        }
        return null
    }
}