package app.allever.android.lib.core.function.businessinterceptor.demo

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import app.allever.android.lib.core.R
import app.allever.android.lib.core.function.businessinterceptor.InterceptChain

class BusinessInterceptorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business_interceptor)
        findViewById<View>(R.id.btnChain).setOnClickListener {
            InterceptChain.create()
                .attach(this)
                .addIntercept(FirstIntoInterceptor())
                .addIntercept(SecIntoInterceptor())
                .addIntercept(ThirdInterceptor())
                .build()
                .process()
        }
    }
}