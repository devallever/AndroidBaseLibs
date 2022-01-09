package app.allever.android.lib.widget.ripple

import android.graphics.Color
import android.view.View

object RippleHelper {
    fun addRippleView(view: View) {
        RippleLayout.on(view).rippleAlpha(0.1f).rippleColor(Color.parseColor("#333333")).create()
    }
}