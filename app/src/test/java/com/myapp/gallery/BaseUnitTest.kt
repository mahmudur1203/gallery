package com.myapp.gallery

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule


open class BaseUnitTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutinesRule = CoroutineTestRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()


}