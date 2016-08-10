package com.github.krzychek.iomerge.server.utils.plugins

import com.google.common.collect.Iterators
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import java.io.SequenceInputStream
import java.net.URL
import java.util.*


internal class ChainedClassLoader(private val classLoaders: List<ClassLoader>) : ClassLoader() {

	@Throws(ClassNotFoundException::class)
	override fun loadClass(name: String): Class<*> {
		classLoaders.forEach {
			try {
				return it.loadClass(name)
			} catch (ignored: ClassNotFoundException) {
			}
		}

		throw ClassNotFoundException("class not found in any chained classLoaders: " + name)
	}

	override fun getResource(name: String): URL? {
		classLoaders.forEach {
			it.getResource(name)?.let { return it }
		}
		return null
	}

	@Throws(IOException::class)
	override fun getResources(name: String): Enumeration<URL> {
		val result = ArrayList<Iterator<URL>>().apply {
			classLoaders.forEach { add(Iterators.forEnumeration(it.getResources(name))) }
		}

		return Iterators.asEnumeration(Iterators.concat(result.iterator()))
	}

	override fun getResourceAsStream(name: String): InputStream {
		return classLoaders.map { it.getResourceAsStream(name) }
				.filterNotNull()
				.fold(ByteArrayInputStream(ByteArray(0)) as InputStream,
						{ s1, s2 -> SequenceInputStream(s1, s2) })
	}

	override fun setDefaultAssertionStatus(enabled: Boolean)
			= classLoaders.forEach { it.setDefaultAssertionStatus(enabled) }

	override fun setPackageAssertionStatus(packageName: String, enabled: Boolean)
			= classLoaders.forEach { it.setPackageAssertionStatus(packageName, enabled) }

	override fun setClassAssertionStatus(className: String, enabled: Boolean)
			= classLoaders.forEach { it.setClassAssertionStatus(className, enabled) }

	override fun clearAssertionStatus()
			= classLoaders.forEach { it.clearAssertionStatus() }
}
