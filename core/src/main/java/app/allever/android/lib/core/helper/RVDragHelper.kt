package app.allever.android.lib.core.helper

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import java.util.*

object RVDragHelper {
    fun <T> bind(recyclerView: RecyclerView?, data: MutableList<T>, dragStateCallback: DragStateCallback? = null) {
            val itemTouchHelper = ItemTouchHelper(
                ItemHelperCallback(/*data,*/
                    dragStateCallback
                )
            )
            itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    interface DragStateCallback {
        fun getData(): MutableList<out Any>
        fun allowDrag(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Boolean
        fun allowSwipe(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Boolean
        fun onItemRangeMoved(from: Int, to: Int)
        fun onDragStart()
        fun onDragEnd(holder: RecyclerView.ViewHolder, from: Int, to: Int)
    }

    class ItemHelperCallback/*<T>*/(/*val datas: MutableList<T>, */val dragStateCallback: DragStateCallback? = null) : ItemTouchHelper.Callback() {
        companion object {
            private val TAG = RVDragHelper.ItemHelperCallback::class.java.simpleName
            private const val ANIM_DURATION = 100L
        }

        private var recordFrom: Int = -1
        private var recordTo: Int = -1

        override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
            //允许上下左右的拖动
            val dragFlags = if (dragStateCallback?.allowDrag(recyclerView, viewHolder) == false) {
                0
            } else ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            val swipeFlags = if (dragStateCallback?.allowSwipe(recyclerView, viewHolder) == false) {
                0
            } else 0
            return ItemTouchHelper.Callback.makeMovementFlags(dragFlags, swipeFlags)
        }

        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            val from = viewHolder.adapterPosition
            val to = target.adapterPosition
            val datas = dragStateCallback?.getData() ?:return false
            if (to >= datas.size) {
                return false
            }
            //交换数据
//            val datas = datas
            if (from < to) {
                for (i in from until to) {
                    Collections.swap(datas, i, i + 1)
                }
            } else {
                for (i in from downTo to + 1) {
                    Collections.swap(datas, i, i - 1)
                }
            }

            recyclerView.adapter?.notifyItemMoved(from, to)
            dragStateCallback?.onItemRangeMoved(from, to)
            recordFrom =  from
            recordTo =  to
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {
        }

        override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
            if (viewHolder != null) {
//                dragStateCallback.onSelected(viewHolder)
                val duration =
                    ANIM_DURATION
                val pvh2 = PropertyValuesHolder.ofFloat("scaleX", 1f, 1.2f)
                val pvh3 = PropertyValuesHolder.ofFloat("scaleY", 1f, 1.2f)
                ObjectAnimator.ofPropertyValuesHolder(viewHolder.itemView, pvh2, pvh3).setDuration(duration).start()
                dragStateCallback?.onDragStart()
            }
            super.onSelectedChanged(viewHolder, actionState)
        }

        override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
            if (!recyclerView.isComputingLayout && recyclerView.scrollState == 0) {
                val duration =
                    ANIM_DURATION
                val pvh2 = PropertyValuesHolder.ofFloat("scaleX", 1.2f, 1f)
                val pvh3 = PropertyValuesHolder.ofFloat("scaleY", 1.2f, 1f)
                ObjectAnimator.ofPropertyValuesHolder(viewHolder.itemView, pvh2, pvh3).setDuration(duration).start()
                recyclerView.adapter?.notifyDataSetChanged()
                dragStateCallback?.onDragEnd(viewHolder,recordFrom ,recordTo)
            }
            super.clearView(recyclerView, viewHolder)
        }
    }
}