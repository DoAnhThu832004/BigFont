package com.thupo.bigfont.domain.repository

interface UserPreferencesRepository {
    fun isIntroDone(): Boolean
    fun setIntroDone(done: Boolean)
}