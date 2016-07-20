package com.github.krzychek.iomerge.server.utils;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


public class BooleanCondition {

	private final ReentrantLock lock;
	private volatile boolean yes;
	private Condition internal;

	public BooleanCondition() {
		lock = new ReentrantLock();
		internal = lock.newCondition();
		yes = false;
	}

	public boolean yes() {
		return yes;
	}

	public boolean no() {
		return !yes;
	}

	public BooleanCondition makeYes() {
		try {
			lock.lock();
			yes = true;
			internal.signal();

		} finally {
			lock.unlock();
		}
		return this;
	}

	public BooleanCondition makeNo() {
		try {
			lock.lock();

			yes = false;
			internal.signal();

		} finally {
			lock.unlock();
		}
		return this;
	}

	public BooleanCondition whileTrue(Runnable runnable) {
		while (yes) runnable.run();
		return this;
	}

	public void await() {
		try {
			lock.lock();
			internal.awaitUninterruptibly();
		} finally {
			lock.unlock();
		}
	}
}
