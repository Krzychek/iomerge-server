package pl.kbieron.iomerge.server.utils.plugins;

import org.pmw.tinylog.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static pl.kbieron.iomerge.server.config.ConstantPaths.PLUGINS_DIR;


public class PluginLoader {

	private final List<ClassLoader> classLoaders;

	public PluginLoader() {
		classLoaders = new ArrayList<>();
		classLoaders.add(ClassLoader.getSystemClassLoader());
	}

	public void loadPluginsToContext(AnnotationConfigApplicationContext ctx) {
		if (!PLUGINS_DIR.isDirectory()) {
			Logger.debug("Plugin dir doesn't exists");
			return;
		}

		getPluginDirectories(PLUGINS_DIR)
				.flatMap(PluginDefinition::readFromDir)
				.flatMap(this::loadConfigClass)
				.forEach(ctx::register);

		ctx.setClassLoader(new ChainedClassLoader(classLoaders));

	}

	private Stream<File> getPluginDirectories(File pluginsDir) {
		File[] pluginDirs = pluginsDir.listFiles(File::isDirectory);
		return (pluginDirs != null) ? Stream.of(pluginDirs) : Stream.empty();

	}

	private Stream<Class<?>> loadConfigClass(PluginDefinition pluginDefinition) {

		try {
			ClassLoader pluginClassLoader = new URLClassLoader(new URL[]{pluginDefinition.jar.toURI().toURL()}, Thread.currentThread()
					.getContextClassLoader());

			classLoaders.add(pluginClassLoader);

			return Stream.of(pluginClassLoader.loadClass(pluginDefinition.className));

		} catch (Exception ex) {
			Logger.warn(ex, "Problem while loading plugin");
			return Stream.empty();
		}
	}

}
