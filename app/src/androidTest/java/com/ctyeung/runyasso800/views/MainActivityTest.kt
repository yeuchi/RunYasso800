package com.ctyeung.runyasso800.views

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.ctyeung.runyasso800.MainActivity
import com.ctyeung.runyasso800.di.RunModule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/*
 * Reference:
 * Coding with Mitch
 * https://www.youtube.com/watch?v=vVJeOACGSOU
 */

@UninstallModules(RunModule::class)
@HiltAndroidTest
class MainActivityTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()
    @Before
    fun init() {
        hiltRule.inject()
    }

//    @Module
//    @InstallIn(ActivityComponent::class)
//    object TestRunModule {
//        @Provides
//        fun provideRunRepository(@ApplicationContext context: Context): RunRepository = TestRunRepository(context)
//
//        @Provides
//        fun provideRunViewModel(runRepository: TestRunRepository): TestMainViewModel {
//            return TestMainViewModel(runRepository)
//        }
//    }

    @Test
    fun happy_path_goal() {
        composeTestRule.onNodeWithText("Goal").assertIsDisplayed()
    }

    @Test
    fun happy_path_run() {
        composeTestRule.onNodeWithText("Run").assertIsDisplayed()
    }
}