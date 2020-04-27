package com.ctyeung.runyasso800.room.splits

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.ctyeung.runyasso800.MainApplication
import com.ctyeung.runyasso800.room.OneTimeObserver
import com.ctyeung.runyasso800.room.YassoDatabase
import com.ctyeung.runyasso800.room.observeOnce
import com.ctyeung.runyasso800.room.steps.StepDao
import junit.framework.Assert
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/*
 * Reference:
 * https://alediaferia.com/2018/12/17/testing-livedata-room-android/
 */
@RunWith(AndroidJUnit4ClassRunner::class)
open class SplitDaoTests {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var splitDao:SplitDao
    private lateinit var stepDao:StepDao
    private lateinit var db:YassoDatabase

    @Before
    fun createDb() {
        //val context = ApplicationProvider.getApplicationContext<Context>()
        val context = MainApplication.applicationContext()
        db = Room.inMemoryDatabaseBuilder(context, YassoDatabase::class.java).build()
        splitDao = db.splitDao()
        stepDao = db.stepDao()
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
        CoroutineScope(Dispatchers.IO).launch {
            db.populate(splitDao, stepDao)
        }

        splitDao.getAll().observeOnce {
            if(it.size>0)
                onHandleInsertedSplits(it)
        }
    }

    fun onHandleInsertedSplits(splits:List<Split>) {
        val now = System.currentTimeMillis()
        val end = now + 100

        if(null==splits)
            throw java.lang.Exception("splits null")

        if(0==splits.size)
            throw java.lang.Exception("splits size == 0")

        val s = splits[0]
        Assert.assertEquals(s.splitIndex, 0)
        Assert.assertEquals(s.dis, 10.0)
        //Assert.assertEquals(s.startTime, now)
        Assert.assertEquals(s.startLat, 0.0)
        Assert.assertEquals(s.startLong, 0.1)
       // Assert.assertEquals(s.endTime, end)
        Assert.assertEquals(s.endLat, 1.0)
        Assert.assertEquals(s.endLong, 1.1)
        Assert.assertEquals(s.meetGoal, false)
    }

    @Test
    fun insert() {
        val now = System.currentTimeMillis()
        val end = now + 100
        val split = Split(0,
            Split.RUN_TYPE_JOG,
            10.0,
            now,
            0.0,
            0.1,
            end,
            1.0,
            1.1,
            false)

        CoroutineScope(Dispatchers.IO).launch {
            splitDao.insert(split)
        }

        splitDao.getAll().observeOnce {
            if(it.size>0)
                onHandleInsertedSplits(it)
        }
    }

    @Test
    fun update() {
        runUpdate()

        CoroutineScope(Dispatchers.IO).launch {
            val data = splitDao.getAll()
            val list = data.value
            Assert.assertEquals(list!![0].meetGoal, true)
        }
    }

    fun runUpdate() = runBlocking {
        val now = System.currentTimeMillis()
        val end = now + 100
        val split = Split(0,
            Split.RUN_TYPE_JOG,
            10.0,
            now,
            0.0,
            0.1,
            end,
            1.0,
            1.1,
            false)

        val job = CoroutineScope(Dispatchers.IO).launch {
            splitDao.insert(split)

            split.meetGoal = true
            splitDao.update(split)
        }
        job.join()
    }

    @Test
    fun deleteAll() {

        CoroutineScope(Dispatchers.IO).launch {
            db.populate(splitDao, stepDao)
            splitDao.deleteAll()

            val data = splitDao.getAll()
            val list = data.value
            Assert.assertEquals(list!!.size, 0)
        }
    }
}