package com.metashift.metadata.model;

/**
 * Created by navid on 4/8/15.
 */
public class FieldAnnotationInfo extends AnnotationInfo implements IFieldAnnotationInfo {

    private IFieldInfo fieldInfo;

    public void setFieldInfo(IFieldInfo fieldInfo) {
        this.fieldInfo = fieldInfo;
    }

    @Override
    public IFieldInfo getFieldInfo() {
        return fieldInfo;
    }
}
