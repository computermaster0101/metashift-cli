package com.metashift.modules.spawn.patchwork;

/**
 * Takes a old thing and gives it new meaning transforms it..
 * Created by navid on 4/27/15.
 */
public interface IPatchwork<FROM, TO> {

    /**
     * Translates the incoming type into the outgoing type
     * @param value
     * @return the translated type
     */
    TO patch(FROM value);

}
