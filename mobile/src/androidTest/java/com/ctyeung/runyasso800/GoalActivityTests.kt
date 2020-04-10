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
class GoalActivityTests {
    @get:Rule
    var mActivityRule: ActivityTestRule<GoalActivity> = ActivityTestRule(
        GoalActivity::class.java
    )

    @Test
    fun getSprintGoal() {
        SharedPrefUtility.setGoal(SharedPrefUtility.keySprintGoal, 100)
        mActivityRule.activity.model.setInitValues()
        val sprintGoal = mActivityRule.activity.model.sprintGoal
        assertEquals(sprintGoal, "00:01:40")
    }

    @Test
    fun getRaceGoal() {
        SharedPrefUtility.setGoal(SharedPrefUtility.keyRaceGoal, 100)
        mActivityRule.activity.model.setInitValues()
        val raceGoal = mActivityRule.activity.model.raceGoal
        assertEquals(raceGoal, "00:01:40")
    }
}