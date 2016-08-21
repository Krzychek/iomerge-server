package com.github.krzychek.iomerge.server.utils

import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock


class ReentrantLockCondition(private val lock: ReentrantLock = ReentrantLock(),
							 private val condition: Condition = lock.newCondition())
: Condition by condition, Lock by lock {

	fun awaitLocked() {
		try {
			lock.lock()
			condition.await()
		} finally {
			lock.unlock()
		}
	}
}