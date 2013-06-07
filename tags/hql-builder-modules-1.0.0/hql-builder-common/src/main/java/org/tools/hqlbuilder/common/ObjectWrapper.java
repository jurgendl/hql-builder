package org.tools.hqlbuilder.common;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * ObjectWrapper
 * 
 * @author jdlandsh
 */
@SuppressWarnings("rawtypes")
public final class ObjectWrapper {
    /** {@link Object}.class */
    private static final Class<Object> OBJECT_CLASS = Object.class;

    /** cache */
    private static final transient Map<Class, ClassCache> cache = new HashMap<Class, ClassCache>();

    /** bean */
    private final transient Class beanclass;

    /** localcache to root */
    private final transient List<ClassCache> localcache = new ArrayList<ClassCache>();

    /** bean */
    private final transient Object bean;

    /** bean */
    private final transient String beanclassname;

    /**
     * Creates a new ObjectWrapper object.
     * 
     * @param bean
     */
    public ObjectWrapper(Object bean) {
        this.bean = bean;
        this.beanclass = bean.getClass();
        this.beanclassname = beanclass.getName();

        Class<?> current = beanclass;

        do {
            this.localcache.add(ow_fromCache(current));
            current = current.getSuperclass();
        } while (!OBJECT_CLASS.equals(current));
    }

    /**
     * Creates a new ObjectWrapper object.
     * 
     * @param bean
     */
    public ObjectWrapper(Class<?> bean) {
        this.bean = bean;
        this.beanclass = bean;
        this.beanclassname = beanclass.getName();

        Class<?> current = beanclass;

        do {
            this.localcache.add(ow_fromCache(current));
            current = current.getSuperclass();
        } while (!OBJECT_CLASS.equals(current));
    }

    /**
     * fromCache
     * 
     * @param type
     * 
     * @return
     */
    private static final ClassCache ow_fromCache(Class type) {
        ClassCache fromCache = cache.get(type);

        if (fromCache == null) {
            fromCache = new ClassCache(type);
            cache.put(type, fromCache);
        }

        return fromCache;
    }

    /**
     * getFieldNames
     * 
     * @return
     */
    public final Set<String> getFieldNames() {
        HashSet<String> list = new HashSet<String>();

        for (ClassCache element : localcache) {
            list.addAll(cc_getFieldNames(element));
        }

        return list;
    }

    /**
     * get value
     * 
     * @param ow
     * @param path
     * 
     * @return
     * 
     * @throws RuntimeException
     */
    private static final Object ow_get(ObjectWrapper ow, String[] path) throws RuntimeException {
        ObjectWrapper current = ow;

        int size_1 = path.length - 1;

        for (int i = 0; i < size_1; i++) {
            Object owGet = ow_get(current, path[i]);

            if (owGet == null) {
                return null;
            }

            current = new ObjectWrapper(owGet);
        }

        return ow_get(current, path[size_1]);
    }

    /**
     * get value
     * 
     * @param ow
     * @param field
     * 
     * @return
     * 
     * @throws RuntimeException
     */
    private static final Object ow_get(ObjectWrapper ow, String field) throws RuntimeException {
        for (ClassCache cc : ow.localcache) {
            try {
                return cc_getValue(cc, field, ow);
            } catch (RuntimeException ex) {
                //
            }
        }

        throw new RuntimeException(field);
    }

    /**
     * get value
     * 
     * @param ow
     * @param field
     * 
     * @return
     * 
     * @throws RuntimeException
     */
    private static final Field ow_getField(ObjectWrapper ow, String field) throws RuntimeException {
        for (ClassCache cc : ow.localcache) {
            try {
                return cc_getField(cc, field);
            } catch (RuntimeException ex) {
                //
            }
        }

        throw new RuntimeException(field);
    }

    /**
     * set value
     * 
     * @param ow
     * @param path
     * @param value
     * 
     * @throws RuntimeException
     */
    private static final void ow_set(ObjectWrapper ow, String[] path, Object value) throws RuntimeException {
        ObjectWrapper current = ow;

        int size_1 = path.length - 1;

        for (int i = 0; i < size_1; i++) {
            Object owGet = ow_get(current, path[i]);

            if (owGet == null) {
                throw new RuntimeException(path[i]);
            }

            current = new ObjectWrapper(owGet);
        }

        ow_set(current, path[size_1], value);
    }

    /**
     * set value
     * 
     * @param ow
     * @param field
     * @param value
     * 
     * @throws RuntimeException
     */
    private static final void ow_set(ObjectWrapper ow, String field, Object value) throws RuntimeException {
        for (ClassCache cc : ow.localcache) {
            try {
                cc_setValue(cc, field, ow, value);

                return;
            } catch (RuntimeException ex) {
                //
            }
        }

        throw new RuntimeException(field);
    }

