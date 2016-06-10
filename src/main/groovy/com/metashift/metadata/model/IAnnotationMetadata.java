package com.metashift.metadata.model;

import java.util.List;
import java.util.Set;

public interface IAnnotationMetadata extends IClassMetadata {
	
	/**
	 * Return the names of all annotation types defined on the underlying class.
	 * @return the annotation type names
	 */
	Set<String> getAnnotationTypes();
	
	/**
	 * Determine whether the underlying class has an annotation of the given
	 * type defined.
	 * @param annotationType the annotation type to look for
	 * @return whether a matching annotation is defined
	 */
	boolean hasAnnotation(String annotationType);
	
	/**
	 * Retrieve all information for the annotations of the given type,
	 * if any (i.e. if defined on the underlying class).
	 * @param annotationType the annotation type to look for
	 * @return a List of {@link AnnotationInfo} objects one for each occurrence of a given annotation type. 
	 * 	If this is a class level annotation type there should only be one object in the list.
	 *  The {@link AnnotationInfo} may be of a specialized type depending on the Annotations Target. 
	 * This return value will be <code>null</code> if no matching
	 * annotation is defined.
	 * @see IAnnotationInfo
	 * @see IParameterAnnotationInfo
	 * @see IMethodAnnotationInfo
	 */
	List<IAnnotationInfo> getAnnotationsInfo(String annotationType);
	
}
