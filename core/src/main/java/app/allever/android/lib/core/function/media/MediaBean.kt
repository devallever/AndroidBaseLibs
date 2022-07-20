package app.allever.android.lib.core.function.media

import android.graphics.Bitmap
import android.net.Uri
import android.os.Parcel
import android.os.Parcelable

class MediaBean() : Parcelable {
    var path = ""
        set(value) {
            var fileName = value
            val array = value.split("/")
            if (array.isNotEmpty()) {
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
    var width = 0
    var height = 0
    var videoThumbnail: Bitmap? = null

    /**
     * 文件名
     */
    var name: String = ""

    /**
     * 标题
     */
    var musicTitle = ""

    /***
     * 艺人
     */
    var musicArtist = ""

    /**
     * 专辑
     */
    var musicAlbum = ""

    var musicCoverUri: Uri? = null
    var musicCoverBitmap: Bitmap? = null

    constructor(parcel: Parcel) : this() {
        date = parcel.readLong()
        degree = parcel.readInt()
        uri = parcel.readParcelable(Uri::class.java.classLoader)
        type = parcel.readInt()
        duration = parcel.readLong()
        width = parcel.readInt()
        height = parcel.readInt()
        videoThumbnail = parcel.readParcelable(Bitmap::class.java.classLoader)
        name = parcel.readString() ?: ""
        musicTitle = parcel.readString() ?: ""
        musicArtist = parcel.readString() ?: ""
        musicAlbum = parcel.readString() ?: ""
        musicCoverUri = parcel.readParcelable(Uri::class.java.classLoader)
        musicCoverBitmap = parcel.readParcelable(Bitmap::class.java.classLoader)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(date)
        parcel.writeInt(degree)
        parcel.writeParcelable(uri, flags)
        parcel.writeInt(type)
        parcel.writeLong(duration)
        parcel.writeInt(width)
        parcel.writeInt(height)
        parcel.writeParcelable(videoThumbnail, flags)
        parcel.writeString(name)
        parcel.writeString(musicTitle)
        parcel.writeString(musicArtist)
        parcel.writeString(musicAlbum)
        parcel.writeParcelable(musicCoverUri, flags)
        parcel.writeParcelable(musicCoverBitmap, flags)
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