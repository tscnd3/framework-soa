package com.xinyue.framework.disconf.config.core;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import com.baidu.disconf.client.DisconfMgrBean;
import com.baidu.disconf.client.DisconfMgrBeanSecond;
import com.baidu.disconf.client.addons.properties.ReloadablePropertiesFactoryBean;
import com.xinyue.framework.disconf.config.constant.DisconfConstant;
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
        String path="classpath:application.properties";
        fileNames.add(path);
        String disconfPath="disconf.properties";
        //从配置文件加载其他要管理的 properties文件
        Properties prop=new Properties();
        try {
			prop.load(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(disconfPath), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        String  propNames=prop.get("prop.names")!=null?(StringUtils.isNotBlank(prop.get("prop.names").toString())
        		?prop.get("prop.names").toString():null):null;
        if(StringUtils.isNotBlank(propNames)){
        	String[] propNameArray=propNames.split(",");
        	for (int i = 0; i < propNameArray.length; i++) {
        		String fileTyle=propNameArray[i].substring(propNameArray[i].lastIndexOf("."),propNameArray[i].length());
        		if(".properties".equals(fileTyle)){
        			fileNames.add("classpath:"+propNameArray[i]);
        		}
			}
        }		
        //====================================================
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

