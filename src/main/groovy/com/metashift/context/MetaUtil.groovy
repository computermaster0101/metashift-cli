package com.metashift.context

/**
 * Created by navid on 3/18/15.
 */
class MetaUtil {

    /**
     * Puts the given value into the map at the key. But stores the value in a list.
     * So if the value already exists it is added to the list. If it does not exist at the key it is added to a LinkedList and then to the map.
     * @param target
     * @param key
     * @param value
     * @return the map passed in as the target
     */
    public static Map putAsList(Map target, String key, Object value){
        assert target != null, 'Target must not be null'
        def temp = target.get(key)
        if (temp) {
            if (!(temp instanceof Collection)) {
                throw new IllegalArgumentException("Target contains a value for ${key} that is not a Collection")
            }
        }else{
           temp = new LinkedList()
        }
        ((Collection)temp).add(value)
        target.put(key,temp)
        target
    }

}
