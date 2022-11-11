package app.allever.android.lib.core.function.camera

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView

@Deprecated("")
class CameraSurfaceView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : SurfaceView(context, attrs, defStyleAttr), SurfaceHolder.Callback {
    private var mSurfaceHolder: SurfaceHolder = holder
    private val mCanvas: Canvas? = null

    init {
        mSurfaceHolder.addCallback(this)
    }

    fun getSurfaceHolder(): SurfaceHolder? {
        return mSurfaceHolder
    }

    fun getCanvas(): Canvas? {
        return mCanvas
    }


    override fun surfaceCreated(holder: SurfaceHolder) {
        log("surfaceCreated")
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        log("surfaceChanged")
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        log("surfaceDestroyed")
    }

    private val TAG: String = CameraSurfaceView::class.java.simpleName

    private fun log(msg: String) {
        Log.d(TAG, msg)
    }
}