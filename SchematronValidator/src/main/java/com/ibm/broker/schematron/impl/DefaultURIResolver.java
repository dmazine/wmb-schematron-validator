/**
 * DefaultURIResolver.java
 *
 * (C) Copyright IBM Corp. 2014. All Rights Reserved.
 */
package com.ibm.broker.schematron.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;

import com.ibm.broker.schematron.util.ResourceLoader;

/**
 * Default URIResolver implementation.
 * 
 * @author Diego Rani Mazine
 */
public class DefaultURIResolver implements URIResolver {

	/** Entity catalog. */
	private Map<String, String> catalog = new HashMap<String, String>();

	/**
	 * Constructs a DefaultURIResolver object.
	 */
	public DefaultURIResolver() {
		// Initializes the entity catalog
		catalog.put("iso_abstract_expand.xsl", "iso_abstract_expand.xsl");
		catalog.put("iso_dsdl_include.xsl", "iso_dsdl_include.xsl");
		catalog.put("iso_schematron_skeleton_for_xslt1.xsl",
				"iso_schematron_skeleton_for_xslt1.xsl");
		catalog.put("iso_svrl_for_xslt1.xsl", "iso_svrl_for_xslt1.xsl");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.xml.transform.URIResolver#resolve(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public Source resolve(String href, String base) throws TransformerException {
		try {
			// Looks up for a matching catalog entry
			return new StreamSource(ResourceLoader.getResourceAsStream(catalog
					.get(href)));
		} catch (IOException e) {
			// Propagates the exception
			throw new TransformerException(e.getMessage(), e);
		}
	}

}
