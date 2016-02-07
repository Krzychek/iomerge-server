package pl.kbieron.iomerge.server.gesture;

import com.google.inject.AbstractModule;
import pl.kbieron.iomerge.server.gesture.calc.LikelihoodCalculator;
import pl.kbieron.iomerge.server.gesture.calc.Normalizer;
import pl.kbieron.iomerge.server.gesture.calc.TemplateMatcher;


public class GestureModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(GestureRecorder.class).asEagerSingleton();
		bind(PatternDatabase.class).asEagerSingleton();
		bind(Normalizer.class).asEagerSingleton();
		bind(TemplateMatcher.class).asEagerSingleton();
		bind(LikelihoodCalculator.class).asEagerSingleton();
	}
}
