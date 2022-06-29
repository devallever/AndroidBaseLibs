package app.allever.android.lib.core.function.businessinterceptor

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import java.lang.ref.WeakReference
import java.util.concurrent.atomic.AtomicBoolean

//todo ArrayList -> Node
class InterceptChain(
    var context: WeakReference<Context>?,
    private var interceptors: ArrayList<Interceptor>,
) {

    companion object {

        fun create(): Builder {
            return Builder()
        }
    }

    private var index = 0

    var isRunning = AtomicBoolean(false)

    fun process() {
        isRunning.set(true)
        when (index) {
            in interceptors.indices -> {
                val interceptor = interceptors[index]
                index++
                interceptor.intercept(this@InterceptChain)
            }
            interceptors.size -> {
                clearAll()
            }
        }
    }

    private fun clearAll() {
        index = 0
        interceptors.clear()
        context = null
        isRunning.set(false)
    }

    open class Builder {
        private var context: Context? = null
        private val interceptors = ArrayList<Interceptor>()

        fun build(): InterceptChain {
            return InterceptChain(WeakReference(context), interceptors)
        }

        fun addIntercept(interceptor: Interceptor): Builder {
            interceptors.add(interceptor)
            return this
        }

        fun addIntercept(interceptors: List<Interceptor>): Builder {
            this.interceptors.addAll(interceptors)
            return this
        }

        fun attach(view: View): Builder {
            this.context = view.context
            return this
        }

        fun attach(fragment: Fragment): Builder {
            this.context = fragment.activity
            return this
        }

        fun attach(activity: Activity): Builder {
            this.context = activity
            return this
        }
    }
}