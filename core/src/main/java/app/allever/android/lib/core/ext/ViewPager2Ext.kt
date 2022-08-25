package app.allever.android.lib.core.ext

import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import java.lang.reflect.Field

/**
 * 设置ViewPager2切换page阈值
 *
 * 解决ViewPager2灵敏度问题
 */
fun ViewPager2.setTouchTouchSlop(touchSlop: Int) {
    try {
        val recyclerViewField: Field = ViewPager2::class.java.getDeclaredField("mRecyclerView")
        recyclerViewField.isAccessible = true
        val recyclerView =
            recyclerViewField.get(this) as RecyclerView
        val touchSlopField: Field = RecyclerView::class.java.getDeclaredField("mTouchSlop")
        touchSlopField.isAccessible = true
//             final int touchSlop = (int) touchSlopField.get(recyclerView);
//             touchSlopField.set(recyclerView, touchSlop*4);//通过获取原有的最小滑动距离 *n来增加此值
        touchSlopField.set(recyclerView, touchSlop) //自己写一个值
    } catch (ignore: Exception) {
        ignore.printStackTrace()
    }
}