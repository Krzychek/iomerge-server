package pl.kbieron.iomerge.server.network;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.kbieron.iomerge.server.api.appState.AppStateManager;

import java.io.IOException;
import java.net.Socket;


@Component
public class SingleClientHandlerFactory {

	private final AppStateManager appStateManager;
	private final MsgProcessor msgProcessor;

	@Autowired
	public SingleClientHandlerFactory(AppStateManager appStateManager, MsgProcessor msgProcessor) {
		this.appStateManager = appStateManager;
		this.msgProcessor = msgProcessor;
	}

	SingleClientHandler createSingleClientHandler(Socket clientSocket) throws IOException {
		return new SingleClientHandler(clientSocket, msgProcessor, appStateManager);
	}
}
