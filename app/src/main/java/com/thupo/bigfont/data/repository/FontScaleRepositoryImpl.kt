package com.thupo.bigfont.data.repository

import android.content.Context
import android.os.Build
import android.provider.Settings
import com.thupo.bigfont.data.local.dao.CustomFontDao
import com.thupo.bigfont.data.local.entity.CustomFontEntity
import com.thupo.bigfont.domain.model.FontScaleItem
import com.thupo.bigfont.domain.repository.FontScaleRepository
import com.thupo.bigfont.domain.util.FontScaleHelper
import com.thupo.bigfont.presentation.widget.FontScaleWidget
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FontScaleRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val customFontDao: CustomFontDao
) : FontScaleRepository {

    private val defaultScales = listOf(
        Pair("100% - Mặc định (1.0x)", 1.0f),
        Pair("120% - Cỡ vừa (1.2x)", 1.2f),
        Pair("140% - Cỡ lớn (1.4x)", 1.4f),
        Pair("160% - Cỡ rất lớn (1.6x)", 1.6f),
        Pair("180% - Cực đại 1 (1.8x)", 1.8f),
        Pair("200% - Cực đại 2 (2.0x)", 2.0f),
        Pair("220% - Siêu to (2.2x)", 2.2f),
        Pair("240% - Khổng lồ (2.4x)", 2.4f)
    )

    override fun getFontScales(): Flow<List<FontScaleItem>> {
        return customFontDao.getAllCustomFonts().map { customEntities ->
            val currentScale = getCurrentSystemScale()
            val defaultItems = defaultScales.mapIndexed { index, pair ->
                FontScaleItem(
                    id = index + 1,
                    title = pair.first,
                    scale = pair.second,
                    isCurrent = Math.abs(currentScale - pair.second) < 0.05f,
                    isCustom = false
                )
            }
            val customItems = customEntities.map { entity ->
                FontScaleItem(
                    id = 1000 + entity.id,
                    title = entity.title,
                    scale = entity.scale,
                    isCurrent = Math.abs(currentScale - entity.scale) < 0.05f,
                    isCustom = true
                )
            }
            defaultItems + customItems
        }
    }

    override fun getCurrentSystemScale(): Float {
        return FontScaleHelper.getCurrentFontScale(context)
    }

    override fun hasWriteSettingsPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.System.canWrite(context)
        } else {
            true
        }
    }

    override suspend fun applyFontScale(scale: Float): Result<Unit> = runCatching {
        if (!hasWriteSettingsPermission()) {
            throw SecurityException("WRITE_SETTINGS permission is not granted")
        }
        Settings.System.putFloat(
            context.contentResolver,
            Settings.System.FONT_SCALE,
            scale
        )
        FontScaleHelper.saveFontScale(context, scale)
        try {
            FontScaleWidget.updateAllWidgets(context, scale)
        } catch (_: Exception) {
        }
    }

    override suspend fun saveCustomFont(scale: Float, title: String): Result<Unit> = runCatching {
        customFontDao.insertCustomFont(
            CustomFontEntity(
                title = title,
                scale = scale
            )
        )
    }

    override suspend fun deleteCustomFont(id: Int): Result<Unit> = runCatching {
        val dbId = if (id >= 1000) id - 1000 else id
        customFontDao.deleteCustomFontById(dbId)
    }
}