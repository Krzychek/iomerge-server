package com.github.krzychek.iomerge.server.plugins

import com.github.krzychek.iomerge.server.api.PluginProperties.PLUGIN_CLASSES_PROP
import org.pmw.tinylog.Logger
import java.io.File
import java.net.URLClassLoader
import java.util.*

class PluginClassLoader(pluginJar: File, parent: ClassLoader = Thread.currentThread().contextClassLoader)
: URLClassLoader(arrayOf(pluginJar.toURI().toURL()), parent) {

	fun loadPluginClasses(): List<Class<*>>
			= getPluginClassNames().map { loadClass(it) }

	private fun getPluginClassNames(): List<String> {
		val classNames: String? = getPluginClassesProperty()

		return when {
			classNames != null && classNames.isNotBlank() -> classNames.split(",").filter { !it.isBlank() }
			else -> {
				Logger.error("Plugin mandatory property: '$PLUGIN_CLASSES_PROP' not found")
				emptyList()
			}
		}
	}

	private fun getPluginClassesProperty(): String? {
		val properties = Properties()
		this.getResourceAsStream("iomerge-plugin.properties").use {
			properties.load(it)
		}

		return properties.getProperty(PLUGIN_CLASSES_PROP)
	}
}