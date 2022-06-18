package app.allever.android.lib.core.function.businessinterceptor.demo

import androidx.appcompat.app.AlertDialog
import app.allever.android.lib.core.function.businessinterceptor.BaseInterceptImpl
import app.allever.android.lib.core.function.businessinterceptor.InterceptChain

class SecIntoIntercept : BaseInterceptImpl() {

    private var isSec = false  //是否有权限

    override fun intercept(chain: InterceptChain) {
        super.intercept(chain)

        if (!isSec) {
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
                    .setCancelable(false)
                    .setPositiveButton("关闭") { p0, _ ->
                        p0.dismiss()
                        chain.process()
                    }
                    .show()
            }
        }
    }

}