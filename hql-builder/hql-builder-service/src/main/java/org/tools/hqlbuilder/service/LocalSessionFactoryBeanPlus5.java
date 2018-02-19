package org.tools.hqlbuilder.service;

import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.ClassPathResource;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;

public class LocalSessionFactoryBeanPlus5 extends org.springframework.orm.hibernate5.LocalSessionFactoryBean
        implements BeanFactoryAware, ApplicationContextAware {
    protected BeanFactory beanFactory;

    protected ApplicationContext applicationContext;

    public LocalSessionFactoryBeanPlus5() {
        super();
    }

    public void setHibernateConfigLocation(String configLocation) {
        if ("${hibernate.cfg.xml}".equals(configLocation)) return;
        setConfigLocation(new ClassPathResource(configLocation));
    }

    public void setHibernatePackagesToScan(String... packagesToScan) {
        super.setPackagesToScan(packagesToScan);
    }

    /**
     * 
     * @see org.springframework.orm.hibernate5.LocalSessionFactoryBean#buildSessionFactory(org.springframework.orm.hibernate5.LocalSessionFactoryBuilder)
     */
    @Override
    protected SessionFactory buildSessionFactory(LocalSessionFactoryBuilder sfb) {
        // Configuration configuration = getConfiguration();
        // postProcessMappings(configuration);
        // postProcessConfiguration(configuration);
        SessionFactory buildSessionFactory = super.buildSessionFactory(sfb);
        afterSessionFactoryCreation();
        return buildSessionFactory;
    }

    /**
     * @see org.springframework.orm.hibernate3.LocalSessionFactoryBean#postProcessConfiguration(org.hibernate.cfg.Configuration)
     */
    protected void postProcessConfiguration(Configuration config) throws HibernateException {
        if (beanFactory instanceof DefaultListableBeanFactory) {
            Map<?, ?> configurationPostProcessors = ((DefaultListableBeanFactory) beanFactory).getBeansOfType(ConfigurationPostProcessor.class);
            if (configurationPostProcessors != null) {
                for (Object configurationPostProcessor : configurationPostProcessors.values()) {
                    ConfigurationPostProcessor.class.cast(configurationPostProcessor).postProcessConfiguration(config);
                }
            }
        }
    }

    /**
     * @see org.springframework.orm.hibernate3.LocalSessionFactoryBean#afterSessionFactoryCreation()
     */
    protected void afterSessionFactoryCreation() {
        for (String configurationBeanName : applicationContext.getBeanNamesForType(ConfigurationBean.class)) {
            ConfigurationBean configurationBean = applicationContext.getBean(configurationBeanName, ConfigurationBean.class);
            configurationBean.setConfiguration(getConfiguration());
        }
    }

    /**
     * @see org.springframework.orm.hibernate3.LocalSessionFactoryBean#postProcessMappings(org.hibernate.cfg.Configuration)
     */
    protected void postProcessMappings(Configuration config) throws HibernateException {
        if (beanFactory instanceof DefaultListableBeanFactory) {
            Map<?, ?> mappingsPostProcessors = ((DefaultListableBeanFactory) beanFactory).getBeansOfType(MappingsPostProcessor.class);
            if (mappingsPostProcessors != null) {
                for (Object mappingsPostProcessor : mappingsPostProcessors.values()) {
                    MappingsPostProcessor.class.cast(mappingsPostProcessor).postProcessMappings(config);
                }
            }
        }
    }

    /**
     * @see org.springframework.beans.factory.BeanFactoryAware#setBeanFactory(org.springframework.beans.factory.BeanFactory)
     */
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    /**
     * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
