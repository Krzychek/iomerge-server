package pl.kbieron.iomerge.server.appState;

import org.springframework.context.ApplicationEvent;


public enum AppState {
	ON_REMOTE, ON_LOCAL, DISCONNECTED;

	private final UpdateEvent updateEvent = new UpdateEvent();

	UpdateEvent getUpdateEvent() {
		return updateEvent;
	}

	public class UpdateEvent extends ApplicationEvent {

		private UpdateEvent() {
			super(AppState.this);
		}

		public AppState getStateChange() {
			return AppState.this;
		}
	}

}