package app.allever.android.lib.core.function.permission

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import app.allever.android.lib.core.ext.log
import app.allever.android.lib.core.helper.ActivityHelper

object DefaultPermissionEngine : BasePermissionEngine() {

    private var mPermissionListener: PermissionListener? = null

    private const val RC_CODE = 0x01

    override fun requestPermission(
        context: Context,
        listener: PermissionListener,
        permissions: MutableList<String>
    ) {
        if (PermissionHelper.hasPermissionOrigin(context, permissions.toList())) {
            listener.onAllGranted()
            return
        }
        mPermissionListener = listener
        request(context, listener) {
            val activity = when (context) {
                is Activity -> {
                    context
                }
                is Fragment -> {
                    context.requireActivity()
                }
                else -> {
                    ActivityHelper.getTopActivity()
                }
            }

            activity?.let {
                PermissionHelper.requestPermissionOrigin(
                    it,
                    RC_CODE,
                    permissions
                )
            }
        }
    }
    override fun requestPermission(
        context: Context,
        listener: PermissionListener,
        vararg permissions: String
    ) {

        if (PermissionHelper.hasPermissionOrigin(context, permissions.toList())) {
            listener.onAllGranted()
            return
        }
        mPermissionListener = listener
        request(context, listener) {
            val activity = when (context) {
                is Activity -> {
                    context
                }
                is Fragment -> {
                    context.requireActivity()
                }
                else -> {
                    ActivityHelper.getTopActivity()
                }
            }

            activity?.let {
                PermissionHelper.requestPermissionOrigin(
                    it,
                    RC_CODE,
                    *permissions
                )
            }
        }
    }

    override fun requestPermission(listener: PermissionListener, vararg permissions: String) {
        ActivityHelper.getTopActivity()?.let { requestPermission(it, listener, *permissions) }
    }

    override fun jumpSetting(context: Context, requestCode: Int) {
        PermissionHelper.gotoSettingOrigin()
    }

    override fun handlePermissionResult(
        context: Context,
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (PermissionHelper.hasPermissionOrigin(
                ActivityHelper.getTopActivity(),
                permissions.toList()
            )
        ) {
            mPermissionListener?.onAllGranted()
        } else {
            mPermissionListener?.let {
                val deniedList = mutableListOf<String>()
                permissions.map { permission ->
                    if (!PermissionHelper.hasPermissionOrigin(
                            ActivityHelper.getTopActivity(),
                            listOf(permission)
                        )
                    ) {
                        deniedList.add(permission)
                    }
                    log("拒绝权限：$permission")
                }
                handleDenied(
                    permissions, context,
                    it, deniedList
                )
            }
        }

        mPermissionListener = null
    }
}