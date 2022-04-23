package app.allever.android.lib.permission.core

import android.app.Dialog

interface PermissionListener {
    fun onAllGranted()

    //    fun onGranted(grantedList: MutableList<String>) {}
    fun onDenied(deniedList: MutableList<String>) {}
    fun alwaysDenied(deniedList: MutableList<String>) {}
    fun getSettingDialog(): Dialog?{
        return null
    }
}