package app.allever.android.lib.core.function.imageloader

import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.ImageView
import app.allever.android.lib.core.util.BitmapUtils
import java.io.File

/**
 * 默认到图片加载，只支持资源和文件，暂不支持网络图片
 */
class DefaultLoader : ILoader {

    override fun load(resource: Any, imageView: ImageView) {
        when (resource) {
            is String -> {
                if (resource.startsWith("http")) {
                    //url
                } else {
                    //文件路径
                    imageView.setImageBitmap(BitmapFactory.decodeFile(resource))
                }
            }

            is Int -> {
                //图片资源
                imageView.setImageResource(resource)
            }

            is File -> {
                imageView.setImageBitmap(BitmapFactory.decodeFile(resource.absolutePath))
            }
        }
    }

    override fun loadGif(resource: Any, imageView: ImageView) {
        load(resource, imageView)
    }

    override fun load(resource: Any, imageView: ImageView, errorResId: Int, placeholder: Int) {
        load(resource, imageView)
    }

}