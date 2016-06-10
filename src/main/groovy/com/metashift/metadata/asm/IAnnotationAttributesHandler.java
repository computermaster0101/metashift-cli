package com.metashift.metadata.asm;

import java.util.Map;

/**
 * A handler to be called when all the attributes for a given annotation have been collected.
 * @author Navid
 *
 */
public interface IAnnotationAttributesHandler {
	/**
	 * Called when all attributes are collected for a given annotation.
	 * @param attributes
	 */
	void handleAttributes(Map<String, Object> attributes);
}

