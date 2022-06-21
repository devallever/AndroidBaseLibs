package app.allever.android.lib.core.function.businessinterceptor.demo

import androidx.appcompat.app.AlertDialog
import app.allever.android.lib.core.function.businessinterceptor.BaseInterceptor
import app.allever.android.lib.core.function.businessinterceptor.InterceptChain
import app.allever.android.lib.core.function.datastore.DataStore

class ThirdInterceptor : BaseInterceptor() {
    private var hasRedPocketMoney = DataStore.getBoolean("hasRedPocketMoney", false)
    override fun intercept(chain: InterceptChain) {
        super.intercept(chain)

        if (!hasRedPocketMoney) {
            doSomeThing(chain)
        } else {
            chain.process()
        }
    }

    private fun doSomeThing(chain: InterceptChain) {
        chain.context?.let { context ->
            AlertDialog.Builder(context)
                .setTitle("领取红包")
                .setCancelable(true)
                .setPositiveButton("领取") { dialog, _ ->
                    DataStore.putBoolean("hasRedPocketMoney", true)
                    dialog.dismiss()
                    chain.process()
                }
                .setOnDismissListener {
                    chain.process()
                }
                .show()
        }
    }
}