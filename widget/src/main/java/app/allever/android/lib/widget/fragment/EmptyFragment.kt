package app.allever.android.lib.widget.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.allever.android.lib.widget.databinding.FragmentEmptyBinding

class EmptyFragment(val content: String = "") : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentEmptyBinding.inflate(layoutInflater)
        binding.tvText.text = content
        return binding.root
    }
}