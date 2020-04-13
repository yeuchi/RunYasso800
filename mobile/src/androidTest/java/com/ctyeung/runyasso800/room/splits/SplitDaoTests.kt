package com.ctyeung.runyasso800.room.splits

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ctyeung.runyasso800.MainApplication
import com.ctyeung.runyasso800.room.YassoDatabase
import junit.framework.Assert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
open class SplitDaoTests {

    @Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var splitDao:SplitDao
    private lateinit var db:YassoDatabase

    @Before
    fun createDb() {
        //val context = ApplicationProvider.getApplicationContext<Context>()
        val context = MainApplication.applicationContext()
        db = Room.inMemoryDatabaseBuilder(context, YassoDatabase::class.java).build()
        splitDao = db.splitDao()
    }

    @After
    //@Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    //@Throws(Exception::class)
    suspend fun writeAndRead() {
        val now = System.currentTimeMillis()
        val end = now + 100
        val split:Split = Split(0,
                                Split.RUN_TYPE_JOG,
                                10.0,
                                now,
                                0.0,
                                0.1,
                                end,
                                1.0,
                                1.1,
                                false)

        splitDao.insert(split)
        var result: LiveData<List<Split>> = splitDao.getAll()

        if(null==result)
            throw java.lang.Exception("result null")

        var splits:List<Split>? = result.value

        if(null==splits)
            throw java.lang.Exception("splits null")

        if(0==splits.size)
            throw java.lang.Exception("split query == 0")

        val s = splits[0]
        Assert.assertEquals(s.splitIndex, 0)
        Assert.assertEquals(s.dis, 10.0)
        Assert.assertEquals(s.startTime, now)
        Assert.assertEquals(s.startLat, 0.0)
        Assert.assertEquals(s.startLong, 0.1)
        Assert.assertEquals(s.endTime, end)
        Assert.assertEquals(s.endLat, 1.0)
        Assert.assertEquals(s.endLong, 1.1)
        Assert.assertEquals(s.meetGoal, false)
    }
}