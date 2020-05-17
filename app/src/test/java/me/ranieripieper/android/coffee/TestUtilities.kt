package me.ranieripieper.android.coffee

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import me.ranieripieper.android.coffee.core.viewmodel.CoroutineContextProvider
import org.koin.dsl.module
import org.mockito.ArgumentCaptor

val courotinesModule = module {
    fun provideCoroutineContextProvider(): CoroutineContextProvider {
        return TestContextProvider()
    }

    single { provideCoroutineContextProvider() }
}

fun <T> T.toDeferred() = GlobalScope.async { this@toDeferred }

object MockitoHelper {

    inline fun <reified T : Any> argumentCaptor(): ArgumentCaptor<T> =
        ArgumentCaptor.forClass(T::class.java)
}

class TestObserver<T> : Observer<T> {
    val observedValues = mutableListOf<T?>()

    override fun onChanged(value: T?) {
        observedValues.add(value)
    }
}

fun <T> LiveData<T>.testObserver() = TestObserver<T>()
    .also {
    observeForever(it)
}