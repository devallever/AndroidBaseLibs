package app.allever.android.lib.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;


public class SearchView extends LinearLayout {

    private SearchListener mListener;
    private LinearLayout mSearchRoot;
    private EditText mEditText;
    private ImageView mIvDelete;
    private ImageView mIvSearch;

    public SearchView(Context context) {
        this(context, null);
    }

    public SearchView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View root = LayoutInflater.from(getContext()).inflate(R.layout.search_view, this);
        mSearchRoot = root.findViewById(R.id.searchRoot);
        mEditText = root.findViewById(R.id.etSearch);
        mIvDelete = root.findViewById(R.id.ivDelete);
        mIvSearch = root.findViewById(R.id.ivSearch);

        mIvDelete.setVisibility(GONE);

        mIvDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditText.setText("");
            }
        });

        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    Editable editable = mEditText.getText();
                    if (editable != null) {
                        String content = editable.toString();
                        hideKeyboard();
                        if (mListener != null) {
                            mListener.onSearch(content);
                        }
                        return true;
                    }
                }

                return false;
            }
        });

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    mIvDelete.setVisibility(GONE);
                } else {
                    mIvDelete.setVisibility(VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void  hideKeyboard() {
        InputMethodManager inputMethodManager =
                (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()) {
            inputMethodManager.hideSoftInputFromWindow(
                    getWindowToken(), 0
            );
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void setBg(Drawable drawable) {
        mSearchRoot.setBackground(drawable);
    }

    public void setHintTextColor(int color) {
        mEditText.setHintTextColor(color);
    }

    public void setTextColor(int color) {
        mEditText.setTextColor(color);
    }

    public void setIconColor(int color) {
        mIvDelete.setColorFilter(color);
        mIvSearch.setColorFilter(color);
    }

    public void setText(String text) {
        mEditText.setText(text);
    }

    public void setSelection(int selection) {
        mEditText.setSelection(selection);
    }

    public void addSearchListener(SearchListener listener) {
        mListener = listener;
    }

    public interface SearchListener {
        void onSearch(String content);
    }
}
