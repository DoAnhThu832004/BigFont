package com.thupo.bigfont.domain.usecase

import com.thupo.bigfont.domain.repository.FontScaleRepository
import javax.inject.Inject

class SaveCustomFontUseCase @Inject constructor(
    private val repository: FontScaleRepository
) {
    suspend operator fun invoke(scale: Float, title: String): Result<Unit> {
        return repository.saveCustomFont(scale, title)
    }
}
