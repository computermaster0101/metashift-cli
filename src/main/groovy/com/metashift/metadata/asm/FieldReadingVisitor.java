package com.metashift.metadata.asm;

import com.metashift.metadata.model.FieldAnnotationInfo;
import com.metashift.metadata.model.IAnnotationInfo;
import com.metashift.metadata.model.IFieldInfo;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.EmptyVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by navid on 4/7/15.
 */
public class FieldReadingVisitor extends EmptyVisitor implements FieldVisitor {

    private IFieldInfo fieldInfo;

    private ClassLoader classLoader;

    private List<IAnnotationInfo> annotationInfos = new ArrayList<>();

    private IAnnotationInfoHandler annotationInfoHandler;

    public FieldReadingVisitor(IFieldInfo fieldInfo,
                               ClassLoader classLoader,
                               IAnnotationInfoHandler annotationInfoHandler) {
        this.fieldInfo = fieldInfo;
        this.classLoader = classLoader;
        this.annotationInfoHandler = annotationInfoHandler;
    }

    public FieldReadingVisitor(IFieldInfo fieldInfo) {
        this.fieldInfo = fieldInfo;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        final String annotationClassName = Type.getType(desc).getClassName();

        FieldAnnotationInfo fieldAnnotationInfo = new FieldAnnotationInfo();
        fieldAnnotationInfo.setFieldInfo(fieldInfo);
        fieldAnnotationInfo.setAnnotationType(annotationClassName);
        AnnotationVisitor ret = null;
        if(classLoader != null) {
            ret = new AnnotationAttributeVistor(annotationClassName
                    , classLoader
                    , new IAnnotationAttributesHandler() {

                public void handleAttributes(Map<String, Object> attributes) {
                    fieldAnnotationInfo.setAttributes(attributes);
                    annotationInfos.add(fieldAnnotationInfo);
                }
            });
        }else{
            annotationInfos.add(fieldAnnotationInfo);
        }
        return ret;
    }

    public IFieldInfo getFieldInfo() {
        return fieldInfo;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public void setAnnotationInfoHandler(IAnnotationInfoHandler annotationInfoHandler) {
        this.annotationInfoHandler = annotationInfoHandler;
    }

    @Override
    public void visitEnd(){
        if(annotationInfoHandler != null) {
            annotationInfoHandler.handleIAnnotationInfos(annotationInfos);
        }
    }

}
