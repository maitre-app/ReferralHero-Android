package com.sdk.rh.networking

data class ListSubscriberData(
    val response: String, val subscribers: List<SubscriberData>, val pagination: Pagination
)

data class Pagination(
    val total_pages: Int, val current_page: Int, val per_page: Int, val total_objects: Int
)
