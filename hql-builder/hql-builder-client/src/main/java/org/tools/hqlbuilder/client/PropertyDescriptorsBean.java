package org.tools.hqlbuilder.client;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class PropertyDescriptorsBean {
    private final Map<String, PropertyDescriptor> propertyDescriptors;

    public PropertyDescriptorsBean(Class<?> clazz) {
        Map<String, Method> get = new HashMap<>();
        Map<String, Method> set = new HashMap<>();
        Map<String, Method> is = new HashMap<>();
        Class<?> current = clazz;
        while (!current.equals(Object.class) && current != null) {
            for (Method method : current.getDeclaredMethods()) {
                String methodName = method.getName();
                int modifiers = method.getModifiers();
                if (Modifier.isPublic(modifiers) && !Modifier.isAbstract(modifiers) && !Modifier.isStatic(modifiers)) {
                    if (methodName.startsWith("get") || methodName.startsWith("is")) {
                        if (method.getParameterTypes().length == 0) {
                            if (!Void.TYPE.isAssignableFrom(method.getReturnType())) {
                                String key = methodName.substring(methodName.startsWith("get") ? 3 : 2) + "_" + method.getReturnType().getName();
                                if (!get.containsKey(key)) {
                                    get.put(key, method);
                                }
                            }
                        }
                    }
                    if (methodName.startsWith("set")) {
                        if (method.getParameterTypes().length == 1) {
                            if (Void.TYPE.isAssignableFrom(method.getReturnType())) {
                                if (method.getParameterTypes().length == 1) {
                                    String propertyName = methodName.substring(3);
                                    String key = propertyName + "_" + method.getParameterTypes()[0].getName();
                                    if (!set.containsKey(key)) {
                                        set.put(key, method);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            current = current.getSuperclass();
        }
        Map<String, PropertyDescriptor> propertyDescriptors = new HashMap<>();
        for (Map.Entry<String, Method> _get : get.entrySet()) {
            String propertyName = _get.getKey().split("_")[0];
            propertyName = Character.toLowerCase(propertyName.charAt(0)) + propertyName.substring(1);
            PropertyDescriptor pd;
            try {
                pd = new PropertyDescriptor(propertyName, _get.getValue(), set.get(_get.getKey()));
            } catch (IntrospectionException ex) {
                ex.printStackTrace();
                continue;
            }
            propertyDescriptors.put(propertyName, pd);
        }
        for (Map.Entry<String, Method> _is : is.entrySet()) {
            String propertyName = _is.getKey().split("_")[0];
            propertyName = Character.toLowerCase(propertyName.charAt(0)) + propertyName.substring(1);
            PropertyDescriptor pd;
            try {
                pd = new PropertyDescriptor(propertyName, _is.getValue(), set.get(_is.getKey()));
            } catch (IntrospectionException ex) {
                ex.printStackTrace();
                continue;
            }
            if (!propertyDescriptors.containsKey(propertyName))
                propertyDescriptors.put(propertyName, pd);
        }
        this.propertyDescriptors = Collections.unmodifiableMap(propertyDescriptors);
    }

    public Map<String, PropertyDescriptor> getPropertyDescriptors() {
        return propertyDescriptors;
    }
}
