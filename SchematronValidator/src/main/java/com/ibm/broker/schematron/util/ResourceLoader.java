/**
 * ResourceLoader.java
 *
 * (C) Copyright IBM Corp. 2014. All Rights Reserved.
 */
package com.ibm.broker.schematron.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Utility class for loading resources.
 * 
 * @author Diego Rani Mazine
 */
public class ResourceLoader {

	/**
	 * Constructs a ResourceLoader object.
	 */
	private ResourceLoader() {
	}

	/**
	 * Returns the context ClassLoader for the currently executing thread.
	 * 
	 * @return the context ClassLoader for the currently executing thread.
	 */
	public static ClassLoader getContextClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}

	/**
	 * Finds the resource with the given name.
	 * 
	 * @param name
	 *            the resource name.
	 * @return a URL object for reading the resource, or null if the resource
	 *         could not be found or the invoker doesn't have adequate
	 *         privileges to get the resource.
	 * @throws IllegalArgumentException
	 *             if name is null.
	 * @throws IOException
	 *             if the resource could not be found.
	 */
	public static URL getResource(String name) throws IOException {
		return getResource(name, null);
	}

	/**
	 * Finds the resource with the given name.
	 * 
	 * @param name
	 *            the resource name.
	 * @param classLoader
	 *            the ClassLoader used to load the resource. If null current
	 *            Thread's context classLoader is used.
	 * @return a URL object for reading the resource.
	 * @throws IllegalArgumentException
	 *             if name is null.
	 * @throws IOException
	 *             if the resource could not be found.
	 */
	public static URL getResource(String name, ClassLoader classLoader)
			throws IOException {
		if (name == null) {
			throw new IllegalArgumentException("name is null");
		}

		URL url = null;
		if (classLoader != null) {
			url = classLoader.getResource(name);
		} else {
			url = getContextClassLoader().getResource(name);
		}
		if (url == null) {
			throw new IOException(String.format("Resource not found: %s", name));
		}
		return url;
	}

	/**
	 * Returns an input stream for reading the specified resource.
	 * 
	 * @param name
	 *            the resource name.
	 * @return an input stream for reading the resource, or null if the resource
	 *         could not be found.
	 * @throws IllegalArgumentException
	 *             if name is null.
	 * @throws IOException
	 *             if the resource could not be found.
	 */
	public static InputStream getResourceAsStream(String name)
			throws IOException {
		return getResourceAsStream(name, null);
	}

	/**
	 * Returns an input stream for reading the specified resource.
	 * 
	 * @param name
	 *            the resource name.
	 * @param classLoader
	 *            the ClassLoader used to load the resource. If null current
	 *            Thread's context classLoader is used.
	 * @return an input stream for reading the resource, or null if the resource
	 *         could not be found.
	 * @throws IllegalArgumentException
	 *             if name is null.
	 * @throws IOException
	 *             if the resource could not be found.
	 */
	public static InputStream getResourceAsStream(String name,
			ClassLoader classLoader) throws IOException {
		if (name == null) {
			throw new IllegalArgumentException("name is null");
		}

		InputStream inputStream = null;
		if (classLoader != null) {
			inputStream = classLoader.getResourceAsStream(name);
		} else {
			inputStream = getContextClassLoader().getResourceAsStream(name);
		}
		if (inputStream == null) {
			throw new IOException(String.format("Resource not found: %s", name));
		}
		return inputStream;
	}

	/**
	 * Loads the class with the specified binary name.
	 * 
	 * @param name
	 *            the binary name of the class.
	 * @return the resulting Class object.
	 * @throws IllegalArgumentException
	 *             if name is null.
	 * @throws ClassNotFoundException
	 *             if the class was not found.
	 */
	public static Class<?> loadClass(String name) throws ClassNotFoundException {
		return loadClass(name, null);
	}

	/**
	 * Loads the class with the specified binary name.
	 * 
	 * @param name
	 *            the binary name of the class.
	 * @param classLoader
	 *            the ClassLoader used to load the resource. If null current
	 *            Thread's context classLoader is used.
	 * @return the resulting Class object.
	 * @throws IllegalArgumentException
	 *             if name is null.
	 * @throws ClassNotFoundException
	 *             if the class was not found.
	 */
	public static Class<?> loadClass(String name, ClassLoader classLoader)
			throws ClassNotFoundException {
		if (name == null) {
			throw new IllegalArgumentException("name is null");
		}

		if (classLoader != null) {
			return classLoader.loadClass(name);
		} else {
			return getContextClassLoader().loadClass(name);
		}
	}

	/**
	 * Creates a new instance of a class.
	 * 
	 * @param name
	 *            the class name.
	 * @return a newly allocated instance of the class represented by this
	 *         object.
	 * @throws IllegalArgumentException
	 *             if name is null.
	 * @throws InstantiationException
	 *             if this Class represents an abstract class, an interface, an
	 *             array class, a primitive type, or void; or if the class has
	 *             no nullary constructor; or if the instantiation fails for
	 *             some other reason.
	 * @throws IllegalAccessException
	 *             if the class or its nullary constructor is not accessible.
	 * @throws ClassNotFoundException
	 *             if the class was not found.
	 */
	public static Object newInstance(String name)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		return newInstance(name, null);
	}

	/**
	 * Creates a new instance of a class.
	 * 
	 * @param name
	 *            the class name.
	 * @return a newly allocated instance of the class represented by this
	 *         object.
	 * @param classLoader
	 *            the ClassLoader used to load the resource. If null current
	 *            Thread's context classLoader is used.
	 * @throws IllegalArgumentException
	 *             if name is null.
	 * @throws InstantiationException
	 *             if this Class represents an abstract class, an interface, an
	 *             array class, a primitive type, or void; or if the class has
	 *             no nullary constructor; or if the instantiation fails for
	 *             some other reason.
	 * @throws IllegalAccessException
	 *             if the class or its nullary constructor is not accessible.
	 * @throws ClassNotFoundException
	 *             if the class was not found.
	 */
	public static Object newInstance(String name, ClassLoader classLoader)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		if (name == null) {
			throw new IllegalArgumentException("name is null");
		}

		return loadClass(name, classLoader).newInstance();
	}

}
