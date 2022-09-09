package com.bpb.android.clips.di

import android.content.Context
import com.bpb.android.clips.repository.data.clips.fake.FakeData
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@InstallIn(ActivityRetainedComponent::class)
@Module
object FakeApiModule {
    @Provides
    fun providesFakeDependency(@ApplicationContext context: Context): FakeData {
        return FakeData(context)
    }
}