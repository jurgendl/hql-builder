package be.ugent.oasis.tools.hqlbuilder;

import java.util.List;

/**
 * 
 * JDOC
 * 
 * @author jdlandsh
 * 
 */
public interface EntityDiscovery {

    public abstract List<Class<?>> getEntityClasses(String classForClassLoaderOrResource);

}
