package com.metashift.metadata.asm;

import com.metashift.metadata.model.IAnnotationInfo;

import java.util.List;

/**
 * Handler defines a callback for visitors that will produce 
 * IAnnotationInfo Objects. 
 * @author Navid
 *
 */
public interface IAnnotationInfoHandler {

	void handleIAnnotationInfos(List<IAnnotationInfo> annotationInfos);
	
}