    /**
     * buildPath
     * 
     * @return
     * 
     * @throws RuntimeException
     */
    private static final String[] ow_buildPath(String... path) {
        if ((path == null) || (path.length == 0)) {
            throw new RuntimeException();
        }

        ArrayList<String> rebuild = new ArrayList<String>();

        for (String arrayElement : path) {
            Enumeration enumer = new StringTokenizer(arrayElement, ".");

            while (enumer.hasMoreElements()) {
                rebuild.add((String) enumer.nextElement());
            }
        }

        return rebuild.toArray(new String[rebuild.size()]);
    }

    /**
     * get value
     * 
     * @return
     * 
     * @throws RuntimeException
     */
    public final Object get(String... path) throws RuntimeException {
        return ow_get(this, ow_buildPath(path));
    }

    /**
     * get value
     * 
     * @param <T>
     * @param type
     * 
     * @return
     * 
     * @throws RuntimeException
     */
    public final <T> T get(Class<T> type, String... path) throws RuntimeException {
        Object obj = get(path);
        try {
            return type.cast(obj);
        } catch (java.lang.ClassCastException ex) {
            throw new RuntimeException(type.getName() + "<>" + obj.getClass().getName());
        }
    }

    /**
     * get value
     * 
     * @param <T>
     * @param path
     * @param type
     * 
     * @return
     * 
     * @throws RuntimeException
     */
    public final <T> T get(String path, Class<T> type) throws RuntimeException {
        return get(type, path);
    }

    /**
     * set value
     * 
     * @param path
     * @param value
     * 
     * @return
     * 
     * @throws RuntimeException
     */
    public final ObjectWrapper set(String path, Object value) throws RuntimeException {
        return set(value, path);
    }

    /**
     * set value null
     * 
     * @param path
     * 
     * @return
     * 
     * @throws RuntimeException
     */
    public final ObjectWrapper unset(String path) throws RuntimeException {
        return set(path, (Object) null);
    }

    /**
     * set each value on deep path
     * 
     * @param path
     * 
     * @return
     * 
     * @throws RuntimeException
     * @throws IllegalArgumentException
     */
    public final ObjectWrapper set(String path, Object... values) throws RuntimeException {
        String[] fullpath = ow_buildPath(path);

        if ((values == null) || (values.length == 0) || (fullpath.length != values.length)) {
            throw new IllegalArgumentException("path.length=values.length");
        }

        ObjectWrapper current = this;

        for (int i = 0; i < fullpath.length; i++) {
            Object value = values[i];
            current.set(fullpath[i], value);
            current = new ObjectWrapper(value);
        }

        return this;
    }

    /**
     * set value
     * 
     * @param value
     * 
     * @return
     * 
     * @throws RuntimeException
     */
    public final ObjectWrapper set(Object value, String... path) throws RuntimeException {
        ow_set(this, ow_buildPath(path), value);

        return this;
    }

    /**
     * get all fields
     * 
     * @return
     */
    public final Map<String, Field> getFields() {
        Map<String, Field> map = new HashMap<String, Field>();

        for (ClassCache element : localcache) {
            map.putAll(cc_getFields(element));
        }

        return map;
    }

    /**
     * configureerbaar wat juist nodig is, subset wordt niet gecached (de volledige set wel) waardoor deze functie net iets trager is dan
     * {@link #getFields()}
     * 
     * @return
     */
    public final Map<String, Class<?>> getTypes(boolean removeStatic, boolean removeFinal, Class<?> exclusiveClass) {
        Map<String, Class<?>> types = new HashMap<String, Class<?>>();

        for (Map.Entry<String, Field> entry : getFields(removeStatic, removeFinal, exclusiveClass).entrySet()) {
            types.put(entry.getKey(), entry.getValue().getType());
        }

        return types;
    }

    /**
     * configureerbaar wat juist nodig is, subset wordt niet gecached (de volledige set wel) waardoor deze functie net iets trager is dan
     * {@link #getFields()}
     * 
     * @return
     */
    public final Map<String, Field> getFields(boolean removeStatic, boolean removeFinal, Class<?> exclusiveClass) {
        Map<String, Field> map = new HashMap<String, Field>();
        List<Class<?>> exclusiveClasses = new ArrayList<Class<?>>();

        if (exclusiveClass != null) {
            Class<?> current = exclusiveClass;
            while (current != null) {
                exclusiveClasses.add(current);
                current = current.getSuperclass();
            }
        }

        for (Map.Entry<String, Field> field : getFields().entrySet()) {
            if (removeStatic && Modifier.isStatic(field.getValue().getModifiers())) {
                continue;
            }

            if (removeFinal && Modifier.isFinal(field.getValue().getModifiers())) {
                continue;
            }

            if (exclusiveClass != null && exclusiveClasses.contains(field.getValue().getDeclaringClass())) {
                continue;
            }

            map.put(field.getKey(), field.getValue());
        }

        return map;
    }

