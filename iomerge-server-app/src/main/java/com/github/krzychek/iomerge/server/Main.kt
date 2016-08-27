package com.github.krzychek.iomerge.server

import com.github.krzychek.iomerge.server.config.AppConfigurator
import org.springframework.context.annotation.AnnotationConfigApplicationContext


object Main {

	@JvmStatic fun main(args: Array<String>) {
		AppConfigurator.apply {
			parseArg(*args)
			configureBootstrap()
		}

		AnnotationConfigApplicationContext().apply {
			register(SpringConfig::class.java)
			registerShutdownHook()
		}.refresh()
	}
}
