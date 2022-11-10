package app.allever.android.lib.widget.ext

import androidx.viewpager2.widget.ViewPager2
import com.flyco.tablayout.SlidingTabLayout


fun SlidingTabLayout.setupViewPager2(
    viewPager: ViewPager2,
    list: List<String> = listOf(),
    currentItem: Int = 0,
    selectTextSizeSp: Float = -1f,
    unSelectTextSizeSp: Float = -1f,
    pageSelected: (position: Int) -> Unit
) {
    if (list.isEmpty()) {
        setViewPager2(viewPager)
    } else {
        setViewPager2(viewPager, list as ArrayList<String>)
    }

    var lastPosition = currentItem
    viewPager.currentItem = currentItem
    getTitleView(lastPosition).textSize = selectTextSizeSp
    viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            if (unSelectTextSizeSp != -1f) {
                getTitleView(lastPosition).textSize = unSelectTextSizeSp
            }
            if (selectTextSizeSp != -1f) {
                getTitleView(position).textSize = selectTextSizeSp
            }
            lastPosition = position

            pageSelected(position)
        }
    })
}