package app.allever.android.lib.project

import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import androidx.lifecycle.lifecycleScope
import app.allever.android.lib.core.base.AbstractActivity
import app.allever.android.lib.core.function.imageloader.*
import kotlinx.coroutines.launch

class ImageLoaderActivity : AbstractActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coil)

        val url =
            "https://img0.baidu.com/it/u=962361882,2281204904&fm=253&fmt=auto&app=138&f=JPEG?w=889&h=500"

        val url2 = "https://img0.baidu.com/it/u=68353154,235073569&fm=253&fmt=auto&app=120&f=JPEG?w=1200&h=675"

        val url3 = "https://img2.baidu.com/it/u=924133470,1725987117&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=281"

        val url4 = "https://img2.baidu.com/it/u=420156118,3874648934&fm=253&fmt=auto&app=120&f=JPEG?w=1280&h=800"

        val url5 = "https://img0.baidu.com/it/u=2066710797,1295269268&fm=253&fmt=auto&app=120&f=JPEG?w=1422&h=800"

        val url6 = "https://img2.baidu.com/it/u=3550900606,1592535269&fm=253&fmt=auto&app=120&f=JPEG?w=1280&h=800"


        findViewById<ImageView>(R.id.iv1).load(url)

        findViewById<ImageView>(R.id.iv2).loadCircle(url2)

        findViewById<ImageView>(R.id.iv3).loadRound(url3, 10F)

        findViewById<ImageView>(R.id.iv4).loadBlur(url4, 10F)

        findViewById<ImageView>(R.id.iv5).loadCircle(url5, 2, Color.parseColor("#ff6c1e"))

        lifecycleScope.launch {
            downloadImg(url6) { _, bitmap ->
                bitmap?.let {
                    findViewById<ImageView>(R.id.iv6).setImageBitmap(BitmapFactory.decodeFile(it.absolutePath))
                }
            }
        }

        findViewById<ImageView>(R.id.iv7).load(R.drawable.ic_icon_gift)

        findViewById<ImageView>(R.id.iv8).loadGif(R.drawable.ic_gif)

    }
}