package com.shakuro.skylocker.model.skyeng.models.skyeng

data class SkyEngMeaning(
        val id: Long,
        val text: String?,
        val wordId: Long?,
        val translation: SkyEngTranslation?,
        val definition: SkyEngTranslation?,
        val alternativeTranslations: MutableList<SkyEngAlternativeTranslation>?)