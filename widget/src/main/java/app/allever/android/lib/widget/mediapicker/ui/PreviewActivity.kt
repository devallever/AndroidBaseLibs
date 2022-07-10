package app.allever.android.lib.widget.mediapicker.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.viewpager.widget.ViewPager
import app.allever.android.lib.core.base.AbstractActivity
import app.allever.android.lib.core.ext.toast
import app.allever.android.lib.core.function.media.MediaBean
import app.allever.android.lib.widget.R
import app.allever.android.lib.widget.databinding.ActivityPreviewBinding
import app.allever.android.lib.widget.mediapicker.MediaItem
import app.allever.android.lib.widget.mediapicker.MediaPicker
import app.allever.android.lib.widget.mediapicker.ui.adapter.PreviewFragmentPagerAdapter

class PreviewActivity : AbstractActivity(), View.OnClickListener {

    companion object {
        const val EXTRA_THUMBNAIL_LIST = "EXTRA_THUMBNAIL_LIST"
        const val EXTRA_POSITION = "dfgdfg"
        const val EXTRA_FROM_SAVE = "snassfla"

        fun startActivity(
            context: Activity,
            position: Int
        ) {
            try {
                val intent = Intent(context, PreviewActivity::class.java)
                intent.putExtra(EXTRA_POSITION, position)
                context.startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }
    private lateinit var mBinding: ActivityPreviewBinding

    private var mPagerAdapter: PreviewFragmentPagerAdapter? = null
    private var mThumbnailBeanList: MutableList<MediaBean> = mutableListOf()
    private var mMediaItemBeanList: MutableList<MediaItem> = mutableListOf()
    private var mPosition: Int = 0
    private var mFromSave = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_preview)
        getIntentData()

        initView()

    }

    private fun getIntentData() {
        val intent = this.intent
        mThumbnailBeanList.addAll(MediaPicker.extraMap[EXTRA_THUMBNAIL_LIST] as Collection<MediaBean>)
        MediaPicker.extraMap.remove(EXTRA_THUMBNAIL_LIST)
        mThumbnailBeanList.map {
            val itemBean = MediaItem(it)
            itemBean.isChecked = false
            mMediaItemBeanList.add(itemBean)
        }
        mPosition = intent.getIntExtra(EXTRA_POSITION, 0)
    }

    private fun initView() {
        mBinding.ivBack.setOnClickListener(this)

        mBinding.ivSelect.setOnClickListener(this)
//        if (mFromSave) {
//            mIvSelect?.visibility = View.GONE
//        } else {
//            mIvSelect?.visibility = View.VISIBLE
//        }

        mPagerAdapter = PreviewFragmentPagerAdapter(
            supportFragmentManager,
            mThumbnailBeanList
        )
        mBinding.idVpImage.adapter = mPagerAdapter

        mPagerAdapter?.notifyDataSetChanged()

        if (checkOutOfBoundary()) {
            return
        }
        try {
            mBinding.idVpImage.currentItem = mPosition
            val thumbnailBean = mMediaItemBeanList[mPosition]
            updateSelecctIcon(thumbnailBean)
        } catch (e: Exception) {
            e.printStackTrace()
        }


        mBinding.idVpImage.addOnPageChangeListener(object : LineIndicator.OnPageChangeListener,
            ViewPager.OnPageChangeListener {
            override fun onPageSelected(position: Int) {
                if (checkOutOfBoundary()) {
                    return
                }
                val fragment = mPagerAdapter?.currentFragment as? PreviewFragment
                fragment?.pause()
                mPosition = position
                if (checkOutOfBoundary()) {
                    return
                }
                val thumbnailBean = mMediaItemBeanList[position]
                updateSelecctIcon(thumbnailBean)
            }

            override fun onPageScrollStateChanged(p0: Int) {}
            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {}
            override fun onPageChange(page: Int) {}
        })

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> {
                finish()
            }

            R.id.iv_select -> {
                if (checkOutOfBoundary()) {
                    return
                }
                val thumbnailBean = mMediaItemBeanList[mPosition]
                val selected = thumbnailBean.isChecked
                thumbnailBean.isChecked = !selected
                updateSelecctIcon(thumbnailBean)

//                postDelay({
//                    val intent = Intent()
//                    intent.putExtra(EXTRA_RESULT_POSITION, mPosition)
//                    intent.putExtra(EXTRA_RESULT_CHECK, thumbnailBean.isChecked)
//                    setResult(Activity.RESULT_OK, intent)
//                    finish()
//                }, 300)
            }
        }
    }

    private fun updateSelecctIcon(thumbnailBean: MediaItem) {
        if (thumbnailBean.isChecked) {
            mBinding.ivSelect.setImageResource(R.drawable.icon_album_select)
        } else {
            mBinding.ivSelect.setImageResource(R.drawable.icon_album_unselected)
        }
    }

    private fun checkOutOfBoundary(): Boolean {
        val result = mPosition !in 0 until mThumbnailBeanList.size
        if (result) {
            toast("Can not display this photo or video.")
        }
        return result
    }
}

class PreviewViewModel: ViewModel() {

}