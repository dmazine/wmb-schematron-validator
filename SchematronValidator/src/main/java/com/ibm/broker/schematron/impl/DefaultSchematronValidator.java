/**
 * DefaultSchematronValidator.java
 *
 * (C) Copyright IBM Corp. 2014. All Rights Reserved.
 */
package com.ibm.broker.schematron.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.util.JAXBResult;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.oclc.purl.dsdl.svrl.FailedAssert;
import org.oclc.purl.dsdl.svrl.SchematronOutput;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLFilter;
import org.xml.sax.XMLReader;

import com.ibm.broker.schematron.SchematronException;
import com.ibm.broker.schematron.SchematronValidationException;
import com.ibm.broker.schematron.SchematronValidator;
import com.ibm.broker.schematron.SchematronValidatorConfigurationException;
import com.ibm.broker.schematron.util.ResourceLoader;

/**
 * Default SchematronValidator interface implementation.
 * 
 * @author Diego Rani Mazine
 */
class DefaultSchematronValidator implements SchematronValidator {

	/** XML schema. */
	private static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";

	/** Schema language. */
	private static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";

	/** Schema source. */
	private static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";

	/** Validation schema template. */
	private Templates validationSchemaTemplates = null;

	/** JAXB context. */
	private JAXBContext jaxbContext = null;

	/**
	 * Constructs a new DefaultSchematronValidator object.
	 * 
	 * @param source
	 *            source of SCH document used to create the validator.
	 * @throws IllegalArgumentException
	 *             if source is null.
	 * @throws SchematronValidatorConfigurationException
	 *             it is not possible to create a the validator instance.
	 */
	public DefaultSchematronValidator(InputSource source)
			throws SchematronValidatorConfigurationException {
		if (source == null) {
			throw new IllegalArgumentException("source is null");
		}

		try {
			// Compiles the Schematron schema into an XSLT script
			this.validationSchemaTemplates = compileSchema(source);

			// Obtains a new instance of a JAXBContext
			this.jaxbContext = JAXBContext
					.newInstance("org.oclc.purl.dsdl.svrl");
		} catch (Exception e) {
			// Propagates the exception
			throw new SchematronValidatorConfigurationException(e.getMessage(),
					e);
		}
	}

	/**
	 * Compiles the Schematron schema into an XSLT script.
	 * 
	 * @param source
	 *            source of SCH document containing the validation rules.
	 * @return a Templates object capable of being used for transformation
	 *         purposes, never null.
	 * @throws IllegalArgumentException
	 *             if source is null.
	 * @throws IOException
	 *             if the resource could not be found.
	 * @throws ParserConfigurationException
	 *             if a parser cannot be created which satisfies the requested
	 *             configuration.
	 * @throws SAXException
	 *             for SAX errors.
	 * @throws TransformerException
	 *             if an unrecoverable error occurs during the course of the
	 *             transformation.
	 */
	private Templates compileSchema(InputSource source) throws IOException,
			ParserConfigurationException, SAXException, TransformerException {
		if (source == null) {
			throw new IllegalArgumentException("source is null");
		}

		// Creates a new instance of a SAXParserFactory
		final SAXParserFactory parserFactory = newSAXParserFactory(true);

		// Creates a new instance of a SAXParser
		final SAXParser parser = newSAXParser(parserFactory);

		// Gets the XML reader
		final XMLReader xmlReader = parser.getXMLReader();

		// Creates the error handler
		final ErrorHandler errorHandler = new DefaultErrorHandler();
		xmlReader.setErrorHandler(errorHandler);

		// Obtains a new instance of a SAXTransformerFactory
		final SAXTransformerFactory transformerFactory = newSAXTransformerFactory();

		// Assembles the schema from various parts
		final XMLFilter isoDsdlIncludeFilter = transformerFactory
				.newXMLFilter(new StreamSource(ResourceLoader
						.getResourceAsStream("iso_dsdl_include.xsl")));
		isoDsdlIncludeFilter.setParent(xmlReader);
		isoDsdlIncludeFilter.setErrorHandler(errorHandler);

		// Converts abstract patterns to real patterns
		final XMLFilter isoAbstractExpandFilter = transformerFactory
				.newXMLFilter(new StreamSource(ResourceLoader
						.getResourceAsStream("iso_abstract_expand.xsl")));
		isoAbstractExpandFilter.setParent(isoDsdlIncludeFilter);
		isoAbstractExpandFilter.setErrorHandler(errorHandler);

		// Compiles the Schematron schema into an XSLT script
		final XMLFilter isoSvrlFilter = transformerFactory
				.newXMLFilter(new StreamSource(ResourceLoader
						.getResourceAsStream("iso_svrl_for_xslt1.xsl")));
		isoSvrlFilter.setParent(isoAbstractExpandFilter);
		isoSvrlFilter.setErrorHandler(errorHandler);

		// Creates the transformation result output stream
		final ByteArrayOutputStream resultOutputStream = new ByteArrayOutputStream();

		// Creates a new instance of a Transformer
		final Transformer transformer = newTransformer(transformerFactory);

		// Compiles the Schematron schema into an XSLT script
		transformer.transform(new SAXSource(isoSvrlFilter, source),
				new StreamResult(resultOutputStream));

		// Processes the Source into a Templates object
		return transformerFactory.newTemplates(new StreamSource(
				new ByteArrayInputStream(resultOutputStream.toByteArray())));
	}

