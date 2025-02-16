package com.myapp.gallery.screens.albums

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.myapp.gallery.BaseScreenTest
import com.myapp.gallery.domain.model.Album
import com.myapp.gallery.domain.state.Resource
import com.myapp.gallery.domain.usecase.GetAlbumsUseCase
import com.myapp.gallery.screens.MainActivity
import com.myapp.gallery.screens.medialist.MediaListScreen
import com.myapp.gallery.testing.data.albumsTestData
import com.myapp.gallery.ui.theme.GalleryTheme
import com.myapp.gallery.ui.util.ContentDescriptions
import com.myapp.gallery.ui.util.TestTag
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class AlbumsScreenKtTest : BaseScreenTest() {


    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()


    private lateinit var viewModel: AlbumsViewModel
    private val getAlbumsUseCase = mock<GetAlbumsUseCase>()


    private val successResult = Resource.Success<List<Album>>(emptyList())

    private val successResultWithData = Resource.Success<List<Album>>(data = albumsTestData)
    private val errorResult = Resource.Error("Error")


    @Before
    fun setup() {

    }

    @Test
    fun `When entering the Albums screen, it shows UI correctly`() {

        runBlocking {
            whenever(getAlbumsUseCase.invoke()).thenReturn(
                flow { emit(successResult) })
        }

        initComposable {

            onNodeWithTag(TestTag.ALBUMS_SCREEN).assertIsDisplayed()
        }

    }

    @Test
    fun `When entering the Albums screen, it shows the loader correctly`() {

        runBlocking {
            whenever(getAlbumsUseCase.invoke()).thenReturn(
                flow {

                })
        }

        initComposable {


            onNodeWithTag(TestTag.LOADING_INDICATOR).assertIsDisplayed()
        }

    }


    @Test
    fun `When entering the Albums screen, it shows the corresponding error`() {

        runBlocking {
            whenever(getAlbumsUseCase.invoke()).thenReturn(
                flow { emit(errorResult) })
        }

        initComposable {

            onNodeWithTag(TestTag.ERROR_MESSAGE).assertIsDisplayed()
        }

    }

    @Test
    fun `When entering the Albums screen, it shows the data grid view correctly`() {

        runBlocking {
            whenever(getAlbumsUseCase.invoke()).thenReturn(
                flow { emit(successResultWithData) })
        }

        initComposable {

            onNodeWithTag(TestTag.ALBUM_GRID).assertIsDisplayed()
        }

    }


    @Test
    fun `When entering the Albums screen, it shows grid the list correctly`() {

        runBlocking {
            whenever(getAlbumsUseCase.invoke()).thenReturn(
                flow {
                    emit(successResultWithData)
                })
        }

        initComposable {

            composeRule.waitForIdle()
            advanceUntilIdle()

            viewModel.toggleLayoutType()

            onNodeWithTag(TestTag.ALBUM_LIST).assertIsDisplayed()
        }

    }

    @Test
    fun `It shows grid the list correctly, on toggle grid to list view`() {


        runBlocking {
            whenever(getAlbumsUseCase.invoke()).thenReturn(
                flow {
                    emit(successResultWithData)
                })
        }

        initComposable {

            composeRule.waitForIdle()
            advanceUntilIdle()

            onNodeWithContentDescription(ContentDescriptions.TOGGLE_VIEW).assertIsDisplayed()

            onNodeWithContentDescription(ContentDescriptions.TOGGLE_VIEW).performClick()

            onNodeWithTag(TestTag.ALBUM_LIST).assertIsDisplayed()
        }

    }

    private fun initComposable(
        testBody: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>.() -> Unit,
    ) {
        initViewModel()

        composeRule.activity.setContent {
            GalleryTheme {
                AlbumsScreen(
                    viewModel = viewModel,
                    navController = rememberNavController()
                )
            }
        }
        testBody(composeRule)
    }

    private fun initViewModel() {
        viewModel = AlbumsViewModel(
            getAlbumsUseCase
        )
    }

}