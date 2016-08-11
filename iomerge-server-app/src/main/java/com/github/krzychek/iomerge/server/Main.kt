package com.github.krzychek.iomerge.server

import com.github.krzychek.iomerge.server.config.AppConfigurator
import com.github.krzychek.iomerge.server.utils.plugins.PluginLoader
import org.springframework.context.annotation.AnnotationConfigApplicationContext


object Main {

	@JvmStatic fun main(args: Array<String>) {
		AppConfigurator(*args).apply { configureBootstrap() }

		AnnotationConfigApplicationContext().apply {

			PluginLoader().loadPluginsToContext(this)
			register(SpringConfig::class.java) // registered after plugins to avoid bean overloading
			registerShutdownHook()

		}.refresh()
	}
}
