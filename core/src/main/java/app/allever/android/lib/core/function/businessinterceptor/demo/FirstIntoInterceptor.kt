package app.allever.android.lib.core.function.businessinterceptor.demo

import androidx.appcompat.app.AlertDialog
import app.allever.android.lib.core.function.businessinterceptor.BaseInterceptor
import app.allever.android.lib.core.function.businessinterceptor.InterceptChain
import app.allever.android.lib.core.function.datastore.DataStore
import app.allever.android.lib.core.function.datastore.PreferenceStore
import app.allever.android.lib.core.helper.CoroutineHelper
import kotlinx.coroutines.launch

class FirstIntoInterceptor : BaseInterceptor() {

    private var isFirstInto: Boolean = true

    override fun intercept(chain: InterceptChain) {
        super.intercept(chain)
        CoroutineHelper.MAIN.launch {
            isFirstInto = PreferenceStore.getBoolean("agree", false) //是否第一次登录
            if (!isFirstInto) {
                doSomeThing() //做任何事，耗时，渲染（持有Context）
            } else {
                chain.process() //释放（通往下一个拦截器若存在）
            }
        }
    }

    private fun doSomeThing() {
        mChain?.let { chain ->
            chain.context?.let { weakReference ->
                AlertDialog.Builder(weakReference.get() ?: return)
                    .setTitle("用户协议和隐私政策")
                    .setCancelable(true)
                    .setPositiveButton("同意") { p0, _ ->
                        CoroutineHelper.MAIN.launch {
                            DataStore.putBoolean("agree", true)
                            p0.dismiss()
                            chain.process()
                        }
                    }
                    .setOnDismissListener {
                        chain.process()
                    }
                    .show()
            }
        }
    }

}