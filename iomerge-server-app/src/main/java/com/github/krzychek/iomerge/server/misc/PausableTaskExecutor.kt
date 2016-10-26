package com.github.krzychek.iomerge.server.misc

import java.util.*
import java.util.concurrent.*

class PausableTaskExecutor(val scheduledExecutorService: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()) {

	private val taskList = IdentityHashMap<() -> Future<*>, Future<*>?>()

	fun scheduleAtFixedRate(command: () -> Unit, period: Long, unit: TimeUnit, addPaused: Boolean = false): ScheduledFuture<*>? {
		val createFun = { -> scheduledExecutorService.scheduleAtFixedRate(command, 0, period, unit) }
		val taskFuture = if (addPaused) null else createFun()

		taskList.put(createFun, taskFuture)
		return taskFuture
	}

	fun scheduleWithFixedDelay(task: () -> Unit, delay: Long, unit: TimeUnit, addPaused: Boolean = false): ScheduledFuture<*>? {
		val createFun = { -> scheduledExecutorService.scheduleWithFixedDelay(task, 0, delay, unit) }
		val taskFuture = if (addPaused) null else createFun()

		taskList.put(createFun, taskFuture)
		return taskFuture
	}

	fun pause() {
		taskList.forEach { function, future -> future?.cancel(false) }
		taskList.replaceAll { key, value -> null }
	}

	fun resume() {
		taskList.replaceAll { key, value -> value ?: key() }
	}
}