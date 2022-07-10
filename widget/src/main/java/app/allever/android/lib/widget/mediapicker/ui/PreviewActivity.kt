package app.allever.android.lib.widget.mediapicker.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.viewpager.widget.ViewPager
import app.allever.android.lib.core.base.AbstractActivity
import app.allever.android.lib.core.ext.toast
import app.allever.android.lib.widget.R
import app.allever.android.lib.widget.databinding.ActivityPreviewBinding
import app.allever.android.lib.widget.mediapicker.MediaItem
import app.allever.android.lib.widget.mediapicker.MediaPicker
import app.allever.android.lib.widget.mediapicker.ui.adapter.PreviewFragmentPagerAdapter

class PreviewActivity : AbstractActivity(), View.OnClickListener {

    companion object {
        const val EXTRA_THUMBNAIL_LIST = "EXTRA_THUMBNAIL_LIST"
        const val EXTRA_POSITION = "EXTRA_POSITION"

        fun startActivity(
            context: Activity,
            position: Int
        ) {
            val intent = Intent(context, PreviewActivity::class.java)
            intent.putExtra(EXTRA_POSITION, position)
            context.startActivity(intent)
        }
    }

    private lateinit var mBinding: ActivityPreviewBinding

    private val mViewModel by viewModels<PreviewViewModel>()

    private var mCallback: Callback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_preview)

        mViewModel.initExtra(intent)

        initView()
    }

    private fun initView() {
        mBinding.ivBack.setOnClickListener(this)
        mBinding.ivSelect.setOnClickListener(this)

        mViewModel.pagerAdapter = PreviewFragmentPagerAdapter(
            supportFragmentManager,
            mViewModel.mediaItemList
        )
        mBinding.idVpImage.adapter = mViewModel.pagerAdapter
        mBinding.idVpImage.addOnPageChangeListener(object :
            ViewPager.OnPageChangeListener {
            override fun onPageSelected(position: Int) {
                if (mViewModel.checkOutOfBoundary()) {
                    return
                }
                val fragment = mViewModel.pagerAdapter?.currentFragment as? PreviewFragment
                fragment?.pause()
                mViewModel.position = position
                if (mViewModel.checkOutOfBoundary()) {
                    return
                }
                val thumbnailBean = mViewModel.mediaItemList[position]
                updateSelectIcon(thumbnailBean)
            }

            override fun onPageScrollStateChanged(p0: Int) {}
            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {}
        })

        mViewModel.pagerAdapter?.notifyDataSetChanged()

        if (mViewModel.checkOutOfBoundary()) {
            return
        }
        mBinding.idVpImage.currentItem = mViewModel.position

        updateSelectIcon(mViewModel.mediaItemList[mViewModel.position])

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> {
                finish()
            }

            R.id.iv_select -> {
                if (mViewModel.checkOutOfBoundary()) {
                    return
                }
                val thumbnailBean = mViewModel.mediaItemList[mViewModel.position]
                val selected = thumbnailBean.isChecked
                thumbnailBean.isChecked = !selected
                updateSelectIcon(thumbnailBean)

                if (thumbnailBean.isChecked) {
                    mCallback?.updateSelected(thumbnailBean)
                    finish()
                }
            }
        }
    }

    private fun updateSelectIcon(thumbnailBean: MediaItem) {
        if (thumbnailBean.isChecked) {
            mBinding.ivSelect.setImageResource(R.drawable.icon_album_select)
        } else {
            mBinding.ivSelect.setImageResource(R.drawable.icon_album_unselected)
        }
    }

    fun setCallback(callback: Callback) {
        mCallback = callback
    }

    interface Callback {
        /**
         * 当预览页面选中后，刷新列表item选中状态
         */
        fun updateSelected(mediaItem: MediaItem)
    }
}

class PreviewViewModel : ViewModel() {
    var pagerAdapter: PreviewFragmentPagerAdapter? = null
    var mediaItemList: MutableList<MediaItem> = mutableListOf()

    var position: Int = 0

    fun initExtra(intent: Intent?) {
        mediaItemList.addAll(MediaPicker.extraMap[PreviewActivity.EXTRA_THUMBNAIL_LIST] as Collection<MediaItem>)
        MediaPicker.extraMap.remove(PreviewActivity.EXTRA_THUMBNAIL_LIST)
        position = intent?.getIntExtra(PreviewActivity.EXTRA_POSITION, 0) ?: 0
    }

    fun checkOutOfBoundary(): Boolean {
        val result = position !in 0 until mediaItemList.size
        if (result) {
            toast("Can not display this photo or video.")
        }
        return result
    }
}