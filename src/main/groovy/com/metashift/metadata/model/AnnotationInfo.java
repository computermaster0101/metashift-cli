package com.metashift.metadata.model;

import java.util.Map;

public class AnnotationInfo implements IAnnotationInfo {

	private String annotationType;
	
	private Map<String,Object> attributes;

	/**
	 * @return the annotationType
	 */
	public String getAnnotationType() {
		return annotationType;
	}

	/**
	 * @param annotationType the annotationType to set
	 */
	public void setAnnotationType(String annotationType) {
		this.annotationType = annotationType;
	}

	/**
	 * @return the attributes
	 */
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	/**
	 * @param attributes the attributes to set
	 */
	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}
	
	

}
