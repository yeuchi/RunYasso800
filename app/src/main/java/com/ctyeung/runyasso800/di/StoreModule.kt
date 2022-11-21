package com.ctyeung.runyasso800.di

import android.content.Context
import com.ctyeung.runyasso800.data.preference.StoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ActivityComponent::class)
object StoreModule {
    @Provides
    fun provideStoreRepository(@ApplicationContext context: Context): StoreRepository =
        StoreRepository(context)
}