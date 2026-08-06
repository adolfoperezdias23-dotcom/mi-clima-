package com.miclima.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [CiudadEntity::class, ClimaCacheEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun ciudadDao(): CiudadDao
    abstract fun climaCacheDao(): ClimaCacheDao
}
