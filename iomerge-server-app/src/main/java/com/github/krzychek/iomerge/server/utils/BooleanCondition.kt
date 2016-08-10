package com.github.krzychek.iomerge.server.utils

import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.ReentrantLock


class BooleanCondition {

	private val lock: ReentrantLock = ReentrantLock()
	private val internal: Condition = lock.newCondition()

	@Volatile var isTrue: Boolean = false
		set(value) {
			try {
				lock.lock()
				field = value
				internal.signal()

			} finally {
				lock.unlock()
			}
		}


	fun await() {
		try {
			lock.lock()
			internal.awaitUninterruptibly()
		} finally {
			lock.unlock()
		}
	}
}
