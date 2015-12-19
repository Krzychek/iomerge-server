package pl.kbieron.iomerge.server.ui.movementReader;

import pl.kbieron.iomerge.server.appState.StateObserver;


public interface MovementReader extends StateObserver {

	void startReading();

	void stopReading();
}
