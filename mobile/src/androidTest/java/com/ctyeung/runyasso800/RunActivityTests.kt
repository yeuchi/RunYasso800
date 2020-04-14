package com.ctyeung.runyasso800

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RunActivityTests {
    @get:Rule
    var mActivityRule: ActivityTestRule<RunActivity> = ActivityTestRule(
        RunActivity::class.java
    )

}