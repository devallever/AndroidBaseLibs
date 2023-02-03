package app.allever.android.lib.core.function.businessinterceptor.demo

import androidx.appcompat.app.AlertDialog
import app.allever.android.lib.core.function.businessinterceptor.BaseInterceptor
import app.allever.android.lib.core.function.businessinterceptor.InterceptChain
import app.allever.android.lib.core.function.datastore.DataStore
import app.allever.android.lib.core.helper.CoroutineHelper
import kotlinx.coroutines.launch

class ThirdInterceptor : BaseInterceptor() {
    private var hasRedPocketMoney = false
    override fun intercept(chain: InterceptChain) {
        super.intercept(chain)
        CoroutineHelper.MAIN.launch {
            hasRedPocketMoney = DataStore.getBoolean("hasRedPocketMoney", false)
            if (!hasRedPocketMoney) {
                doSomeThing(chain)
            } else {
                chain.process()
            }
        }
    }

    private fun doSomeThing(chain: InterceptChain) {
        chain.context?.let { weakReference ->
            AlertDialog.Builder(weakReference.get() ?: return)
                .setTitle("领取红包")
                .setCancelable(true)
                .setPositiveButton("领取") { dialog, _ ->
                    CoroutineHelper.MAIN.launch {
                        DataStore.putBoolean("hasRedPocketMoney", true)
                        dialog.dismiss()
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