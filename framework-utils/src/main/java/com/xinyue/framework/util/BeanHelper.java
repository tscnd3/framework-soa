package com.xinyue.framework.util;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class BeanHelper {
	
	
    public static String[] getNullPropertyNames (Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        Field[] fields = source.getClass().getDeclaredFields();  
        Set<String> emptyNames = new HashSet<String>();
        for (Field field : fields) { 
        	 if("serialVersionUID".equals(field.getName())){
        		 continue;
        	 }
        	 Object srcValue = src.getPropertyValue(field.getName());
             if (srcValue == null) emptyNames.add(field.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    /**
     * copy 不为null 的
     * @param src
     * @param target
     */
    public static void copyPropertiesIgnoreNull(Object src, Object target){
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
    }

	
}
