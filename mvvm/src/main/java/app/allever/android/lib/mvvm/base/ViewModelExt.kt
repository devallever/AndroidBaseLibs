package app.allever.android.lib.mvvm.base

import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider
import kotlin.reflect.KClass

public inline fun <VM : ViewModel> ComponentActivity.viewModel(
    clz: KClass<VM>,
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
): Lazy<VM> {
    val factoryPromise = factoryProducer ?: {
        defaultViewModelProviderFactory
    }

    return ViewModelLazy(clz, { viewModelStore }, factoryPromise)
}

public inline fun <VM : ViewModel> Fragment.viewModel(
    clz: KClass<VM>,
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
): Lazy<VM> {
    val factoryPromise = factoryProducer ?: {
        defaultViewModelProviderFactory
    }

    return ViewModelLazy(clz, { viewModelStore }, factoryPromise)
}