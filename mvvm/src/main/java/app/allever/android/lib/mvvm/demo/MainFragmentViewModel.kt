package app.allever.android.lib.mvvm.demo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import app.allever.android.lib.mvvm.base.BaseViewModel
import kotlinx.coroutines.launch

class MainFragmentViewModel : BaseViewModel() {

    val usernameLiveData = MutableLiveData<String>()

    override fun init() {
    }

    fun login() {
        viewModelScope.launch {
            usernameLiveData.value = "allever"
        }
    }
}