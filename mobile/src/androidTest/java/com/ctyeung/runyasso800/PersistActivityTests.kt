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

    @Test
    fun getSprintGoal() {
        SharedPrefUtility.setGoal(SharedPrefUtility.keySprintGoal, 100)
        val sprintGoal = mActivityRule.activity.getSprintGoal()
        assertEquals(sprintGoal, "Sprint Goal:0hours 1minutes 40seconds")
    }

    @Test
    fun getRaceGoal() {
        SharedPrefUtility.setGoal(SharedPrefUtility.keyRaceGoal, 100)
        val raceGoal = mActivityRule.activity.getRaceGoal()
        assertEquals(raceGoal, "Race Goal:0hours 1minutes 40seconds")
    }
}

