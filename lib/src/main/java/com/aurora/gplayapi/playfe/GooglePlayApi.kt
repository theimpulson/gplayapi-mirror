package com.aurora.gplayapi.playfe

import com.aurora.gplayapi.data.models.AuthData

class GooglePlayApi(private val authData: AuthData) {

    companion object {
        private const val URL_BASE = "https://play-fe.googleapis.com"
        const val URL_FDFE = "$URL_BASE/fdfe"

        const val URL_ITEMS = "$URL_BASE/getItems"
        const val URL_MODULE = "$URL_BASE/moduleDelivery"

        const val URL_PING = "$URL_BASE/ping"

        const val URL_DETAILS = "$URL_BASE/details"
        const val URL_DETAILS_DEVELOPER = "$URL_BASE/browseDeveloperPage"
        const val URL_DETAILS_STREAM = "$URL_BASE/getDetailsStream"
        const val URL_DETAILS_STREAM_POST_ACQUIRE = "$URL_BASE/getPostAcquireDetailsStream"

        const val URL_REVIEW = "$URL_BASE/rev"
        const val URL_REVIEW_ALL = "$URL_BASE/allReviews"
        const val URL_REVIEW_SUMMARY = "$URL_BASE/reviewSummary"
        const val URL_REVIEW_QUESTIONS = "$URL_BASE/reviewQuestions"
    }
}
