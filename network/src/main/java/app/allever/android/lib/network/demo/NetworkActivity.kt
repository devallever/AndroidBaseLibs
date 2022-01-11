package app.allever.android.lib.network.demo

import android.app.Activity
import android.os.Bundle
import app.allever.android.lib.network.HttpConfig
import app.allever.android.lib.network.R

class NetworkActivity: Activity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_network)

        HttpConfig.baseUrl("https://www.wanandroid.com/")
        NetRepository.getBanner()
    }
}