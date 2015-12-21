package pl.kbieron.iomerge.server.appState;

import org.springframework.context.ApplicationEvent;


public class AppStateUpdateEvent extends ApplicationEvent {

	private AppState stateChange;

	AppStateUpdateEvent(AppState stateChange) {
		super(stateChange);
		this.stateChange = stateChange;
	}

	public AppState getStateChange() {
		return stateChange;
	}
}
