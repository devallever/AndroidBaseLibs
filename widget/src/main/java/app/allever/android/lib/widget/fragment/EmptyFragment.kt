package app.allever.android.lib.widget.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import app.allever.android.lib.widget.R
import app.allever.android.lib.widget.databinding.FragmentEmptyBinding

class EmptyFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<FragmentEmptyBinding>(
            inflater,
            R.layout.fragment_empty,
            container,
            false
        )
        binding.tvText.text = this.hashCode().toString()
        return binding.root
    }
}