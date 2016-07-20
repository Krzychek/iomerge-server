package com.github.krzychek.iomerge.server.ui;

import com.github.krzychek.iomerge.server.model.Edge;
import com.github.krzychek.iomerge.server.movementReader.VirtualScreen;
import com.github.krzychek.iomerge.server.network.EventServer;
import com.sun.javafx.application.PlatformImpl;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.stage.Stage;
import org.pmw.tinylog.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class SettingsWindow {

	private final EventServer eventServer;
	private final EdgeTrigger edgeTrigger;
	private final VirtualScreen virtualScreen;

	private Stage stage;

	@FXML
	private ComboBox<Edge> edge;
	@FXML
	private Spinner<Integer> triggerLength;
	@FXML
	private Spinner<Integer> triggerOffset;
	@FXML
	private Spinner<Double> moveScale;
	@FXML
	private Spinner<Integer> port;


	@Autowired
	SettingsWindow(EventServer eventServer, EdgeTrigger edgeTrigger, VirtualScreen virtualScreen) {
		this.eventServer = eventServer;
		this.edgeTrigger = edgeTrigger;
		this.virtualScreen = virtualScreen;
	}


	void show() {
		PlatformImpl.runAndWait(() -> {
			if (stage == null) {
				try {
					// load scene
					FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/settings_window.fxml"));
					fxmlLoader.setControllerFactory(aClass -> this);
					Parent page = fxmlLoader.load();
					stage = new Stage();
					stage.setTitle("IOMerge");
					stage.setScene(new Scene(page));
				} catch (IOException e) {
					Logger.error(e);
					return;
				}
			}
			refreshFields();
			stage.show();
		});
	}

	@FXML
	private void saveValues() {
		edgeTrigger.setProperties(edge.getValue(), triggerLength.getValue(), triggerOffset.getValue());
		eventServer.setPort(port.getValue());
		virtualScreen.setMovementScale(moveScale.getValue());
	}

	private void refreshFields() {
		triggerLength.getValueFactory().setValue(edgeTrigger.getLength());
		triggerOffset.getValueFactory().setValue(edgeTrigger.getOffset());
		moveScale.getValueFactory().setValue(virtualScreen.getMovementScale());
		port.getValueFactory().setValue(eventServer.getPort());
		edge.setValue(edgeTrigger.getEdge());
	}

}
