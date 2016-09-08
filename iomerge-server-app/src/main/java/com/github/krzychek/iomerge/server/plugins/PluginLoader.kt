package com.github.krzychek.iomerge.server.plugins

import com.github.krzychek.iomerge.server.api.appState.AppStateManager
import com.github.krzychek.iomerge.server.api.network.MessageDispatcher
import com.github.krzychek.iomerge.server.config.AppConfiguration
import com.github.krzychek.iomerge.server.network.MessageDispatcherImpl
import com.google.common.eventbus.EventBus
import org.pmw.tinylog.Logger
import java.io.File
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

	private val pluginJars = appConfiguration.pluginsDir
			.listFiles { file, name -> file.isFile && name.endsWith(".jar") }
			?.sortedBy { it.name } ?: emptyList()

	private val pluginObjects = pluginJars
			.flatMap { it.loadPluginClasses() }
			.map { getObjectOfType(it) }

	/**
	 * creates (or gets from cache, if already created) plugin objects of particular supertype
	 */
	fun <T> getPluginObjectsOfType(type: Class<T>): List<T>
			= pluginObjects.filterIsInstance(type)


	/**
	 * creates objects of particular class or gets it from cache
	 *
	 * @param clazz type of object to get
	 */
	private fun <T> getObjectOfType(clazz: Class<T>): T = clazz.cast(
			clazz.constructors.maxBy { it.parameterCount }!!.run {
				val parameters = parameterTypes.map { injectableObjects[it] }.toTypedArray()
				newInstance(*parameters).apply { eventBus.register(this) }
			}
	)

	/**
	 * @return stream of configuration classes in given plugin jar file
	 */
	private fun File.loadPluginClasses(): List<Class<*>> = try {
		PluginClassLoader(this).loadPluginClasses()
	} catch (ex: Exception) {
		Logger.error(ex, "Problem while loading plugin $this")
		emptyList()
	}
}
