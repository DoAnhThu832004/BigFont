package com.thupo.bigfont.domain.model

data class FontScaleItem(
    val id: Int,
    val title: String,
    val scale: Float,
    val isCurrent: Boolean = false,
    val isCustom: Boolean = false
)