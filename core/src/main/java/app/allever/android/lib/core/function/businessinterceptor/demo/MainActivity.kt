package app.allever.android.lib.core.function.businessinterceptor.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import app.allever.android.lib.core.function.businessinterceptor.InterceptChain

class MainActivity : AppCompatActivity() {

    private val testChain by lazy {
        InterceptChain.create()
            .attach(this)
            .addIntercept(FirstIntoIntercept())
            .addIntercept(SecIntoIntercept())
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        testChain.process()
    }
}