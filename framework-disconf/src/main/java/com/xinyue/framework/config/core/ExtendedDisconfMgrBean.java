package com.xinyue.framework.config.core;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import com.baidu.disconf.client.DisconfMgrBean;
import com.baidu.disconf.client.config.DisClientConfig;
import com.xinyue.framework.config.constant.EnvConfigurer;


public class ExtendedDisconfMgrBean extends DisconfMgrBean {
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {

        String env = System.getenv("DISCONF_ENV");
        if (!StringUtils.isEmpty(env)) {
            EnvConfigurer.env = env;
        }
        if (EnvConfigurer.env != null) {
            try {
                DisClientConfig.getInstance().loadConfig(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            DisClientConfig.getInstance().ENV = EnvConfigurer.env;
        }
        super.postProcessBeanDefinitionRegistry(registry);
    }
}
