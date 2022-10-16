package com.ctyeung.runyasso800.di

import android.app.Activity
import android.content.Context
import com.ctyeung.runyasso800.data.RunRepository
import com.ctyeung.runyasso800.viewmodels.RunViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ActivityComponent::class)
object RunModule {

    @Provides
    fun provideRunRepository(@ApplicationContext context: Context):RunRepository = RunRepository(context)

    @Provides
    fun provideRunViewModel(runRepository: RunRepository):RunViewModel {
        return RunViewModel(runRepository)
    }
}