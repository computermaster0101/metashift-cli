package com.metashift.metadata.model;

/**
 * Represents any annotations for parameters of a given method. 
 * @author Navid
 *
 */
public interface IParameterAnnotationInfo extends IAnnotationInfo {
	
	
	/**
	 * Returns the {@link IMethodInfo} that this parameter is for. 
	 * This may be a ({@link IMethodAnnotationInfo} if the parent method also has annotations. 
	 * @return
	 */
	IMethodInfo getParentMethodInfo();
	
	/**
	 * The index for the given parameter. This would be the order the annotated parameter appears in the method argument list.
	 * @return
	 */
	Integer getParameterIndex();
	
	
}
