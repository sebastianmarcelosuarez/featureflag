package com.featureflag.web

import com.featureflag.service.FeatureFlagService
import com.featureflag.web.dto.CreateFlagRequest
import com.featureflag.web.dto.FlagResponse
import com.featureflag.web.dto.ModifyFlagRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/flags")
class FeatureFlagController(private val service: FeatureFlagService) {

    @GetMapping("/hello")
    fun helloWorld(): String =
       "hello feature flag!"

    @GetMapping
    fun listFlags(): List<FlagResponse> {
        logListFlagsRequest()
        emitListFlagsTelemetry()

        val flags = service.getAllFlags().map(FlagResponse::from)

        logListFlagsResponse(flags)
        emitListFlagsResponseTelemetry(flags)
        return flags
    }

    @GetMapping("/{name}")
    fun getFlagByName(@PathVariable name: String): FlagResponse {
        logGetFlagByNameRequest(name)
        emitGetFlagByNameTelemetry(name)

        val flag = FlagResponse.from(service.getFlagByName(name))

        logGetFlagByNameResponse(flag)
        emitGetFlagByNameResponseTelemetry(flag)
        return flag
    }

    @PostMapping
    fun createFlag(@RequestBody request: CreateFlagRequest): ResponseEntity<FlagResponse> {
        logCreateFlagRequest(request)
        emitCreateFlagTelemetry(request)

        val created = service.createFlag(request.name, request.enabled, request.description)
        val response = ResponseEntity.status(HttpStatus.CREATED).body(FlagResponse.from(created))

        logCreateFlagResponse(response.body)
        emitCreateFlagResponseTelemetry(response.body)
        return response
    }

    @PutMapping("/{name}")
    fun updateFlag(@PathVariable name: String, @RequestBody request: ModifyFlagRequest): FlagResponse {
        logUpdateFlagRequest(name, request)
        emitUpdateFlagTelemetry(name, request)

        val updated = service.updateFlag(
            name = name,
            enabled = request.enabled,
            description = request.description
        )
        val response = FlagResponse.from(updated)

        logUpdateFlagResponse(response)
        emitUpdateFlagResponseTelemetry(response)
        return response
    }

    @PatchMapping("/{name}/enable")
    fun enableFlag(@PathVariable name: String): FlagResponse {
        logEnableFlagRequest(name)
        emitEnableFlagTelemetry(name)

        val updated = service.enableFlag(name)
        val response = FlagResponse.from(updated)

        logEnableFlagResponse(response)
        emitEnableFlagResponseTelemetry(response)
        return response
    }

    @PatchMapping("/{name}/disable")
    fun disableFlag(@PathVariable name: String): FlagResponse {
        logDisableFlagRequest(name)
        emitDisableFlagTelemetry(name)

        val updated = service.disableFlag(name)
        val response = FlagResponse.from(updated)

        logDisableFlagResponse(response)
        emitDisableFlagResponseTelemetry(response)
        return response
    }

    @DeleteMapping("/{name}")
    fun deleteFlag(@PathVariable name: String): ResponseEntity<Void> {
        logDeleteFlagRequest(name)
        emitDeleteFlagTelemetry(name)

        service.deleteFlag(name)
        val response: ResponseEntity<Void> = ResponseEntity.noContent().build()

        logDeleteFlagResponse(name)
        emitDeleteFlagResponseTelemetry(name)
        return response
    }

    private fun logListFlagsRequest() {}
    private fun logListFlagsResponse(flags: List<FlagResponse>) {}
    private fun emitListFlagsTelemetry() {}
    private fun emitListFlagsResponseTelemetry(flags: List<FlagResponse>) {}

    private fun logGetFlagByNameRequest(name: String) {}
    private fun logGetFlagByNameResponse(flag: FlagResponse) {}
    private fun emitGetFlagByNameTelemetry(name: String) {}
    private fun emitGetFlagByNameResponseTelemetry(flag: FlagResponse) {}

    private fun logCreateFlagRequest(request: CreateFlagRequest) {}
    private fun logCreateFlagResponse(flag: FlagResponse?) {}
    private fun emitCreateFlagTelemetry(request: CreateFlagRequest) {}
    private fun emitCreateFlagResponseTelemetry(flag: FlagResponse?) {}

    private fun logUpdateFlagRequest(name: String, request: ModifyFlagRequest) {}
    private fun logUpdateFlagResponse(flag: FlagResponse) {}
    private fun emitUpdateFlagTelemetry(name: String, request: ModifyFlagRequest) {}
    private fun emitUpdateFlagResponseTelemetry(flag: FlagResponse) {}

    private fun logEnableFlagRequest(name: String) {}
    private fun logEnableFlagResponse(flag: FlagResponse) {}
    private fun emitEnableFlagTelemetry(name: String) {}
    private fun emitEnableFlagResponseTelemetry(flag: FlagResponse) {}

    private fun logDisableFlagRequest(name: String) {}
    private fun logDisableFlagResponse(flag: FlagResponse) {}
    private fun emitDisableFlagTelemetry(name: String) {}
    private fun emitDisableFlagResponseTelemetry(flag: FlagResponse) {}

    private fun logDeleteFlagRequest(name: String) {}
    private fun logDeleteFlagResponse(name: String) {}
    private fun emitDeleteFlagTelemetry(name: String) {}
    private fun emitDeleteFlagResponseTelemetry(name: String) {}
}
