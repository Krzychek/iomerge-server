package com.github.krzychek.server

import com.github.krzychek.server.api.movementReader.IOListener
import com.github.krzychek.server.network.ConnectionHandler
import com.github.krzychek.server.network.EventServer
import com.github.krzychek.server.ui.EdgeTrigger
import com.github.krzychek.server.ui.SettingsWindow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener
import spock.lang.Specification
import spock.lang.Unroll

@ContextConfiguration(classes = BasicSpringTestContext)
@TestExecutionListeners(listeners = DependencyInjectionTestExecutionListener)
class SpringContextTest extends Specification {

	@Autowired
	ApplicationContext ctx;

	@Unroll
	def "context should be autowired and have bean of #beanClass"() {
		when:
		ctx.getBean(beanClass)

		then:
		noExceptionThrown()

		where:
		beanClass         | _
		EdgeTrigger       | _
		EventServer       | _
		IOListener        | _
		ConnectionHandler | _
		SettingsWindow    | _
	}
}
