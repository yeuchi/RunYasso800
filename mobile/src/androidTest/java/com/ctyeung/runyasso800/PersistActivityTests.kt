package com.ctyeung.runyasso800

import org.junit.Rule
import androidx.test.rule.ActivityTestRule
import com.ctyeung.runyasso800.viewModels.SharedPrefUtility
import org.junit.Test
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.runner.RunWith
import junit.framework.Assert.assertEquals

@RunWith(AndroidJUnit4::class)
class PersistActivityTests {
    @get:Rule
    var mActivityRule: ActivityTestRule<PersistActivity> = ActivityTestRule(
        PersistActivity::class.java
    )

    @Test
    fun onCreate() {
        val title = mActivityRule.activity.title
        val expected = MainApplication.applicationContext().resources.getString(R.string.app_name)
        assertEquals(title, expected)
    }

    @Test
    fun getSplits() {

    }

    @Test
    fun getSteps() {

    }

    @Test
    fun getPerformance() {

    }

    @Test
    fun buildYassoMsg() {

    }

}

