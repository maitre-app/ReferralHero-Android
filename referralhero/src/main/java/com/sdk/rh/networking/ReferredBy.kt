package com.sdk.rh.networking

class ReferredBy {
    var id: String? = null
        get() = if (field != null) field else ""
    var name: String? = null
        get() = if (field != null) field else ""
    var email: String? = null
        get() = if (field != null) field else ""
    var code: String? = null
        get() = if (field != null) field else ""
    var peopleReferred: Int? = null
        get() = if (field != null) field else 0
    var points: Int? = null
        get() = if (field != null) field else 0
}