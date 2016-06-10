/*
 * Copyright 2002-2008 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.metashift.metadata.asm;

import com.metashift.metadata.model.AnnotationInfo;
import com.metashift.metadata.model.IAnnotationInfo;
import com.metashift.metadata.model.IAnnotationMetadata;
import com.metashift.metadata.model.MethodInfo;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import java.util.*;


/**
 * ASM class visitor which looks for the class name and implemented types as
 * well as for the annotations defined on the class, exposing them through
 * the {@link IAnnotationMetadata} interface.
 *
 * @author Juergen Hoeller
 * @author Mark Fisher
 * Note: This has been modified from the original spring version. 
 */
public class AnnotationMetadataReadingVisitor extends ClassMetadataReadingVisitor implements
		IAnnotationMetadata {

	private final Map<String, List<IAnnotationInfo>> annotationTypes = new LinkedHashMap<String, List<IAnnotationInfo>>();
	
	private final ClassLoader classLoader;


	public AnnotationMetadataReadingVisitor(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}
	
	

	/* (non-Javadoc)
	 * @see org.objectweb.asm.commons.EmptyVisitor#visitMethod(int, java.lang.String, java.lang.String, java.lang.String, java.lang.String[])
	 */
	@Override
	public MethodVisitor visitMethod(int access,final String name,final String desc,
			String signature, String[] exceptions) {
		
		final MethodInfo methodInfo = new MethodInfo();
		methodInfo.setMethodName(name);
		
		// first parse the method descriptor if available
		if(desc != null){
			String returnTypeClassName = Type.getReturnType(desc).getClassName();
			List<String> argumentClassNames = new ArrayList<String>();
			
			// Get the class names for all argument types
			for(Type arg : Type.getArgumentTypes(desc)){
				argumentClassNames.add(arg.getClassName());
			}
			
			// now populate method info object with the rest of the data 
			methodInfo.setReturnTypeClassName(returnTypeClassName);
			methodInfo.setArgumentsClassNames(argumentClassNames);
		}
		
		return new MethodAnnotationVisitor(methodInfo,
				                           classLoader, 
				                           new IAnnotationInfoHandler(){

			public void handleIAnnotationInfos(List<IAnnotationInfo> annotationInfos) {
				// store created IAnnotationInfo Objects.
				addAnnotationInfoToMap(annotationInfos);
			}
			
		});
	}

	@Override
	public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
		FieldVisitor visitor =  super.visitField(access, name, desc, signature, value);
		// Superclass will provide annotation procesing
		if(visitor instanceof FieldReadingVisitor){
			FieldReadingVisitor temp = (FieldReadingVisitor)visitor;
			temp.setClassLoader(classLoader);
			temp.setAnnotationInfoHandler(new IAnnotationInfoHandler(){
				public void handleIAnnotationInfos(List<IAnnotationInfo> annotationInfos) {
					// store created IAnnotationInfo Objects.
					addAnnotationInfoToMap(annotationInfos);
				}
			});

		}
		return visitor;
	}

	/**
	 * Handles all annotations except for method annotations. 
	 */
	public AnnotationVisitor visitAnnotation(final String desc, boolean visible) {
		final String annotationClassName = Type.getType(desc).getClassName();
		final AnnotationInfo annotationInfo = new AnnotationInfo();
		annotationInfo.setAnnotationType(annotationClassName);
		
		return new AnnotationAttributeVistor(annotationClassName,
				     						 classLoader,
											 new IAnnotationAttributesHandler () {

			public void handleAttributes(Map<String, Object> attributes) {
				annotationInfo.setAttributes(attributes);
				addAnnotationInfoToMap(annotationInfo);
			}
			
		});
	}

	
	/**
	 * Adds the newley created info object to the internal map for safe keeping
	 * @param infos
	 */
	private void addAnnotationInfoToMap(List<IAnnotationInfo> infos){	
		for(IAnnotationInfo info : infos){
			addAnnotationInfoToMap(info);
		}
	}
	
	
	/**
	 * Adds the newley created info object to the internal map for safe keeping
	 * @param info
	 */
	private void addAnnotationInfoToMap(IAnnotationInfo info){	
		if(annotationTypes.containsKey(info.getAnnotationType())){
			annotationTypes.get(info.getAnnotationType()).add(info);
		}else{
			List<IAnnotationInfo>  list = new ArrayList<IAnnotationInfo>();
			list.add(info);
			annotationTypes.put(info.getAnnotationType(), list);
		}
	}
	
	
	public List<IAnnotationInfo> getAnnotationsInfo(
			String annotationType) {
		return annotationTypes.get(annotationType);
	}

	public Set<String> getAnnotationTypes() {
		return annotationTypes.keySet();
	}


	public boolean hasAnnotation(String annotationType) {
		return annotationTypes.containsKey(annotationType);
	}

}
