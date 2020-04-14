package com.ctyeung.runyasso800

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.ctyeung.runyasso800.viewModels.SharedPrefUtility
import junit.framework.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import junit.framework.Assert.assertEquals

@RunWith(AndroidJUnit4::class)
class GoalActivityTests {
    @get:Rule
    var mActivityRule: ActivityTestRule<GoalActivity> = ActivityTestRule(
        GoalActivity::class.java
    )

    @Test
    fun onCreate() {
        val title = mActivityRule.activity.title
        val expected = MainApplication.applicationContext().resources.getString(R.string.app_name)
        Assert.assertEquals(title, expected)
    }

    /*
     * Bad form below - sort of testing viewModel
     */

    @Test
    fun getModelSprintGoal() {
        SharedPrefUtility.setGoal(SharedPrefUtility.keySprintGoal, 100)
        mActivityRule.activity.model.setInitValues()
        val sprintGoal = mActivityRule.activity.model.sprintGoal
        assertEquals(sprintGoal, "00:01:40")
    }

    @Test
    fun getModelRaceGoal() {
        SharedPrefUtility.setGoal(SharedPrefUtility.keyRaceGoal, 100)
        mActivityRule.activity.model.setInitValues()
        val raceGoal = mActivityRule.activity.model.raceGoal
        assertEquals(raceGoal, "00:01:40")
    }

    @Test
    fun getModelPersistName() {
        // test default value
        mActivityRule.activity.model.persistName("")
        val name = SharedPrefUtility.getName()
        val expected = MainApplication.applicationContext().resources.getString(R.string.run_yasso_800)
        assertEquals(name, expected)
    }
}