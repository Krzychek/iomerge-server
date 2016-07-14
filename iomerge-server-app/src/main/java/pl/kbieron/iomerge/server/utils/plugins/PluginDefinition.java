package pl.kbieron.iomerge.server.utils.plugins;

import org.pmw.tinylog.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Stream;


class PluginDefinition {

	private static final String JAR_PROP = "plugin.jar";
	private static final String SPRING_CONFIGURATION_CLASS_PROP = "plugin.spring.configuration.class";

	private static final String PLUGIN_PROPERTIES = "plugin.properties";

	final File jar;
	final String className;

	private PluginDefinition(File jar, String className) {
		this.jar = jar;
		this.className = className;
	}

	static Stream<PluginDefinition> read(File pluginDir) {
		// load props
		Properties props = new Properties();
		try {
			File propsFile = new File(pluginDir, PLUGIN_PROPERTIES);
			props.load(new FileInputStream(propsFile));

		} catch (IOException e) {
			Logger.error(e, "problem with loading plugin props");
			return Stream.empty();
		}

		// check jar file
		Optional<File> jar = getExistingFile(pluginDir, props.getProperty(JAR_PROP));
		if (!jar.isPresent()) {
			Logger.warn("plugin jar doesn't exist", jar);
			return Stream.empty();
		}

		String springClass = props.getProperty(SPRING_CONFIGURATION_CLASS_PROP);
		// check springClass
		if (springClass == null) return Stream.empty();

		return Stream.of(new PluginDefinition(jar.get(), springClass));
	}

	private static Optional<File> getExistingFile(File parent, String fileName) {
		if (fileName == null)
			return Optional.empty();

		File file = new File(parent, fileName);
		if (!file.exists())
			return Optional.empty();

		return Optional.of(file);
	}

}
