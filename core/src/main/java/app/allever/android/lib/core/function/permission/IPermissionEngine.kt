package app.allever.android.lib.core.function.permission

import android.content.Context

interface IPermissionEngine {
    /**
     * 解决哪些不在栈顶Activity请求的权限
     */
    fun requestPermission(
        context: Context,
        listener: PermissionListener,
        vararg permissions: String
    ) {
    }

    fun requestPermission(
        context: Context,
        listener: PermissionListener,
        permissions: MutableList<String>
    ) {
    }

    fun requestPermission(listener: PermissionListener, vararg permissions: String)
    fun jumpSetting(context: Context, requestCode: Int)
    fun handlePermissionResult(
        context: Context,
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

    }
}