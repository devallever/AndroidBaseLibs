package app.allever.android.lib.network.demo

import app.allever.android.lib.core.helper.CoroutineHelper
import app.allever.android.lib.core.log
import app.allever.android.lib.core.toast
import app.allever.android.lib.network.ApiService
import kotlinx.coroutines.launch

object NetRepository {
    private val wanAndroidApi by lazy {
        ApiService.get(WanAndroidApi::class.java)
    }
    fun getBanner(){
        CoroutineHelper.mainCoroutine.launch {
            val result = wanAndroidApi.getBanner()
            log(result.errorMsg)
            toast(result.errorMsg)
        }
    }
}