package pl.kbieron.iomerge.server.utils.plugins;

import org.pmw.tinylog.Logger;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import static pl.kbieron.iomerge.server.config.ConstantPaths.PLUGINS_DIR;


public class PluginLoader {


	private Optional<Class<?>> loadFromJar(PluginDefinition pluginDefinition) {

		try {
			loadJar(pluginDefinition);

			return Optional.of(Class.forName(pluginDefinition.className));

		} catch (Exception ex) {
			Logger.warn(ex, "Problem while loading plugin");
			return Optional.empty();
		}
	}

	private void loadJar(PluginDefinition pluginDefinition) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, MalformedURLException {
		Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
		method.setAccessible(true);
		URL url = pluginDefinition.jar.toURI().toURL();
		method.invoke(ClassLoader.getSystemClassLoader(), url);
	}

	public Class<?>[] loadPlugins() {
		if (!PLUGINS_DIR.exists() || !PLUGINS_DIR.isDirectory()) {
			Logger.debug("Plugin dir doesn't exists");
			return new Class[0];
		}

		return Arrays.stream(getPluginDirectories())
				.map(PluginDefinition::read).flatMap(this::toStream)
				.map(this::loadFromJar).flatMap(this::toStream)
				.toArray(Class[]::new);

	}

	private File[] getPluginDirectories() {
		File[] pluginDirs = PLUGINS_DIR.listFiles(File::isDirectory);
		return (pluginDirs != null) ? pluginDirs : new File[0];

	}

	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	private <T> Stream<T> toStream(Optional<T> optional) {
		return optional.isPresent() ? Stream.of(optional.get()) : Stream.empty();
	}

}
