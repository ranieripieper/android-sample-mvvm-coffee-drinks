package me.ranieripieper.android.coffee.core

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import me.ranieripieper.android.coffee.core.viewmodel.CoroutineContextProvider
import org.junit.Rule
import org.koin.dsl.module
import org.mockito.ArgumentCaptor

abstract class BaseUnitTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    val koinTestRule = KoinTestRule()
}

val courotinesModule = module {
    fun provideCoroutineContextProvider(): CoroutineContextProvider {
        return TestContextProvider()
    }

    single { provideCoroutineContextProvider() }
}

fun <T> T.toDeferred() = GlobalScope.async { this@toDeferred }

object MockitoHelper {

    /**
     * Function for creating an argumentCaptor in kotlin.
     */
    inline fun <reified T : Any> argumentCaptor(): ArgumentCaptor<T> =
        ArgumentCaptor.forClass(T::class.java)
}