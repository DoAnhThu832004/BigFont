package com.thupo.bigfont

import android.app.Application
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import androidx.glance.appwidget.updateAll
import com.thupo.bigfont.presentation.widget.FontScaleWidget
import com.thupo.bigfont.domain.util.FontScaleHelper
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        registerFontScaleObserver()
    }

    private fun registerFontScaleObserver() {
        try {
            val uri = Settings.System.getUriFor(Settings.System.FONT_SCALE)
            val observer = object : ContentObserver(Handler(Looper.getMainLooper())) {
                override fun onChange(selfChange: Boolean, uri: Uri?) {
                    super.onChange(selfChange, uri)
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val newScale = FontScaleHelper.getCurrentFontScale(this@App)
                            FontScaleWidget.updateAllWidgets(this@App, newScale)
                        } catch (_: Exception) {
                        }
                    }
                }
            }
            contentResolver.registerContentObserver(uri, false, observer)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}