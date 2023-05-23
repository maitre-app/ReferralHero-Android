package com.sdk.rh.networking

data class ApiResponse(
    val status: String?,
    val message: String?,
    val code: String?,
    val data: SubscriberData?,
    val calls_left: Int?,
    val timestamp: Long?
)