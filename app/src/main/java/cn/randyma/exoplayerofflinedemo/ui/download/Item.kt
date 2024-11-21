package cn.randyma.exoplayerofflinedemo.ui.download

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Item(
    val url: String,
    val drmLicenseUrl: String = "",
    val token: String = "",
    val mimetype: String,
    var updateTimeMs: Long = 0L
) : Parcelable
