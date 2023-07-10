package com.ctyeung.runyasso800.di

import com.ctyeung.runyasso800.MainViewModel
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

@Module
@InstallIn(ActivityComponent::class)
object RunModule {

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
    fun provideGoalViewModel(
        storeRepository: StoreRepository,
        stepRepository: StepRepository,
        splitRepository: SplitRepository
    ): GoalViewModel {
        return GoalViewModel(storeRepository, stepRepository, splitRepository)
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