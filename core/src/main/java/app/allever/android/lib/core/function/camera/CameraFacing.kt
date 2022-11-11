package app.allever.android.lib.core.function.camera

import androidx.annotation.IntDef

class CameraFacing{

    @IntDef(FACE_BACK, FACE_FRONT)
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class STATE

    companion object {
        const val FACE_BACK = 0
        const val FACE_FRONT = 1;
    }
}
