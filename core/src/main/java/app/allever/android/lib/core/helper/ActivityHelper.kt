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
import app.allever.android.lib.core.log
import java.lang.ref.WeakReference
import java.util.*

/**
 * 应用中所有Activity的管理器，可用于一键杀死所有Activity。
 *
 * @author guolin`
 * @since 18/2/8
 */
object ActivityHelper {

    private val activityList = ArrayList<WeakReference<Activity>?>()

    fun size(): Int {
        return activityList.size
    }

    fun add(weakRefActivity: WeakReference<Activity>?) {
        activityList.add(weakRefActivity)
    }

    fun remove(weakRefActivity: WeakReference<Activity>?) {
        val result = activityList.remove(weakRefActivity)
        log("remove activity reference $result")
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

    fun <T : Activity> startActivity(clazz: Class<T>) {
        val context = getTopActivity()
        val intent = Intent(context, clazz)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context?.startActivity(intent)
    }

    inline fun <reified T> startActivity(block: Intent.() -> Unit) {
        val context = getTopActivity()
        val intent = Intent(context, T::class.java)
        intent.block()
        context?.startActivity(intent)
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
        if (activityList.isNotEmpty()) {
            return activityList[activityList.size - 1]?.get()
        }
        return null
    }
}
