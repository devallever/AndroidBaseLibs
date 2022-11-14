package app.allever.android.lib.widget.recycler

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class BaseBindingViewHolder<DB : ViewDataBinding>(val binding: DB) :
    RecyclerView.ViewHolder(binding.root) {
}
