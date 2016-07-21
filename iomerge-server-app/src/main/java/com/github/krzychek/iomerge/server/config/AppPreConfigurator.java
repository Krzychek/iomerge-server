package com.github.krzychek.iomerge.server.config;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.pmw.tinylog.Configurator;
import org.pmw.tinylog.Level;
import org.pmw.tinylog.writers.ConsoleWriter;
import org.pmw.tinylog.writers.FileWriter;


public class AppPreConfigurator {

	private static final String LOG_FORMAT = "\\t\\t{date:yyyy-MM-dd HH:mm:ss} [{thread}] {class_name}" +
											 "\\n{level}: {message}";
	@Option(name = "-logLevel", aliases = "-log",
			usage = "level of logging")
	private Level logLevel = Level.INFO;
	@Option(name = "-debug", aliases = "debug",
			usage = "enables debug level of logging",
			forbids = "-logLevel")
	private boolean debug = false;
	private boolean logWritingThread = true;
	private boolean logToFile = true;

	public AppPreConfigurator(String... args) {
		CmdLineParser parser = new CmdLineParser(this);
		try {
			parser.parseArgument(args);
		} catch (CmdLineException e) {
			System.out.println(e.getLocalizedMessage());
			parser.printUsage(System.out);
			System.exit(1);
		}
	}

	public void configure() {
		configureLogger();
	}

	private void configureLogger() {
		Configurator configurator = Configurator.defaultConfig();
		configurator.formatPattern(LOG_FORMAT);
		configurator.writer(new ConsoleWriter());
		configurator.level(getLogLevel());
		configurator.writingThread(logWritingThread);

		if (logToFile)
			configurator.addWriter(new FileWriter(ConstantPaths.LOG_FILE.getAbsolutePath()));

		configurator.activate();
	}

	private Level getLogLevel() {
		return debug ? Level.DEBUG : logLevel;
	}

	public AppPreConfigurator setLogLevel(Level logLevel) {
		this.logLevel = logLevel;
		return this;
	}

	public AppPreConfigurator setLogWritingThread(boolean writingThread) {
		this.logWritingThread = writingThread;
		return this;
	}

	public AppPreConfigurator setLogToFile(boolean logToFile) {
		this.logToFile = logToFile;
		return this;
	}
}
