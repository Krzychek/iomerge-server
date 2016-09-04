@file:Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")

package com.github.krzychek.iomerge.server.misc

class NotifyingValueHolder<T>(initialVal: T) {

	private val monitor = Object()

	@Volatile var value = initialVal
		set(value) {
			if (value != field) {
				field = value
				synchronized(monitor) { monitor.notifyAll() }
			}
		}

	fun waitToChange(expected: T? = null) {
		synchronized(monitor) {
			do monitor.wait()
			while (expected != null && expected != value)
		}
	}
}