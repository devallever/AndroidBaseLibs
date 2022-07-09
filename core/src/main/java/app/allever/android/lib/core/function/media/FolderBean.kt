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
    var bucketId: String? = null

    /**
     * 所有资源列表
     */
    var totalList: MutableList<MediaBean> = mutableListOf()

    /**
     * 图片列表
     */
    var imageMediaList: MutableList<MediaBean> = mutableListOf()

    /**
     * 视频列表
     */
    var videoMediaList: MutableList<MediaBean> = mutableListOf()

    /**
     * 音频列表
     */
    var audioMediaList: MutableList<MediaBean> = mutableListOf()

    /**
     * 资源总数
     */
    var total = 0

    /**
     * 图片的数量
     */
    var photoCount = 0

    /**
     * 视频的数量
     */
    var videoCount = 0

    /**
     * 音频的数量
     */
    var audioCount = 0

    /**
     * 第一张图片/封面
     */
    var coverMediaBean: MediaBean? = null

    fun setDirAndName(dir: String) {
        this.dir = dir
        val lastIndexOf = this.dir.lastIndexOf("/")
        name = this.dir.substring(lastIndexOf)
    }
}