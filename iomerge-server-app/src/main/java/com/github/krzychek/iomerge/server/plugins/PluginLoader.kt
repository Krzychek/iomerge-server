package com.github.krzychek.iomerge.server.plugins

import com.github.krzychek.iomerge.server.api.PluginProperties.PLUGIN_CLASSES_PROP
import com.github.krzychek.iomerge.server.api.appState.AppStateManager
import com.github.krzychek.iomerge.server.api.network.MessageDispatcher
import com.github.krzychek.iomerge.server.config.AppConfiguration
import com.github.krzychek.iomerge.server.network.MessageDispatcherImpl
import org.pmw.tinylog.Logger
import java.io.File
import java.net.URLClassLoader
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Component responsible for loading plugins
 */
@Singleton class PluginLoader
@Inject constructor(appStateManager: AppStateManager, messageDispatcher: MessageDispatcherImpl, appConfiguration: AppConfiguration) {

	private val injectableObjects = mapOf(
			AppStateManager::class.java to appStateManager,
			MessageDispatcher::class.java to messageDispatcher
	)

	private val pluginJars by lazy {
		appConfiguration.pluginsDir
				.listFiles { file, name -> file.isFile && name.endsWith(".jar") }
				?.sortedBy { it.name } ?: emptyList()
	}

	private val pluginClasses by lazy { pluginJars.flatMap { loadPluginClasses(it) } }

	private val classToObject = WeakHashMap<Class<*>, Any>()

	/**
	 * create (or gets from cache, if already created) plugin objects of particular supertype
	 */
	fun <T> getPluginObjectsOfType(type: Class<T>): List<T>
			= pluginClasses
			.filter { type.isAssignableFrom(it) }
			.map { @Suppress("UNCHECKED_CAST") (it as Class<out T>) }
			.map { getObjectOfType(it) }


	/**
	 * creates objects of particular class or gets it from cache
	 *
	 * @param clazz type of object to get
	 */
	private fun <T> getObjectOfType(clazz: Class<T>): T = clazz.cast(
			classToObject[clazz] ?:
					clazz.constructors.maxBy { it.parameterCount }!!.run {
						val parameters = parameterTypes.map { injectableObjects[it] }.toTypedArray()
						newInstance(*parameters)
					}
	)


	/**
	 * @return stream of configuration classes in given plugin jar file
	 */
	private fun loadPluginClasses(pluginJar: File): List<Class<*>> {
		return try {
			val pluginClassloader = createPluginClassloader(pluginJar)
			pluginClassloader.getPluginClassNames()
					.map { pluginClassloader.loadClass(it) }

		} catch (ex: Exception) {
			Logger.warn(ex, "Problem while loading plugin $pluginJar")
			emptyList()
		}
	}

	/**
	 * creates class loader for plugin
	 *
	 * @param pluginJar jar file of the plugin
	 */
	private fun createPluginClassloader(pluginJar: File) =
			object : URLClassLoader(arrayOf(pluginJar.toURI().toURL()), Thread.currentThread().contextClassLoader) {

				fun getPluginClassNames(): List<String> {
					val properties = Properties()
					this.getResourceAsStream("iomerge-plugin.properties").use {
						properties.load(it)
					}

					val classNames: String? = properties.getProperty(PLUGIN_CLASSES_PROP)

					return when {
						classNames != null && classNames.isNotBlank() -> classNames.split(",").filter { !it.isBlank() }
						else -> {
							Logger.error("Plugin mandatory property: '$PLUGIN_CLASSES_PROP' not found")
							emptyList()
						}
					}
				}
			}
}
