package com.featureflag

import com.featureflag.domain.FeatureFlagRepository
import com.featureflag.infrastructure.InMemoryFeatureFlagRepository
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class FeatureFlagApplication {

    @Bean
    fun featureFlagRepository(): FeatureFlagRepository = InMemoryFeatureFlagRepository()
}

fun main(args: Array<String>) {
    runApplication<FeatureFlagApplication>(*args)
}
