package com.github.krzychek.iomerge.server.utils.plugins

import com.github.krzychek.iomerge.server.api.Chainable
import org.springframework.core.annotation.AnnotationAwareOrderComparator
import java.lang.reflect.Proxy


internal fun <T : Chainable<T>> List<T>.createChainOfType(clazz: Class<T>): T {
	return sortedWith(AnnotationAwareOrderComparator.INSTANCE)
			.foldRight(clazz.NOOPProxy()) {
				next, previous ->
				next.chain(previous)
				next
			}
}

@Suppress("UNCHECKED_CAST")
private fun <T> Class<T>.NOOPProxy()
		= Proxy.newProxyInstance(classLoader, arrayOf(this), { a, b, c -> null }) as T

