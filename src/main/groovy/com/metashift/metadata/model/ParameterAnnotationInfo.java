package com.metashift.metadata.model;

/**
 * Default implementation. 
 * @author Navid
 *
 */
/**
 * @author Navid
 *
 */
public class ParameterAnnotationInfo extends AnnotationInfo implements IParameterAnnotationInfo {

	private Integer parameterIndex;
	
	private IMethodInfo parentMethodInfo; 
	
	
	/* (non-Javadoc)
	 * @see com.metashift.annotations.model.IParameterAnnotationInfo#getParentMethodInfo()
	 */
	public IMethodInfo getParentMethodInfo() {
		return parentMethodInfo;
	}

	/**
	 * @param parentMethodInfo the parentMethodInfo to set
	 */
	public void setParentMethodInfo(IMethodInfo parentMethodInfo) {
		this.parentMethodInfo = parentMethodInfo;
	}

	/**
	 * @param parameterIndex the parameterIndex to set
	 */
	public void setParameterIndex(Integer parameterIndex) {
		this.parameterIndex = parameterIndex;
	}

	
	/* (non-Javadoc)
	 * @see com.metashift.annotations.model.IParameterAnnotationInfo#getParameterIndex()
	 */
	public Integer getParameterIndex() {
		return parameterIndex;
	}

}
