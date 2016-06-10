package com.metashift.modules.spawn.extension.crud.annotations;

import java.lang.annotation.*;

/**
 * Marks a domain object as wanting to have crud scaffolding generated.
 * Created by navid on 4/1/15.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Crud {
}
