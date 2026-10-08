package com.thupo.bigfont.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.thupo.bigfont.data.local.dao.CustomFontDao
import com.thupo.bigfont.data.local.entity.CustomFontEntity

@Database(
    entities = [CustomFontEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun customFontDao(): CustomFontDao
}
