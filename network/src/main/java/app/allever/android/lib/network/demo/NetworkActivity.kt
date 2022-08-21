package app.allever.android.lib.network.demo

import android.os.Bundle
import android.view.View
import android.widget.TextView
import app.allever.android.lib.core.base.AbstractActivity
import app.allever.android.lib.core.ext.toast
import app.allever.android.lib.core.function.network.HttpConfig
import app.allever.android.lib.core.helper.GsonHelper
import app.allever.android.lib.network.ApiService
import app.allever.android.lib.network.R
import kotlinx.coroutines.launch

class NetworkActivity : AbstractActivity() {
    private lateinit var tvResult: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_network)

        HttpConfig.baseUrl("https://www.wanandroid.com/")
            .interceptor(GlobalInterceptor())
            .header("KEY", "VALUE")
            .init(ApiService)

        tvResult = findViewById<TextView>(R.id.tvResult)
        findViewById<View>(R.id.btnSend).setOnClickListener {
            toast("发送请求")
            send()
        }
    }

    private fun send() {
        mainCoroutine.launch {
//            val result = NetRepository.getBanner(BannerResponseCache())
//            result.let {
//                tvResult.text = GsonHelper.toJson(it)
//            }

            //LiveData方式
            NetRepository.getBannerForLiveData(BannerResponseCache()).observe(this@NetworkActivity) {
                tvResult.text = GsonHelper.toJson(it)
            }
        }
    }
}