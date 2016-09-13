package com.github.krzychek.iomerge.server.plugins

import com.github.krzychek.iomerge.server.api.Chainable
import com.github.krzychek.iomerge.server.api.Order
import com.github.krzychek.iomerge.server.misc.NOOPProxy


internal fun <T : Chainable<T>> List<T>.convertToChainOfType(clazz: Class<T>): T
		= this.sortedBy { it.order }
		.foldRight(clazz.NOOPProxy()) {
			next, previous ->
			next.chain(previous)
			next
		}

private val Any.order: Int
	get() = this.javaClass.getAnnotation(Order::class.java)?.value
			?: Integer.MAX_VALUE
