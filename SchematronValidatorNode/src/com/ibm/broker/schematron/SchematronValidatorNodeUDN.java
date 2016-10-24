package com.ibm.broker.schematron;

import com.ibm.broker.config.appdev.InputTerminal;
import com.ibm.broker.config.appdev.Node;
import com.ibm.broker.config.appdev.NodeProperty;
import com.ibm.broker.config.appdev.OutputTerminal;

/*** 
 * <p>  <I>SchematronValidatorNodeUDN</I> instance</p>
 * <p></p>
 */
public class SchematronValidatorNodeUDN extends Node {

	private static final long serialVersionUID = 1L;

	// Node constants
	protected final static String NODE_TYPE_NAME = "com/ibm/broker/schematron/SchematronValidatorNode";
	protected final static String NODE_GRAPHIC_16 = "platform:/plugin/com.ibm.broker.schematron.SchematronValidator/icons/full/obj16/com/ibm/broker/schematron/SchematronValidator.gif";
	protected final static String NODE_GRAPHIC_32 = "platform:/plugin/com.ibm.broker.schematron.SchematronValidator/icons/full/obj30/com/ibm/broker/schematron/SchematronValidator.gif";

	protected final static String PROPERTY_VALIDATE = "validate";
	protected final static String PROPERTY_SCHEMANAME = "schemaName";

	protected NodeProperty[] getNodeProperties() {
		return new NodeProperty[] {
			new NodeProperty(SchematronValidatorNodeUDN.PROPERTY_VALIDATE,		NodeProperty.Usage.OPTIONAL,	true,	NodeProperty.Type.BOOLEAN, "true","","",	"com/ibm/broker/schematron/SchematronValidator",	"com.ibm.broker.schematron.SchematronValidator"),
			new NodeProperty(SchematronValidatorNodeUDN.PROPERTY_SCHEMANAME,		NodeProperty.Usage.MANDATORY,	false,	NodeProperty.Type.STRING, null,"","",	"com/ibm/broker/schematron/SchematronValidator",	"com.ibm.broker.schematron.SchematronValidator")
		};
	}

	public SchematronValidatorNodeUDN() {
	}

	public final InputTerminal INPUT_TERMINAL_IN = new InputTerminal(this,"InTerminal.In");
	@Override
	public InputTerminal[] getInputTerminals() {
		return new InputTerminal[] {
			INPUT_TERMINAL_IN
	};
	}

	public final OutputTerminal OUTPUT_TERMINAL_MATCH = new OutputTerminal(this,"OutTerminal.Match");
	public final OutputTerminal OUTPUT_TERMINAL_FAILURE = new OutputTerminal(this,"OutTerminal.Failure");
	@Override
	public OutputTerminal[] getOutputTerminals() {
		return new OutputTerminal[] {
			OUTPUT_TERMINAL_MATCH,
			OUTPUT_TERMINAL_FAILURE
		};
	}

	@Override
	public String getTypeName() {
		return NODE_TYPE_NAME;
	}

	protected String getGraphic16() {
		return NODE_GRAPHIC_16;
	}

	protected String getGraphic32() {
		return NODE_GRAPHIC_32;
	}

	/**
	 * Set the <I>SchematronValidatorNodeUDN</I> "<I>Validate</I>" property
	 * 
	 * @param boolean value; the value to set the property "<I>Validate</I>"
	 */
	public SchematronValidatorNodeUDN setValidate(boolean value) {
		setProperty(SchematronValidatorNodeUDN.PROPERTY_VALIDATE, String.valueOf(value));
		return this;
	}

	/**
	 * Get the <I>SchematronValidatorNodeUDN</I> "<I>Validate</I>" property
	 * 
	 * @return boolean; the value of the property "<I>Validate</I>"
	 */
	public boolean getValidate(){
	if (getPropertyValue(SchematronValidatorNodeUDN.PROPERTY_VALIDATE).equals("true")){
		return true;
	} else {
		return false;
		}
	}

	/**
	 * Set the <I>SchematronValidatorNodeUDN</I> "<I>Schema Name</I>" property
	 * 
	 * @param String value; the value to set the property "<I>Schema Name</I>"
	 */
	public SchematronValidatorNodeUDN setSchemaName(String value) {
		setProperty(SchematronValidatorNodeUDN.PROPERTY_SCHEMANAME, value);
		return this;
	}

	/**
	 * Get the <I>SchematronValidatorNodeUDN</I> "<I>Schema Name</I>" property
	 * 
	 * @return String; the value of the property "<I>Schema Name</I>"
	 */
	public String getSchemaName() {
		return (String)getPropertyValue(SchematronValidatorNodeUDN.PROPERTY_SCHEMANAME);
	}

	public String getNodeName() {
		String retVal = super.getNodeName();
		if ((retVal==null) || retVal.equals(""))
			retVal = "Schematron Validator";
		return retVal;
	};
}
