

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
package com.metashift.metadata.model;


import java.util.List;

/**
 * Interface that defines abstract metadata of a specific class,
 * in a form that does not require that class to be loaded yet.
 *
 *
 * @author Juergen Hoeller Spring. 
 * @since 2.5 
 * @see IAnnotationMetadata
 * Note: This class has been modified from original spring version.  
 */
public interface IClassMetadata {
	/**
	 * Return the name of the underlying class.
	 */
	String getClazzName();

	/**
	 * Return whether the underlying class represents an interface.
	 */
	boolean isInterface();

	/**
	 * Return whether the underlying class is marked as abstract.
	 */
	boolean isAbstract();

	/**
	 * Return whether the underlying class represents a concrete class,
	 * i.e. neither an interface nor an abstract class.
	 */
	boolean isConcrete();

	/**
	 * Determine whether the underlying class is independent,
	 * i.e. whether it is a top-level class or a nested class
	 * (static inner class) that can be constructed independent
	 * from an enclosing class.
	 */
	boolean isIndependent();

	/**
	 * Return whether the underlying class has an enclosing class
	 * (i.e. the underlying class is an inner/nested class or
	 * a local class within a method).
	 * <p>If this method returns <code>false</code>, then the
	 * underlying class is a top-level class.
	 */
	boolean hasEnclosingClass();

	/**
	 * Return the name of the enclosing class of the underlying class,
	 * or <code>null</code> if the underlying class is a top-level class.
	 */
	String getEnclosingClassName();

	/**
	 * Return whether the underlying class has a super class.
	 */
	boolean hasSuperClass();

	/**
	 * Return the name of the super class of the underlying class,
	 * or <code>null</code> if there is no super class defined.
	 */
	String getSuperClassName();

	/**
	 * Return the name of all interfaces that the underlying class
	 * implements, or an empty array if there are none.
	 */
	String[] getInterfaceNames();

	/**
	 * All fields of the given class
	 * @return
	 */
	List<FieldInfo> getFields();
}
