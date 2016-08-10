package com.github.krzychek.iomerge.server.utils

import com.github.krzychek.iomerge.server.api.Chainable

import java.lang.reflect.Proxy


object ChainHelper {

	@Suppress("UNCHECKED_CAST")
	private fun <T> getNOOPChainLink(clazz: Class<T>): T {
		return Proxy.newProxyInstance(clazz.classLoader, arrayOf(clazz), { proxy, method, args -> null }) as T
	}

	fun <T : Chainable<T>> createChain(chain: List<T>, clazz: Class<T>): T {
		return chain.foldRight(getNOOPChainLink(clazz),
				{ listener1, listener2 ->
					listener1.chain(listener2)
					listener1
				})
	}
}
