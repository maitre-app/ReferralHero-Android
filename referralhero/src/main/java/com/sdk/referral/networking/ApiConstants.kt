package com.sdk.referral.networking

interface ApiConstants {
    interface RequestParam {
        companion object {
            const val RH_API_TOKEN = "api_token"
            const val RH_UUID = "uuid"
            const val RH_SUBSCRIBERS = "subscribers"
            const val RH_CREATED_AT = "created_at"
            const val RH_EMAIL = "email"
            const val RH_DOMAIN = "domain"
            const val RH_SUBSCRIBER_ID = "subscriber_id"
            const val RH_TRANSACTION_ID = "transaction_id"
            const val RH_CONVERSION_CATREGORY = "conversion_category"
            const val RH_CONVERSION_VALUE = "conversion_value"
            const val RH_DEVICE = "device"
            const val RH_IP = "ip_address"
            const val RH_OS = "os_type"
            const val RH_SCREEN_SIZE = "screen_size"
            const val RH_SOURCE = "source"
            const val RH_DOUBLE_OPTION = "double_option"
            const val RH_POINTS = "points"
            const val RH_REFERRER = "referrer"
            const val RH_STATUS = "status"
            const val RH_EXTRA_FIELD_2 = "extra_field_2"
            const val RH_EXTRA_FIELD = "extra_field"
            const val RH_NAME = "name"
            const val RH_PHONE_NUMBER = "phone_number"
            const val RH_CRYPTO_WALLET_ADDRESS = "crypto_wallet_address"
            const val RH_PAGE = "page"
            const val RH_SORT_BY = "sort_by"
        }
    }

    enum class OperationType {
        DELETE, ADD, GET, REWARDS, TRACK, CAPTURE, MYREFERRAL, LEADERBOARD, UPDATE, VISITORREFERRAL
    }
}