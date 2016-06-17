package pl.kbieron.iomerge.server.appState;

/**
 * Interface that should be implemented, by classes listening for {@link AppState} changes
 */
public interface AppStateListener {

	void onStateChange(AppState newState);
}
