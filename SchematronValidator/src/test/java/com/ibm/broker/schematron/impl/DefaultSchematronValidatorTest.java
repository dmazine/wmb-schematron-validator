/**
 * DefaultSchematronValidatorTest.java
 *
 * (C) Copyright IBM Corp. 2014. All Rights Reserved.
 */
package com.ibm.broker.schematron.impl;

import java.io.IOException;

import javax.xml.transform.stream.StreamSource;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.InputSource;

import com.ibm.broker.schematron.SchematronException;
import com.ibm.broker.schematron.SchematronValidationException;
import com.ibm.broker.schematron.SchematronValidator;
import com.ibm.broker.schematron.SchematronValidatorConfigurationException;
import com.ibm.broker.schematron.SchematronValidatorFactory;
import com.ibm.broker.schematron.SchematronValidatorFactoryConfigurationException;
import com.ibm.broker.schematron.util.ResourceLoader;

/**
 * DefaultSchematronValidator test case.
 * 
 * @author Diego Rani Mazine
 */
public class DefaultSchematronValidatorTest {

	/** Schematron validator factory. */
	private SchematronValidatorFactory schematronValidatorFactory = null;

	/** Schematron validator. */
	private SchematronValidator schematronValidator = null;

	/**
	 * Constructs a DefaultSchematronValidatorTest object.
	 */
	public DefaultSchematronValidatorTest() {
	}

	/**
	 * Sets up the test case.
	 * 
	 * @throws SchematronValidatorFactoryConfigurationException
	 *             thrown if the implementation is not available or cannot be
	 *             instantiated.
	 * @throws SchematronValidatorConfigurationException
	 *             it is not possible to create a SchematronValidator instance.
	 * @throws IOException
	 *             if an I/O error has occurred.
	 */
	@Before
	public void setUp()
			throws SchematronValidatorFactoryConfigurationException,
			SchematronValidatorConfigurationException, IOException {
		// Obtains a new instance of a SchematronValidatorFactory
		schematronValidatorFactory = SchematronValidatorFactory.newInstance();

		// Creates the Schematron validator
		schematronValidator = schematronValidatorFactory
				.newSchematronValidator(new InputSource(ResourceLoader
						.getResourceAsStream("example.sch")));
	}

	/**
	 * Test method for
	 * {@link com.ibm.xml.validation.schematron.impl.DefaultSchematronValidator#validate(javax.xml.transform.Source)}
	 * .
	 * 
	 * @throws IOException
	 *             if an I/O error has occurred.
	 * @throws SchematronValidationException
	 *             if the document is found to be invalid.
	 * @throws SchematronException
	 *             if an exceptional condition that occurred during the
	 *             validation process.
	 */
	@Test
	public void testValidMessage() throws SchematronValidationException,
			SchematronException, IOException {
		// Validates the specified input
		schematronValidator.validate(new StreamSource(ResourceLoader
				.getResourceAsStream("valid-message.xml")));
	}

	/**
	 * Test method for
	 * {@link com.ibm.xml.validation.schematron.impl.DefaultSchematronValidator#validate(javax.xml.transform.Source)}
	 * .
	 * 
	 * @throws IOException
	 *             if an I/O error has occurred.
	 * @throws SchematronValidationException
	 *             if the document is found to be invalid.
	 * @throws SchematronException
	 *             if an exceptional condition that occurred during the
	 *             validation process.
	 */
	@Test(expected = SchematronValidationException.class)
	public void testInvalidMessage() throws SchematronValidationException,
			SchematronException, IOException {

//InputStream i = ResourceLoader
//.getResourceAsStream("invalid-message.xml");
//while(true) {
//	int b = i.read();
//	if (b==-1) break;
//	System.out.print((char)b);
//}
//System.out.println();


		// Validates the specified input
		schematronValidator.validate(new StreamSource(ResourceLoader
				.getResourceAsStream("invalid-message.xml")));
	}

}
