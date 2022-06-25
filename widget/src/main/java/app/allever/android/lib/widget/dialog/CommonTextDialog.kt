package app.allever.android.lib.widget.dialog

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.widget.TextView
import app.allever.android.lib.core.base.dialog.AbstractCenterDialog
import app.allever.android.lib.widget.R

class CommonTextDialog(
    context: Context
) : AbstractCenterDialog(context) {

    private var leftTask: Runnable? = null
    private var rightTask: Runnable? = null
    private var title: String? = null
    private var message: String? = null
    private var leftText: String? = null
    private var rightText: String? = null

    private var tvTitle: TextView? = null
    private var tvMessage: TextView? = null
    private var tvLeft: TextView? = null
    private var tvRight: TextView? = null


    override fun getGravity() = Gravity.CENTER

    override fun getLayoutId() = R.layout.dialog_common_text

    override fun initView() {
         tvTitle = findViewById<TextView>(R.id.tvTitle)
         tvMessage = findViewById<TextView>(R.id.tvMessage)
         tvLeft = findViewById<TextView>(R.id.tvCancel)
         tvRight = findViewById<TextView>(R.id.tvConfirm)
        title?.let {
            tvTitle?.text = it
        }

        message?.let {
            tvMessage?.text = it
        }

        leftText?.let {
            tvLeft?.text = it
        }

        rightText?.let {
            tvRight?.text = it
        }

        tvLeft?.setOnClickListener {
            leftTask?.run()
            dismiss()
        }

        tvRight?.setOnClickListener {
            rightTask?.run()
            dismiss()
        }
    }

    fun title(title: String): Dialog {
        this.title = title
        tvTitle?.text = title
        return this
    }

    fun message(msg: String): Dialog {
        this.message = msg
        tvMessage?.text = msg
        return this
    }

    fun leftText(text: String): Dialog {
        this.leftText = text
        tvLeft?.text = text
        return this
    }

    fun rightText(text: String): Dialog {
        this.rightText = text
        tvRight?.text = text
        return this
    }

    fun clickLeft(task: Runnable): Dialog {
        this.leftTask = task
        return this
    }

    fun clickRight(task: Runnable): Dialog {
        this.rightTask = task
        return this
    }
}