package com.myapp.gallery

import com.myapp.gallery.util.DispatchersProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@OptIn(ExperimentalCoroutinesApi::class)
class CoroutineTestRule constructor(var testDispatcher: TestDispatcher = UnconfinedTestDispatcher()) :
    TestWatcher() {

    val testDispatcherProvider = object : DispatchersProvider {

        override val io: CoroutineDispatcher
            get() = testDispatcher

        override val main: CoroutineDispatcher
            get() = testDispatcher

        override val default: CoroutineDispatcher
            get() = testDispatcher
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.setMain(testDispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun finished(description: Description?) {
        super.finished(description)
        //advanceUntilIdle()
        //cleanupTestCoroutines()
        Dispatchers.resetMain()
    }
}