package org.integrational.greetings.gender.genderapi

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import feign.RequestInterceptor
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

/**
 * Represents an API client's view of a sub-set of [Gender API](https://gender-api.com/en/api-docs/v2)
 * following Feign conventions with JAX-RS annotations.
 * Only the sub-set of Gender API's functionality needed for implementing
 * [org.integrational.greetings.domain.gender.GenderService] is captured here.
 */
@FeignClient("genderAPI", url = "https://gender-api.com", configuration = [Config::class])
@RequestMapping("/v2", produces = [APPLICATION_JSON_VALUE])
interface GenderAPI {

    /**
     * Represents [query-by-full-name](https://gender-api.com/en/api-docs/v2/query-by-full-name).
     */
    @PostMapping("/gender", consumes = [APPLICATION_JSON_VALUE])
    fun queryByFullName(@RequestBody requ: QueryByFullName.Requ): QueryByFullName.Resp
}

/**
 * Helper to instantiate Spring beans to be used by the API client specified by [GenderAPI].
 */
@Configuration
private class Config {
    /**
     * Return a [RequestInterceptor] Spring bean that adds an Authorization HTTP request header with the bearer token
     * required for invoking Gender API.
     */
    @Bean
    fun authRequestInterceptor(@Value("\${genderAPI.token}") token: String) = RequestInterceptor { template ->
        template.header("Authorization", "Bearer $token")
    }
}

class QueryByFullName {
    @JsonIgnoreProperties(ignoreUnknown = true) // Jackson not JSON-B
    data class Requ(
        val full_name: String? = null,
        val country: String? = null,
        val locale: String? = null,
        val ip: String? = null,
        val id: String? = null
    )

    @JsonIgnoreProperties(ignoreUnknown = true) // Jackson not JSON-B
    data class Resp(
        val input: Map<String, Any>, // treat generically because underdocumented
        val details: Details?,
        val result_found: Boolean,
        val first_name: String?, // is returned but not documented
        val full_name: String?,
        val probability: Double?,
        val gender: String
    )

    @JsonIgnoreProperties(ignoreUnknown = true) // Jackson not JSON-B
    data class Details(
        val credits_used: Int?,
        val samples: Int?,
        val country: String?,
        val first_name_sanitized: String?, // is returned but not documented
        val full_name_sanitized: String?,
        val duration: String?
    )
}

//@Named
//@Singleton
//class GenderAPIDummyImpl : GenderAPI {
//    override fun queryByFullName(requ: QueryByFullName.Requ) = QueryByFullName.Resp(
//        input = mapOf(),
//        details = QueryByFullName.Details(
//            credits_used = null,
//            samples = null,
//            country = null,
//            first_name_sanitized = null,
//            full_name_sanitized = null,
//            duration = null
//        ),
//        result_found = true,
//        first_name = null,
//        full_name = null,
//        probability = 1.0,
//        gender = "female"
//    )
//}