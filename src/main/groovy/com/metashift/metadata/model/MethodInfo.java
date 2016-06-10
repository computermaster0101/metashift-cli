package com.metashift.metadata.model;

import java.util.List;

public class MethodInfo implements IMethodInfo {

	private String methodName;
	
	private String returnTypeClassName;
	
	private List<String> argumentsClassNames;

	
	/**
	 * @return the returnTypeClassName
	 */
	public String getReturnTypeClassName() {
		return returnTypeClassName;
	}



	/**
	 * @param returnTypeClassName the returnTypeClassName to set
	 */
	public void setReturnTypeClassName(String returnTypeClassName) {
		this.returnTypeClassName = returnTypeClassName;
	}



	/**
	 * @return the argumentsClassNames
	 */
	public List<String> getArgumentsClassNames() {
		return argumentsClassNames;
	}



	/**
	 * @param argumentsClassNames the argumentsClassNames to set
	 */
	public void setArgumentsClassNames(List<String> argumentsClassNames) {
		this.argumentsClassNames = argumentsClassNames;
	}



	/**
	 * @param methodName the methodName to set
	 */
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}



	/* (non-Javadoc)
	 * @see net.executime.utility.annotations.IMethodAnnotationAttribute#getMethodName()
	 */
	public String getMethodName() {
		return methodName;
	}

}
