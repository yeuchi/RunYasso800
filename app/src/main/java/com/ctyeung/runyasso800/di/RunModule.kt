package com.ctyeung.runyasso800.di

import android.content.Context
import com.ctyeung.runyasso800.RunViewModel
import com.ctyeung.runyasso800.data.preference.StoreRepository
import com.ctyeung.runyasso800.data.room.splits.SplitRepository
import com.ctyeung.runyasso800.data.room.steps.StepRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ActivityComponent::class)
object RunModule {

    @Provides
    fun provideRunViewModel(storeRepository: StoreRepository, stepRepository: StepRepository, splitRepository: SplitRepository):RunViewModel {
        return RunViewModel(storeRepository, stepRepository, splitRepository)
    }
}