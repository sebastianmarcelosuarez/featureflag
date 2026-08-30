package com.featureflag.web.dto

data class ModifyFlagRequest(
    val enabled: Boolean = false,
    val description: String? = null
)
