package com.featureflag.domain

import java.time.Instant

data class FeatureFlag(
    val name: String,
    val enabled: Boolean,
    val description: String? = null,
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = createdAt
) {
    init {
        require(name.isNotBlank()) { "Feature flag name cannot be blank" }
    }

    fun withUpdates(enabled: Boolean? = null, description: String? = null): FeatureFlag {
        return copy(
            enabled = enabled ?: this.enabled,
            description = if (description != null) description else this.description,
            updatedAt = Instant.now()
        )
    }
}
