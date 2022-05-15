package app.allever.android.lib.mvvm.demo

import androidx.lifecycle.MutableLiveData
import app.allever.android.lib.mvvm.base.BaseViewModel

class MainViewModel : BaseViewModel() {
    val usernameLiveData = MutableLiveData<String>()
    override fun init() {

    }

    fun login() {
        usernameLiveData.value = "allever"
    }
}