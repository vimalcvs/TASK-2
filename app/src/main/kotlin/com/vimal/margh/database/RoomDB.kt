package com.vimal.margh.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.vimal.margh.model.ModelCategory
import com.vimal.margh.model.ModelMeal

@Database(entities = [ModelMeal::class, ModelCategory::class], version = 1)
abstract class RoomDB : RoomDatabase() {

    abstract fun getDao(): RoomDao

    companion object {
        @Volatile
        private var instance: RoomDB? = null

        fun getDatabase(context: Context): RoomDB {
            return instance ?: synchronized(this) {
                val newInstance = Room.databaseBuilder(
                    context.applicationContext,
                    RoomDB::class.java,
                    "data.db"
                ).build()
                instance = newInstance
                newInstance
            }
        }
    }
}