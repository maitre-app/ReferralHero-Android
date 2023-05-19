package com.sdk.rh

import android.net.Uri

interface RHLinkGenerate {
    fun onLinkGenerateFinished(link: Uri?)
}