/**
 * SchematronValidatorMbNode.java
 *
 * (C) Copyright IBM Corp. 2014. All Rights Reserved.
 */
package com.ibm.broker.schematron;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.transform.stream.StreamSource;

import org.xml.sax.InputSource;

import com.ibm.broker.personality.Personality;
import com.ibm.broker.plugin.MbElement;
import com.ibm.broker.plugin.MbException;
import com.ibm.broker.plugin.MbInputTerminal;
import com.ibm.broker.plugin.MbMessageAssembly;
import com.ibm.broker.plugin.MbNode;
import com.ibm.broker.plugin.MbNodeInterface;
import com.ibm.broker.plugin.MbOutputTerminal;
import com.ibm.broker.plugin.MbRecoverableException;
import com.ibm.broker.plugin.MbUtilities;
import com.ibm.broker.resourceobserver.MbFileResourceObserver;

/**
 * Schematron validator user defined node implementation.
 * 
 * @author Diego Rani Mazine.
 * @see <a href="http://xml.ascc.net/resource/schematron/">The Schematron</a>
 */
public class SchematronValidatorMbNode extends MbNode implements
		MbNodeInterface {

	/** Indicates whether message validation is enabled. */
	private boolean validate = true;

	/** Schematron validation schema name. */
	private String schemaName = null;

	/** Schematron validator. */
	private SchematronValidator schematronValidator = null;

	/**
	 * Constructs a SchematronValidatorMbNode object.
	 * 
	 * @throws MbException
	 *             if an error has occurred.
	 */
	public SchematronValidatorMbNode() throws MbException {
		createInputTerminal("In");
		createOutputTerminal("Match");
		createOutputTerminal("Failure");
	}

	/**
	 * Returns the node name.
	 * 
	 * @return the node name.
	 */
	public static String getNodeName() {
		return "SchematronValidatorNode";
	}

	/**
	 * Returns true if message validation is enabled.
	 * 
	 * @return true if message validation is enabled.
	 */
	public String getValidate() {
		return Boolean.toString(validate);
	}

	/**
	 * Enables or disables message validation.
	 * 
	 * @param validate
	 *            true to enable message validation.
	 */
	public void setValidate(String validate) {
		this.validate = Boolean.parseBoolean(validate);
	}

	/**
	 * Gets the Schematron validation schema name.
	 * 
	 * @return the Schematron validation schema name.
	 */
	public String getSchemaName() {
		return schemaName;
	}

	/**
	 * Sets the Schematron validation schema name.
	 * 
	 * @param schemaName
	 *            the Schematron validation schema name.
	 * @throws IllegalArgumentException
	 *             if schemaName is null.
	 */
	public void setSchemaName(String schemaName) {
		if (schemaName == null) {
			throw new IllegalArgumentException("schemaName is null");
		}
		this.schemaName = schemaName;
	}

	/**
	 * Initializes the Schematron validator.
	 * 
	 * @throws SchematronValidatorFactoryConfigurationException
	 *             thrown if the implementation is not available or cannot be
	 *             instantiated.
	 * @throws SchematronValidatorConfigurationException
	 *             it is not possible to create a SchematronValidator instance.
	 * @throws IOException
	 *             if the validation schema could not be found.
	 */
	private void initializeSchematronValidator()
			throws SchematronValidatorFactoryConfigurationException,
			SchematronValidatorConfigurationException, IOException {
		// Current thread context class loader
		ClassLoader currentThreadContextClassLoader = null;

		try {
			// Saves the current thread class loader
			currentThreadContextClassLoader = Thread.currentThread()
					.getContextClassLoader();

			// Sets the current thread context loader
			Thread.currentThread().setContextClassLoader(
					getClass().getClassLoader());

			// Obtains an instance of a SchematronValidatorFactory
			final SchematronValidatorFactory schematronValidatorFactory = SchematronValidatorFactory
					.newInstance();

			// Creates the Schematron validator
			schematronValidator = schematronValidatorFactory
					.newSchematronValidator(getResource(schemaName));
		} finally {
			if (currentThreadContextClassLoader != null) {
				// Restores the current thread class loader
				Thread.currentThread().setContextClassLoader(
						currentThreadContextClassLoader);
			}
		}
	}

	/**
	 * Returns an input source for reading the specified resource.
	 * 
	 * @param name
	 *            the resource name.
	 * @return an input source for reading the resource.
	 * @throws IllegalArgumentException
	 *             if name is null.
	 * @throws SchematronValidatorConfigurationException
	 *             if the name was not found.
	 */
	private InputSource getResource(String name)
			throws SchematronValidatorConfigurationException {
		if (name == null) {
			throw new IllegalArgumentException("name is null");
		}

		try {
			// TODO: Check if its thread safe
			final MbFileResourceObserver resourceObserver = new MbFileResourceObserver(
					System.currentTimeMillis());

			// TODO: Is this necessary?
			// resourceObserver.createImbJniFileResourceObserver();

			// Creates a new input source with the byte stream
			return new InputSource(new ByteArrayInputStream(
					resourceObserver.readResource(schemaName)));
		} catch (MbException e) {
			// Propagates the exception
			throw new SchematronValidatorConfigurationException(String.format(
					"Unable to resolve %s", name), e);
		}
	}

	/**
	 * Obtains an instance of a SchematronValidator.
	 * 
	 * @return an instance of a SchematronValidator.
	 * @throws SchematronValidatorFactoryConfigurationException
	 *             thrown if the implementation is not available or cannot be
	 *             instantiated.
	 * @throws SchematronValidatorConfigurationException
	 *             it is not possible to create a SchematronValidator instance.
	 * @throws IOException
	 *             if the validation schema could not be found.
	 */
	private SchematronValidator getSchematronValidator()
			throws SchematronValidatorFactoryConfigurationException,
			SchematronValidatorConfigurationException, IOException {
		if (schematronValidator == null) {
			synchronized (this) {
				if (schematronValidator == null) {
					// Initializes the Schematron validator
					initializeSchematronValidator();
				}
			}
		}
		return schematronValidator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ibm.broker.plugin.MbNodeInterface#evaluate(com.ibm.broker.plugin.
	 * MbMessageAssembly, com.ibm.broker.plugin.MbInputTerminal)
	 */
	@Override
	public void evaluate(MbMessageAssembly assembly, MbInputTerminal in)
			throws MbException {
		try {
			// TODO: Implement trace
			// import com.ibm.broker.trace.Trace;
			// if(Trace.isOn) {
			// Trace.logNamedEntry("JMSClientInputNode", s);
			// }
			// if(s3 != null) {
			// Trace.logNamedDebugTraceData(this, s, "Java System Property",
			// (new
			// StringBuilder()).append("java.lib.path = ").append(s3).toString());
			// }
			// if(Trace.isOn) {
			// Trace.logNamedExit(this, s);
			// }

			if (validate) {
				// Gets the message body
				final MbElement messageBody = assembly.getMessage()
						.getRootElement().getLastChild();

				// Validates the message
				getSchematronValidator().validate(
						new StreamSource(new ByteArrayInputStream(messageBody
								.toBitstream(null, null, null, 0, 0, 0))));
			}

			// Propagates the message
			final MbOutputTerminal out = getOutputTerminal("Match");
			out.propagate(assembly);
		} catch (SchematronValidationException e) {
			// TODO: Creating message catalogs
			// MbService.logError(this, "evaluate()", "", "", e.getMessage(),
			// null);

			// TODO: Refactor this variable to an attribute
			// Gets the message catalog name
			final String messageCatalogueName = Personality.getInstance()
					.messageCatalogueName();

			// TODO: Correct this
			final MbRecoverableException userException = new MbRecoverableException(
					this, "evaluate()", messageCatalogueName, "2006",
					e.getMessage(), new String[] { e.toString() });

			// Creates the exception list
			MbUtilities.createExceptionList(this, assembly.getExceptionList(),
					userException);

			// Propagates the message
			final MbOutputTerminal out = getOutputTerminal("Failure");
			out.propagate(assembly);
		} catch (MbException e) {
			// TODO: Creating message catalogs
			// MbService.logError(this, "evaluate()", "", "", e.getMessage(),
			// null);

			// Propagates the exception
			throw e;
		} catch (RuntimeException e) {
			// TODO: Creating message catalogs
			// MbService.logError(this, "evaluate()", "", "", e.getMessage(),
			// null);

			// Propagates the exception
			throw e;
		} catch (Exception e) {
			// TODO: Creating message catalogs
			// MbService.logError(this, "evaluate()", "BIPmsgs", "2006",
			// e.getMessage(), new String[] { e.toString() });

			// TODO: Consider replacing Exception with type(s) thrown by user
			// code Example handling ensures all exceptions are re-thrown to be
			// handled in the flow
			throw new MbRecoverableException(this, "evaluate()", "BIPmsgs",
					"2006", e.getMessage(), new String[] { e.toString() });
		}
	}

}
