package app.allever.android.lib.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView.OnEditorActionListener
import androidx.annotation.RequiresApi

class SearchView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private var mListener: SearchListener? = null
    private var mSearchRoot: LinearLayout? = null
    private var mEditText: EditText? = null
    private var mIvDelete: ImageView? = null
    private var mIvSearch: ImageView? = null
    private fun init() {
        val root = LayoutInflater.from(context).inflate(R.layout.search_view, this)
        mSearchRoot = root.findViewById(R.id.searchRoot)
        mEditText = root.findViewById(R.id.etSearch)
        mIvDelete = root.findViewById(R.id.ivDelete)
        mIvSearch = root.findViewById(R.id.ivSearch)
        mIvDelete?.visibility = GONE
        mIvDelete?.setOnClickListener(OnClickListener { mEditText?.setText("") })
        mEditText?.setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val editable = mEditText?.text
                if (editable != null) {
                    val content = editable.toString()
                    hideKeyboard()
                    mListener?.onSearch(content)
                    return@OnEditorActionListener true
                }
            }
            false
        })
        mEditText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isEmpty()) {
                    mIvDelete?.visibility = GONE
                } else {
                    mIvDelete?.visibility = VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }

    fun hideKeyboard() {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (inputMethodManager.isActive) {
            inputMethodManager.hideSoftInputFromWindow(
                windowToken, 0
            )
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    fun setBg(drawable: Drawable?) {
        mSearchRoot?.background = drawable
    }

    fun setHintTextColor(color: Int) {
        mEditText?.setHintTextColor(color)
    }

    fun setTextColor(color: Int) {
        mEditText?.setTextColor(color)
    }

    fun setIconColor(color: Int) {
        mIvDelete?.setColorFilter(color)
        mIvSearch?.setColorFilter(color)
    }

    fun setText(text: String?) {
        mEditText?.setText(text)
    }

    fun setSelection(selection: Int) {
        mEditText?.setSelection(selection)
    }

    fun addSearchListener(listener: SearchListener?) {
        mListener = listener
    }

    interface SearchListener {
        fun onSearch(content: String?)
    }

    init {
        init()
    }
}