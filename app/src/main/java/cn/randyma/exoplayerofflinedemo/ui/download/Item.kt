package cn.randyma.exoplayerofflinedemo.ui.download

import android.os.Parcel
import android.os.Parcelable

data class Item(
    val url: String,
    val drmLicenseUrl: String = "",
    val token: String = "",
    val mimetype: String,
    var updateTimeMs: Long = 0L
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readLong()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(url)
        parcel.writeString(drmLicenseUrl)
        parcel.writeString(token)
        parcel.writeString(mimetype)
        parcel.writeLong(updateTimeMs)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Item> {
        override fun createFromParcel(parcel: Parcel): Item {
            return Item(parcel)
        }

        override fun newArray(size: Int): Array<Item?> {
            return arrayOfNulls(size)
        }
    }
}
