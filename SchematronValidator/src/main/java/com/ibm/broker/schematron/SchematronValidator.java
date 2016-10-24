/**
 * SchematronValidator.java
 *
 * (C) Copyright IBM Corp. 2014. All Rights Reserved.
 */
package com.ibm.broker.schematron;

import javax.xml.transform.Source;

/**
 * A processor that checks an XML document against Schematron validation rules.
 * 
 * @author Diego Rani Mazine
 * @see <a href="http://xml.ascc.net/resource/schematron/">The Schematron</a>
 */
public interface SchematronValidator {

	/**
	 * Validates the specified input.
	 * 
	 * @param source
	 *            the XML document to be validated.
	 * @throws IllegalArgumentException
	 *             if source is null.
	 * @throws SchematronValidationException
	 *             if the document is found to be invalid.
	 * @throws SchematronException
	 *             if an exceptional condition that occurred during the
	 *             validation process.
	 */
	public void validate(Source source) throws SchematronValidationException,
			SchematronException;

}
