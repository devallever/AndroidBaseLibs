package app.allever.android.lib.core.function.businessinterceptor.demo

import androidx.appcompat.app.AlertDialog
import app.allever.android.lib.core.function.businessinterceptor.BaseInterceptor
import app.allever.android.lib.core.function.businessinterceptor.InterceptChain

class SecIntoInterceptor : BaseInterceptor() {

    private var hasPermission = false  //是否有权限

    override fun intercept(chain: InterceptChain) {
        super.intercept(chain)

        if (!hasPermission) {
            doSomeThing()
        } else {
            chain.process()
        }
    }

    private fun doSomeThing() {
        mChain?.let { chain ->
            chain.context?.let { context ->
                AlertDialog.Builder(context)
                    .setTitle("权限申请")
                    .setCancelable(true)
                    .setPositiveButton("关闭") { p0, _ ->
                        p0.dismiss()
                    }
                    .setOnDismissListener {
                        chain.process()
                    }
                    .show()
            }
        }
    }

}