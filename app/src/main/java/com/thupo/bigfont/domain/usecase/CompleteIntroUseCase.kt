package com.thupo.bigfont.domain.usecase

import com.thupo.bigfont.domain.repository.UserPreferencesRepository
import javax.inject.Inject

class CompleteIntroUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    operator fun invoke() {
        userPreferencesRepository.setIntroDone(true)
    }
}