/**
 * SchematronValidatorFactory.java
 *
 * (C) Copyright IBM Corp. 2014. All Rights Reserved.
 */
package com.ibm.broker.schematron;

import org.xml.sax.InputSource;

import com.ibm.broker.schematron.impl.DefaultSchematronValidatorFactory;
import com.ibm.broker.schematron.util.ResourceLoader;

/**
 * Defines a factory API that enables applications to obtain a validator.
 * 
 * @author Diego Rani Mazine
 */
public abstract class SchematronValidatorFactory {

	/** Default factory implementation. */
	public static final String DEFAULT_FACTORY = DefaultSchematronValidatorFactory.class.getCanonicalName();

	/**
	 * Constructs a SchematronValidatorFactory object.
	 */
	protected SchematronValidatorFactory() {
	}

	/**
	 * Obtains a new instance of a SchematronValidatorFactory. This static
	 * method creates a new factory instance.
	 * 
	 * @return a new instance of a SchematronValidatorFactory.
	 * @throws SchematronValidatorFactoryConfigurationException
	 *             thrown if the implementation is not available or cannot be
	 *             instantiated.
	 */
	public static SchematronValidatorFactory newInstance()
			throws SchematronValidatorFactoryConfigurationException {
		return newInstance(DEFAULT_FACTORY, null);
	}

	/**
	 * Obtains a new instance of a SchematronValidatorFactory from class name.
	 * 
	 * @param factoryClassName
	 *            fully qualified factory class name that provides
	 *            implementation of
	 *            com.ibm.broker.schematronValidatorFactory.
	 * @param classLoader
	 *            classLoader used to load the factory class. If null current
	 *            Thread's context classLoader is used to load the factory
	 *            class.
	 * @return a new instance of a SchematronValidatorFactory.
	 * @throws IllegalArgumentException
	 *             if factoryClassName is null.
	 * @throws SchematronValidatorFactoryConfigurationException
	 *             thrown if the implementation is not available or cannot be
	 *             instantiated.
	 */
	public static SchematronValidatorFactory newInstance(
			String factoryClassName, ClassLoader classLoader)
			throws SchematronValidatorFactoryConfigurationException {
		if (factoryClassName == null) {
			throw new IllegalArgumentException("factoryClassName is null");
		}

		try {
			// Obtains a new instance of a SchematronValidatorFactory
			return (SchematronValidatorFactory) ResourceLoader.newInstance(
					factoryClassName, classLoader);
		} catch (Exception e) {
			// Propagates the exception
			throw new SchematronValidatorFactoryConfigurationException(
					e.getMessage(), e);
		}
	}

	/**
	 * Creates a new instance of a SchematronValidator.
	 * 
	 * @param source
	 *            source of SCH document used to create validator.
	 * @return a new instance of a SchematronValidator.
	 * @throws SchematronValidatorConfigurationException
	 *             it is not possible to create a SchematronValidator instance.
	 */
	public abstract SchematronValidator newSchematronValidator(
			InputSource source)
			throws SchematronValidatorConfigurationException;

}