	/**
	 * Obtains a new instance of a SAXParserFactory.
	 * 
	 * @param validating
	 *            true if the parser produced by this code will validate
	 *            documents as they are parsed, false otherwise.
	 * @return the new instance of a SAXParserFactory.
	 * @throws FactoryConfigurationError
	 *             if the factory class cannot be loaded, instantiated.
	 */
	private SAXParserFactory newSAXParserFactory(boolean validating) {
		// Creates a new instance of a SAXParserFactory
		final SAXParserFactory parserFactory = SAXParserFactory.newInstance();
		if (validating) {
			parserFactory.setNamespaceAware(true);
			parserFactory.setValidating(true);
		}
		return parserFactory;
	}

	/**
	 * Creates a new instance of a SAXParser using the currently configured
	 * factory parameters.
	 * 
	 * @param parserFactory
	 *            the parser factory implementation to use.
	 * @return a new instance of a SAXParser.
	 * @throws IllegalArgumentException
	 *             if parserFactory is null.
	 * @throws IOException
	 *             if the resource could not be found.
	 * @throws ParserConfigurationException
	 *             if a parser cannot be created which satisfies the requested
	 *             configuration.
	 * @throws SAXException
	 *             for SAX errors.
	 */
	private SAXParser newSAXParser(SAXParserFactory parserFactory)
			throws IOException, ParserConfigurationException, SAXException {
		if (parserFactory == null) {
			throw new IllegalArgumentException("parserFactory is null");
		}

		// Schemas declared in the application
		final String[] schemas = {
				ResourceLoader.getResource("schematron.xsd").toString(),
				ResourceLoader.getResource("svrl.xsd").toString(),
				ResourceLoader.getResource("xml.xsd").toString() };

		// Creates a new instance of a SAXParser
		final SAXParser parser = parserFactory.newSAXParser();
		parser.setProperty(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
		parser.setProperty(JAXP_SCHEMA_SOURCE, schemas);
		return parser;
	}

	/**
	 * Obtains a new instance of a SAXTransformerFactory.
	 * 
	 * @return a new SAXTransformerFactory instance.
	 * @throws TransformerFactoryConfigurationError
	 *             if the factory class cannot be loaded, instantiated.
	 */
	private SAXTransformerFactory newSAXTransformerFactory()
			throws TransformerFactoryConfigurationError {
		// Creates a new instance of a TransformerFactory
		final TransformerFactory transformerFactory = TransformerFactory
				.newInstance();

		// Asserts that required features are supported
		if (!transformerFactory.getFeature(SAXTransformerFactory.FEATURE)) {
			throw new TransformerFactoryConfigurationError(
					"SAXTransformerFactory not supported");
		}
		if (!transformerFactory.getFeature(SAXSource.FEATURE)) {
			throw new TransformerFactoryConfigurationError(
					"SAXSource not supported");
		}
		if (!transformerFactory.getFeature(SAXResult.FEATURE)) {
			throw new TransformerFactoryConfigurationError(
					"SAXResult not supported");
		}
		if (!transformerFactory
				.getFeature(SAXTransformerFactory.FEATURE_XMLFILTER)) {
			throw new TransformerFactoryConfigurationError(
					"XMLFilter not supported");
		}

		// Returns the created instance
		final SAXTransformerFactory saxTransformerFactory = (SAXTransformerFactory) transformerFactory;
		transformerFactory.setURIResolver(new DefaultURIResolver());
		return saxTransformerFactory;
	}

	/**
	 * Obtains a new instance of a Transformer.
	 * 
	 * @param transformerFactory
	 *            the transformer factory implementation to use.
	 * @return a new Transformer instance.
	 * @throws IllegalArgumentException
	 *             if transformerFactory is null.
	 * @throws TransformerConfigurationException
	 *             when it is not possible to create a Transformer instance.
	 */
	private Transformer newTransformer(TransformerFactory transformerFactory)
			throws TransformerConfigurationException {
		if (transformerFactory == null) {
			throw new IllegalArgumentException("transformerFactory is null");
		}

		// Creates a new instance of a Transformer
		return transformerFactory.newTransformer();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ibm.broker.schematronValidator#validate(javax.
	 * xml.transform.Source)
	 */
	@Override
	public void validate(Source source) throws SchematronValidationException,
			SchematronException {
		if (source == null) {
			throw new IllegalArgumentException("source is null");
		}

		try {
			// Create a new transformation context for this Templates object
			final Transformer validationSchemaTransformer = validationSchemaTemplates
					.newTransformer();

			// Creates a new instance of a JAXBResult
			final JAXBResult validationResult = new JAXBResult(jaxbContext);

			// Transforms the XML Source to a Result
			validationSchemaTransformer.transform(source, validationResult);

			// Gets the failed assertions list
			final List<FailedAssert> failedAsserts = getFailedAsserts((SchematronOutput) validationResult
					.getResult());

			// Checks if the document is invalid
			if (!failedAsserts.isEmpty()) {
				// Launches the validation exception
				throw new SchematronValidationException(failedAsserts);
			}
		} catch (SchematronValidationException e) {
			// Propagates the exception
			throw e;
		} catch (Exception e) {
			// Propagates the exception
			throw new SchematronException(e.getMessage(), e);
		}
	}

	/**
	 * Returns the validation result failed assertions list.
	 * 
	 * @param validatioResult
	 *            the Schematron validation result.
	 * @return the validation result failed assertions list.
	 * @throws IllegalArgumentException
	 *             if validatioResult is null.
	 */
	private List<FailedAssert> getFailedAsserts(SchematronOutput validatioResult) {
		if (validatioResult == null) {
			throw new IllegalArgumentException("validatioResult is null");
		}

		// Gets the failed assertions list
		final List<FailedAssert> failedAsserts = new LinkedList<FailedAssert>();
		for (Object object : validatioResult
				.getActivePatternAndFiredRuleAndFailedAssert()) {
			if (object instanceof FailedAssert) {
				failedAsserts.add((FailedAssert) object);
			}
		}
		return failedAsserts;
	}

}
