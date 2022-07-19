package com.ctyeung.runyasso800.di

import android.content.Context
import androidx.room.Room
import com.ctyeung.runyasso800.MainApplication
import com.ctyeung.runyasso800.data.room.YassoDatabase
import com.ctyeung.runyasso800.data.room.splits.SplitDao
import com.ctyeung.runyasso800.data.room.splits.SplitRepository
import com.ctyeung.runyasso800.data.room.steps.StepDao
import com.ctyeung.runyasso800.data.room.steps.StepRepository
import com.ctyeung.runyasso800.features.run.RunRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object YassoModule {

    @Provides
    @Singleton
    fun provideYassoDatabase(@ApplicationContext appContext: Context): YassoDatabase {
        return Room.databaseBuilder(
            appContext,
            YassoDatabase::class.java,
            "yasso_database"
        ).build()
    }

    @Provides
    fun provideSplitDao(db:YassoDatabase):SplitDao {
        return db.splitDao()
    }

    @Provides
    fun provideStepDao(db:YassoDatabase):StepDao {
        return db.stepDao()
    }

    @Provides
    fun provideSplitRepository(splitDao:SplitDao):SplitRepository {
//        var context = MainApplication.applicationContext()
//        val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
//        val splitDao = YassoDatabase.getDatabase(context, scope).splitDao()
        return SplitRepository(splitDao)
    }

    @Provides
    fun provideStepRepository(stepDao: StepDao): StepRepository {
//        var context = MainApplication.applicationContext()
//        val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
//        val stepDao = YassoDatabase.getDatabase(context, scope).stepDao()
        return StepRepository(stepDao)
    }

    @Provides
    fun provideRunRepository(splitRepository: SplitRepository,
    stepRepository: StepRepository): RunRepository {
//        var context = MainApplication.applicationContext()
//        val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
//        val split = provideSplitRepository()
//        val step = provideStepRepository()
        return RunRepository(splitRepository, stepRepository)
    }
}