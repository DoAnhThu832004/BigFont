package com.thupo.bigfont.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.thupo.bigfont.data.local.entity.CustomFontEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomFontDao {
    @Query("SELECT * FROM custom_fonts ORDER BY id DESC")
    fun getAllCustomFonts(): Flow<List<CustomFontEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomFont(font: CustomFontEntity): Long

    @Delete
    suspend fun deleteCustomFont(font: CustomFontEntity): Int

    @Query("DELETE FROM custom_fonts WHERE id = :id")
    suspend fun deleteCustomFontById(id: Int): Int
}
