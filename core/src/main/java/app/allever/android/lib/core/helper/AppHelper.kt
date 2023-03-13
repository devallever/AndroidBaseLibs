package app.allever.android.lib.core.helper

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Build
import androidx.annotation.RequiresApi
import app.allever.android.lib.core.app.App
import app.allever.android.lib.core.ext.log
import app.allever.android.lib.core.ext.removeItemIf
import app.allever.android.lib.core.ext.toJson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * App相关，App信息如：包名/版本号/签名/图标/Activity名等；App操作如安装/卸载/打开等...
 *
 */
object AppHelper {
    @SuppressLint("QueryPermissionsNeeded")
    @RequiresApi(Build.VERSION_CODES.N)
    suspend fun getInstalledPackages(containSystem: Boolean = false): MutableList<PackageInfo> =
        withContext(Dispatchers.IO) {
            val manager = App.context.packageManager
            val list = manager.getInstalledPackages(PackageManager.MATCH_UNINSTALLED_PACKAGES)
            list.map {
                val appName = it.applicationInfo.loadLabel(manager)
                if ((it.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) == 0) {
                    log("非系统应用：${appName}")
                } else {
                    log("系统应用: $appName")
                }
                log("${it.applicationInfo.name} ->  ${it.toJson()}")
            }

            if (!containSystem) {
                list.removeItemIf {
                    (it.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0
                }
            }

            return@withContext list
        }

    @SuppressLint("QueryPermissionsNeeded")
    @RequiresApi(Build.VERSION_CODES.N)
    suspend fun getInstalledApplications(containSystem: Boolean = false): MutableList<ApplicationInfo> =
        withContext(Dispatchers.IO) {
            val manager = App.context.packageManager
            val list = manager.getInstalledApplications(0)
            list.map {
                val appName = it.loadLabel(manager)
                if ((it.flags and ApplicationInfo.FLAG_SYSTEM) == 0) {
                    log("非系统应用：${appName}")
                } else {
                    log("系统应用: $appName")
                }
                log("${it.name} ->  ${it.toJson()}")
            }
            if (!containSystem) {
                list.removeItemIf {
                    //排除系统应用
                    (it.flags and ApplicationInfo.FLAG_SYSTEM) != 0
                }
            }
            return@withContext list
        }

    @SuppressLint("QueryPermissionsNeeded")
    suspend fun queryIntentActivities(containSystem: Boolean = false): MutableList<ResolveInfo> =
        withContext(Dispatchers.IO) {
            val manager = App.context.packageManager
            val queryIntentActivities: MutableList<ResolveInfo>
            val intent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)
            queryIntentActivities = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                App.context.packageManager.queryIntentActivities(intent, PackageManager.MATCH_ALL)
            } else {
                manager.queryIntentActivities(intent, 0)
            }
            queryIntentActivities.map {
                val appName = it.loadLabel(manager)
                val pkgInfo = manager.getPackageInfo(it.activityInfo.packageName, 0)
                if ((pkgInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) == 0) {
                    log("非系统应用：${appName}")
                } else {
                    log("系统应用: $appName")
                }
            }
            if (!containSystem) {
                queryIntentActivities.removeItemIf {
                    val pkgInfo = manager.getPackageInfo(it.activityInfo.packageName, 0)
                    (pkgInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0
                }
            }
            return@withContext queryIntentActivities
        }
}