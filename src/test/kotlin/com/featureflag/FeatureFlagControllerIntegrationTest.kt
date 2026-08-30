package com.featureflag

import com.featureflag.service.FeatureFlagService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class FeatureFlagControllerIntegrationTest {

    @Autowired
    private lateinit var service: FeatureFlagService

    @Test
    fun `should create and update a feature flag`() {
        val name = "beta-flag-${System.nanoTime()}"

        val created = service.createFlag(name, true, "For experimental rollout")
        assertEquals(name, created.name)
        assertTrue(created.enabled)

        val updated = service.updateFlag(name, false, "Updated rollout")
        assertFalse(updated.enabled)
        assertEquals("Updated rollout", updated.description)

        val loaded = service.getFlagByName(name)
        assertEquals(name, loaded.name)
        assertFalse(service.isEnabled(name))
    }
}
