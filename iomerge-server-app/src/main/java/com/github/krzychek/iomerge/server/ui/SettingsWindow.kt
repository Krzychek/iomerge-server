package com.github.krzychek.iomerge.server.ui

import com.github.krzychek.iomerge.server.misc.JFXHelper
import com.github.krzychek.iomerge.server.model.Edge
import com.github.krzychek.iomerge.server.movementReader.MouseInputProcessor
import com.github.krzychek.iomerge.server.network.ServerManager
import com.sun.javafx.collections.ObservableListWrapper
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.CheckBox
import javafx.scene.control.ComboBox
import javafx.scene.control.Spinner
import javafx.stage.Stage
import javax.inject.Inject
import javax.inject.Singleton


@Singleton class SettingsWindow
@Inject constructor(private val serverManager: ServerManager, private val edgeTrigger: EdgeTrigger, private val mouseInputProcessor: MouseInputProcessor) {

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

	init {
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
		serverManager.port = port.value
		mouseInputProcessor.movementScale = moveScale.value
		mouseInputProcessor.reverseScroll = reverseScroll.isSelected
	}

	@FXML
	private fun saveAndExit() {
		saveValues()
		stage.hide()
	}

	private fun refreshFields() {
		triggerLength.valueFactory.value = edgeTrigger.length
		triggerOffset.valueFactory.value = edgeTrigger.offset
		moveScale.valueFactory.value = mouseInputProcessor.movementScale
		port.valueFactory.value = serverManager.port
		edge.value = edgeTrigger.edge
		reverseScroll.isSelected = mouseInputProcessor.reverseScroll
	}

}
