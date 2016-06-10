package com.metashift.metadata.model;

import java.util.List;

/**
 * Created by navid on 4/7/15.
 */
public class FieldInfo implements IFieldInfo {

    private String name;

    private String className;

    private Object initialValue;

    private Access access;

    private List<IAnnotationInfo> annotationInfos;

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @Override
    public Object getInitialValue() {
        return initialValue;
    }

    public void setInitialValue(Object initialValue) {
        this.initialValue = initialValue;
    }

    @Override
    public Access getAccess() {
        return access;
    }

    public void setAccess(Access access) {
        this.access = access;
    }

    public List<IAnnotationInfo> getAnnotationInfos() {
        return annotationInfos;
    }

    public void setAnnotationInfos(List<IAnnotationInfo> annotationInfos) {
        this.annotationInfos = annotationInfos;
    }

}
