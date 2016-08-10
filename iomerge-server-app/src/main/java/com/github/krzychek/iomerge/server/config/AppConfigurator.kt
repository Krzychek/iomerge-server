package com.github.krzychek.iomerge.server.config

import org.kohsuke.args4j.CmdLineException
import org.kohsuke.args4j.CmdLineParser
import org.kohsuke.args4j.Option
import org.pmw.tinylog.Configurator
import org.pmw.tinylog.Level
import org.pmw.tinylog.writers.ConsoleWriter
import org.pmw.tinylog.writers.FileWriter
import java.io.File
import java.nio.file.Paths


class AppConfigurator(vararg args: String = emptyArray(),
					  private val logWritingThread: Boolean = true,
					  private val logToFile: Boolean = true) {

	@Option(name = "-debug", usage = "enables debug level of logging", forbids = arrayOf("-logLevel"))
	var debug = false

	@Option(name = "-logLevel", aliases = arrayOf("-log"), usage = "level of logging")
	var logLevel = Level.INFO
		get() = if (debug) Level.DEBUG else field

	@Option(name = "-confDir", usage = "configuration directory path")
	var configurationDirArg: String? = null

	@Option(name = "-pluginsDir", usage = "plugins directory path")
	var pluginsDirArg: String? = null

	@Option(name = "-settingsFile", usage = "configuration file path")
	var settingsFileArg: String? = null

	@Option(name = "-logFile", usage = "log directory path")
	var logFileArg: String? = null


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

		if (configurationDirArg.isNotBlank())
			configurationDir = Paths.get("user.dir", configurationDirArg).toFile()

		if (pluginsDirArg.isNotBlank())
			pluginsDir = Paths.get("user.dir", pluginsDirArg).toFile()

		if (settingsFileArg.isNotBlank())
			settingsFile = Paths.get("user.dir", settingsFileArg).toFile()

		if (logFileArg.isNotBlank())
			logFile = Paths.get("user.dir", logFileArg).toFile()

	}

	fun configureBootstrap() {
		Configurator.defaultConfig().apply {

			formatPattern(LOG_FORMAT)
			writer(ConsoleWriter())
			level(logLevel)

			if (logWritingThread)
				writingThread(null)

			if (logToFile)
				addWriter(FileWriter(logFile.absolutePath))

			activate()
		}
	}

	companion object {

		var configurationDir = Paths.get(System.getProperty("user.home"), ".config", "iomerge").toFile()

		private var _pluginsDir: File? = null
		var pluginsDir: File
			get() = _pluginsDir ?: configurationDir.resolve("plugins")
			set(value) {
				_pluginsDir = value
			}

		private var _settingsFile: File? = null
		var settingsFile: File
			get() = _settingsFile ?: configurationDir.resolve("config.properties")
			set(value) {
				_settingsFile = value
			}

		private var _logFile: File? = null
		var logFile: File
			get():File = _logFile ?: configurationDir.resolve("iomerge.log")
			set(value) {
				_logFile = value
			}

		private val LOG_FORMAT = "\\t\\t{date:yyyy-MM-dd HH:mm:ss} [{thread}] {class_name}" + "\\n{level}: {message}"
	}

	internal fun String?.isNotBlank(): Boolean = !this.isNullOrBlank()
}
