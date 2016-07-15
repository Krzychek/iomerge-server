package pl.kbieron.iomerge.server.utils.plugins;

import org.pmw.tinylog.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.function.Function;
import java.util.stream.Stream;

import static pl.kbieron.iomerge.server.config.ConstantPaths.PLUGINS_DIR;


public class PluginLoader {

	private static final String SPRING_CONFIGURATION_PROP = "plugin.spring.configuration";

	private final List<ClassLoader> classLoaders;

	public PluginLoader() {
		classLoaders = new ArrayList<>();
		classLoaders.add(Thread.currentThread().getContextClassLoader());
	}

	public void loadPluginsToContext(AnnotationConfigApplicationContext ctx) {
		if (!PLUGINS_DIR.isDirectory()) {
			Logger.info("Plugin dir doesn't exists: ", PLUGINS_DIR);
			return;
		}

		getPluginsJars()
				.flatMap(this::loadConfigClass)
				.forEach(ctx::register);

		ctx.setClassLoader(new ChainedClassLoader(classLoaders));

	}

	/**
	 * @return stream of jar files in  plugins directory
	 */
	private Stream<File> getPluginsJars() {
		File[] plugins = PLUGINS_DIR.listFiles(file -> file.isFile() && file.getName().endsWith(".jar"));
		return (plugins != null) ? Arrays.stream(plugins) : Stream.empty();

	}


	/**
	 * @return stream of configuration classes in given plugin jar file
	 */
	private Stream<Class<?>> loadConfigClass(File pluginJar) {
		try {
			ClassLoader pluginClassLoader = new URLClassLoader(
					new URL[]{pluginJar.toURI().toURL()},
					Thread.currentThread().getContextClassLoader());

			// load classes
			Stream<Class<?>> classStream = getConfigurationClassNames(pluginClassLoader)
					.map(mapToClassObjects(pluginClassLoader));

			classLoaders.add(pluginClassLoader);

			return classStream;

		} catch (Exception ex) {
			Logger.warn(ex, "Problem while loading plugin");
			return Stream.empty();
		}
	}

	private Stream<String> getConfigurationClassNames(ClassLoader pluginClassLoader) throws IOException {
		Properties properties = PropertiesLoaderUtils.loadAllProperties("iomerge-plugin.properties", pluginClassLoader);
		String classNames = properties.getProperty(SPRING_CONFIGURATION_PROP);

		if (StringUtils.isEmpty(classNames))
			throw new IllegalArgumentException("Plugin mendatory property: '" + SPRING_CONFIGURATION_PROP + "' not found");

		// split class names by ',' and map to class stream
		return Arrays.stream(classNames.split(","));
	}

	private Function<String, Class<?>> mapToClassObjects(ClassLoader pluginClassLoader) {
		return className -> {
			try {
				return pluginClassLoader.loadClass(className);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
		};
	}

}
