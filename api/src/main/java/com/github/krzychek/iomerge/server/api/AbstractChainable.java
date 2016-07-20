package com.github.krzychek.iomerge.server.api;

/**
 * @see Chainable
 */
public abstract class AbstractChainable<T> implements Chainable<T> {

	protected T nextInChain;

	@Override
	public void chain(T nextInChain) {
		this.nextInChain = nextInChain;
	}
}
