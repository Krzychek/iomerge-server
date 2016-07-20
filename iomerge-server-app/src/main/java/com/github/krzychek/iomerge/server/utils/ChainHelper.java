package com.github.krzychek.iomerge.server.utils;

import com.github.krzychek.iomerge.server.api.Chainable;

import java.lang.reflect.Proxy;
import java.util.List;


public class ChainHelper {

	@SuppressWarnings("unchecked")
	private static <T> T getNOOPChainLink(Class<T> clazz) {
		return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, (proxy, method, args) -> null);
	}

	public static <T extends Chainable<T>> T createChain(List<T> chain, Class<T> clazz) {
		//noinspection OptionalGetWithoutIsPresent
		chain.stream()
				.reduce((listener1, listener2) -> {
					listener1.chain(listener2);
					return listener2;
				})
				.get()
				.chain(getNOOPChainLink(clazz));

		return chain.get(0);
	}
}
