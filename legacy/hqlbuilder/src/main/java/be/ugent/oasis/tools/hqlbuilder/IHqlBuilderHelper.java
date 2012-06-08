package be.ugent.oasis.tools.hqlbuilder;

import java.util.Properties;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JFrame;

import org.hibernate.SessionFactory;

public interface IHqlBuilderHelper {
    public abstract JComponent getPropertyFrame(SessionFactory sessionFactory, Object object, boolean editable);

    public abstract EntityDiscovery getEntityDiscovery();

    public abstract String getHibernateConfiguration();

    public abstract String getPersistenceContext();

    public abstract String getResourceLocationAllDataSources();

    public abstract String[] getResourceLocations();

    public abstract void onInit(Properties props);

    public abstract boolean accept(Class<?> type);

    public abstract <V> V showDialog(JFrame parent, String title, V... options);

    public abstract String getHelpUrl();

    public abstract void onFirstSessionReady(SessionFactory sessionFactory);

    public abstract <T extends JComponent> T font(T comp, Integer size);

    public abstract void log(Object message);

    public abstract Set<String> getReservedKeywords();
}