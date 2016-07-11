package pl.kbieron.iomerge.server;

import org.annoprops.PropertyManagerHelperBean;
import org.annoprops.springframework.SpringframeworkAnnopropsBeanFactory;
import org.kohsuke.args4j.CmdLineException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import pl.kbieron.iomerge.server.config.TinyLogConfigurator;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.io.IOException;

import static pl.kbieron.iomerge.server.config.ConstantPaths.SETTINGS_FILE;

@Configuration
@EnableSpringConfigured
@ComponentScan(basePackages = "pl.kbieron.iomerge.server")
public class Bootstrap {

	public static void main(String... args) throws IOException, CmdLineException {
		// should be invoked before spring context
		TinyLogConfigurator.configure(args);

		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Bootstrap.class);
		applicationContext.registerShutdownHook();
	}

	@Bean
	Clipboard clipboard() {
		return Toolkit.getDefaultToolkit().getSystemClipboard();
	}

	@Bean
	PropertyManagerHelperBean propertyManager(ListableBeanFactory beanFactory) throws IOException {
		return SpringframeworkAnnopropsBeanFactory.createWithSpringFactory(beanFactory, SETTINGS_FILE);
	}
}
