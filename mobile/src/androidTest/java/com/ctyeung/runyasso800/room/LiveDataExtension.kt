package com.ctyeung.runyasso800.room

import androidx.lifecycle.LiveData

/*
 * Reference:
 * https://gist.github.com/Sloy/7a267237f7bc27a2057be744209c1c61
 */
fun <T> LiveData<T>.observeOnce(onChangeHandler: (T) -> Unit) {
    val observer = OneTimeObserver(handler = onChangeHandler)
    observe(observer, observer)
}