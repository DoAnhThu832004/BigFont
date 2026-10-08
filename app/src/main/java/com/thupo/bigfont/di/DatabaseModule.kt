package com.thupo.bigfont.di

import android.content.Context
import androidx.room.Room
import com.thupo.bigfont.data.local.AppDatabase
import com.thupo.bigfont.data.local.dao.CustomFontDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "bigfont_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideCustomFontDao(database: AppDatabase): CustomFontDao {
        return database.customFontDao()
    }
}
