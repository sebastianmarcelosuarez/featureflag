package com.featureflag.web.dto

data class CreateFlagRequest(
    val name: String,
    val enabled: Boolean = false,
    val description: String? = null
)
