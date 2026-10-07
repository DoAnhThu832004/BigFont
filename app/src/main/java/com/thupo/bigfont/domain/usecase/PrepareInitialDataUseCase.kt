package com.thupo.bigfont.domain.usecase

import com.thupo.bigfont.domain.repository.UserPreferencesRepository
import javax.inject.Inject

class PrepareInitialDataUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke(): Result<Boolean> {
        return runCatching {
            !userPreferencesRepository.isIntroDone()
        }
    }
}