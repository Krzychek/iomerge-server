package com.github.krzychek.iomerge.server

import com.github.krzychek.iomerge.server.config.AppPreConfigurator
import org.annoprops.PropertyManagerHelperBean
import org.pmw.tinylog.Level
import org.springframework.beans.factory.ListableBeanFactory
import org.springframework.context.annotation.Configuration

import javax.annotation.PostConstruct
import java.awt.*
import java.awt.datatransfer.Clipboard

import static org.mockito.Mockito.mock
import static org.powermock.api.mockito.PowerMockito.whenNew

@Configuration
class BasicSpringTestContext extends SpringConfig {
	static {
		new AppPreConfigurator()
				.setLogWritingThread(false)
				.setLogLevel(Level.WARNING)
				.setLogToFile(false)
				.configure();
	}

	@Override
	def Clipboard clipboard() {
		return mock(Clipboard)
	}

	@Override
	def Robot robot() throws AWTException {
		return mock(Robot)
	}

	@Override
	def PropertyManagerHelperBean propertyManager(ListableBeanFactory beanFactory) throws IOException {
		return mock(PropertyManagerHelperBean)
	}

	@SuppressWarnings("GrMethodMayBeStatic")
	@PostConstruct
	def mockSocket() {
		def socketMock = mock(Socket);

		whenNew(Socket).withAnyArguments().thenReturn(socketMock)
	}
}
