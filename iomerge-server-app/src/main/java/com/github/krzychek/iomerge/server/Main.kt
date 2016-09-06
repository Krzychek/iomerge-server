@file:JvmName("Main")

package com.github.krzychek.iomerge.server

import com.github.krzychek.iomerge.server.config.AppConfiguration
import com.github.krzychek.iomerge.server.config.AppConfigurator
import com.github.krzychek.iomerge.server.daggerConfig.ChainingModule
import com.github.krzychek.iomerge.server.daggerConfig.EagerSingletons
import com.github.krzychek.iomerge.server.daggerConfig.IfaceMappingModule
import com.github.krzychek.iomerge.server.daggerConfig.MiscModule
import dagger.Component
import javax.inject.Singleton


fun main(vararg args: String) {
	val appConfiguration = AppConfigurator(*args).createConfig()

	val mainComponent: MainComponent = DaggerMainComponent.builder()
			.appConfiguration(appConfiguration)
			.build()

	mainComponent.init()
}


// Left in Main file until intellij stops complaining about missing Dagger generated class..
@Component(modules = arrayOf(MiscModule::class, IfaceMappingModule::class, ChainingModule::class), dependencies = arrayOf(AppConfiguration::class))
@Singleton
interface MainComponent {
	fun init(): EagerSingletons
}

