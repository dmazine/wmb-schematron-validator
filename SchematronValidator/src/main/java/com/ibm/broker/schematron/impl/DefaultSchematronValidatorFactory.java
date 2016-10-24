/**
 * DefaultSchematronValidatorFactory.java
 *
 * (C) Copyright IBM Corp. 2014. All Rights Reserved.
 */
package com.ibm.broker.schematron.impl;

import org.xml.sax.InputSource;

import com.ibm.broker.schematron.SchematronValidator;
import com.ibm.broker.schematron.SchematronValidatorConfigurationException;
import com.ibm.broker.schematron.SchematronValidatorFactory;

/**
 * SchematronValidatorFactory default implementation.
 * 
 * @author Diego Rani Mazine
 */
public class DefaultSchematronValidatorFactory extends
		SchematronValidatorFactory {

	/**
	 * Constructs a DefaultSchematronValidatorFactory object.
	 */
	public DefaultSchematronValidatorFactory() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.broker.schematronValidatorFactory#
	 * newSchematronValidator(org.xml.sax.InputSource)
	 */
	@Override
	public SchematronValidator newSchematronValidator(InputSource source)
			throws SchematronValidatorConfigurationException {
		return new DefaultSchematronValidator(source);
	}

}
