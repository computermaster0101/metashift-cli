package com.metashift.modules.spawn.core.impl;

import com.metashift.modules.spawn.core.INamingStrategy;

import java.util.Map;

/**
 * Kinda Pointless.. Returns the key as the name
 * Created by navidmitchell on 3/16/16
 */
public class KeyForNameNamingStrategy implements INamingStrategy {


    @Override
    public String getName(Map<String, Object> context, String key) {
        return key;
    }
}
