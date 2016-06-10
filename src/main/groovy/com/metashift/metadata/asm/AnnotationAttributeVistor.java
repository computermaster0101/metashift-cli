package com.metashift.metadata.asm;

import org.objectweb.asm.commons.EmptyVisitor;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Accumulates all Attributes for a given annotation and returns them via a call back mechanism. 
 * @author Navid
 *
 */
public class AnnotationAttributeVistor extends EmptyVisitor {
	
	private final Map<String, Object> attributes = new LinkedHashMap<String, Object>();
	
	private IAnnotationAttributesHandler attributeHandler = null;
	
	private String annotationClassName = null;

	private ClassLoader classLoader = null;



	public AnnotationAttributeVistor(String annotationClassName,
									 ClassLoader classLoader,
									 IAnnotationAttributesHandler attributeHandler) {
		super();
		this.annotationClassName = annotationClassName;
		this.attributeHandler = attributeHandler;
		this.classLoader = classLoader;
	}


	public void visit(String name, Object value) {
		// Explicitly defined annotation attribute value.
		attributes.put(name, value);
	}
	
	
	@SuppressWarnings("unchecked")
	public void visitEnd() {
		try {
			Class annotationClass = classLoader.loadClass(annotationClassName);
			// Check declared default values of attributes in the annotation type.
			Method[] annotationAttributes = annotationClass.getMethods();
			for (int i = 0; i < annotationAttributes.length; i++) {
				Method annotationAttribute = annotationAttributes[i];
				String attributeName = annotationAttribute.getName();
				Object defaultValue = annotationAttribute.getDefaultValue();
				if (defaultValue != null && !attributes.containsKey(attributeName)) {
					attributes.put(attributeName, defaultValue);
				}
			}
		}
		catch (ClassNotFoundException ex) {
			// Class not found - can't determine meta-annotations.
		}
		// return attributes via call back.
		if(attributes.size() > 0){
			attributeHandler.handleAttributes(attributes);
		}
		
	}

}
