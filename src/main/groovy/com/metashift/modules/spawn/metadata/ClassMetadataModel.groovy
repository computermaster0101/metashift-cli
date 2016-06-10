package com.metashift.modules.spawn.metadata

import com.metashift.metadata.model.IAnnotationInfo
import com.metashift.metadata.model.IFieldInfo
import com.metashift.metadata.model.IMetadataReader
/**
 * Metadata for all model classes. A model is typically a pogo or a pojo
 * Created by navid on 4/2/15.
 */
class ClassMetadataModel{

    /**
     * A reader that will provide access to the pogo that is represented by this metadata
     */
    IMetadataReader metadataReader
    
    File sourceFile

    ClassMetadataModel(IMetadataReader metadataReader) {
        this.metadataReader = metadataReader
    }

    Class getModelClass(){
        Class.forName(metadataReader?.classMetadata?.clazzName)
    }

    List<IFieldInfo> getModelFields(){
        this.metadataReader?.getClassMetadata()?.getFields()
    }

    String getLongName(){
        metadataReader?.classMetadata?.clazzName
    }

    String getShortName(){
        metadataReader?.classMetadata?.clazzName?.with {String name ->
            name.substring(name.lastIndexOf(".")+1,name.length())
        }
    }

    String getPackageName(){
        metadataReader?.classMetadata?.clazzName?.with {String name ->
            name.substring(0,name.lastIndexOf('.'))
        }
    }

    String getParentPackageName(){
        getPackageName()?.with{ String name ->
            name.substring(0,name.lastIndexOf('.'))
        }
    }

    List<IAnnotationInfo> getAnnotationsInfo(String annotationType){
        metadataReader?.getAnnotationMetadata()?.getAnnotationsInfo(annotationType)
    }


}
