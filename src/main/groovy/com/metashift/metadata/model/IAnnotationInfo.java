package com.metashift.metadata.model;


import java.util.Map;

public interface IAnnotationInfo {

	
	String getAnnotationType();
	
	/**
	 * Retrieve the attributes of the annotation of the given type.
	 * @return a Map of attributes, with the attribute name as key
	 * (e.g. "value") and the defined attribute value as Map value.
	 * This return value will be <code>null</code> if no matching
	 * annotation is defined.
	 */
	Map<String, Object> getAttributes();
	
	/**
	 * Allows the attributes to be set on this info object. 
	 * @param attributes
	 */
	void setAttributes(Map<String, Object> attributes);
	
}
