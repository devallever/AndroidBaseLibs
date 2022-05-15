package app.allever.android.lib.core.function.permission

import android.app.Dialog

interface PermissionListener {
    /**
     * 全部同意
     */
    fun onAllGranted()

    //    fun onGranted(grantedList: MutableList<String>) {}
    /**
     * 拒绝
     */
    fun onDenied(deniedList: MutableList<String>) {}

    /**
     * 总是拒绝
     */
    fun alwaysDenied(deniedList: MutableList<String>) {}

    /**
     * 自定义跳转弹窗，否则使用默认的
     */
    fun getSettingDialog(): Dialog? {
        return null
    }

    /**
     * 默认总是拒绝后弹出设置权限，设置false不弹出
     */
    fun needShowJumpSettingDialog(): Boolean = true

    fun getWhyRequestPermissionDialog(): Dialog? {
        return null
    }

    fun needShowWhyRequestPermissionDialog(): Boolean = false
}