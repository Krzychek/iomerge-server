package pl.kbieron.iomerge.server.ui;

import pl.kbieron.iomerge.model.Edge;
import pl.kbieron.iomerge.server.movementReader.VirtualScreen;
import pl.kbieron.iomerge.server.network.EventServer;

import javax.inject.Inject;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;


class SettingsWindow extends JFrame {

	private EventServer eventServer;

	private EdgeTrigger edgeTrigger;

	private VirtualScreen virtualScreen;

	private JComboBox<Edge> edge;

	private JFormattedTextField triggerLength;

	private JFormattedTextField triggerOffset;

	private JFormattedTextField moveScale;

	private JFormattedTextField port;

	@Inject
	SettingsWindow(EventServer eventServer, EdgeTrigger edgeTrigger, VirtualScreen virtualScreen)
			throws HeadlessException {
		super("IOMerge Settings");
		this.eventServer = eventServer;
		this.edgeTrigger = edgeTrigger;
		this.virtualScreen = virtualScreen;
		init();
	}

	private void init() {
		createFields();
		setLayout();
	}

	private void setLayout() {
		this.setMinimumSize(new Dimension(200, 200));// T
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridy = 0;

		c.weightx = 0.5;
		c.gridx = 0;
		this.add(new JLabel("Edge"), c);
		c.weightx = 1.0;
		c.gridx = 1;
		this.add(edge, c);

		++c.gridy;
		c.weightx = 0.5;
		c.gridx = 0;
		this.add(new JLabel("Trigger length"), c);
		c.weightx = 1.0;
		c.gridx = 1;
		this.add(triggerLength, c);

		++c.gridy;
		c.weightx = 0.5;
		c.gridx = 0;
		this.add(new JLabel("Trigger offset"), c);
		c.weightx = 1.0;
		c.gridx = 1;
		this.add(triggerOffset, c);

		++c.gridy;
		c.weightx = 0.5;
		c.gridx = 0;
		this.add(new JLabel("Movement scale"), c);
		c.weightx = 1.0;
		c.gridx = 1;
		this.add(moveScale, c);

		++c.gridy;
		c.weightx = 0.5;
		c.gridx = 0;
		this.add(new JLabel("Server port"), c);
		c.weightx = 1.0;
		c.gridx = 1;
		this.add(port, c);

		++c.gridy;
		c.weightx = 0.5;
		c.gridx = 0;
		c.weighty = 1.0;
		c.anchor = GridBagConstraints.PAGE_END;
		JButton saveAndExitBtn = new JButton("Save");
		this.add(saveAndExitBtn, c);
		saveAndExitBtn.addActionListener(actionEvent -> {
			saveValues();
			setVisible(false);
		});
	}

	private void saveValues() {
		edgeTrigger.setProperties( //
				(Edge) edge.getSelectedItem(), //
				(int) triggerLength.getValue(), //
				(int) triggerOffset.getValue());
		eventServer.setPort((int) port.getValue());
		virtualScreen.setMovementScale((double) moveScale.getValue());
	}

	private void createFields() {
		triggerLength = new JFormattedTextField(edgeTrigger.getLength());
		triggerOffset = new JFormattedTextField(edgeTrigger.getOffset());
		moveScale = new JFormattedTextField(virtualScreen.getMovementScale());
		port = new JFormattedTextField(eventServer.getPort());

		edge = new JComboBox<>(Edge.values());
		edge.setSelectedItem(edgeTrigger.getEdge());
	}

}
