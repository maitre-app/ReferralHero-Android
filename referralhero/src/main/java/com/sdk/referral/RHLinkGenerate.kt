package com.sdk.referral

import android.net.Uri

interface RHLinkGenerate {
    fun onLinkGenerateFinished(link: Uri?)
}