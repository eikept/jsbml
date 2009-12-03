package org.sbml.jsbml.xml.sbmlParsers;

import java.util.ArrayList;
import java.util.Date;

import org.sbml.jsbml.element.Annotation;
import org.sbml.jsbml.element.ModelHistory;
import org.sbml.jsbml.element.SBMLDocument;
import org.sbml.jsbml.xml.DateProcessor;
import org.sbml.jsbml.xml.SBMLObjectForXML;
import org.sbml.jsbml.xml.SBMLParser;
import org.w3c.util.DateParser;
import org.w3c.util.InvalidDateException;

public class DatesParser implements SBMLParser{
	
	private String previousElement = "";
	private boolean hasReadCreated = false;
	boolean hasReadW3CDTF = false;
	boolean hasReadModified = false;

	public void processCharactersOf(String elementName, String characters,
			Object contextObject) {
		
		if (elementName != null){
			if (contextObject instanceof ModelHistory){
				ModelHistory modelHistory = (ModelHistory) contextObject;
				DateProcessor dateProcessor = new DateProcessor();
				
				if (elementName.equals("W3CDTF") && hasReadW3CDTF){
					if (hasReadCreated && previousElement.equals("created")){
						String stringDate = dateProcessor.formatToW3CDTF(characters);

						try {
							Date createdDate = DateParser.parse(stringDate);
							modelHistory.setCreatedDate(createdDate);
						} catch (InvalidDateException e) {
							// TODO : can't create a Date, what to do?
							e.printStackTrace();
						}
					}
					else if (previousElement.equals("modified")){
						String stringDate = dateProcessor.formatToW3CDTF(characters);

						try {
							Date modifiedDate = DateParser.parse(stringDate);
							modelHistory.setModifiedDate(modifiedDate);
						} catch (InvalidDateException e) {
							// TODO : can't create a Date, what to do?
							e.printStackTrace();
						}
					}
					else {
						// TODO : SBML syntax error, what to do?
					}
				}
				else {
					// TODO : SBML syntax error, what to do?
				}
			}
			else {
				// TODO : the date instances are only created for the model history object in the annotation. Throw an error?
			}
		}
	}

	public void processEndElement(String elementName, String prefix,
			boolean isNested, Object contextObject) {
		
		if (contextObject instanceof ModelHistory){
			if (elementName.equals("created") || elementName.equals("modified")){
				this.previousElement = "";
				hasReadW3CDTF = false;
				if (elementName.equals("created")){
					hasReadCreated = false;
				}
			}
			else {
				// TODO : the date instances are only created for the created and/or modified nodes in the annotation. Throw an error?
			}
		}
		else {
			// TODO : the date instances are only created for the model history object in the annotation. Throw an error?
		}
	}

	public Object processStartElement(String elementName, String prefix,
			boolean hasAttributes, boolean hasNamespaces,
			Object contextObject) {

		if (contextObject instanceof Annotation){
			Annotation modelAnnotation = (Annotation) contextObject;

			if (modelAnnotation.isSetModelHistory()){
				ModelHistory modelHistory = modelAnnotation.getModelHistory();

				if (elementName.equals("created") && !hasReadCreated){
					hasReadCreated = true;
					this.previousElement = elementName;
					
					return modelHistory;
				}
				else if (elementName.equals("modified")){
					this.previousElement = elementName;
					
					return modelHistory;
				}
				else {
					// TODO : SBML syntax error, what to do?
				}
			}
			else {
				// TODO : create a modelHistory instance? throw an exception?
			}
		}
		else if (contextObject instanceof ModelHistory){
			
			if (elementName.equals("W3CDTF") && (previousElement.equals("created") || previousElement.equals("modified"))){
				hasReadW3CDTF = true;
			}
		}
		else {
			// TODO : should be changed depending on the version. Now, there is not only the model which contain a model history. 
		}
		return contextObject;
	}

	public void processEndDocument(SBMLDocument sbmlDocument) {
		previousElement = "";
		hasReadCreated = false;
	}

	public void processNamespace(String elementName, String URI, String prefix,
			String localName, boolean hasAttributes, boolean isLastNamespace,
			Object contextObject) {
		if (elementName.equals("RDF") && contextObject instanceof Annotation){
			Annotation annotation = (Annotation) contextObject;
			
			annotation.addRDFAnnotationNamespace(localName, prefix, URI);
		}
	}

	public ArrayList<Object> getListOfSBMLElementsToWrite(Object objectToWrite) {
		return null;
	}

	public void writeElement(SBMLObjectForXML xmlObject, Object sbmlElementToWrite) {
		
	}

	public void writeAttributes(SBMLObjectForXML xmlObject,
			Object sbmlElementToWrite) {
		// TODO Auto-generated method stub
		
	}

	public void writeCharacters(SBMLObjectForXML xmlObject,
			Object sbmlElementToWrite) {
		// TODO Auto-generated method stub
		
	}

	public void writeNamespaces(SBMLObjectForXML xmlObject,
			Object sbmlElementToWrite) {
		// TODO Auto-generated method stub
		
	}

	public void processAttribute(String ElementName, String AttributeName,
			String value, String prefix, boolean isLastAttribute,
			Object contextObject) {
		// TODO : There is no attributes with the namespace "http://purl.org/dc/terms/". There is a SBML
		// syntax error, throw an exception?
	}
}
