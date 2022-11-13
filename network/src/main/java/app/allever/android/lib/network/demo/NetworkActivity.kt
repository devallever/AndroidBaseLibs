package app.allever.android.lib.network.demo

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.lifecycleScope
import app.allever.android.lib.core.base.AbstractActivity
import app.allever.android.lib.core.ext.log
import app.allever.android.lib.core.ext.toast
import app.allever.android.lib.core.function.network.HttpConfig
import app.allever.android.lib.core.function.network.ResponseCallback
import app.allever.android.lib.core.function.network.response.NetResponse
import app.allever.android.lib.core.helper.GsonHelper.toJson
import app.allever.android.lib.network.ApiService
import app.allever.android.lib.network.R
import kotlinx.coroutines.launch

class NetworkActivity : AbstractActivity() {
    private lateinit var tvResult: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_network)

        HttpConfig.baseUrl("https://www.wanandroid.com/")
            .baseResponseClass(BaseResponse::class.java)
            .interceptor(GlobalInterceptor())
            .header("KEY", "VALUE")
            .init(ApiService)

        tvResult = findViewById(R.id.tvResult)
        findViewById<View>(R.id.btnSend).setOnClickListener {
            sendForJava()
        }

        findViewById<View>(R.id.btnSendKotlin).setOnClickListener {
            sendKotlin()
        }

        findViewById<View>(R.id.btnSendKotlinLiveData1).setOnClickListener {
            sendKotlinLiveData1()
        }

        findViewById<View>(R.id.btnSendKotlinLiveData2).setOnClickListener {
            sendKotlinLiveData2()
        }

        findViewById<View>(R.id.btnClear).setOnClickListener {
            tvResult.text = ""
        }


        bannerMultiLiveData.observe(this) {
            updateUi(it)
        }

        bannerLiveData.observe(this) {
            updateUi(it)
        }
    }

    private fun sendForJava() {
        AppRepository.getBannerCall(BannerResponseCache(), object : ResponseCallback<List<BannerData>> {
            override fun onFail(response: NetResponse<List<BannerData>>) {
                log(response.getMsg())
                updateUi((response as? BaseResponse<List<BannerData>>)?:return)
            }

            override fun onSuccess(response: NetResponse<List<BannerData>>) {
                log(response.getMsg())
                updateUi(response as BaseResponse<List<BannerData>>)
            }
        })
    }

    private fun sendKotlin() {
        lifecycleScope.launch {
            val result = AppRepository.getBanner(BannerResponseCache())
            if (result.success()) {
                //处理数据
                bannerMultiLiveData.value = result
            } else {
                toast(result.errorMsg)
            }
        }
    }

    private val bannerMultiLiveData = MutableLiveData<BaseResponse<List<BannerData>>>()

    private val bannerRequestLiveData = MutableLiveData<Any?>(null)
    private val bannerLiveData = Transformations.switchMap(bannerRequestLiveData) {
        val result = AppRepository.getBannerWithLiveData(BannerResponseCache())
        result
    }

    private fun sendKotlinLiveData1() {
        lifecycleScope.launch {
            AppRepository.getBannerForLiveData(BannerResponseCache()).value?.let {
                updateUi(it)
            }
        }
    }

    private fun sendKotlinLiveData2() {
        bannerRequestLiveData.value = null
    }

    private fun updateUi(it: BaseResponse<List<BannerData>>) {
        tvResult.text = toJson(it)
    }
}