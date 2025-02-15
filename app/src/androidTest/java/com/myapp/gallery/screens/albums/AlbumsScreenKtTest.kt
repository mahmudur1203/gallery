package com.myapp.gallery.screens.albums

import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import com.myapp.gallery.domain.state.LayoutOrientation
import com.myapp.gallery.domain.state.Resource
import com.myapp.gallery.testing.data.albumsTestData
import com.myapp.gallery.ui.util.TestTag
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify

class AlbumsScreenKtTest{

    @get:Rule
    val composeTestRule = createComposeRule()


    @Before
    fun setup() {

    }

    @Test
    fun albumsScreen_displaysLoadingIndicator_whenLoading() {


        composeTestRule.setContent {
            AlbumsScreenContent(AlbumsUiState.Loading,{},{})
        }

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag(TestTag.LOADING_INDICATOR).assertExists()

    }

    @Test
    fun albumsScreen_displaysError_whenError() {
        composeTestRule.setContent {
            AlbumsScreenContent(AlbumsUiState.Error("Error Test"),{},{})
        }


        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag(TestTag.ERROR_MESSAGE).assertExists()
        composeTestRule.onNodeWithTag(TestTag.RETRY_BUTTON).assertExists()
        composeTestRule.onNodeWithTag(TestTag.ERROR_MESSAGE).assertTextContains("Error Test")
    }

    @Test
    fun retryButtonPress_FetchData(){

        val mockOnRetryClick = mock(Runnable::class.java)

        composeTestRule.setContent {
            AlbumsScreenContent(AlbumsUiState.Error("Error Test"), {}, onRetryClick =
                mockOnRetryClick::run)

        }

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag(TestTag.RETRY_BUTTON).assertExists()
        composeTestRule.onNodeWithTag(TestTag.RETRY_BUTTON).performClick()

        verify(mockOnRetryClick, times(1)).run()

    }

    @Test
    fun albumsScreen_displayAlbums_whenSuccessInListView() {

        val albumsTestData = (albumsTestData)
        composeTestRule.setContent {
            AlbumsScreenContent(AlbumsUiState.Success(albumsTestData,
                layoutOrientation = LayoutOrientation.LIST
                ), {} , {})
        }

        val startIndex = 0;
        val lastIndex = albumsTestData.size - 1;

        composeTestRule.onNodeWithTag( TestTag.ALBUM_ITEM_PREFIX + albumsTestData[startIndex].id).assertExists()
        composeTestRule.onNodeWithTag(TestTag.ALBUM_LIST).performScrollToIndex(lastIndex)
        composeTestRule.onNodeWithTag(TestTag.ALBUM_ITEM_PREFIX+ albumsTestData[lastIndex].id).isDisplayed()

    }

    @Test
    fun albumsScreen_displayAlbums_whenSuccessInGridView() {

        val albumsTestData = (albumsTestData)
        composeTestRule.setContent {
            AlbumsScreenContent(AlbumsUiState.Success(albumsTestData,
                layoutOrientation = LayoutOrientation.GRID
            ), {} , {})
        }

        val startIndex = 0;
        val lastIndex = albumsTestData.size - 1;

        composeTestRule.onNodeWithTag( TestTag.ALBUM_ITEM_PREFIX + albumsTestData[startIndex].id).assertExists()
        composeTestRule.onNodeWithTag(TestTag.ALBUM_GRID).performScrollToIndex(lastIndex)
        composeTestRule.onNodeWithTag(TestTag.ALBUM_ITEM_PREFIX+ albumsTestData[lastIndex].id).isDisplayed()

    }


}