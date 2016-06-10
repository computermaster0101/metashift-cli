package com.metashift.metadata.model;

import java.util.Map;

/**
 * Defines the annotation and method information for a scanned class.  
 * @author Navid
 *
 */
public interface IMethodAnnotationInfo extends IAnnotationInfo, IMethodInfo{
	
	/**
	 * Returns any annotated Parameters for this AnnotatedMethod.
	 * The method will contain all parameters even if they are not annotated. 
	 * @return Map where the key is the index of the given parameter in the methods argument list and
	 *  the value is the {@link IParameterAnnotationInfo} objects.
	 */
	Map<Integer,IParameterAnnotationInfo> getParameterAnnotationsInfo();
	
}
