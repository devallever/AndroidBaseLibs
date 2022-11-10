package app.allever.android.lib.widget.ext

import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import java.lang.reflect.Field

/**
 * 设置ViewPager2切换page阈值
 *
 * 解决ViewPager2灵敏度问题
 */
fun ViewPager2.modifyTouchSlop() {
    try {
        val recyclerViewField: Field = ViewPager2::class.java.getDeclaredField("mRecyclerView")
        recyclerViewField.isAccessible = true
        val recyclerView = recyclerViewField.get(this) as RecyclerView
        val touchSlopField: Field = RecyclerView::class.java.getDeclaredField("mTouchSlop")
        touchSlopField.isAccessible = true
        val touchSlop = touchSlopField.get(recyclerView) as Int
        touchSlopField.set(recyclerView, touchSlop * 4)  //6是经验值
    } catch (ignore: Exception) {
        ignore.printStackTrace()
    }
}