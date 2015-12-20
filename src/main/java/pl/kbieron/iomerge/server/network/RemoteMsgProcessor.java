package pl.kbieron.iomerge.server.network;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.kbieron.iomerge.server.appState.AppStateManager;

import static pl.kbieron.iomerge.model.RemoteMsgTypes.REMOTE_EXIT;


@Component
class RemoteMsgProcessor {

	@Autowired
	private AppStateManager appStateManager;

	public void process(byte[] actionBytes) {
		if ( actionBytes[0] == REMOTE_EXIT ) {
			appStateManager.exitRemote();
		}
	}
}
