package com.featureflag.service

import com.featureflag.domain.FeatureFlag
import com.featureflag.domain.FeatureFlagRepository
import org.springframework.stereotype.Service

@Service
class FeatureFlagService(private val repository: FeatureFlagRepository) {

    fun getAllFlags(): List<FeatureFlag> = repository.findAll()

    fun getFlagByName(name: String): FeatureFlag = repository.findByName(normalizeName(name))
        ?: throw IllegalArgumentException("Feature flag '$name' was not found.")

    fun createFlag(name: String, enabled: Boolean = false, description: String? = null): FeatureFlag {
        val normalizedName = normalizeName(name)
        require(repository.findByName(normalizedName) == null) {
            "Feature flag '$normalizedName' already exists."
        }

        return repository.save(
            FeatureFlag(
                name = normalizedName,
                enabled = enabled,
                description = description
            )
        )
    }

    fun updateFlag(name: String, enabled: Boolean? = null, description: String? = null): FeatureFlag {
        val existing = getFlagByName(name)
        val updated = existing.withUpdates(enabled = enabled, description = description)
        return repository.save(updated)
    }

    fun enableFlag(name: String): FeatureFlag = updateFlag(name, enabled = true)

    fun disableFlag(name: String): FeatureFlag = updateFlag(name, enabled = false)

    fun deleteFlag(name: String) {
        val normalizedName = normalizeName(name)
        if (!repository.deleteByName(normalizedName)) {
            throw IllegalArgumentException("Feature flag '$normalizedName' was not found.")
        }
    }

    fun isEnabled(name: String): Boolean = getFlagByName(name).enabled

    private fun normalizeName(name: String): String = name.trim().ifBlank {
        throw IllegalArgumentException("Feature flag name cannot be blank.")
    }
}
