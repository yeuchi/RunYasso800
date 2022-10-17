package com.ctyeung.runyasso800.views

import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/*
 * Reference:
 * Coding with Mitch
 * https://www.youtube.com/watch?v=vVJeOACGSOU
 */

@HiltAndroidTest

class MainActivityTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun happy_path_goal() {

    }
}