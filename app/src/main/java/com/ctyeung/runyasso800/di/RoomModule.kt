package com.ctyeung.runyasso800.di

import android.content.Context
import androidx.room.Room
import com.ctyeung.runyasso800.data.room.YassDatabase
import com.ctyeung.runyasso800.data.room.splits.SplitDao
import com.ctyeung.runyasso800.data.room.splits.SplitRepository
import com.ctyeung.runyasso800.data.room.steps.StepDao
import com.ctyeung.runyasso800.data.room.steps.StepRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    fun provideSplitRepository(splitDao: SplitDao): SplitRepository {
        return SplitRepository(splitDao = splitDao)
    }

    @Provides
    fun provideSplitDao(database: YassDatabase): SplitDao {
        return database.splitDao()
    }

    @Provides
    fun provideStepRepository(stepDao: StepDao): StepRepository {
        return StepRepository(stepDao = stepDao)
    }

    @Provides
    fun provideStepDao(database: YassDatabase): StepDao {
        return database.stepDao()
    }

    @Provides
    @Singleton
    fun providesMyPlaceDatabase(@ApplicationContext appContext: Context):YassDatabase {
        return Room.databaseBuilder(appContext, YassDatabase::class.java, "my_place_database").build()
    }
}