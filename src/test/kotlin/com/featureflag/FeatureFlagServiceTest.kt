package com.featureflag

import com.featureflag.infrastructure.InMemoryFeatureFlagRepository
import com.featureflag.service.FeatureFlagService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class FeatureFlagServiceTest {

    private val service = FeatureFlagService(InMemoryFeatureFlagRepository())

    @Test
    fun `createFlag saves a flag with normalized name`() {
        val created = service.createFlag("  beta-rollout  ", true, "Enabled for beta users")

        assertEquals("beta-rollout", created.name)
        assertTrue(created.enabled)
        assertEquals("Enabled for beta users", created.description)
        assertEquals(created, service.getFlagByName("beta-rollout"))
    }

    @Test
    fun `createFlag throws when duplicate name exists`() {
        service.createFlag("checkout-v2", true)

        val exception = assertThrows(IllegalArgumentException::class.java) {
            service.createFlag("checkout-v2", false)
        }

        assertTrue(exception.message!!.contains("already exists"))
    }

    @Test
    fun `updateFlag modifies enabled and description`() {
        service.createFlag("new-dashboard", false)

        val updated = service.updateFlag("new-dashboard", true, "New dashboard enabled")

        assertTrue(updated.enabled)
        assertEquals("New dashboard enabled", updated.description)
        assertTrue(service.isEnabled("new-dashboard"))
    }

    @Test
    fun `deleteFlag removes a flag`() {
        service.createFlag("legacy-flag", false)

        service.deleteFlag("legacy-flag")

        assertThrows(IllegalArgumentException::class.java) {
            service.getFlagByName("legacy-flag")
        }
    }

    @Test
    fun `getFlagByName throws when flag does not exist`() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            service.getFlagByName("missing-flag")
        }

        assertTrue(exception.message!!.contains("was not found"))
    }

    @Test
    fun `isEnabled uses current stored flag value`() {
        service.createFlag("dark-mode", false)

        assertFalse(service.isEnabled("dark-mode"))

        service.updateFlag("dark-mode", true)

        assertTrue(service.isEnabled("dark-mode"))
    }
}
