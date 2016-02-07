package pl.kbieron.iomerge.server.network;

import com.google.inject.AbstractModule;


public class NetworkModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(EventServer.class).asEagerSingleton();
		bind(MsgDispatcher.class).asEagerSingleton();
		bind(MsgProcessor.class).asEagerSingleton();
		bind(ConnectionHandler.class).to(EventServer.class).asEagerSingleton();
	}
}
