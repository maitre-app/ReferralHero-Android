package com.sdk.rh.networking

class ApiResponse {
    private var status: String? = null
    var data: Subscriber? = null
        get() = if (field != null) field else null
    var message: String? = null
    var code: String? = null
    var callsLeft = 0
        get() = if (field != 0) field else 0
    var timestamp: Long = 0
        get() = if (field != 0L) field else 0

    fun getStatus(): String {
        return status ?: ""
    }

    fun setStatus(status: String?) {
        this.status = status
    }

    val isSuccess: Boolean
        get() = status.equals("ok", ignoreCase = true)
}