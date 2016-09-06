package com.github.krzychek.iomerge.server.dagger

import dagger.Lazy
import kotlin.reflect.KProperty


operator fun <T> Lazy<T>.getValue(any: Any, property: KProperty<*>): T = get()