package com.ctyeung.runyasso800.room.steps

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.ctyeung.runyasso800.MainApplication
import com.ctyeung.runyasso800.room.YassoDatabase
import com.ctyeung.runyasso800.room.observeOnce
import com.ctyeung.runyasso800.room.splits.Split
import com.ctyeung.runyasso800.room.splits.SplitDao
import junit.framework.Assert
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.Exception

/*
 * Reference:
 * https://alediaferia.com/2018/12/17/testing-livedata-room-android/
 */
@RunWith(AndroidJUnit4ClassRunner::class)
open class StepDaoTests {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var splitDao: SplitDao
    private lateinit var stepDao:StepDao
    private lateinit var db: YassoDatabase

    @Before
    fun createDb() {
        //val context = ApplicationProvider.getApplicationContext<Context>()
        val context = MainApplication.applicationContext()
        db = Room.inMemoryDatabaseBuilder(context, YassoDatabase::class.java).build()
        splitDao = db.splitDao()
        stepDao = db.stepDao()
        CoroutineScope(Dispatchers.IO).launch {db.populate(splitDao, stepDao)}
    }

    @After
    //@Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    /*
     * Test Split insert() / getAll()
     */
    @Test
    //@Throws(Exception::class)
    fun getAll() {
        stepDao.getAll().observeOnce {
            if(it.size>0)
                onHandleInsertedSteps(it)
        }
    }

    fun onHandleInsertedSteps(steps:List<Step>) {

        if(null==steps)
            throw Exception("steps null")

        if(0==steps.size)
            throw Exception("step size == 0")

        val step = steps[0]

        Assert.assertEquals(step.splitIndex, 0)
        Assert.assertEquals(step.stepIndex, 0)
        Assert.assertEquals(step.dis, 10.0)
        Assert.assertEquals(step.run_type, Split.RUN_TYPE_SPRINT)
        Assert.assertEquals(step.latitude, 1.0)
        Assert.assertEquals(step.longitude, 1.1)
    }
}