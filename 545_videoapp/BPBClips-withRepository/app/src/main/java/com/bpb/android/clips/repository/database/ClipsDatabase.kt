package com.bpb.android.clips.repository.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.bpb.android.clips.repository.data.clips.model.Clips
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Clips::class], version = 1)
abstract class ClipsDatabase : RoomDatabase() {
    abstract fun clipsDao(): ClipsDao

    companion object {
        @Volatile
        private var INSTANCE: ClipsDatabase? = null

        fun getDatabase(
            context: Context, scope: CoroutineScope
        ): ClipsDatabase {
            // If INSTANCE is null, then create the database
            // else return INSTANCE
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ClipsDatabase::class.java,
                    "bpb_clips_db"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

    private class ClipsDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        // Override the onCreate method to populate the database.
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            // If you want to keep the data through app restarts,
            // comment out the following line.
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateDatabase(database.clipsDao())
                }
            }
        }

        suspend fun populateDatabase(dao: ClipsDao) {
            // Start the app with a fresh dataset
            dao.getAllClips()
        }
    }
}