package com.bpb.android.clips.di

import com.bpb.android.clips.repository.data.clips.fake.FakeClipDataSource
import com.bpb.android.clips.repository.data.clips.fake.FakeData
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@InstallIn(ActivityRetainedComponent::class)
@Module
object MockRepositoryModule {

    @Provides
    fun providesDataRepository(fakeData: FakeData): FakeClipDataSource {
        return FakeClipDataSource(fakeData)
    }
}