package com.github.krzychek.server.api;


/**
 * generic interface for chainable components in application
 * plugin implementing any of subtypes should delegate method calls to object passed to {@link Chainable#chain} method
 *
 * @param <T> interface to chain
 */
public interface Chainable<T> {

	/**
	 * to this method is passed next object in chain during context initialization
	 *
	 * @param nextInChain object implementing class should (optionally) delegate method calls
	 */
	void chain(T nextInChain);
}
