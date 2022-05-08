package app.allever.android.lib.mvvm.base

import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider
import kotlin.reflect.KClass

/**
 * 这样写也会异常
 *
 */
public inline fun <VM : ViewModel> ComponentActivity.viewModel(
    clz: KClass<VM>,
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
): Lazy<VM> {
    val factoryPromise = factoryProducer ?: {
        defaultViewModelProviderFactory
    }

    return ViewModelLazy(clz, { viewModelStore }, factoryPromise)
}

/***
 * 这样写也会异常
 * if (mFragmentManager == null) {
 *      throw new IllegalStateException("Can't access ViewModels from detached fragment");
 * }
 */
public inline fun <VM : ViewModel> Fragment.viewModel(
    clz: KClass<VM>,
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
): Lazy<VM> {
    val factoryPromise = factoryProducer ?: {
        defaultViewModelProviderFactory
    }

    return ViewModelLazy(clz, { viewModelStore }, factoryPromise)
}