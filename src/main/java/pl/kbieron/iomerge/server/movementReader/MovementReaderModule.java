package pl.kbieron.iomerge.server.movementReader;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import pl.kbieron.iomerge.server.appState.AppStateListener;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelListener;


public class MovementReaderModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(MouseTrapReader.class).asEagerSingleton();
		bind(VirtualScreen.class).asEagerSingleton();

		bind(MovementListener.class).to(CompositeListener.class).asEagerSingleton();
		bind(KeyListener.class).to(CompositeListener.class).asEagerSingleton();
		bind(MouseWheelListener.class).to(CompositeListener.class).asEagerSingleton();
		bind(MouseListener.class).to(CompositeListener.class).asEagerSingleton();

		Multibinder.newSetBinder(binder(), AppStateListener.class).addBinding().to(MouseTrapReader.class);
	}
}
