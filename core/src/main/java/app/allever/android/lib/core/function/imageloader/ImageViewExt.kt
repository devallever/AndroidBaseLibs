package app.allever.android.lib.core.function.imageloader

import android.widget.ImageView
import java.io.File

fun ImageView.load(resource: Any) {
    when (resource) {
        is String -> {
            if (resource.endsWith("gif", true)) {
                loadGif(resource)
                return
            }
        }
        is File -> {
            if (resource.absolutePath.endsWith("gif", true)) {
                loadGif(resource)
                return
            }
        }
    }

    load(resource, 0, 0)
}

fun ImageView.load(resource: Any, errorIcon: Int = 0, placeHolder: Int = 0) {
    ImageLoader.load(resource, this, errorIcon, placeHolder)
}

fun ImageView.loadGif(resource: Any) {
    ImageLoader.loadGif(resource, this)
}
