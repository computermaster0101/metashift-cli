package com.metashift.modules.spawn.patchwork

import org.boon.Boon

/**
 * Uses a "Path Expression" Map Where the keys in the map will be the new property or key
 * and the value is the path expression to use to extract the value from the orginal object
 *
 * Boon has powerful path expressions that work with objects, lists and maps. It also works with JSON. Full examples of Java, Maps/Lists and JSON are included in this tutorial.
 *
 * Supports expressions like:
 *
 * 0.name
 * 1.employees
 * this.employees
 * name
 * employees.firstName
 * employees.contactInfo.phoneNumbers

 * Ex:
 *
 * ['user':'employee.firstname',
 *  'year_born' :'employee.birthdate.year']
 *
 *
 * Created by navid on 4/27/15.
 */
public class PathExpressionPatchwork implements IPatchwork<Object, Map<String, Object>> {

    private Map<String, String> pathExpressionMap = null;

    public PathExpressionPatchwork(Map<String, String> pathExpressionMap) {
        this.pathExpressionMap = pathExpressionMap;
    }

    @Override
    public Map<String, Object> patch(Object value) {
        Map<String, Object> ret = new HashMap<>(pathExpressionMap.size());
        for (Map.Entry<String, String> entry : pathExpressionMap.entrySet()) {
            ret.put(entry.getKey(), Boon.atIndex(value, entry.getValue()));
        }
        return ret;
    }
}
