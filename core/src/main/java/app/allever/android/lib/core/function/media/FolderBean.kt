package app.allever.android.lib.core.function.media

class FolderBean {
    /**
     * 图片的文件夹路径(包含名称)
     */
    var dir: String = ""

    /**
     * 文件夹的名称
     */
    var name: String = ""

    /**
     * 文件夹的名称
     */
    var bucketId: String = ""

    /**
     * 所有ThumbnailBean
     */
    var totalList: MutableList<MediaBean> = mutableListOf()

    /**
     * Photo ThumbnailBean
     */
    var imageMediaList: MutableList<MediaBean> = mutableListOf()

    /**
     * Video ThumbnailBean
     */
    var videoMediaList: MutableList<MediaBean> = mutableListOf()

    /**
     * 图片的数量
     */
    var total = 0

    var photoCount = 0

    var videoCount = 0

    /**
     * 第一张图片的ThumbnailBean
     */
    var coverMediaBean: MediaBean? = null

    fun setDirAndName(dir: String) {
        this.dir = dir
        val lastIndexOf = this.dir.lastIndexOf("/")
        name = this.dir.substring(lastIndexOf)
    }
}