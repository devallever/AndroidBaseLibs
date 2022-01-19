package app.allever.android.lib.permission.core

interface PermissionListener {
    fun onAllGranted()
//    fun onGranted(grantedList: MutableList<String>) {}
    fun onDenied(deniedList: MutableList<String>) {}
    fun alwaysDenied(deniedList: MutableList<String>) {}
}