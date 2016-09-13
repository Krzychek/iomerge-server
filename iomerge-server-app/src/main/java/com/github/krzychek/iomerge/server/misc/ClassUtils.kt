package com.github.krzychek.iomerge.server.misc

import java.lang.reflect.Proxy


@Suppress("UNCHECKED_CAST")
internal fun <T> Class<T>.NOOPProxy()
		= Proxy.newProxyInstance(classLoader, arrayOf(this), { a, b, c -> null }) as T


@Suppress("UNCHECKED_CAST")
internal fun <T> Class<T>.notImplementedProxy()
		= Proxy.newProxyInstance(classLoader, arrayOf(this), { a, b, c -> throw UnsupportedOperationException("not implemented") }) as T