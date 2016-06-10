package com.metashift.metadata.model;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author Navid
 *
 */
public class MethodAnnotationInfo extends AnnotationInfo implements IMethodAnnotationInfo,IMethodInfo {

	private IMethodInfo methodInfo;

	private Map<Integer,IParameterAnnotationInfo> parameterAnnotationsInfo;
	
	/**
	 * @return the methodInfo
	 */
	public IMethodInfo getMethodInfo() {
		return methodInfo;
	}

	/**
	 * @param methodInfo the methodInfo to set
	 */
	public void setMethodInfo(IMethodInfo methodInfo) {
		this.methodInfo = methodInfo;
	}

	/**
	 * @return
	 * @see com.metashift.metadata.model.IMethodInfo#getArgumentsClassNames()
	 */
	public List<String> getArgumentsClassNames() {
		return methodInfo.getArgumentsClassNames();
	}

	/**
	 * @return
	 * @see com.metashift.metadata.model.IMethodInfo#getMethodName()
	 */
	public String getMethodName() {
		return methodInfo.getMethodName();
	}

	/**
	 * @return
	 * @see com.metashift.metadata.model.IMethodInfo#getReturnTypeClassName()
	 */
	public String getReturnTypeClassName() {
		return methodInfo.getReturnTypeClassName();
	}

	/**
	 * @return the parameterAnnotationsInfo
	 */
	public Map<Integer,IParameterAnnotationInfo> getParameterAnnotationsInfo() {
		return parameterAnnotationsInfo;
	}

	/**
	 * @param parameterAnnotationsInfo the parameterAnnotationsInfo to set
	 */
	public void setParameterAnnotationsInfo(
			Map<Integer,IParameterAnnotationInfo> parameterAnnotationsInfo) {
		this.parameterAnnotationsInfo = parameterAnnotationsInfo;
	}
	
	


}
