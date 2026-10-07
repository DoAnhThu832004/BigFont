package com.thupo.bigfont.domain.repository

import com.thupo.bigfont.domain.model.FontScaleItem
import kotlinx.coroutines.flow.Flow

interface FontScaleRepository {
    fun getFontScales(): Flow<List<FontScaleItem>>
    fun getCurrentSystemScale(): Float
    fun hasWriteSettingsPermission(): Boolean
    suspend fun applyFontScale(scale: Float): Result<Unit>
}