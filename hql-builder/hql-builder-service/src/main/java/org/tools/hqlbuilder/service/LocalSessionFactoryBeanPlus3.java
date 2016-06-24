package org.tools.hqlbuilder.service;

import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

@SuppressWarnings("deprecation")
public class LocalSessionFactoryBeanPlus3 extends org.springframework.orm.hibernate3.LocalSessionFactoryBean implements BeanFactoryAware,
        ApplicationContextAware {
    protected BeanFactory beanFactory;

    protected ApplicationContext applicationContext;

    public LocalSessionFactoryBeanPlus3() {
        super();
    }

    /**
     * @see org.springframework.orm.hibernate3.LocalSessionFactoryBean#postProcessConfiguration(org.hibernate.cfg.Configuration)
     */
    @Override
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
    @Override
    protected void afterSessionFactoryCreation() throws Exception {
        super.afterSessionFactoryCreation();
        for (String configurationBeanName : applicationContext.getBeanNamesForType(ConfigurationBean.class)) {
            ConfigurationBean configurationBean = applicationContext.getBean(configurationBeanName, ConfigurationBean.class);
            configurationBean.setConfiguration(getConfiguration());
        }
    }

    /**
     * @see org.springframework.orm.hibernate3.LocalSessionFactoryBean#postProcessMappings(org.hibernate.cfg.Configuration)
     */
    @Override
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
