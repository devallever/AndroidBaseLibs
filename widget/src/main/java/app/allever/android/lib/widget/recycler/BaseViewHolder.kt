package app.allever.android.lib.widget.recycler

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.IdRes
import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.RecyclerView

class BaseViewHolder(private val mContext: Context, itemView: View) :
    RecyclerView.ViewHolder(itemView) {
    private val mViews: SparseArrayCompat<View> = SparseArrayCompat()

    fun <V : View> getView(@IdRes res: Int): V? {
        var v: View? = mViews.get(res)
        if (v == null) {
            v = itemView.findViewById(res)
            mViews.put(res, v)
        }
        return v as? V
    }


    /****以下为辅助方法 */

    /**
     * 设置TextView的值
     *
     * @param viewId
     * @param text
     * @return
     */
    fun setText(viewId: Int, text: String): BaseViewHolder {
        val view = getView<TextView>(viewId)
        view?.text = text
        return this
    }

    fun setImageResource(viewId: Int, resId: Int): BaseViewHolder {
        val view = getView<ImageView>(viewId)
        view?.setImageResource(resId)
        return this
    }

    fun setImageBitmap(viewId: Int, bitmap: Bitmap): BaseViewHolder {
        val view = getView<ImageView>(viewId)
        view?.setImageBitmap(bitmap)
        return this
    }

    fun setImageDrawable(viewId: Int, drawable: Drawable): BaseViewHolder {
        val view = getView<ImageView>(viewId)
        view?.setImageDrawable(drawable)
        return this
    }

    fun setBackgroundColor(viewId: Int, color: Int): BaseViewHolder {
        val view = getView<View>(viewId)
        view?.setBackgroundColor(color)
        return this
    }

    fun setBackgroundRes(viewId: Int, backgroundRes: Int): BaseViewHolder {
        val view = getView<View>(viewId)
        view?.setBackgroundResource(backgroundRes)
        return this
    }

    fun setTextColor(viewId: Int, textColor: Int): BaseViewHolder {
        val view = getView<TextView>(viewId)
        view?.setTextColor(textColor)
        return this
    }

    fun setTextColorRes(viewId: Int, textColorRes: Int): BaseViewHolder {
        val view = getView<TextView>(viewId)
        view?.setTextColor(mContext.resources.getColor(textColorRes))
        return this
    }

    fun setAlpha(viewId: Int, value: Float): BaseViewHolder {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//            getView<View>(viewId).alpha = value
//        } else {
//            // Pre-honeycomb hack to set Alpha value
//            val alpha = AlphaAnimation(value, value)
//            alpha.duration = 0
//            alpha.fillAfter = true
//            getView<View>(viewId).startAnimation(alpha)
//        }
        getView<View>(viewId)?.alpha = value
        return this
    }

    fun setVisible(viewId: Int, visible: Boolean): BaseViewHolder {
        val view = getView<View>(viewId)
        view?.visibility = if (visible) View.VISIBLE else View.GONE
        return this
    }

    fun linkify(viewId: Int): BaseViewHolder {
        val view = getView<TextView>(viewId) ?: return this
        Linkify.addLinks(view, Linkify.ALL)
        return this
    }

    fun setTypeface(typeface: Typeface, vararg viewIds: Int): BaseViewHolder {
        for (viewId in viewIds) {
            val view = getView<TextView>(viewId)
            view ?: continue
            view.typeface = typeface
            view.paintFlags = view.paintFlags or Paint.SUBPIXEL_TEXT_FLAG
        }
        return this
    }

    fun setProgress(viewId: Int, progress: Int): BaseViewHolder {
        val view = getView<ProgressBar>(viewId)
        view?.progress = progress
        return this
    }

    fun setProgress(viewId: Int, progress: Int, max: Int): BaseViewHolder {
        val view = getView<ProgressBar>(viewId)
        view?.max = max
        view?.progress = progress
        return this
    }

    fun setMax(viewId: Int, max: Int): BaseViewHolder {
        val view = getView<ProgressBar>(viewId)
        view?.max = max
        return this
    }

    fun setRating(viewId: Int, rating: Float): BaseViewHolder {
        val view = getView<RatingBar>(viewId)
        view?.rating = rating
        return this
    }

    fun setRating(viewId: Int, rating: Float, max: Int): BaseViewHolder {
        val view = getView<RatingBar>(viewId)
        view?.max = max
        view?.rating = rating
        return this
    }

    fun setTag(viewId: Int, tag: Any): BaseViewHolder {
        val view = getView<View>(viewId)
        view?.tag = tag
        return this
    }

    fun setTag(viewId: Int, key: Int, tag: Any): BaseViewHolder {
        val view = getView<View>(viewId)
        view?.setTag(key, tag)
        return this
    }

    fun setChecked(viewId: Int, checked: Boolean): BaseViewHolder {
        val view = getView<View>(viewId) as Checkable
        view?.isChecked = checked
        return this
    }

    /**
     * 关于事件的
     */
    fun setOnClickListener(
        viewId: Int,
        listener: View.OnClickListener
    ): BaseViewHolder {
        val view = getView<View>(viewId)
        view?.setOnClickListener(listener)
        return this
    }

    fun setOnTouchListener(
        viewId: Int,
        listener: View.OnTouchListener
    ): BaseViewHolder {
        val view = getView<View>(viewId)
        view?.setOnTouchListener(listener)
        return this
    }

    fun setOnLongClickListener(
        viewId: Int,
        listener: View.OnLongClickListener
    ): BaseViewHolder {
        val view = getView<View>(viewId)
        view?.setOnLongClickListener(listener)
        return this
    }

    companion object {

        fun getHolder(context: Context, parent: ViewGroup, layoutId: Int): BaseViewHolder {

            val itemView = LayoutInflater.from(context).inflate(
                layoutId, parent,
                false
            )
            val holder = BaseViewHolder(context, itemView)
            itemView.tag = holder
            return holder
        }
    }

}
