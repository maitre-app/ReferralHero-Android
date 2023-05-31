package com.sdk.rh.networking

data class SubscriberData(
    val id: String?,
    val name: String?,
    val email: String?,
    val phone_number: String?,
    val crypto_wallet_address: String?,
    val crypto_wallet_provider: String?,
    val extra_field: String?,
    val extra_field_2: String?,
    val option_field: String?,
    val conversion_amount: Double?,
    val code: String?,
    val position: Int?,
    val referred: Boolean?,
    val referred_by: ReferredBy?,
    val people_referred: Int?,
    val promoted: Boolean?,
    val promoted_at: Long?,
    val verified: Boolean?,
    val verified_at: Long?,
    val points: Int?,
    val risk_level: Int?,
    val host: String?,
    val source: String?,
    val device: String?,
    val referral_link: String?,
    val universal_link: String?,
    val created_at: Long,
    val last_updated_at: Long?,
    val response: String?
)

data class ReferredBy(
    val id: String?,
    val name: String?,
    val email: String?,
    val phone_number: String?,
    val people_referred: Int?,
    val points: Int?,
)