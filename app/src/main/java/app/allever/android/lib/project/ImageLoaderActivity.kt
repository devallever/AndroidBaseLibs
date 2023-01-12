package app.allever.android.lib.project

import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.lifecycle.lifecycleScope
import app.allever.android.lib.common.BaseActivity
import app.allever.android.lib.core.function.imageloader.*
import app.allever.android.lib.mvvm.base.BaseViewModel
import app.allever.android.lib.project.databinding.ActivityCoilBinding
import kotlinx.coroutines.launch

class ImageLoaderActivity : BaseActivity<ActivityCoilBinding, ImageLoaderViewModel>() {

    override fun inflateChildBinding() = ActivityCoilBinding.inflate(layoutInflater)

    override fun init() {
        initTopBar("图片加载器")
        val url =
            "https://img0.baidu.com/it/u=962361882,2281204904&fm=253&fmt=auto&app=138&f=JPEG?w=889&h=500"

        val url2 = "https://img0.baidu.com/it/u=68353154,235073569&fm=253&fmt=auto&app=120&f=JPEG?w=1200&h=675"

        val url3 = "https://img2.baidu.com/it/u=924133470,1725987117&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=281"

        val url4 = "https://img2.baidu.com/it/u=420156118,3874648934&fm=253&fmt=auto&app=120&f=JPEG?w=1280&h=800"

        val url5 = "https://img0.baidu.com/it/u=2066710797,1295269268&fm=253&fmt=auto&app=120&f=JPEG?w=1422&h=800"

        val url6 = "https://img2.baidu.com/it/u=3550900606,1592535269&fm=253&fmt=auto&app=120&f=JPEG?w=1280&h=800"


        binding.iv1.load(url)

        binding.iv2.loadCircle(url2)

        binding.iv3.loadRound(url3, 10F)

        binding.iv4.loadBlur(url4, 10F)

        binding.iv5.loadCircle(url5, 2, Color.parseColor("#ff6c1e"))

        lifecycleScope.launch {
            downloadImg(url6) { _, bitmap ->
                bitmap?.let {
                    (binding.iv6).setImageBitmap(BitmapFactory.decodeFile(it.absolutePath))
                }
            }
        }

        binding.iv7.load(R.drawable.ic_icon_gift)

        binding.iv8.loadGif(R.drawable.ic_gif)
    }
}

class ImageLoaderViewModel: BaseViewModel() {
    override fun init() {

    }
}