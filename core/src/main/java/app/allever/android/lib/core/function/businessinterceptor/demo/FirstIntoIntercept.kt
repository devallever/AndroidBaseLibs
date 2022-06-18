package app.allever.android.lib.core.function.businessinterceptor.demo

import androidx.appcompat.app.AlertDialog
import app.allever.android.lib.core.function.businessinterceptor.BaseInterceptImpl
import app.allever.android.lib.core.function.businessinterceptor.InterceptChain

class FirstIntoIntercept : BaseInterceptImpl() {

    private var isFirstInto = false //是否第一次登录

    override fun intercept(chain: InterceptChain) {
        super.intercept(chain)
        if (!isFirstInto) {
            doSomeThing() //做任何事，耗时，渲染（持有Context）
        } else {
            chain.process() //释放（通往下一个拦截器若存在）
        }
    }

    private fun doSomeThing() {
        mChain?.let { chain ->
            chain.context?.let { context ->
                AlertDialog.Builder(context)
                    .setTitle("用户协议")
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