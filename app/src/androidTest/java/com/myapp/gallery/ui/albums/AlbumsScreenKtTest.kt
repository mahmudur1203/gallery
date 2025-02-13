package com.myapp.gallery.ui.albums

import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToIndex
import com.myapp.gallery.domain.model.Album
import com.myapp.gallery.domain.state.Resource
import com.myapp.gallery.testing.data.albumsTestData
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

        val albumsTestData = (albumsTestData)
        composeTestRule.setContent {
            AlbumsScreenContent(Resource.Success(albumsTestData), {} , {})
        }

        composeTestRule.onNodeWithTag("AlbumItem_Album 1").isDisplayed()

        composeTestRule.onNodeWithTag("AlbumList").performScrollToIndex(20)
        composeTestRule.onNodeWithTag("AlbumItem_Album 19").isDisplayed()

    }



}