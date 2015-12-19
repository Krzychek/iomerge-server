package pl.kbieron.iomerge.server.appState;

import java.util.Observable;
import java.util.Observer;


public interface StateObserver extends Observer {

	@Override
	default void update(Observable observable, Object o) {
		if ( observable instanceof AppStateManager ) update((AppStateManager) observable);
	}

	void update(AppStateManager appStateManager);
}
