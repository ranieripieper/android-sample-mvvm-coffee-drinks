package me.ranieripieper.android.coffee.core.view

import android.widget.Toast
import androidx.fragment.app.Fragment
import me.ranieripieper.android.coffee.core.viewmodel.BaseViewModel
import org.koin.androidx.viewmodel.ext.android.getSharedViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel
import java.lang.reflect.ParameterizedType
import kotlin.reflect.KClass

abstract class BaseFragment<VM : BaseViewModel>(private val shared: Boolean = false) : Fragment() {

    val viewModel: VM by lazy {
        if (shared) getSharedViewModel(viewModelClass()) else getViewModel(
            viewModelClass()
        )
    }

    @Suppress("UNCHECKED_CAST")
    private fun viewModelClass(): KClass<VM> {
        return ((javaClass.genericSuperclass as ParameterizedType)
            .actualTypeArguments[0] as Class<VM>).kotlin
    }

    open fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }
}