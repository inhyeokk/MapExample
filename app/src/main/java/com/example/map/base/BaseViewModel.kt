package com.example.map.base;

import androidx.lifecycle.ViewModel;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class BaseViewModel extends ViewModel {
    protected final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public void onDestroy() {
        compositeDisposable.clear();
    }
}
