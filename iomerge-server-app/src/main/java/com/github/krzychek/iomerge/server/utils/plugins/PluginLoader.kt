package com.github.krzychek.iomerge.server.utils.plugins

import com.github.krzychek.iomerge.server.config.AppConfigurator.Companion.pluginsDir
import org.pmw.tinylog.Logger
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.core.io.support.PropertiesLoaderUtils
import org.springframework.util.StringUtils
import java.io.File
import java.net.URLClassLoader


class PluginLoader() {

	private val classLoaders = arrayListOf(Thread.currentThread().contextClassLoader)

	/**
	 * @return stream of jar files in  plugins directory
	 */
	fun readPluginJars(): Array<File> {
		return pluginsDir.listFiles { file -> file.isFile && file.name.endsWith(".jar") }
				?: emptyArray<File>()
	}

	/**
	 * register plugin classes and sets proper classLoader in @param ctx
	 */
	fun loadPluginsToContext(ctx: AnnotationConfigApplicationContext) {
		if (pluginsDir.isDirectory.not()) {
			Logger.info("Plugin dir doesn't exists: ", pluginsDir)
			return
		}

		readPluginJars()
				.flatMap { loadConfigClass(it) }
				.forEach { ctx.register(it) }

		ctx.classLoader = ChainedClassLoader(classLoaders)

	}


	/**
	 * @return stream of configuration classes in given plugin jar file
	 */
	private fun loadConfigClass(pluginJar: File): List<Class<*>> {
		try {
			val pluginClassLoader = URLClassLoader(
					arrayOf(pluginJar.toURI().toURL()),
					Thread.currentThread().contextClassLoader)

			// load classes
			val classStream = getConfigurationClassNames(pluginClassLoader)
					.map { pluginClassLoader.loadClass(it) }

			classLoaders.add(pluginClassLoader)

			return classStream

		} catch (ex: Exception) {
			Logger.warn(ex, "Problem while loading plugin")
			return emptyList()
		}

	}

	private fun getConfigurationClassNames(pluginClassLoader: ClassLoader): List<String> {
		val properties = PropertiesLoaderUtils.loadAllProperties("iomerge-plugin.properties", pluginClassLoader)
		val classNames = properties.getProperty(SPRING_CONFIGURATION_PROP)

		if (StringUtils.isEmpty(classNames))
			throw IllegalArgumentException("Plugin mendatory property: '$SPRING_CONFIGURATION_PROP' not found")

		// split class names by ',' and map to class name list
		return classNames.split(",").filter { it.isEmpty() }
	}

	companion object {

		private val SPRING_CONFIGURATION_PROP = "plugin.spring.configuration"
	}

}
