package com.myapp.gallery.ui.albums

import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import com.myapp.gallery.domain.model.Album
import com.myapp.gallery.domain.state.Resource
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify

class AlbumsScreenKtTest{

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun albumsScreen_displaysLoadingIndicator_whenLoading() {
        composeTestRule.setContent {
            AlbumsScreenContent(Resource.Loading,{},{})
        }

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag("LoadingIndicator").assertExists()

    }

    @Test
    fun albumsScreen_displaysError_whenError() {
        composeTestRule.setContent {
            AlbumsScreenContent(Resource.Error("Error Test"),{},{})
        }

        composeTestRule.onNodeWithTag("ErrorMessage").assertExists()
        composeTestRule.onNodeWithTag("RetryButton").assertExists()
        composeTestRule.onNodeWithTag("ErrorMessage").assertTextContains("Error Test")
    }

    @Test
    fun retryButtonPress_FetchData(){

        val mockOnRetryClick = mock(Runnable::class.java)

        composeTestRule.setContent {
            AlbumsScreenContent(Resource.Error("Error Test"), {}, onRetryClick =
                mockOnRetryClick::run)

        }

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag("RetryButton").isDisplayed()
        composeTestRule.onNodeWithTag("RetryButton").performClick()

        verify(mockOnRetryClick, times(1)).run()

    }

    @Test
    fun albumsScreen_displayAlbums_whenSuccess() {

        val albumsTestData =
            listOf(
                Album("Album 1", 100, "https://picsum.photos/id/100/200/300"),
                Album("Album 2", 200, "https://picsum.photos/id/200/200/300"),
                Album("Album 3", 300, "https://picsum.photos/id/300/200/300"),
                Album("Album 4", 400, "https://picsum.photos/id/400/200/300"),
                Album("Album 5", 500, "https://picsum.photos/id/500/200/300"),
                Album("Album 6", 600, "https://picsum.photos/id/600/200/300"),
                Album("Album 7", 700, "https://picsum.photos/id/700/200/300"),
                Album("Album 8", 800, "https://picsum.photos/id/800/200/300"),
                Album("Album 9", 900, "https://picsum.photos/id/900/200/300"),
                Album("Album 10", 1000, "https://picsum.photos/id/1000/200/300"))

        composeTestRule.setContent {
            AlbumsScreenContent(Resource.Success(albumsTestData), {} , {})
        }

        composeTestRule.onNodeWithTag("AlbumItem_Album 1").isDisplayed()

        composeTestRule.onNodeWithTag("AlbumItem_Album 10").performScrollTo()
        composeTestRule.onNodeWithTag("AlbumItem_Album 10").isDisplayed()

    }



}