package app.allever.android.lib.core.helper

import android.animation.Animator
import android.graphics.Color
import android.graphics.Rect
import android.os.Build
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import androidx.recyclerview.widget.RecyclerView
import app.allever.android.lib.core.util.StatusBarCompat

object ViewHelper {

    /**
     *
     */
    /**
     *
     */
    fun setVisible(view: View, show: Boolean) {
        if (show) {
            view.animate().setListener(object: Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {
                    view.visibility = View.VISIBLE
                }

                override fun onAnimationEnd(animation: Animator) {
                }

                override fun onAnimationCancel(animation: Animator) {
                }

                override fun onAnimationRepeat(animation: Animator) {
                }
            }).setDuration(300).setInterpolator(AccelerateInterpolator()).alpha(1f).start()

        } else {
            view.visibility = View.GONE
        }
    }

    /**
     *
     */
    fun adaptStatusBarView(statusBarView: View?) {
        adaptStatusBarView(statusBarView, Color.parseColor("#ffffff"))
    }

    fun adaptStatusBarView(statusBarView: View?, statusBarColor: Int) {
        statusBarView ?: return
        val layoutParams = statusBarView.layoutParams
        layoutParams.height = StatusBarCompat.getStatusBarHeight(statusBarView.context)
        statusBarView.layoutParams = layoutParams
        statusBarView.setBackgroundColor(statusBarColor)
    }

    fun setMarginTop(view: View, marginTop: Int) {
        view.post {
            val lp = view.layoutParams as ViewGroup.MarginLayoutParams
            lp.topMargin = marginTop
            view.layoutParams = lp
        }
    }

    fun setMarginBottom(view: View, marginBottom: Int) {
        view.post {
            val lp = view.layoutParams as ViewGroup.MarginLayoutParams
            lp.bottomMargin = marginBottom
            view.layoutParams = lp
        }
    }

    fun setMarginHorizontal(view: View, margin: Int) {
        view.post {
            val lp = view.layoutParams as ViewGroup.MarginLayoutParams
            lp.leftMargin = margin
            lp.rightMargin = margin
            view.layoutParams = lp
        }
    }

    fun setMarginVertical(view: View, margin: Int) {
        view.post {
            val lp = view.layoutParams as ViewGroup.MarginLayoutParams
            lp.topMargin = margin
            lp.bottomMargin = margin
            view.layoutParams = lp
        }
    }

    fun setMarginLeft(view: View, marginLeft: Int) {
        view.post {
            val lp = view.layoutParams as ViewGroup.MarginLayoutParams
            lp.leftMargin = marginLeft
            view.layoutParams = lp
        }
    }

    fun setMarginRight(view: View, marginRight: Int) {
        view.post {
            val lp = view.layoutParams as ViewGroup.MarginLayoutParams
            lp.rightMargin = marginRight
            view.layoutParams = lp
        }
    }


    fun setViewHeight(view: View, height: Int) {
        view.post {
            val lp = view.layoutParams as ViewGroup.LayoutParams
            view.layoutParams.height = height
            view.layoutParams = lp
        }
    }

    fun setViewWidth(view: View, value: Int) {
        view.post {
            val lp = view.layoutParams as ViewGroup.LayoutParams
            view.layoutParams.width = value
            view.layoutParams = lp
        }
    }

    fun setViewSize(view: View, sizePx: Int) {
        view.post {
            val lp = view.layoutParams as ViewGroup.LayoutParams
            view.layoutParams.width = sizePx
            view.layoutParams.height = sizePx
            view.layoutParams = lp
        }
    }

    fun setViewWidthHeight(view: View, width: Int,height: Int) {
        if (view.layoutParams == null) return
        val lp = view.layoutParams as ViewGroup.LayoutParams
        view.layoutParams.width = width
        view.layoutParams.height = height
        view.layoutParams = lp
    }

    fun setViewHorizontalMargin(view: View,leftMargin:Int,rightMargin:Int){
        if (view.layoutParams == null) return
        val lp = view.layoutParams as ViewGroup.MarginLayoutParams
        lp.leftMargin = leftMargin
        lp.rightMargin = rightMargin
        view.layoutParams = lp
    }

    /**
     * 获取屏幕中位置
     */
    @Deprecated("")
    fun getLocation(view: View): IntArray? {
        val location = IntArray(2)
        if (Build.VERSION.SDK_INT >= 24) {
            val rect = Rect()
            view.getGlobalVisibleRect(rect)
            location[0] = rect.left
            location[1] = rect.top
        } else {
            view.getLocationOnScreen(location)
        }
        return location
    }

    fun getGlobalVisibleRect(view: View?): Rect {
        val rect = Rect()
        view?.getGlobalVisibleRect(rect)
        return rect
    }

    fun isInViewArea(view: View?, x: Float, y: Float): Boolean {
        if (view == null) {
            return false
        }
        val r = Rect()
        view.getGlobalVisibleRect(r)
        return x > r.left && x < r.right && y > r.top && y < r.bottom
    }

    fun isInViewAreaWithMargin(
        view: View?,
        x: Float,
        y: Float,
        marginLeft: Int,
        marginTop: Int,
        marginRight: Int,
        marginBottom: Int
    ): Boolean {
        if (view == null) {
            return false
        }
        val r = Rect()
        view.getGlobalVisibleRect(r)
        return x > r.left - marginLeft && x < r.right + marginRight && y > r.top - marginTop && y < r.bottom + marginBottom
    }

    /**
     * 获取view在屏幕中的位置
     */
    fun getViewLocationOnScreen(view: View): IntArray {
        val intArray = IntArray(2)
        view.getLocationOnScreen(intArray)
        Log.d("ViewHelper", "${view.javaClass.simpleName}, x = ${intArray[0]}, y = ${intArray[1]}")
        return intArray
    }
}

fun View.isVisible() = visibility == View.VISIBLE

fun View.visible(show: Boolean) {
    ViewHelper.setVisible(this, show)
}

fun RecyclerView.Adapter<*>.getRecyclerViewItem(recyclerView: RecyclerView?, position: Int): View? {
    if (recyclerView == null || recyclerView.layoutManager == null || recyclerView.adapter == null) {
        return null
    }
    if (position > recyclerView.adapter!!.itemCount) {
        return null
    }
    val viewHolder: RecyclerView.ViewHolder = recyclerView.adapter!!.createViewHolder(
        recyclerView,
        recyclerView.adapter!!.getItemViewType(position)
    )
    recyclerView.adapter!!.onBindViewHolder(viewHolder, position)
//    viewHolder.itemView.measure(
//        View.MeasureSpec.makeMeasureSpec(recyclerView.width, View.MeasureSpec.EXACTLY),
//        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
//    )
    val p = viewHolder.itemView.layoutParams
    p.height = ViewGroup.LayoutParams.WRAP_CONTENT
    viewHolder.itemView.layoutParams = p
    return viewHolder.itemView
}