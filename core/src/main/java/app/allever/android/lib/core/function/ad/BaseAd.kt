package app.allever.android.lib.core.function.ad

import android.view.ViewGroup

@Deprecated("")
abstract class BaseAd {

    abstract fun load(adPosition: String, container: ViewGroup?, adListener: AdListener?)

    abstract fun show()

    abstract fun loadAndShow(adPosition: String, container: ViewGroup?, adListener: AdListener?)

    abstract fun destroy()
}