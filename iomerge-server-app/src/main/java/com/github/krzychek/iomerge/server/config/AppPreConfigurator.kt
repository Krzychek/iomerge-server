package com.github.krzychek.iomerge.server.config

import org.kohsuke.args4j.CmdLineException
import org.kohsuke.args4j.CmdLineParser
import org.kohsuke.args4j.Option
import org.pmw.tinylog.Configurator
import org.pmw.tinylog.Level
import org.pmw.tinylog.writers.ConsoleWriter
import org.pmw.tinylog.writers.FileWriter


class AppPreConfigurator(vararg args: String) {

	@Option(name = "-logLevel", aliases = arrayOf("-log"), usage = "level of logging")
	private var logLevel = Level.INFO

	@Option(name = "-debug", aliases = arrayOf("debug"), usage = "enables debug level of logging", forbids = arrayOf("-logLevel"))
	private var debug = false

	private var logWritingThread = true
	private var logToFile = true

	init {
		CmdLineParser(this).apply {
			try {
				parseArgument(*args)
			} catch (e: CmdLineException) {
				println(e.localizedMessage)
				printUsage(System.out)
				System.exit(1)
			}
		}
	}

	fun configure() {
		configureLogger()
	}

	private fun configureLogger() {
		Configurator.defaultConfig().apply {

			formatPattern(LOG_FORMAT)
			writer(ConsoleWriter())
			level(getLogLevel())

			if (logWritingThread)
				writingThread(null)

			if (logToFile)
				addWriter(FileWriter(ConstantPaths.LOG_FILE.absolutePath))

			activate()
		}
	}

	private fun getLogLevel(): Level {
		return if (debug) Level.DEBUG else logLevel
	}

	fun setLogLevel(logLevel: Level): AppPreConfigurator {
		this.logLevel = logLevel
		return this
	}

	fun setLogWritingThread(writingThread: Boolean): AppPreConfigurator {
		this.logWritingThread = writingThread
		return this
	}

	fun setLogToFile(logToFile: Boolean): AppPreConfigurator {
		this.logToFile = logToFile
		return this
	}

	companion object {

		private val LOG_FORMAT = "\\t\\t{date:yyyy-MM-dd HH:mm:ss} [{thread}] {class_name}" + "\\n{level}: {message}"
	}
}
