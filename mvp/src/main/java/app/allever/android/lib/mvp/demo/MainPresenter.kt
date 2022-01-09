package app.allever.android.lib.mvp.demo

import app.allever.android.lib.mvp.base.BasePresenter

class MainPresenter : BasePresenter<MainView>() {
    fun login() {
        mViewRef.get()?.updateUsername("allever")
    }
}