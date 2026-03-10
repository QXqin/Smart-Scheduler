package com.smartscheduler.app.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface LlmApiService {
    @POST("chat/completions")
    suspend fun chatCompletion(
        @Header("Authorization") auth: String,
        @Body request: ChatRequest
    ): ChatResponse
}

@JsonClass(generateAdapter = true)
data class ChatRequest(
    val model: String,
    val messages: List<ChatMessage>,
    val temperature: Double = 0.3,
    @Json(name = "response_format") val responseFormat: ResponseFormat? = ResponseFormat("json_object")
)

@JsonClass(generateAdapter = true)
data class ChatMessage(
    val role: String,
    val content: String
)

@JsonClass(generateAdapter = true)
data class ResponseFormat(
    val type: String
)

@JsonClass(generateAdapter = true)
data class ChatResponse(
    val id: String? = null,
    val choices: List<Choice> = emptyList()
)

@JsonClass(generateAdapter = true)
data class Choice(
    val index: Int = 0,
    val message: ChatMessage? = null,
    @Json(name = "finish_reason") val finishReason: String? = null
)
