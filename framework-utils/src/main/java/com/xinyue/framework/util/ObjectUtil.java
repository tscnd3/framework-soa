package com.xinyue.framework.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;



public class ObjectUtil {
	/**
     * 根据属性名获取属性值
     * */
    private static Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[] {});
            Object value = method.invoke(o, new Object[] {});
            return value;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取属性名数组
     * */
    private static String[] getFiledName(Object o) {
        Field[] fields = o.getClass().getDeclaredFields();
        String[] fieldNames = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            System.out.println(fields[i].getType());
            fieldNames[i] = fields[i].getName();
        }
        return fieldNames;
    }

    /**
     * 获取属性类型(type)，属性名(name)，属性值(value)的map组成的list
     * */
    @SuppressWarnings({ "unused", "rawtypes", "unchecked" })
    private static List getFiledsInfo(Object o) {
        Field[] fields = o.getClass().getDeclaredFields();
        String[] fieldNames = new String[fields.length];
        List list = new ArrayList();
        Map infoMap = null;
        for (int i = 0; i < fields.length; i++) {
            infoMap = new HashMap();
            infoMap.put("type", fields[i].getType().toString());
            infoMap.put("name", fields[i].getName());
            infoMap.put("value", getFieldValueByName(fields[i].getName(), o));
            list.add(infoMap);
        }
        return list;
    }

    /**
     * 获取对象的所有属性值，返回一个对象数组
     * */
    @SuppressWarnings("static-access")
    public Object[] getFiledValues(Object o) {
        String[] fieldNames = this.getFiledName(o);
        Object[] value = new Object[fieldNames.length];
        for (int i = 0; i < fieldNames.length; i++) {
            value[i] = this.getFieldValueByName(fieldNames[i], o);
        }
        return value;
    }

    /**
     * 判断是否为空
     * 
     * @param files
     * @param o
     * @return
     */
    @SuppressWarnings("unchecked")
	public static boolean checkObjectFile(String[] files, Object o) {
        if (o==null) {
            return false;
        }
        List<Map<String, Object>> list = getFiledsInfo(o);
        for (Map<String, Object> map : list) {
            String fileName = map.get("name").toString();
            if (Arrays.asList(files).contains(fileName)) {
                Object value = map.get("value");
                if (value == null) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 判断是否为空
     * 
     * @param files
     * @param o
     * @return
     */
    @Deprecated
    public static boolean checkObjectFile(String[] files, Map<String, Object> map) {
        if (map==null || map.isEmpty()) {
            return false;
        }
        for (String key : files) {
            if (StringUtils.isNoneBlank(key) && map.keySet().contains(key)) {
                Object value = map.get(key);
                if (value == null) {
                    return false;
                }
            }else{
                return false;
            }
        }
        return true;
    }
    
    /**
     * @Description: 检测对象是否为空
     * @param @param files
     * @param @param map
     * @param @return   
     * @return boolean  
     * @throws
     * @author pengzhihao
     * @date 2016-10-24下午8:11:57
     */
    public static boolean checkObjectFileIsEmpty(String[] files, Map<String, Object> map) {
        if (map==null || map.isEmpty()) {
            return false;
        }
        for (String key : files) {
            if (StringUtils.isNoneBlank(key) && map.keySet().contains(key)) {
                Object value = map.get(key);
                if (value == null || "".equals(value.toString().trim()) || "null".equals(value.toString().trim())) {
                    return false;
                }
            }else{
                return false;
            }
        }
        return true;
    }
	
		/**
     * 判断是否为空
     * 
     * @param files
     * @param o
     * @return
     */
    public static boolean checkObject(String[] files, Map<String, Object> map) {
        if (map==null || map.isEmpty()) {
            return false;
        }
        for (String key : files) {
            if (StringUtils.isNoneBlank(key) && map.keySet().contains(key)) {
                Object value = map.get(key);
                if (value == null || "".equals(value)) {
                    return false;
                }
            }else{
                return false;
            }
        }
        return true;
    }
    
    /**
     * 判断参数是否为空
     * @param obj
     * @return
     */
    public static boolean isEmpty(Object obj){
    	if(obj==null||"".equals(obj.toString().trim())){
    		return true;
    	}else{
    		return false;
    	}
    }
	
	/**
     * 判断是否为空
     * 
     * @param files
     * @param o
     * @return
     */
    @SuppressWarnings("unchecked")
	public static boolean checkObject(String[] files, Object o) {
        if (o==null) {
            return false;
        }
        List<Map<String, Object>> list = getFiledsInfo(o);
        for (Map<String, Object> map : list) {
            String fileName = map.get("name").toString();
            if (Arrays.asList(files).contains(fileName)) {
                Object value = map.get("value");
                if (value == null || "".equals(value)) {
                    return false;
                }
            }
        }
        return true;
    }
	


    /**
     * 
     * @author buyuer
     * @Title: getObjectByFileNames
     * @Description: 获取一个object对象中的字段
     * @param files
     *            字段数组
     * @param o
     *            对象
     * @return
     */
    @SuppressWarnings("unchecked")
	public static Map<String, Object> getObjectByFileNames(String[] files, Object o) {
        if (o == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        List<Map<String, Object>> list = getFiledsInfo(o);
        for (Map<String, Object> filemap : list) {
            String fileName = filemap.get("name").toString();
            if (Arrays.asList(files).contains(fileName)) {
                Object value = filemap.get("value");
                map.put(fileName, value);
            }
        }
        return map;
    }

    /**
     * 
     * @author buyuer
     * @Title: getObjectByFileNames
     * @Description: 获取一个object对象中的字段
     * @param files
     *            字段数组
     * @param o
     *            对象
     * @return
     */
    @SuppressWarnings({ "rawtypes", "null", "unchecked" })
	public static List<Map<String, Object>> getListByFileNames(String[] files, List o) {
        if (o == null&&o.isEmpty()) {
            return null;
        }
        List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
        for (Object obj : o) {
            Map<String, Object> map = new HashMap<String, Object>();
            List<Map<String, Object>> list = getFiledsInfo(obj);
            for (Map<String, Object> filemap : list) {
                String fileName = filemap.get("name").toString();
                if (Arrays.asList(files).contains(fileName)) {
                    Object value = filemap.get("value");
                    map.put(fileName, value);
                }
            }
            listMap.add(map);
        }
        return listMap;
    }
    
    
    
    
    /**
	 * @Description: 过滤对象属性
	 * @param @param files  为空的话默认不过滤
	 * @param @param o   
	 * @return void  
	 * @throws
	 * @author pengzhihao
	 * @date 2017-11-27 上午10:45:04
	 */
    public static Object filterObjectFiled(String[] files, Object o) {
        if (o == null || files == null) {
            return o;
        }
        Field[] fields = o.getClass().getDeclaredFields();
        try {
			for(Field field : fields){
				String fileName = field.getName();
				 if (!Modifier.isFinal(field.getModifiers()) && !Arrays.asList(files).contains(fileName)) {
					 //设置些属性是可以访问的 
					 field.setAccessible(true);
					 field.set(o, null);
				 }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        return o;
    }

   /**
    * @Description: 过滤对象属性（list集合所有对象）
    * @param @param files 为空的话默认不过滤
    * @param @param o
    * @param @return   
    * @return List<Object>  
    * @throws
    * @author pengzhihao
    * @date 2017-11-27 上午10:52:16
    */
    public static List<?> filterListObjectFiled(String[] files, List<?> o) {
        if (o == null || files == null) {
            return o;
        }
        for (Object obj : o) {
        	filterObjectFiled(files, obj);
        }
        return o;
    }

}
