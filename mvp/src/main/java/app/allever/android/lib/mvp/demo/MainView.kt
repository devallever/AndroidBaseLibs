package app.allever.android.lib.mvp.demo

import app.allever.android.lib.mvp.base.BaseView

interface MainView : BaseView {
    fun updateUsername(username: String)
}