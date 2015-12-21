package pl.kbieron.iomerge.server.appState;

public enum AppState {
	ON_REMOTE, ON_LOCAL, DISCONNECTED;

	private AppStateUpdateEvent updateEvent = new AppStateUpdateEvent(this);

	AppStateUpdateEvent getUpdateEvent() {
		return updateEvent;
	}

}