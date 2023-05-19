package com.sdk.rh.networking

class Subscriber {
    var id: String? = null
        get() = if (field != null) field else ""
    var name: String? = null
        get() = if (field != null) field else ""
    var email: String? = null
        get() = if (field != null) field else ""
    var phoneNumber: String? = null
        get() = if (field != null) field else ""
    var cryptoWalletAddress: String? = null
        get() = if (field != null) field else ""
    var cryptoWalletProvider: String? = null
        get() = if (field != null) field else ""
    var extraField: String? = null
        get() = if (field != null) field else ""
    var extraField2: String? = null
        get() = if (field != null) field else ""
    var optionField: String? = null
        get() = if (field != null) field else ""
    var conversionAmount: Int? = null
        get() = if (field != null) field else 0
    var code: String? = null
        get() = if (field != null) field else ""
    var position: Int? = null
        get() = if (field != null) field else 0
    var referred: Boolean? = null
        get() = if (field != null) field else false
    var referredBy: ReferredBy? = null
        get() = if (field != null) field else null
    var peopleReferred: Int? = null
        get() = if (field != null) field else 0
    var promoted: Boolean? = null
        get() = if (field != null) field else false
    var promotedAt: Any? = null
        get() = if (field != null) field else ""
    var verified: Boolean? = null
        get() = if (field != null) field else false
    var verifiedAt: Long? = null
        get() = if (field != null) field else 0
    var points: Int? = null
        get() = if (field != null) field else 0
    var riskLevel: Int? = null
        get() = if (field != null) field else 0
    var host: String? = null
        get() = if (field != null) field else ""
    private var referral_link: String? = null
    var source: Any? = null
        get() = if (field != null) field else ""
    var device: Any? = null
        get() = if (field != null) field else ""
    var createdAt: Long? = null
        get() = if (field != null) field else 0
    var lastUpdatedAt: Long? = null
        get() = if (field != null) field else 0
    var response: String? = null
        get() = if (field != null) field else ""
    var referrallink: String?
        get() = if (referral_link != null) referral_link else ""
        set(referrallink) {
            referral_link = referrallink
        }
}