    /**
     * get type for field
     * 
     * @param field
     * 
     * @return
     * 
     * @throws RuntimeException
     */
    public final Class<?> getType(String field) throws RuntimeException {
        return getField(field).getType();
    }

    /**
     * get field
     * 
     * @param field
     * 
     * @return
     * 
     * @throws RuntimeException
     */
    public final Field getField(String field) throws RuntimeException {
        String[] path = ow_buildPath(field);
        ObjectWrapper current = this;
        int size_1 = path.length - 1;

        for (int i = 0; i < size_1; i++) {
            current = new ObjectWrapper(ow_get(current, path[i]));
        }

        return ow_getField(current, path[size_1]);
    }

    /**
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ObjectWrapper [");

        if (this.bean != null) {
            builder.append("bean=");
            builder.append(this.bean);
        }

        builder.append("]");

        return builder.toString();
    }

    /**
     * getBeanclass
     * 
     * @return the beanclass
     */
    public final Class getBeanclass() {
        return this.beanclass;
    }

    /**
     * getBean
     * 
     * @return the bean
     */
    public final Object getBean() {
        return this.bean;
    }

    /**
     * getBeanclassname
     * 
     * @return the beanclassname
     */
    public final String getBeanclassname() {
        return this.beanclassname;
    }

    /**
     * initAllFields
     * 
     * @param cc
     * 
     * @return
     */
    private static final Set<String> cc_getFieldNames(ClassCache cc) {
        return cc_getFields(cc).keySet();
    }

    /**
     * get value
     * 
     * @param cc
     * @param fieldName
     * @param wrapper
     * 
     * @return
     * 
     * @throws RuntimeException
     */
    private static final Object cc_getValue(ClassCache cc, String fieldName, ObjectWrapper wrapper) throws RuntimeException {
        try {
            Field field = cc_getField(cc, fieldName);
            boolean accessible = field.isAccessible();

            if (!accessible) {
                field.setAccessible(true);
            }

            Object value = field.get(wrapper.bean);

            if (!accessible) {
                field.setAccessible(false);
            }

            return value;
        } catch (RuntimeException ex) {
            throw new RuntimeException(ex);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * set value
     * 
     * @param cc
     * @param fieldName
     * @param wrapper
     * @param value
     * 
     * @throws RuntimeException
     */
    private static final void cc_setValue(ClassCache cc, String fieldName, ObjectWrapper wrapper, Object value) throws RuntimeException {
        try {
            Field field = cc_getField(cc, fieldName);
            boolean accessible = field.isAccessible();

            if (!accessible) {
                field.setAccessible(true);
            }

            field.set(wrapper.bean, value);

            if (!accessible) {
                field.setAccessible(false);
            }
        } catch (RuntimeException ex) {
            throw new RuntimeException(ex);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * get {@link Field} for name
     * 
     * @param cc
     * @param fieldName
     * 
     * @return
     * 
     * @throws RuntimeException
     */
    private static final Field cc_getField(ClassCache cc, String fieldName) throws RuntimeException {
        Field field = cc.fields.get(fieldName);

        if (field == null) {
            try {
                field = cc.type.getDeclaredField(fieldName);
            } catch (NoSuchFieldException ex) {
                throw new RuntimeException(ex);
            } catch (SecurityException ex) {
                throw new RuntimeException(ex);
            }
        }

        return field;
    }

    /**
     * getFields
     * 
     * @param cc
     * 
     * @return the fields
     */
    private static final Map<String, Field> cc_getFields(ClassCache cc) {
        if (!cc.allFieldInititalized) {
            for (Field field : cc.type.getDeclaredFields()) {
                cc.fields.put(field.getName(), field);
            }

            cc.allFieldInititalized = true;
        }

        return cc.fields;
    }

    /**
     * ClassCache
     */
    private static final class ClassCache {
        /** type */
        private final Class type;

        /** map */
        private final Map<String, Field> fields = new HashMap<String, Field>();

        /** allFieldInititalized */
        private boolean allFieldInititalized = false;

        /**
         * Creates a new ClassCache object.
         * 
         * @param type
         */
        private ClassCache(Class type) {
            this.type = type;
        }
    }
}
