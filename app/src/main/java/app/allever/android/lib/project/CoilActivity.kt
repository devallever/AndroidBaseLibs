package app.allever.android.lib.project

import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import app.allever.android.lib.core.base.AbstractActivity
import app.allever.android.lib.core.function.imageloader.*

class CoilActivity : AbstractActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coil)

        val url =
            "https://img0.baidu.com/it/u=962361882,2281204904&fm=253&fmt=auto&app=138&f=JPEG?w=889&h=500"

        val url2 = "https://img0.baidu.com/it/u=68353154,235073569&fm=253&fmt=auto&app=120&f=JPEG?w=1200&h=675"


        findViewById<ImageView>(R.id.iv1).load(url)

        findViewById<ImageView>(R.id.iv2).loadCircle(url)

        findViewById<ImageView>(R.id.iv3).loadRound(url, 10F)

        findViewById<ImageView>(R.id.iv4).loadBlur(url, 10F)

        findViewById<ImageView>(R.id.iv5).loadCircle(url, 2, Color.parseColor("#ff6c1e"))

        downloadImg(url2) { _, bitmap ->
            bitmap?.let {
                findViewById<ImageView>(R.id.iv6).setImageBitmap(it)
            }
        }

        findViewById<ImageView>(R.id.iv7).load(R.drawable.ic_icon_gift)

        findViewById<ImageView>(R.id.iv8).loadGif(R.drawable.ic_gif)

    }
}