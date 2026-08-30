package com.featureflag.infrastructure

import com.featureflag.domain.FeatureFlag
import com.featureflag.domain.FeatureFlagRepository
import java.util.concurrent.ConcurrentHashMap

class InMemoryFeatureFlagRepository : FeatureFlagRepository {

    private val flags = ConcurrentHashMap<String, FeatureFlag>()

    override fun save(featureFlag: FeatureFlag): FeatureFlag {
        flags[featureFlag.name] = featureFlag
        return featureFlag
    }

    override fun findByName(name: String): FeatureFlag? = flags[name]

    override fun findAll(): List<FeatureFlag> = flags.values.sortedBy { it.name }

    override fun deleteByName(name: String): Boolean = flags.remove(name) != null

}
