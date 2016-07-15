package com.github.krzychek.server.config;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.pmw.tinylog.Configurator;
import org.pmw.tinylog.Level;
import org.pmw.tinylog.writers.ConsoleWriter;
import org.pmw.tinylog.writers.FileWriter;

import static com.github.krzychek.server.config.ConstantPaths.LOG_FILE;

public class TinyLogConfigurator {

	private static final String LOG_FORMAT = "{date:yyyy-MM-dd HH:mm:ss} [{thread}] {class_name}\\n{level}: {message}";

	@Option(name = "-logLevel", aliases = "-log",
			usage = "level of logging")
	private Level logLevel = Level.INFO;

	@Option(name = "-debug", aliases = "debug",
			usage = "enables debug level of logging",
			forbids = "-logLevel")
	private boolean debug = false;

	public static void configure(String... args) {
		TinyLogConfigurator config = new TinyLogConfigurator();
		CmdLineParser parser = new CmdLineParser(config);
		try {
			parser.parseArgument(args);
			config.setupLogger();
		} catch (CmdLineException e) {
			System.out.println(e.getLocalizedMessage());
			parser.printUsage(System.out);
			System.exit(1);
		}
	}

	private void setupLogger() {
		Configurator.defaultConfig()
				.formatPattern(LOG_FORMAT)
				.writer(new ConsoleWriter())
				.addWriter(new FileWriter(LOG_FILE.getAbsolutePath()))
				.level(getLogLevel())
				.writingThread(null)
				.activate();
	}

	private Level getLogLevel() {
		return debug ? Level.DEBUG : logLevel;
	}
}
