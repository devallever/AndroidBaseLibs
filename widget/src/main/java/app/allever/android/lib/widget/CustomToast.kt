package app.allever.android.lib.widget

import app.allever.android.lib.widget.R
import android.text.TextUtils
import android.view.LayoutInflater
import app.allever.android.lib.core.app.App
import android.widget.TextView
import android.widget.LinearLayout
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.Toast

/**
 * @author Administrator
 * CustomToast.builder()
 * .showImage(true)
 * .showText(true)
 * .onlyDebug(true) //默认全弹
 * .text("展示文本和图片就设置true，默认不展示")
 * .icon(R.drawable.tianliaologo)
 * .show();
 */
object CustomToast {
    fun builder(): Builder {
        return Builder()
    }

    class Builder {
        private var showText = true
        private var showImage = false
        private var msg = ""
        private var onlyDebug = false
        private var icon = 0
        private var layoutId = R.layout.custom_toast_layout
        private var duration = Toast.LENGTH_SHORT
        fun showText(show: Boolean): Builder {
            showText = show
            return this
        }

        fun showImage(show: Boolean): Builder {
            showImage = show
            return this
        }

        fun text(msg: String): Builder {
            this.msg = msg
            return this
        }

        fun icon(resId: Int): Builder {
            icon = resId
            return this
        }

        fun layout(layoutId: Int): Builder {
            this.layoutId = layoutId
            return this
        }

        fun onlyDebug(onlyDebug: Boolean): Builder {
            this.onlyDebug = onlyDebug
            return this
        }

        fun duration(duration: Int): Builder {
            this.duration = duration
            return this
        }

        fun show() {
            if (TextUtils.isEmpty(msg) && showText) {
                return
            }
            val toastLayout = LayoutInflater.from(App.context).inflate(layoutId, null)
            val textView = toastLayout.findViewById<View>(R.id.tvMsg) as TextView
            val imageView = toastLayout.findViewById<ImageView>(R.id.ivIcon)
            if (showText) {
                textView.visibility = View.VISIBLE
                textView.text = msg
            } else {
                textView.visibility = View.GONE
            }
            if (showImage) {
                imageView.visibility = View.VISIBLE
                imageView.setImageResource(icon)
            } else {
                imageView.visibility = View.GONE
            }
            if (showText && !showImage) {
                //只显示文本到时候讲上边距设为0， 确保居中显示
                val layoutParams = textView.layoutParams as LinearLayout.LayoutParams
                layoutParams.topMargin = 0
                textView.layoutParams = layoutParams
            }
            val toast = Toast(App.context)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.duration = duration
            toast.view = toastLayout
            if (onlyDebug) {
                if (BuildConfig.DEBUG) {
                    toast.show()
                }
            } else {
                toast.show()
            }
        }
    }
}