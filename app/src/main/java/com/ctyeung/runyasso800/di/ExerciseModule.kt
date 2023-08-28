package com.ctyeung.runyasso800.di

import android.content.Context
import com.ctyeung.runyasso800.MainViewModel
import com.ctyeung.runyasso800.data.ExeriseRepository
import com.ctyeung.runyasso800.data.preference.StoreRepository
import com.ctyeung.runyasso800.data.room.splits.SplitRepository
import com.ctyeung.runyasso800.data.room.steps.StepRepository
import com.ctyeung.runyasso800.viewmodels.ConfigViewModel
import com.ctyeung.runyasso800.viewmodels.ExerciseViewModel
import com.ctyeung.runyasso800.viewmodels.GoalViewModel
import com.ctyeung.runyasso800.viewmodels.RecapViewModel
import com.ctyeung.runyasso800.viewmodels.ShareViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ActivityComponent::class)
object ExerciseModule {

    @Provides
    fun provideMainViewModel(
        storeRepository: StoreRepository,
        stepRepository: StepRepository,
        splitRepository: SplitRepository
    ): MainViewModel {
        return MainViewModel(storeRepository, stepRepository, splitRepository)
    }

    @Provides
    fun provideConfigViewModel(storeRepository: StoreRepository): ConfigViewModel {
        return ConfigViewModel(storeRepository)
    }

    @Provides
    fun provideExerciseViewModel(
        storeRepository: StoreRepository,
        stepRepository: StepRepository,
        splitRepository: SplitRepository
    ): ExerciseViewModel {
        return ExerciseViewModel(storeRepository, stepRepository, splitRepository)
    }

    @Provides
    fun provideExeriseRepository(@ApplicationContext context: Context): ExeriseRepository {
        return ExeriseRepository(context)
    }

    @Provides
    fun provideGoalViewModel(
        storeRepository: StoreRepository,
    ): GoalViewModel {
        return GoalViewModel(storeRepository)
    }

    @Provides
    fun provideRecapViewModel(
        storeRepository: StoreRepository,
        stepRepository: StepRepository,
        splitRepository: SplitRepository
    ): RecapViewModel {
        return RecapViewModel(storeRepository, stepRepository, splitRepository)
    }

    @Provides
    fun provideShareViewModel(
        storeRepository: StoreRepository,
        stepRepository: StepRepository,
        splitRepository: SplitRepository
    ): ShareViewModel {
        return ShareViewModel(storeRepository, stepRepository, splitRepository)
    }
}