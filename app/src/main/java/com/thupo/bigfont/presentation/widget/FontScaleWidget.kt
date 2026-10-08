package com.thupo.bigfont.presentation.widget

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.thupo.bigfont.MainActivity
import com.thupo.bigfont.domain.util.FontScaleHelper

class FontScaleWidget : GlanceAppWidget() {

    override val stateDefinition: GlanceStateDefinition<*> = PreferencesGlanceStateDefinition

    companion object {
        val SCALES = listOf(1.0f, 1.2f, 1.4f, 1.6f, 1.8f, 2.0f, 2.2f, 2.4f)
        val ACTION_KEY_DIRECTION = ActionParameters.Key<String>("direction")
        val PREF_KEY_SCALE = floatPreferencesKey("current_font_scale")

        /**
         * Cập nhật State cho tất cả widget instance đang có trên màn hình chính
         */
        suspend fun updateAllWidgets(context: Context, newScale: Float? = null) {
            val scale = newScale ?: FontScaleHelper.getCurrentFontScale(context)
            try {
                val manager = GlanceAppWidgetManager(context)
                val glanceIds = manager.getGlanceIds(FontScaleWidget::class.java)
                for (glanceId in glanceIds) {
                    updateAppWidgetState(context, PreferencesGlanceStateDefinition, glanceId) { prefs ->
                        prefs.toMutablePreferences().apply {
                            this[PREF_KEY_SCALE] = scale
                        }
                    }
                    FontScaleWidget().update(context, glanceId)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            // Lắng nghe state của Glance - khi state thay đổi, Glance TỰ ĐỘNG RECOMPOSE giao diện
            val prefs = currentState<Preferences>()
            val currentScale = prefs[PREF_KEY_SCALE] ?: FontScaleHelper.getCurrentFontScale(context)
            val currentPercent = FontScaleHelper.getPercent(currentScale)

            Row(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .background(Color(0xFF1E222B))
                    .cornerRadius(16.dp)
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Nút Giảm cỡ chữ (-)
                Box(
                    modifier = GlanceModifier
                        .size(44.dp)
                        .background(Color(0xFF2C323D))
                        .cornerRadius(12.dp)
                        .clickable(
                            actionRunCallback<ChangeFontScaleCallback>(
                                actionParametersOf(ACTION_KEY_DIRECTION to "DECREASE")
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "–",
                        style = TextStyle(
                            color = ColorProvider(Color.White),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Spacer(modifier = GlanceModifier.width(8.dp))

                // Khu vực hiển thị % cỡ chữ hiện tại (Bấm vào để mở app)
                Column(
                    modifier = GlanceModifier
                        .defaultWeight()
                        .clickable(actionStartActivity<MainActivity>()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$currentPercent%",
                        style = TextStyle(
                            color = ColorProvider(Color(0xFF4C8DFF)),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = "Big Font",
                        style = TextStyle(
                            color = ColorProvider(Color(0xFF9AA0A6)),
                            fontSize = 11.sp
                        )
                    )
                }

                Spacer(modifier = GlanceModifier.width(8.dp))

                // Nút Tăng cỡ chữ (+)
                Box(
                    modifier = GlanceModifier
                        .size(44.dp)
                        .background(Color(0xFF2C323D))
                        .cornerRadius(12.dp)
                        .clickable(
                            actionRunCallback<ChangeFontScaleCallback>(
                                actionParametersOf(ACTION_KEY_DIRECTION to "INCREASE")
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "+",
                        style = TextStyle(
                            color = ColorProvider(Color.White),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Spacer(modifier = GlanceModifier.width(8.dp))

                // Nút Reset về 100% (1.0x)
                Box(
                    modifier = GlanceModifier
                        .size(44.dp)
                        .background(Color(0xFF1A73E8))
                        .cornerRadius(12.dp)
                        .clickable(
                            actionRunCallback<ChangeFontScaleCallback>(
                                actionParametersOf(ACTION_KEY_DIRECTION to "RESET")
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "1.0x",
                        style = TextStyle(
                            color = ColorProvider(Color.White),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}

class ChangeFontScaleCallback : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val direction = parameters[FontScaleWidget.ACTION_KEY_DIRECTION] ?: return

        // 1. Kiểm tra quyền WRITE_SETTINGS
        val hasPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.System.canWrite(context)
        } else {
            true
        }

        if (!hasPermission) {
            val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS).apply {
                data = Uri.parse("package:${context.packageName}")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)

            Handler(Looper.getMainLooper()).post {
                Toast.makeText(
                    context,
                    "Vui lòng cấp quyền sửa cài đặt hệ thống để đổi cỡ chữ",
                    Toast.LENGTH_LONG
                ).show()
            }
            return
        }

        // 2. Lấy cỡ chữ hiện tại và tính toán mục tiêu
        val current = FontScaleHelper.getCurrentFontScale(context)
        val scales = FontScaleWidget.SCALES

        val targetScale = when (direction) {
            "RESET" -> 1.0f
            "INCREASE" -> {
                scales.firstOrNull { it > current + 0.05f } ?: scales.last()
            }
            "DECREASE" -> {
                scales.lastOrNull { it < current - 0.05f } ?: scales.first()
            }
            else -> current
        }

        // 3. Ghi cấu hình mới vào hệ thống & cache
        try {
            Settings.System.putFloat(
                context.contentResolver,
                Settings.System.FONT_SCALE,
                targetScale
            )
            FontScaleHelper.saveFontScale(context, targetScale)

            // Cập nhật State cho Glance Widget (kích hoạt recomposition)
            FontScaleWidget.updateAllWidgets(context, targetScale)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
