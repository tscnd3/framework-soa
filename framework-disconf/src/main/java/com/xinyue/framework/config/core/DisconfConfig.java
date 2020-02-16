package com.xinyue.framework.config.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import com.baidu.disconf.client.DisconfMgrBean;
import com.baidu.disconf.client.DisconfMgrBeanSecond;
import com.baidu.disconf.client.addons.properties.ReloadablePropertiesFactoryBean;
import com.xinyue.framework.config.constant.DisconfConstant;
@Configuration
public class DisconfConfig{
	
    @Bean(destroyMethod = "destroy")
    public DisconfMgrBean disconfMgrBean(){
    	ExtendedDisconfMgrBean extendedDisconfMgrBean = new ExtendedDisconfMgrBean();
        //extendedDisconfMgrBean.setScanPackage("no_used");
    	extendedDisconfMgrBean.setScanPackage(DisconfConstant.DEFAULT_CORE_SCAN_PACKAGE);
        return extendedDisconfMgrBean;
    }

    @Bean(initMethod = "init", destroyMethod = "destroy")
    public DisconfMgrBeanSecond disconfMgrBeanSecond(){
        return new DisconfMgrBeanSecond();
    }
	
    

    @Bean(name = "reloadablePropertiesFactoryBean")
    @AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
    public ReloadablePropertiesFactoryBean reloadablePropertiesFactoryBean() {
    	ReloadablePropertiesFactoryBean propertiesFactoryBean = new ReloadablePropertiesFactoryBean();
        propertiesFactoryBean.setSingleton(true);
        // disconf 配置文件 (这里只有application.properties)
        List<String> fileNames = new ArrayList<>();
        fileNames.add("classpath:application.properties");
        propertiesFactoryBean.setLocations(fileNames);
        return propertiesFactoryBean;
    }

    @Bean(name = "fwReloadingPropertyPlaceholderConfigurer")
    public FwReloadingPropertyPlaceholderConfigurer fwReloadingPropertyPlaceholderConfigurer(ReloadablePropertiesFactoryBean reloadablePropertiesFactoryBean) throws IOException{
    	FwReloadingPropertyPlaceholderConfigurer fwReloadingPropertyPlaceholderConfigurer = new FwReloadingPropertyPlaceholderConfigurer();
    	fwReloadingPropertyPlaceholderConfigurer.setIgnoreResourceNotFound(true);
    	fwReloadingPropertyPlaceholderConfigurer.setIgnoreUnresolvablePlaceholders(true);
    	 try {
             Properties properties = reloadablePropertiesFactoryBean.getObject();
             fwReloadingPropertyPlaceholderConfigurer.setProperties(properties);
         } catch (IOException e) {
             throw new RuntimeException("unable to find properties", e);
         }
        return fwReloadingPropertyPlaceholderConfigurer;
    }
    
//    @Bean(name = "propertyPlaceholderConfigurer")
//    public PropertyPlaceholderConfigurer propertyPlaceholderConfigurer(ReloadablePropertiesFactoryBean reloadablePropertiesFactoryBean) {
//        PropertyPlaceholderConfigurer placeholderConfigurer = new PropertyPlaceholderConfigurer();
//        placeholderConfigurer.setIgnoreResourceNotFound(true);
//        placeholderConfigurer.setIgnoreUnresolvablePlaceholders(true);
//        try {
//            Properties properties = reloadablePropertiesFactoryBean.getObject();
//            placeholderConfigurer.setProperties(properties);
//        } catch (IOException e) {
//            throw new RuntimeException("unable to find properties", e);
//        }
//        return placeholderConfigurer;
//    }


    
    
    


}

