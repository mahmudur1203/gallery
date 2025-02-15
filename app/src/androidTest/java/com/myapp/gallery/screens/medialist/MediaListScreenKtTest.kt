package com.myapp.gallery.screens.medialist

import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import com.myapp.gallery.domain.state.Resource
import com.myapp.gallery.testing.data.mediaListTestData
import com.myapp.gallery.ui.util.TestTag
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify

class MediaListScreenKtTest {
    @get:Rule
    val composeTestRule = createComposeRule()


    @Before
    fun setup() {

    }

    @Test
    fun mediaListScreen_displaysLoadingIndicator_whenLoading() {
        composeTestRule.setContent {
            MediaListScreenContent(Resource.Loading, {}, {})
        }

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("LoadingIndicator")
            .assertExists()
    }

    @Test
    fun mediaListScreen_displaysError_whenError() {


        val mockOnRetryClick = mock(Runnable::class.java)

        composeTestRule.setContent {
            MediaListScreenContent(Resource.Error("Error Test"), {}, onRetryClick = {
                mockOnRetryClick.run()
            })
        }

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag(TestTag.ERROR_MESSAGE)
            .assertExists()

        composeTestRule
            .onNodeWithTag(TestTag.RETRY_BUTTON)
            .assertExists()

        composeTestRule.onNodeWithTag(TestTag.RETRY_BUTTON).performClick()

        verify(mockOnRetryClick, times(1)).run()
    }

    @Test
    fun mediaListScreen_displaysMediaItems_whenSuccess() {

        val mediaList = mediaListTestData

        composeTestRule.setContent {
            MediaListScreenContent(Resource.Success(mediaList), {}, {})
        }

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag(TestTag.MEDIA_LIST)
            .assertExists()

        val startIndex = 0;
        val lastIndex = mediaListTestData.size - 1;

        composeTestRule.onNodeWithTag(TestTag.MEDIA_LIST).performScrollToIndex(70)
        composeTestRule.onNodeWithTag(TestTag.MEDIA_ITEM_PREFIX + mediaList[lastIndex].id).isDisplayed()

    }


}
