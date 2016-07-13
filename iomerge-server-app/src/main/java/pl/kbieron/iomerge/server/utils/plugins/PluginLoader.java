package pl.kbieron.iomerge.server.utils.plugins;

import org.pmw.tinylog.Logger;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class PluginLoader {

	private final File pluginDir;

	public PluginLoader(File pluginDir) {
		this.pluginDir = pluginDir;
	}

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

	public List<Class<?>> loadPlugins() {
		if (!pluginDir.exists() || !pluginDir.isDirectory()) {
			Logger.debug("Plugin dir doesn't exists");
			return Collections.emptyList();
		}

		return Arrays.stream(getpluginDirectories())
				.map(PluginDefinition::read).flatMap(this::toStream)
				.map(this::loadFromJar).flatMap(this::toStream)
				.collect(Collectors.toList());

	}

	private File[] getpluginDirectories() {
		File[] pluginDirs = pluginDir.listFiles(File::isDirectory);
		return (pluginDirs != null) ? pluginDirs : new File[0];

	}

	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	private <T> Stream<T> toStream(Optional<T> optional) {
		return optional.isPresent() ? Stream.of(optional.get()) : Stream.empty();
	}

}