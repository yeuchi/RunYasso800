package com.ctyeung.runyasso800.room

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer

/*
 * Reference:
 * https://alediaferia.com/2018/12/17/testing-livedata-room-android/
 */
class OneTimeObserver<T>(private val handler: (T) -> Unit) : Observer<T>, LifecycleOwner {
    private val lifecycle = LifecycleRegistry(this)
    init {
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
    }

    override fun getLifecycle(): Lifecycle = lifecycle

    override fun onChanged(t: T) {
        handler(t)
        //lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    }
}