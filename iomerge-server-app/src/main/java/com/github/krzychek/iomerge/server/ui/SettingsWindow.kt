package com.github.krzychek.iomerge.server.ui

import com.github.krzychek.iomerge.server.model.Edge
import com.github.krzychek.iomerge.server.movementReader.VirtualScreen
import com.github.krzychek.iomerge.server.network.EventServer
import com.github.krzychek.iomerge.server.utils.JFXHelper
import com.sun.javafx.collections.ObservableListWrapper
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.CheckBox
import javafx.scene.control.ComboBox
import javafx.scene.control.Spinner
import javafx.stage.Stage
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct


@Lazy
@Component
open class SettingsWindow(private val eventServer: EventServer, private val edgeTrigger: EdgeTrigger, private val virtualScreen: VirtualScreen) {

	private lateinit var stage: Stage

	@FXML
	private lateinit var edge: ComboBox<Edge>
	@FXML
	private lateinit var triggerLength: Spinner<Int>
	@FXML
	private lateinit var triggerOffset: Spinner<Int>
	@FXML
	private lateinit var moveScale: Spinner<Double>
	@FXML
	private lateinit var port: Spinner<Int>
	@FXML
	private lateinit var reverseScroll: CheckBox

	@PostConstruct
	fun init() {
		JFXHelper.runLater {
			// load scene
			val parent = FXMLLoader(javaClass.getResource("/settings_window.fxml")).run {
				setControllerFactory { it -> this@SettingsWindow }
				load<Parent>()
			}
			stage = Stage().apply {
				title = "IOMerge"
				scene = Scene(parent)
			}

			edge.items = ObservableListWrapper(Edge.values().toList())
		}
	}

	fun show() {
		JFXHelper.runLater {
			refreshFields()
			stage.show()
		}
	}

	@FXML
	private fun saveValues() {
		edgeTrigger.setProperties(edge.value, triggerLength.value, triggerOffset.value)
		eventServer.port = port.value
		virtualScreen.movementScale = moveScale.value
		virtualScreen.reverseScroll = reverseScroll.isSelected
	}

	@FXML
	private fun saveAndExit() {
		saveValues()
		stage.hide()
	}

	private fun refreshFields() {
		triggerLength.valueFactory.value = edgeTrigger.length
		triggerOffset.valueFactory.value = edgeTrigger.offset
		moveScale.valueFactory.value = virtualScreen.movementScale
		port.valueFactory.value = eventServer.port
		edge.value = edgeTrigger.edge
		reverseScroll.isSelected = virtualScreen.reverseScroll
	}

}
