/**
 * SchematronValidationException.java
 *
 * (C) Copyright IBM Corp. 2014. All Rights Reserved.
 */
package com.ibm.broker.schematron;

import java.util.Collections;
import java.util.List;

import org.oclc.purl.dsdl.svrl.FailedAssert;

/**
 * Exception thrown when a document is found to be invalid.
 * 
 * @author Diego Rani Mazine
 */
public class SchematronValidationException extends SchematronException {

	/** Serial version ID. */
	private static final long serialVersionUID = 3256553859871939843L;

	/** Failed assertions list. */
	private List<FailedAssert> failedAsserts = Collections.emptyList();

	/**
	 * Constructs a new SchematronValidationException from a failed assertions
	 * list.
	 * 
	 * @param failedAsserts
	 *            failed assertions list.
	 * @throws IllegalArgumentException
	 *             if failedAsserts is null.
	 */
	public SchematronValidationException(List<FailedAssert> failedAsserts) {
		super(formatMessage(failedAsserts));
		setFailedAsserts(failedAsserts);
	}

	/**
	 * Constructs a new SchematronValidationException from a failed assertions
	 * list.
	 * 
	 * @param failedAsserts
	 *            failed assertions list (may be null).
	 * @param cause
	 *            the cause (which is saved for later retrieval by the
	 *            {@link #getCause()} method). (A <tt>null</tt> value is
	 *            permitted, and indicates that the cause is nonexistent or
	 *            unknown.)
	 */
	public SchematronValidationException(List<FailedAssert> failedAsserts,
			Throwable cause) {
		super(formatMessage(failedAsserts), cause);
		setFailedAsserts(failedAsserts);
	}

	/**
	 * Sets the failed assertions list.
	 * 
	 * @param failedAsserts
	 *            the failed assertions list to set.
	 * @throws IllegalArgumentException
	 *             if failedAsserts is null.
	 */
	private void setFailedAsserts(List<FailedAssert> failedAsserts) {
		if (failedAsserts == null) {
			throw new IllegalArgumentException("failedAsserts is null");
		}
		this.failedAsserts = failedAsserts;
	}

	/**
	 * Gets the failed assertions list.
	 * 
	 * @return the an unmodifiable view of the failed assertions list; never
	 *         null.
	 */
	public List<FailedAssert> getFailedAsserts() {
		return Collections.unmodifiableList(failedAsserts);
	}

	/**
	 * Formats the detail message string.
	 * 
	 * @param failedAsserts
	 *            the failed assertions list.
	 * @return the formatted failed assertions list.
	 */
	private static String formatMessage(List<FailedAssert> failedAsserts) {
		if (failedAsserts == null) {
			return null;
		}

		// Gets the number of elements in this list
		final int failedAssertsSize = failedAsserts.size();

		// Formats the detail message string
		final StringBuilder exceptionMessage = new StringBuilder();
		for (int index = 0; index < failedAssertsSize; index++) {
			final FailedAssert failedAssert = failedAsserts.get(index);
			exceptionMessage.append("Assertion failed at ")
					.append(trim(failedAssert.getLocation())).append(": ")
					.append(trim(failedAssert.getText()));
			if (index != failedAssertsSize - 1) {
				exceptionMessage.append(", ");
			}
		}
		return exceptionMessage.toString();
	}

	/**
	 * Returns a copy of the string, with leading and trailing whitespace
	 * omitted.
	 * 
	 * @param str
	 *            the String to be trimmed, may be null.
	 * @return A copy of the string with leading and trailing white space
	 *         removed, or the string if it has no leading or trailing white
	 *         space.
	 */
	private static String trim(String str) {
		if (str == null) {
			return null;
		}
		return str.trim();
	}
}
