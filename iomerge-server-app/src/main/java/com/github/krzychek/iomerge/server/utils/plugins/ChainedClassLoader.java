package com.github.krzychek.iomerge.server.utils.plugins;

import com.google.common.collect.Iterators;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;


class ChainedClassLoader extends ClassLoader {

	private final List<ClassLoader> classLoaders;

	ChainedClassLoader(List<ClassLoader> classLoaders) {
		this.classLoaders = classLoaders;
	}

	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		for (ClassLoader classLoader : classLoaders) {
			try {
				return classLoader.loadClass(name);
			} catch (ClassNotFoundException ignored) {
			}
		}

		throw new ClassNotFoundException("class not found in any chained classLoaders" + name);
	}

	@Override
	public URL getResource(String name) {
		for (ClassLoader classLoader : classLoaders) {
			URL resource = classLoader.getResource(name);
			if (resource != null) return null;
		}
		return null;
	}

	@Override
	public Enumeration<URL> getResources(String name) throws IOException {
		List<Iterator<URL>> result = new ArrayList<>();
		for (ClassLoader classLoader : classLoaders) {
			result.add(Iterators.forEnumeration(classLoader.getResources(name)));
		}

		return Iterators.asEnumeration(Iterators.concat(result.iterator()));
	}

	@Override
	public InputStream getResourceAsStream(String name) {
		return classLoaders.stream()
				.map(classLoader -> classLoader.getResourceAsStream(name))
				.filter(Objects::nonNull)
				.reduce(SequenceInputStream::new)
				.orElseGet(() -> new ByteArrayInputStream(new byte[0]));
	}

	@Override
	public void setDefaultAssertionStatus(boolean enabled) {
		classLoaders.forEach(classLoader -> setDefaultAssertionStatus(enabled));
	}

	@Override
	public void setPackageAssertionStatus(String packageName, boolean enabled) {
		classLoaders.forEach(classLoader -> setPackageAssertionStatus(packageName, enabled));
	}

	@Override
	public void setClassAssertionStatus(String className, boolean enabled) {
		classLoaders.forEach(classLoader -> classLoader.setClassAssertionStatus(className, enabled));
	}

	@Override
	public void clearAssertionStatus() {
		classLoaders.forEach(ClassLoader::clearAssertionStatus);
	}
}
