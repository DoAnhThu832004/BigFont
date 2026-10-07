package com.thupo.bigfont.domain.usecase

import com.thupo.bigfont.domain.model.FontScaleItem
import com.thupo.bigfont.domain.repository.FontScaleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFontScalesUseCase @Inject constructor(
    private val fontScaleRepository: FontScaleRepository
) {
    operator fun invoke(): Flow<List<FontScaleItem>> = fontScaleRepository.getFontScales()
}