package com.github.krzychek.iomerge.server.dagger

import com.github.krzychek.iomerge.server.appState.AppStateHolder
import com.github.krzychek.iomerge.server.config.AppConfiguration
import org.annoprops.PropertyManager
import org.pmw.tinylog.Logger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton class LifecycleManager
@Inject constructor(private val appStateHolder: AppStateHolder,
					private val propertyManager: PropertyManager,
					private val appConfiguration: AppConfiguration) {

	private var initialized = false
	private var shuttingdown = false

	fun init() {
		if (initialized) {
			Logger.warn("App was already initialized")
			return
		}
		appConfiguration.startupCallback()

		propertyManager.readPropertiesFromFile(appConfiguration.settingsFile)

		appStateHolder.start()

		Runtime.getRuntime().addShutdownHook(Thread { shutdown(exitSystem = false) })

		initialized = true
	}

	fun shutdown(exitSystem: Boolean = true) {
		if (!shuttingdown) {
			propertyManager.savePropertiesToFile(appConfiguration.settingsFile)

			appStateHolder.shutdown()

			appConfiguration.shutdownCallback()

			// TODO show non daemon thread warning
			if (exitSystem) System.exit(0)
		}
	}
}