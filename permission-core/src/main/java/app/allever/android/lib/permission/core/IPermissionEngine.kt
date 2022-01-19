package app.allever.android.lib.permission.core

interface IPermissionEngine {
    fun requestPermission(listener: PermissionListener, vararg permissions: String)
}