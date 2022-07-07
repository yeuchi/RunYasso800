package com.ctyeung.runyasso800.di

import com.ctyeung.runyasso800.MainApplication
import com.ctyeung.runyasso800.data.room.YassoDatabase
import com.ctyeung.runyasso800.data.room.splits.SplitRepository
import com.ctyeung.runyasso800.data.room.steps.StepRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(ActivityComponent::class)
object YassoModule {

    @Provides
    fun provideSplitRepository():SplitRepository {
        var context = MainApplication.applicationContext()
        val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
        val splitDao = YassoDatabase.getDatabase(context, scope).splitDao()
        return SplitRepository(splitDao)
    }

    @Provides
    fun provideStepRepository(): StepRepository {
        var context = MainApplication.applicationContext()
        val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
        val stepDao = YassoDatabase.getDatabase(context, scope).stepDao()
        return StepRepository(stepDao)
    }
}