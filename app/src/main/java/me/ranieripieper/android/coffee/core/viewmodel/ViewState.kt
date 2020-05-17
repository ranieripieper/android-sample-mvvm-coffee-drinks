package me.ranieripieper.android.coffee.core.viewmodel

sealed class ViewState {
    class Error(val error: String) : ViewState()
    class Loading(val loading: Boolean) : ViewState()
}