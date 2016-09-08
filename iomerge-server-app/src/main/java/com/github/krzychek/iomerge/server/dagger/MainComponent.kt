package com.github.krzychek.iomerge.server.dagger

import com.github.krzychek.iomerge.server.config.AppConfiguration
import dagger.Component
import javax.inject.Singleton

@Component(modules = arrayOf(MiscModule::class, IfaceMappingModule::class, ChainingModule::class), dependencies = arrayOf(AppConfiguration::class))
@Singleton
interface MainComponent {
	fun init(): EagerSingletons
}