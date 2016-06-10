package com.metashift.modules.spawn.extension.crud.annotations;

import java.lang.annotation.*;

/**
 * Marks a field of a model object as having a label.
 * The Label can be used for various purposes during scaffolding
 * Created by navid on 4/2/15.
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.TYPE_PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Label {

}
