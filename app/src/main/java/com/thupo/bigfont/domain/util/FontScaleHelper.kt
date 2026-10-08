package com.thupo.bigfont.domain.util

import android.content.Context
import android.provider.Settings
import kotlin.math.roundToInt

object FontScaleHelper {

    private const val PREFS_NAME = "app_prefs"
    private const val KEY_SAVED_SCALE = "saved_font_scale"

    /**
     * Lấy cỡ chữ thực tế hiện tại:
     * 1. Đọc trực tiếp từ Settings.System.FONT_SCALE (nguồn chính xác nhất của hệ thống).
     * 2. Nếu chưa có hoặc xảy ra lỗi, đọc từ SharedPreferences cache.
     * 3. Cuối cùng fallback về context.resources.configuration.fontScale.
     */
    fun getCurrentFontScale(context: Context): Float {
        return try {
            val scale = Settings.System.getFloat(
                context.contentResolver,
                Settings.System.FONT_SCALE
            )
            // Đồng bộ lại vào SharedPreferences cache
            saveFontScale(context, scale)
            scale
        } catch (_: Exception) {
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            prefs.getFloat(KEY_SAVED_SCALE, context.resources.configuration.fontScale)
        }
    }

    /**
     * Lưu lại scale vào SharedPreferences cache
     */
    fun saveFontScale(context: Context, scale: Float) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putFloat(KEY_SAVED_SCALE, scale).commit()
    }

    /**
     * Làm tròn an toàn % hiển thị (ví dụ: 1.2f -> 120, 1.4f -> 140)
     */
    fun getPercent(scale: Float): Int {
        return (scale * 100).roundToInt()
    }
}
