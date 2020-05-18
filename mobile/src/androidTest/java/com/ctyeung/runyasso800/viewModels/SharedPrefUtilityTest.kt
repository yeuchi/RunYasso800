package com.ctyeung.runyasso800.viewModels

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.ctyeung.runyasso800.stateMachine.StateError
import com.ctyeung.runyasso800.stateMachine.StateJog
import com.ctyeung.runyasso800.utilities.LocationUpdateService
import com.ctyeung.runyasso800.viewModels.SharedPrefUtility
import junit.framework.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import junit.framework.Assert.assertEquals

@RunWith(AndroidJUnit4::class)
class SharedPrefUtilityTest {

    /*
     * Test integer types
     */

    @Test
    fun SprintLength() {
        val expected = 111
        SharedPrefUtility.set(SharedPrefUtility.keySprintLength, expected)
        val len = SharedPrefUtility.get(SharedPrefUtility.keySprintLength, 0)
        assertEquals(expected, len)
    }

    @Test
    fun JogLength() {
        val expected = 111
        SharedPrefUtility.set(SharedPrefUtility.keyJogLength, expected)
        val len = SharedPrefUtility.get(SharedPrefUtility.keyJogLength, 0)
        assertEquals(expected, len)
    }

    @Test
    fun NumIterations() {
        val expected = 6
        SharedPrefUtility.set(SharedPrefUtility.keyNumIterations, expected)
        val len = SharedPrefUtility.get(SharedPrefUtility.keyNumIterations, 0)
        assertEquals(expected, len)
    }

    @Test
    fun SplitIndex() {
        val expected = 3
        SharedPrefUtility.set(SharedPrefUtility.keySplitIndex, expected)
        val len = SharedPrefUtility.get(SharedPrefUtility.keySplitIndex, 0)
        assertEquals(expected, len)
    }

    @Test
    fun StepIndex() {
        val expected = 4
        SharedPrefUtility.set(SharedPrefUtility.keyStepIndex, expected)
        val len = SharedPrefUtility.get(SharedPrefUtility.keyStepIndex, 0)
        assertEquals(expected, len)
    }

    /*
     * Test Long types
     * NOTE: Default value must be of type LONG
     */

    @Test
    fun GPSsampleRate() {
        val expected = LocationUpdateService.MAX_SAMPLE_RATE
        SharedPrefUtility.set(SharedPrefUtility.keyGPSsampleRate, expected)
        val len = SharedPrefUtility.get(SharedPrefUtility.keyGPSsampleRate, LocationUpdateService.DEFAULT_SAMPLE_RATE)
        assertEquals(expected, len)
    }

    @Test
    fun SprintGoal() {
        val expected = 12345L
        SharedPrefUtility.set(SharedPrefUtility.keySprintGoal, expected)
        val len = SharedPrefUtility.get(SharedPrefUtility.keySprintGoal, 0L)
        assertEquals(expected, len)
    }

    @Test
    fun RaceGoal() {
        val expected = 12345L
        SharedPrefUtility.set(SharedPrefUtility.keyRaceGoal, expected)
        val len = SharedPrefUtility.get(SharedPrefUtility.keyRaceGoal, 0L)
        assertEquals(expected, len)
    }

    @Test
    fun Name() {
        val expected = "Run Test"
        SharedPrefUtility.set(SharedPrefUtility.keyName, expected)
        val len = SharedPrefUtility.get(SharedPrefUtility.keyName, "")
        assertEquals(expected, len)
    }

    @Test
    fun LastLatitude() {
        val expected = "44.9178902"
        SharedPrefUtility.set(SharedPrefUtility.keyLastLatitutde, expected)
        val len = SharedPrefUtility.get(SharedPrefUtility.keyLastLatitutde, "")
        assertEquals(expected, len)
    }

    @Test
    fun LastLongitude() {
        val expected = "-93.3162731"
        SharedPrefUtility.set(SharedPrefUtility.keyLastLongitude, expected)
        val len = SharedPrefUtility.get(SharedPrefUtility.keyLastLongitude, "")
        assertEquals(expected, len)
    }

    @Test
    fun SplitDistance() {
        val expected = 800.5757446289062f
        SharedPrefUtility.set(SharedPrefUtility.keySplitDistance, expected)
        val len = SharedPrefUtility.get(SharedPrefUtility.keySplitDistance, 0f)
        assertEquals(expected, len)
    }

    @Test
    fun RunState() {
        val expected = StateJog::class.java
        SharedPrefUtility.set(SharedPrefUtility.keyRunState, expected)
        val len = SharedPrefUtility.get(SharedPrefUtility.keyRunState, StateError::class.java)
        assertEquals(expected, len)
    }
}