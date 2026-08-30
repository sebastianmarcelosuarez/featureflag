package com.featureflag.web.dto

import com.featureflag.domain.FeatureFlag
import java.time.Instant

data class FlagResponse(
    val name: String,
    val enabled: Boolean,
    val description: String?,
    val createdAt: Instant,
    val updatedAt: Instant
) {
    companion object {
        fun from(featureFlag: FeatureFlag): FlagResponse = FlagResponse(
            name = featureFlag.name,
            enabled = featureFlag.enabled,
            description = featureFlag.description,
            createdAt = featureFlag.createdAt,
            updatedAt = featureFlag.updatedAt
        )
    }
}
