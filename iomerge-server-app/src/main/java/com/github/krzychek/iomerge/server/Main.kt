@file:JvmName("Main")

package com.github.krzychek.iomerge.server

import com.github.krzychek.iomerge.server.config.AppConfigurator
import com.github.krzychek.iomerge.server.dagger.DaggerMainComponent
import com.github.krzychek.iomerge.server.dagger.MainComponent


fun main(vararg args: String) {
	val appConfiguration = AppConfigurator(*args).createConfig()

	val mainComponent: MainComponent = DaggerMainComponent.builder()
			.appConfiguration(appConfiguration)
			.build()

	mainComponent.init()
}


