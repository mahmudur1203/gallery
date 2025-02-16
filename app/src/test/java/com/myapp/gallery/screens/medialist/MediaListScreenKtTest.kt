package com.myapp.gallery.screens.medialist

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import androidx.compose.ui.test.swipeRight
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.myapp.gallery.BaseScreenTest
import com.myapp.gallery.domain.model.Media
import com.myapp.gallery.domain.state.Resource
import com.myapp.gallery.domain.usecase.GetMediaListUseCase
import com.myapp.gallery.screens.MainActivity
import com.myapp.gallery.testing.data.mediaListTestData
import com.myapp.gallery.ui.theme.GalleryTheme
import com.myapp.gallery.ui.util.TestTag
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MediaListScreenKtTest : BaseScreenTest() {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()


    private lateinit var viewModel: MediaListViewModel
    private val getMediaListUseCase = mock<GetMediaListUseCase>()


    private val successResult = Resource.Success<List<Media>>(mediaListTestData)
    private val errorResult = Resource.Error("Error")

    private val albumId = 1L
    private val albumName = "Album 1"

    @Test
    fun `When entering the MediaList screen, it shows Title correctly`() {

        runBlocking {
            whenever(getMediaListUseCase.invoke(1)).thenReturn(
                flow { emit(successResult) })
        }

        initComposable {

            onNodeWithText(albumName).assertIsDisplayed()
        }

    }

    @Test
    fun `When entering the Media List screen, it shows the loader correctly`() {

        runBlocking {
            whenever(getMediaListUseCase.invoke(1)).thenReturn(
                flow {

                })
        }

        initComposable {


            onNodeWithTag(TestTag.LOADING_INDICATOR).assertIsDisplayed()
        }

    }

    @Test
    fun `When entering the MediaList screen, it shows the corresponding error`() {

        runBlocking {
            whenever(getMediaListUseCase.invoke(1)).thenReturn(
                flow { emit(errorResult) })
        }

        initComposable {
            onNodeWithTag(TestTag.ERROR_MESSAGE).assertIsDisplayed()
        }

    }

    @Test
    fun `When entering the Medial screen, it shows the data images correctly`() {

        runBlocking {
            whenever(getMediaListUseCase.invoke(1)).thenReturn(
                flow { emit(successResult) })
        }

        initComposable {

            onNodeWithTag(TestTag.MEDIA_LIST).assertIsDisplayed()

        }

    }

    @Test
    fun `Image should be displayed correctly, on clicking on image`() {

        runBlocking {
            whenever(getMediaListUseCase.invoke(1)).thenReturn(
                flow { emit(successResult) })
        }

        initComposable {

            onNodeWithTag(TestTag.MEDIA_ITEM_PREFIX + mediaListTestData.first().id).assertIsDisplayed()

            onNodeWithTag(TestTag.MEDIA_ITEM_PREFIX + mediaListTestData.first().id).performClick()

            onNodeWithTag(TestTag.MEDIA_FULL_SCREEN_VIEW).assertIsDisplayed()


        }

    }

    @Test
    fun `Full screen display swipe correctly, on swipe left or right`() {

        runBlocking {
            whenever(getMediaListUseCase.invoke(1)).thenReturn(
                flow { emit(successResult) })
        }

        initComposable {

            onNodeWithTag(TestTag.MEDIA_ITEM_PREFIX + mediaListTestData[2].id).performClick()

            onNodeWithTag(TestTag.MEDIA_FULL_SCREEN_VIEW).assertIsDisplayed()

            onNodeWithTag(TestTag.MEDIA_FULL_SCREEN_VIEW_PAGER).performTouchInput { swipeLeft() }

            onNodeWithTag(TestTag.MEDIA_FULL_SCREEN_VIEW_PAGER + mediaListTestData[3].id).assertIsDisplayed()

            onNodeWithTag(TestTag.MEDIA_FULL_SCREEN_VIEW_PAGER).performTouchInput { swipeRight() }

            onNodeWithTag(TestTag.MEDIA_FULL_SCREEN_VIEW_PAGER + mediaListTestData[2].id).assertIsDisplayed()

        }

    }



    private fun initComposable(
        testBody: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>.() -> Unit,
    ) {
        initViewModel()

        composeRule.activity.setContent {
            GalleryTheme {
                MediaListScreen(
                    viewModel = viewModel,
                    albumId = albumId,
                    albumName = albumName,
                    navController = rememberNavController()
                )
            }
        }
        testBody(composeRule)
    }

    private fun initViewModel() {
        viewModel = MediaListViewModel(
            getMediaListUseCase
        )
    }

}