package com.ctyeung.runyasso800.views

import android.content.Context
import com.ctyeung.runyasso800.data.RunRepository
import com.ctyeung.runyasso800.data.TestRunRepository
import com.ctyeung.runyasso800.di.RunModule
import com.ctyeung.runyasso800.viewmodels.RunViewModel
import com.ctyeung.runyasso800.viewmodels.TestRunViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
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

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Module
    @InstallIn(ActivityComponent::class)
    object TestRunModule {
        @Provides
        fun provideRunRepository(@ApplicationContext context: Context): RunRepository = TestRunRepository(context)

        @Provides
        fun provideRunViewModel(runRepository: TestRunRepository): RunViewModel {
            return TestRunViewModel(runRepository)
        }
    }

    @Test
    fun happy_path_run() {

    }
}