package pl.kbieron.iomerge.server.api;

public interface Chainable<T> {

	void chain(T nextInChain);
}
