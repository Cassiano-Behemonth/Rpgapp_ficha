package com.example.rpgapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.rpgapp.data.dao.*
import com.example.rpgapp.data.entity.*

@Database(
    entities = [FichaEntity::class, PericiaEntity::class, ItemEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun fichaDao(): FichaDao
    abstract fun periciaDao(): PericiaDao
    abstract fun itemDao(): ItemDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "rpg_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}