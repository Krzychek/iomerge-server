package com.github.krzychek.server.config;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;


public interface ConstantPaths {

	Path APP_CONFIG_DIR = Paths.get(System.getProperty("user.home"), ".config", "iomerge");

	File SETTINGS_FILE = APP_CONFIG_DIR.resolve("config.properties").toFile();

	File PLUGINS_DIR = APP_CONFIG_DIR.resolve("plugins").toFile();

	File LOG_FILE = APP_CONFIG_DIR.resolve("iomerge.log").toFile();
}
