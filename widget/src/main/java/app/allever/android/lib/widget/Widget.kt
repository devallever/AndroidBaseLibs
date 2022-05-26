package app.allever.android.lib.widget

import android.annotation.SuppressLint
import android.content.Context
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout

@SuppressLint("StaticFieldLeak")
object Widget {
    lateinit var context: Context
    fun init(context: Context) {
        this.context = context

        //初始化SmartRefreshLayout
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            //全局设置主题颜色
            ClassicsHeader(context)
        }
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
            //指定为经典Footer，默认是 BallPulseFooter
            ClassicsFooter(context).setDrawableSize(20f)
        }

    }
}