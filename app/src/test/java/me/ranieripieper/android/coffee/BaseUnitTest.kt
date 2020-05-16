package me.ranieripieper.android.coffee

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Rule

abstract class BaseUnitTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule =
        TestCoroutineRule()

    @get:Rule
    val koinTestRule = KoinTestRule()
}