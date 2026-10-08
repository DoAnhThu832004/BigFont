package com.thupo.bigfont.data.repository

import android.content.Context
import android.os.Build
import com.thupo.bigfont.domain.model.FontScaleItem
import com.thupo.bigfont.domain.repository.FontScaleRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton
import com.thupo.bigfont.presentation.widget.FontScaleWidget
import com.thupo.bigfont.domain.util.FontScaleHelper
import androidx.glance.appwidget.updateAll
import android.provider.Settings

@Singleton
class FontScaleRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
): FontScaleRepository {
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
    override fun getFontScales(): Flow<List<FontScaleItem>> = flow {
        val currentScale = getCurrentSystemScale()
        val items = defaultScales.mapIndexed { index, pair ->
            FontScaleItem(
                id = index + 1,
                title = pair.first,
                scale = pair.second,
                isCurrent = Math.abs(currentScale - pair.second) < 0.05f
            )
        }
        emit(items)
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
}