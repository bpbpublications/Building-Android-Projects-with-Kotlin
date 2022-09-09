package com.bpb.android.clips

import android.app.Application
import android.content.Context
import com.bpb.android.clips.repository.data.clips.ClipsDataSource
import com.bpb.android.clips.repository.data.clips.ClipsRepository
import com.bpb.android.clips.repository.data.clips.source.local.ClipsLocalDataSource
import com.bpb.android.clips.repository.database.ClipsDatabase
import com.google.android.exoplayer2.database.DatabaseProvider
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class BpbClipsApp : Application() {
    // Using by lazy so the database and the repository
    // are only created when they're needed.
    private val coroutineScope = CoroutineScope(SupervisorJob())
    val database by lazy { ClipsDatabase.getDatabase(this, coroutineScope) }
    private val datasource: ClipsDataSource by lazy {
        ClipsLocalDataSource(database.clipsDao())
    }
    val repository: ClipsRepository by lazy { ClipsRepository(datasource) }

    override fun onCreate() {
        super.onCreate()
        context = this

        ClipsDatabase.getDatabase(this, coroutineScope)

        val lruCacheEvictor = LeastRecentlyUsedCacheEvictor(90 * 1024 * 1024)
        val databaseProvider: DatabaseProvider = ExoDatabaseProvider(this)

        if (simpleCache == null) {
            simpleCache = SimpleCache(cacheDir, lruCacheEvictor, databaseProvider)
        }
    }

    companion object {
        var simpleCache: SimpleCache? = null
        var context: Context? = null
    }
}