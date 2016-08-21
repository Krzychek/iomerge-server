package com.github.krzychek.iomerge.server.utils

class ThrottledCall<T : Any>(private val threadsholdMilis: Long, private val call: () -> T) {

	var nextSystemCall = System.currentTimeMillis()
	lateinit var result: T

	operator fun invoke(): T {
		val currentTime = System.currentTimeMillis()

		if (currentTime >= nextSystemCall) {
			result = call()
			nextSystemCall = System.currentTimeMillis() + threadsholdMilis
		} else {
			Thread.sleep(nextSystemCall - currentTime)
		}

		return result
	}
}
