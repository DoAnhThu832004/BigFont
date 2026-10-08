package com.thupo.bigfont.domain.usecase

import com.thupo.bigfont.domain.repository.FontScaleRepository
import javax.inject.Inject

class DeleteCustomFontUseCase @Inject constructor(
    private val repository: FontScaleRepository
) {
    suspend operator fun invoke(id: Int): Result<Unit> {
        return repository.deleteCustomFont(id)
    }
}
