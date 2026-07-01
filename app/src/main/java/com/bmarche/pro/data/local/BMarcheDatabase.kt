package com.bmarche.pro.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [FavoriEntity::class, ChecklistEntity::class],
    version = 1,
    exportSchema = false
)
abstract class BMarcheDatabase : RoomDatabase() {
    abstract fun dao(): BMarcheDao

    companion object {
        @Volatile
        private var INSTANCE: BMarcheDatabase? = null

        fun get(context: Context): BMarcheDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    BMarcheDatabase::class.java,
                    "bmarche.db"
                ).build().also { INSTANCE = it }
            }
    }
}
