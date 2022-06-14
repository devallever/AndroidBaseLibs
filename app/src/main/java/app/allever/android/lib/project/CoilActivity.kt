package app.allever.android.lib.project

import android.os.Bundle
import android.widget.ImageView
import app.allever.android.lib.core.base.AbstractActivity
import app.allever.android.lib.core.function.imageloader.coil.load
import app.allever.android.lib.core.function.imageloader.coil.loadBlur
import app.allever.android.lib.core.function.imageloader.coil.loadCircle
import app.allever.android.lib.core.function.imageloader.coil.loadRound

class CoilActivity: AbstractActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coil)

        val url = "https://img0.baidu.com/it/u=962361882,2281204904&fm=253&fmt=auto&app=138&f=JPEG?w=889&h=500"
        findViewById<ImageView>(R.id.iv1).load(url)

        findViewById<ImageView>(R.id.iv2).loadCircle(url)

        findViewById<ImageView>(R.id.iv3).loadRound(url, 8f)

        findViewById<ImageView>(R.id.iv4).loadBlur(url, 20f)
    }
}