package app.allever.android.lib.network.demo

import android.os.Bundle
import android.view.View
import android.widget.TextView
import app.allever.android.lib.core.base.AbstractActivity
import app.allever.android.lib.network.HttpConfig
import app.allever.android.lib.network.R
import app.allever.android.lib.network.interceptor.LoggerInterceptor
import kotlinx.coroutines.launch
import okhttp3.logging.HttpLoggingInterceptor

class NetworkActivity : AbstractActivity() {
    private lateinit var tvResult: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_network)

        HttpConfig.baseUrl("https://www.wanandroid.com/")
            .interceptor(GlobalInterceptor())
            .header("KEY","VALUE")
        HttpConfig.baseUrl("https://www.wanandroid.com/")

        tvResult = findViewById<TextView>(R.id.tvResult)
        findViewById<View>(R.id.btnSend).setOnClickListener {
            send()
        }
    }

    private fun send() {
        mainCoroutine.launch {
            val result = NetRepository.getBanner()
            NetRepository.showMessageIfFail(result)
            if (result.isSuccess) {
                val data = result.getOrNull()!!
                val list = data.data
                list?.size
            } else {

            }
            tvResult.text = NetRepository.getJson(result)
        }
    }
}