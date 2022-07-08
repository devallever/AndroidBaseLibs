package app.allever.android.lib.core.function.media

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable

class MediaBean() : Parcelable {
    var path = ""
        set(value) {
            var fileName = value
            val array = value.split("/")
            if (array.isNotEmpty()){
                fileName = array[array.size - 1]
            }
            this.name = fileName
            field = value
        }
    var date: Long = 0
    var degree: Int = 0
    var uri: Uri? = null
    var type: Int = MediaType.TYPE_OTHER_IMAGE
    var duration: Long = 0
    var name: String = ""

    constructor(parcel: Parcel) : this() {
        path = parcel.readString().toString()
        date = parcel.readLong()
        degree = parcel.readInt()
        uri = parcel.readParcelable(Uri::class.java.classLoader)
        type = parcel.readInt()
        duration = parcel.readLong()
        name = parcel.readString().toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(path)
        parcel.writeLong(date)
        parcel.writeInt(degree)
        parcel.writeParcelable(uri, flags)
        parcel.writeInt(type)
        parcel.writeLong(duration)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MediaBean> {
        override fun createFromParcel(parcel: Parcel): MediaBean {
            return MediaBean(parcel)
        }

        override fun newArray(size: Int): Array<MediaBean?> {
            return arrayOfNulls(size)
        }
    }
}