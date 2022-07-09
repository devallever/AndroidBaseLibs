/*
 * Copyright (C) guolin, Suzhou Quxiang Inc. Open source codes for study only.
 * Do not use for commercial purpose.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package app.allever.android.lib.core.helper

import android.app.Activity
import android.content.Intent
import app.allever.android.lib.core.ext.log
import java.lang.ref.WeakReference
import java.util.*

/**
 * 应用中所有Activity的管理器，可用于一键杀死所有Activity。
 *
 * @author guolin`
 * @since 18/2/8
 */
object ActivityHelper {

    private val activityList = LinkedList<WeakReference<Activity>?>()
    private var weakReference: WeakReference<Activity>? = null

    fun size(): Int {
        return activityList.size
    }

    fun add(weakRefActivity: WeakReference<Activity>?) {
        activityList.add(weakRefActivity)
    }

    fun resumeTop(weakRefActivity: WeakReference<Activity>?) {
        val index = activityList.indexOf(weakRefActivity)
        activityList.add(activityList.removeAt(index))
    }

    fun remove(weakRefActivity: WeakReference<Activity>?) {
        val result = activityList.remove(weakRefActivity)
        log("remove activity reference $result")
    }

    @Deprecated("")
    fun setCurrent(activity: Activity) {
        weakReference = WeakReference(activity)
    }

    fun finishAll() {
        if (activityList.isNotEmpty()) {
            for (activityWeakReference in activityList) {
                val activity = activityWeakReference?.get()
                if (activity != null && !activity.isFinishing) {
                    activity.finish()
                }
            }
            activityList.clear()
        }
    }

    fun startActivity(clazz: Class<*>, context: Activity? = null, block: Intent.() -> Unit = {}) {
        val ctx = context ?: getTopActivity()
        val intent = Intent(ctx, clazz)
        intent.block()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        ctx?.startActivity(intent)
    }

    inline fun <reified T> startActivity(context: Activity? = null, block: Intent.() -> Unit = {}) {
        val ctx = context ?: getTopActivity()
        val intent = Intent(ctx, T::class.java)
        intent.block()
        ctx?.startActivity(intent)
    }

    fun <T : Activity> startActivityForResult(
        context: Activity,
        clazz: Class<T>,
        requestCode: Int
    ) {
        val intent = Intent(context, clazz)
        context.startActivityForResult(intent, requestCode)
    }

    fun getTopActivity(): Activity? {
        return weakReference?.get() ?: if (activityList.isNotEmpty()) {
            return activityList[activityList.size - 1]?.get()
        } else {
            null
        }
    }

    fun getActivity(clazz: Class<*>): Activity? {
        if (activityList.isNotEmpty()) {
            for (activityWeakReference in activityList) {
                val activity = activityWeakReference?.get()
                if (activity?.javaClass == clazz) {
                    return activity
                }
            }
        }
        return null
    }


    /**
     * 是否存在Activity
     */
    fun contain(clazz: Class<*>): Boolean {
        return getActivity(clazz) != null
    }

    fun contain(clzSimpleName: String): Boolean {
        if (activityList.isNotEmpty()) {
            for (activityWeakReference in activityList) {
                val activity = activityWeakReference?.get()
                if (activity != null && activity.javaClass.simpleName == clzSimpleName) {
                    return true
                }
            }
        }
        return false
    }

}
