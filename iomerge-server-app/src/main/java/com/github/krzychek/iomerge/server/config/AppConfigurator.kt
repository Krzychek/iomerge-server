package com.github.krzychek.iomerge.server.config

import org.kohsuke.args4j.CmdLineException
import org.kohsuke.args4j.CmdLineParser
import org.kohsuke.args4j.Option
import org.pmw.tinylog.Configurator
import org.pmw.tinylog.Level
import org.pmw.tinylog.writers.ConsoleWriter
import org.pmw.tinylog.writers.FileWriter
import java.io.File
import java.nio.file.Paths.get as getPath

class AppConfiguration(val startupCallback: () -> Unit,
					   val pluginsDir: File,
					   val settingsFile: File,
					   val shutdownCallback: () -> Unit)

private val LOG_FORMAT = "\\t\\t{date:yyyy-MM-dd HH:mm:ss} [{thread}] {class_name}" + "\\n{level}: {message}"

class AppConfigurator(vararg args: String) {

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
	}

	fun createConfig(): AppConfiguration {
		return AppConfiguration(
				startupCallback = { configureBootstrap() },
				pluginsDir = pluginsDir,
				settingsFile = settingsFile,
				shutdownCallback = { shutdown() })
	}

	fun configureBootstrap() {
		Configurator.defaultConfig()
				.formatPattern(LOG_FORMAT)
				.writer(ConsoleWriter())
				.level(logLevel)
				.writingThread(null)
				.addWriter(FileWriter(logFile.absolutePath))
				.activate()
	}

	fun shutdown() {
		Configurator.shutdownWritingThread(true)
	}

	val configurationDir: File get() = configurationDirArg?.let { getPath("user.dir", it).toFile() }
			?: getPath(System.getProperty("user.home"), ".config", "iomerge").toFile()

	val pluginsDir: File get() = pluginsDirArg?.let { getPath("user.dir", it).toFile() }
			?: configurationDir.resolve("plugins")

	val settingsFile: File get() = settingsFileArg?.let { getPath("user.dir", it).toFile() }
			?: configurationDir.resolve("config.properties")

	val logFile: File get() = logFileArg?.let { getPath("user.dir", it).toFile() }
			?: configurationDir.resolve("iomerge.log")
}