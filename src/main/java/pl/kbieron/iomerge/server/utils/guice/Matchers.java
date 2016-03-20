package pl.kbieron.iomerge.server.utils.guice;

import com.google.inject.TypeLiteral;
import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.matcher.Matcher;


public class Matchers {

	public static Matcher<TypeLiteral<?>> subclassOf(Class<?> clazz) {
		return new SubClassesOf(clazz);
	}

	private static class SubClassesOf extends AbstractMatcher<TypeLiteral<?>> {

		private final Class<?> baseClass;

		private SubClassesOf(Class<?> baseClass) {
			this.baseClass = baseClass;
		}

		@Override
		public boolean matches(TypeLiteral<?> t) {
			return baseClass.isAssignableFrom(t.getRawType());
		}
	}
}
