package com.ctyeung.runyasso800.dagger

import com.ctyeung.runyasso800.MainApplication
import com.ctyeung.runyasso800.room.YassoDatabase
import com.ctyeung.runyasso800.room.splits.SplitRepository
import com.ctyeung.runyasso800.room.steps.StepRepository
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Named

@Module
class SplitModule {

    @Provides
    @Named("split")
    fun splitRequest(): SplitRepository {
        val splitDao = YassoDatabase.getDatabase(
            MainApplication.applicationContext(), CoroutineScope(
            Dispatchers.IO)
        ).splitDao()

        return SplitRepository(splitDao)
    }
}

@Module
class StepModule {

    @Provides @Named("step")
    fun stepRequest():StepRepository {
        val stepDao = YassoDatabase.getDatabase(
            MainApplication.applicationContext(), CoroutineScope(
                Dispatchers.IO)
        ).stepDao()

        return StepRepository(stepDao)
    }
}