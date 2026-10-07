package com.thupo.bigfont.di

import com.thupo.bigfont.data.repository.FontScaleRepositoryImpl
import com.thupo.bigfont.data.repository.UserPreferencesRepositoryImpl
import com.thupo.bigfont.domain.repository.FontScaleRepository
import com.thupo.bigfont.domain.repository.UserPreferencesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindUserPreferencesRepository(
        userPreferencesRepositoryImpl: UserPreferencesRepositoryImpl
    ): UserPreferencesRepository

    @Binds
    @Singleton
    abstract fun bindFontScaleRepository(
        fontScaleRepositoryImpl: FontScaleRepositoryImpl
    ): FontScaleRepository
}
