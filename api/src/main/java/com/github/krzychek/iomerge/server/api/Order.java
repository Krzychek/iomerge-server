package com.github.krzychek.iomerge.server.api;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * specifies order of element in object chain.
 * App components have Order value of 0, which means one should use nagative value in order to place your object before method calls gets to
 * IOMerge main implementation of Chainable sub-interface.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Order {

	int value();
}
