package pl.kbieron.iomerge.server.appState;

import org.springframework.context.ApplicationListener;


public interface AppStateListener extends ApplicationListener<AppState.UpdateEvent> {}
