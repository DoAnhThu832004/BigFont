package com.thupo.bigfont.domain.usecase

import com.thupo.bigfont.domain.repository.FontScaleRepository
import javax.inject.Inject

class CheckWritePermissionUseCase @Inject constructor(
    private val fontScaleRepository: FontScaleRepository
) {
    operator fun invoke(): Boolean = fontScaleRepository.hasWriteSettingsPermission()
}