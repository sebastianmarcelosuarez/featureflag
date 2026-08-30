package com.featureflag.domain

interface FeatureFlagRepository {
    fun save(featureFlag: FeatureFlag): FeatureFlag
    fun findByName(name: String): FeatureFlag?
    fun findAll(): List<FeatureFlag>
    fun deleteByName(name: String): Boolean
}
