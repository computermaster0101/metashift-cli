package com.metashift.metadata.model;

import java.util.List;

/**
 * Created by navid on 4/7/15.
 */
public interface IFieldInfo {
    String getName();

    String getClassName();

    Object getInitialValue();

    Access getAccess();

    List<IAnnotationInfo> getAnnotationInfos();
}
