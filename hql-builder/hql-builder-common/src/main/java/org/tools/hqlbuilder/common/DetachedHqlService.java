package org.tools.hqlbuilder.common;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.proxy.HibernateProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tools.hqlbuilder.service.PersistentCollectionHelper;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class DetachedHqlService extends DelegatingHqlService {
    protected HqlService delegate;

    @Override
    public HqlService getDelegate() {
        return this.delegate;
    }

    public void setDelegate(HqlService delegate) {
        this.delegate = delegate;
    }

    @Override
    public ExecutionResult execute(QueryParameters queryParameters) {
        ExecutionResult result = super.execute(queryParameters);
        crush(result.getResults().getValue());
        return result;
    }

    @Override
    public <T extends Serializable, I extends Serializable> T get(Class<T> type, I id) {
        return crush(super.get(type, id));
    }

    protected List<Serializable> crush(List<Serializable> values) {
        for (Serializable value : values.toArray(new Serializable[values.size()])) {
            values.remove(value);
            values.add(crush(value));
        }
        return values;
    }

    protected <T extends Serializable> T crush(T value) {
        return value;
    }

    private Class baseBeanClass = Object.class;

    private static Logger log = LoggerFactory.getLogger(DetachedHqlService.class);

    private PersistentCollectionHelper persistentCollectionHelper = new PersistentCollectionHelper();

    /**
     * This function would take as a prameter any kind of object and recursively access all of its member and clean it from any uninitialized
     * variables. The function will stop the recursion if the member variable is not of type baseBean (defined in the application) and if not of type
     * collection
     */
    public void cleanObject(Object listObj, HashSet visitedBeansSet)
            throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, InstantiationException, InvocationTargetException {
        if (visitedBeansSet == null) {
            visitedBeansSet = new HashSet();
        }
        if (listObj == null) {
            return;
        }

        // to handle the case of abnormal return consisting of array Object
        // case if hybrid bean
        if (listObj instanceof Object[]) {
            Object[] objArray = (Object[]) listObj;
            for (Object element : objArray) {
                cleanObject(element, visitedBeansSet);
            }
        } else {

            Iterator itOn = null;

            if (listObj instanceof List) {
                itOn = ((List) listObj).iterator();
            } else if (listObj instanceof Set) {
                itOn = ((Set) listObj).iterator();
            } else if (listObj instanceof Map) {
                itOn = ((Map) listObj).values().iterator();
            }

            if (itOn != null) {
                while (itOn.hasNext()) {
                    cleanObject(itOn.next(), visitedBeansSet);
                }
            } else {
                if (!visitedBeansSet.contains(listObj)) {
                    visitedBeansSet.add(listObj);
                    processBean(listObj, visitedBeansSet);
                }

            }
        }
    }

    /**
     * Remove the un-initialized proxies from the given object
     */
    private void processBean(Object objBean, HashSet visitedBeans)
            throws IllegalAccessException, IllegalArgumentException, ClassNotFoundException, InstantiationException, InvocationTargetException {
        Class tmpClass = objBean.getClass();
        Field[] classFields = null;
        while (tmpClass != null && tmpClass != baseBeanClass && tmpClass != Object.class) {
            classFields = tmpClass.getDeclaredFields();
            cleanFields(objBean, classFields, visitedBeans);
            tmpClass = tmpClass.getSuperclass();
        }
    }

	@SuppressWarnings("deprecation")
	private void cleanFields(Object objBean, Field[] classFields, HashSet visitedBeans)
            throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, InstantiationException, InvocationTargetException {
        boolean accessModifierFlag = false;
        for (Field classField : classFields) {
            Field field = classField;
            accessModifierFlag = false;
            if (!field.isAccessible()) {
                field.setAccessible(true);
                accessModifierFlag = true;
            }

            Object fieldValue = field.get(objBean);

            if (fieldValue instanceof HibernateProxy) {
                String className = ((HibernateProxy) fieldValue).getHibernateLazyInitializer().getEntityName();
                Class clazz = Class.forName(className);
                Class[] constArgs = { Integer.class };
                Constructor construct = null;
                Object baseBean = null;

                try {
                    construct = clazz.getConstructor(constArgs);
                } catch (NoSuchMethodException e) {
                    log.info("No such method for base bean " + className);
                }

                if (construct != null) {
                    baseBean = construct.newInstance((Integer) ((HibernateProxy) fieldValue).getHibernateLazyInitializer().getIdentifier());
                }
                field.set(objBean, baseBean);
            } else {
                if (persistentCollectionHelper.instanceOf(fieldValue)) {
                    // checking if it is a set, list, or bag (simply if it is a
                    // collection)
                    if (!persistentCollectionHelper.wasInitialized(fieldValue)) {
                        field.set(objBean, null);
                    } else {
                        cleanObject((fieldValue), visitedBeans);
                    }

                } else {
                    if (/* fieldValue instanceof BaseBean || */fieldValue instanceof Collection) {
                        cleanObject(fieldValue, visitedBeans);
                    }
                }
            }
            if (accessModifierFlag) {
                field.setAccessible(false);
            }
        }
    }
}
