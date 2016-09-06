package com.github.krzychek.iomerge.server.plugins

import com.github.krzychek.iomerge.server.api.appState.AppStateManager
import com.github.krzychek.iomerge.server.api.network.MessageDispatcher
import com.github.krzychek.iomerge.server.config.AppConfiguration
import com.github.krzychek.iomerge.server.network.MessageDispatcherImpl
import com.google.common.eventbus.EventBus
import org.pmw.tinylog.Logger
import java.io.File
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Component responsible for loading plugins
 */
@Singleton class PluginLoader
@Inject constructor(private val eventBus: EventBus,
					appConfiguration: AppConfiguration,
					appStateManager: AppStateManager,
					messageDispatcher: MessageDispatcherImpl) {

	private val injectableObjects = mapOf(
			AppStateManager::class.java to appStateManager,
			MessageDispatcher::class.java to messageDispatcher,
			EventBus::class.java to eventBus
	)

	private val pluginJars by lazy {
		appConfiguration.pluginsDir
				.listFiles { file, name -> file.isFile && name.endsWith(".jar") }
				?.sortedBy { it.name } ?: emptyList()
	}

	private val pluginClasses by lazy { pluginJars.flatMap { loadPluginClasses(it) } }

	private val classToObject = WeakHashMap<Class<*>, Any>()

	/**
	 * creates (or gets from cache, if already created) plugin objects of particular supertype
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
						newInstance(*parameters).apply { eventBus.register(this) }
					}
	)


	/**
	 * @return stream of configuration classes in given plugin jar file
	 */
	private fun loadPluginClasses(pluginJar: File): List<Class<*>> = try {
		PluginClassLoader(pluginJar).loadPluginClasses()
	} catch (ex: Exception) {
		Logger.error(ex, "Problem while loading plugin $pluginJar")
		emptyList()
	}
}