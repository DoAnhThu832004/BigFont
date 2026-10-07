package com.thupo.bigfont.domain.usecase

import com.thupo.bigfont.domain.repository.FontScaleRepository
import javax.inject.Inject

class ApplyFontScaleUseCase @Inject constructor(
    private val fontScaleRepository: FontScaleRepository
) {
    suspend operator fun invoke(scale: Float): Result<Unit> {
        return fontScaleRepository.applyFontScale(scale)
    }
}