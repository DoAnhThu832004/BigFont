package com.thupo.bigfont.domain.repository

import com.thupo.bigfont.domain.model.FontScaleItem
import kotlinx.coroutines.flow.Flow

interface FontScaleRepository {
    fun getFontScales(): Flow<List<FontScaleItem>>
    fun getCurrentSystemScale(): Float
    fun hasWriteSettingsPermission(): Boolean
    suspend fun applyFontScale(scale: Float): Result<Unit>
    suspend fun saveCustomFont(scale: Float, title: String): Result<Unit>
    suspend fun deleteCustomFont(id: Int): Result<Unit>
}