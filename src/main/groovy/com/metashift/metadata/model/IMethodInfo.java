package com.metashift.metadata.model;

import com.metashift.metadata.MetadataReaderFactory;

import java.util.List;

/**
 * Provides information about a given method being scanned by the {@link MetadataReaderFactory}
 * @author Navid
 *
 */
public interface IMethodInfo {

	/**
	 * Returns the method name of the method that was scanned. 
	 * @return
	 */
	String getMethodName();
	
	/**
	 * Returns the Class name for the methods return type. 
	 * @return
	 */
	String getReturnTypeClassName();
	
	/**
	 * Returns a list of class names for all of this methods arguments.
	 * @return
	 */
	List<String> getArgumentsClassNames();
	
}
