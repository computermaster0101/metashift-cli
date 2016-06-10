package com.metashift.metadata.asm;

import com.metashift.metadata.model.*;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.EmptyVisitor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Visits all Annotations for a method including Parameter annotations and returns a list of IAnnotationInfo objects 
 * via a {@link IAnnotationInfoHandler}. 
 * @author Navid
 *
 */
public class MethodAnnotationVisitor extends EmptyVisitor {
	
	private IMethodInfo methodInfo;

	private ClassLoader classLoader;
	
	private IAnnotationInfoHandler annotationInfoHandler;
	
	private MethodAnnotationInfo methodAnnotationInfo = null;
	
	private Map<Integer,IParameterAnnotationInfo> parameterAnnotationsInfo = new LinkedHashMap<Integer,IParameterAnnotationInfo>();
	
	public MethodAnnotationVisitor(IMethodInfo methodInfo,
									ClassLoader classLoader, 
									IAnnotationInfoHandler annotationInfoHandler) {
		
		this.annotationInfoHandler = annotationInfoHandler;
		this.classLoader = classLoader;
		this.methodInfo = methodInfo;
	}
	
	
	/**
	 * Collect information about the Method's Annotations. 
	 */
	public AnnotationVisitor visitAnnotation(String desc,boolean visible) {
		
		final String annotationClassName = Type.getType(desc).getClassName();
		methodAnnotationInfo = new MethodAnnotationInfo();
		methodAnnotationInfo.setAnnotationType(annotationClassName);
		methodAnnotationInfo.setMethodInfo(methodInfo);
		
		return new AnnotationAttributeVistor(annotationClassName
													  ,classLoader
													  ,new IAnnotationAttributesHandler () {

			public void handleAttributes(Map<String, Object> attributes) {
				methodAnnotationInfo.setAttributes(attributes);
			}
			
		});
	}
	
	/**
	 * Collect information about the methods parameter annotations.
	 */
    public AnnotationVisitor visitParameterAnnotation(final int parameter,
            										  final String desc,
            										  final boolean visible){
    	final String annotationClassName = Type.getType(desc).getClassName();
    	final ParameterAnnotationInfo annotationInfo = new ParameterAnnotationInfo();
    	annotationInfo.setAnnotationType(annotationClassName);
    	annotationInfo.setParameterIndex(parameter);
    	
		return new AnnotationAttributeVistor(annotationClassName,
										     classLoader,
										     new IAnnotationAttributesHandler () {

			public void handleAttributes(Map<String, Object> attributes) {
				annotationInfo.setAttributes(attributes);
				annotationInfo.setParentMethodInfo(methodInfo);
				// add to list to be used later
				parameterAnnotationsInfo.put(parameter, annotationInfo);
			}
			
		});
    }


	public void visitEnd() {
		// build list of all annotationInfos created to be returned. 
		List<IAnnotationInfo> annotationInfos = new ArrayList<IAnnotationInfo>();
		annotationInfos.addAll(parameterAnnotationsInfo.values());
		
		if(methodAnnotationInfo != null){
			// build map for all parameter annotations found. 
			
			methodAnnotationInfo.setParameterAnnotationsInfo(parameterAnnotationsInfo);
			annotationInfos.add(methodAnnotationInfo);
		}
		
		annotationInfoHandler.handleIAnnotationInfos(annotationInfos);
	}
	
	
}
