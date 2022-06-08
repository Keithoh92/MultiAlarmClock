package com.example.multialarmclock.feature.base.liveData

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

/**
 * A variation of the MutableLiveData, used for events.
 * The observer of this LiveData will be updated only once, after a setValue() is called.
 *
 * Use this for:
 * - Open dialog, toast, snackBar
 * - Navigate
 *
 * With MutableLiveData, every time there is a configuration change or the view is re-built,
 * every observer will be called with the last "value".
 * This will lead, for instance, to show all the dialog previously shown.
 *
 * SingleLiveEvent instead, will take care of notifying the observer only once.
 *
 *
 * Note: that only one observer is going to be notified of changes.
 */
class SingleLiveEvent<T> : MutableLiveData<T>() {

    private val mPending = AtomicBoolean(false)

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {

        if (hasActiveObservers()) {
            throw IllegalStateException("Multiple observers registered but only one will be notified of changes.")
        }

        // Observe the internal MutableLiveData
        super.observe(owner, Observer { t ->
            if (mPending.compareAndSet(true, false)) {
                observer.onChanged(t)
            }
        })
    }

    @MainThread
    @Deprecated("Use call() for consistency")
    override fun setValue(t: T?) {
        mPending.set(true)
        super.setValue(t)
    }

    @Deprecated("Use call() for consistency")
    override fun postValue(value: T?) {
        mPending.set(true)
        super.postValue(value)
    }

    /**
     * Posted from the main thread instantly
     */
    @MainThread
    fun call() {
        value = null
    }

    @MainThread
    fun call(t: T?) {
        value = t
    }

    /**
     * Post to the main thread from another thread (Not instant)
     */
    fun postCall() {
        postValue(null)
    }

    fun postCall(t: T?) {
        postValue(t)
    }
}