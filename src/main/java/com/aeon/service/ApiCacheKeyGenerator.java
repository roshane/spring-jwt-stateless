package com.aeon.service;

import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;

/**
 * Created by roshane on 3/5/17.
 */
public class ApiCacheKeyGenerator implements KeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params) {
        String key = "";
        key += method.getName().toLowerCase();
        for (Object param : params) {
            key += "-" + param.getClass().getSimpleName().toLowerCase() + "-" + Math.abs(param.hashCode());
        }
        return key;
    }
}
