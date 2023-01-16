package app.allever.android.lib.widget.recycler.binding

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

class BaseBindingViewHolder<DB : ViewBinding>(val binding: DB) :
    RecyclerView.ViewHolder(binding.root) {
}
