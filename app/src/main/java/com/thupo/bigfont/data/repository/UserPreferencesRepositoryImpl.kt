package com.thupo.bigfont.data.repository

import com.thupo.bigfont.data.pref.AppPreferences
import com.thupo.bigfont.domain.repository.UserPreferencesRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferencesRepositoryImpl @Inject constructor(
    private val appPreferences: AppPreferences
): UserPreferencesRepository {
    override fun isIntroDone(): Boolean = appPreferences.isIntroDone()
    override fun setIntroDone(done: Boolean) = appPreferences.setIntroDone(done)
}