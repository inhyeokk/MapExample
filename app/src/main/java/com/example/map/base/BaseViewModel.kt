package com.example.map.base

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable

open class BaseViewModel : ViewModel() {
    protected val compositeDisposable = CompositeDisposable()
    fun onDestroy() {
        compositeDisposable.clear()
    }
}
