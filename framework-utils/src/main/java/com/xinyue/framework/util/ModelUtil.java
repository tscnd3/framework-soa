package com.xinyue.framework.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

public class ModelUtil {

	/**
     * 检查一个List是否为空
     * 
     * @author cyl
     * @param <T>
     * @param list
     * @return
     */
    public static <T> boolean checkListIsBlank(Collection<T> list) {
        if (list != null && list.size() > 0) {
            return false;
        } else {
            return true;
        }
    }
    
	/**
	 * List转化成Map根据key值
	 * @param list
	 * @param key
	 * @return
	 */
	public static Map<Object, Map<String,String>> getMapStringMap(List<Map<String,String>> list, String key) {
        Map<Object, Map<String,String>> recordMap = new HashMap<Object, Map<String,String>>();
        for (Map<String,String> r : list) {
            if(r.get(key)!=null){
                recordMap.put(r.get(key), r);
            }
        }
        return recordMap;
    }
	
	/**
     * 检查一个Map中指定的列是否有空的
     * 
     * @author cyl
     * @param map
     * @param paraNames
     * @return
     */
    public static Boolean checkMapHasColIsNull(Map<String, Object> map, String... paraNames) {
        if (map != null) {
            for (String str : paraNames) {
                if (map.get(str) == null) {
                    return true;
                }
            }
            return false;
        } else {
            return true;
        }
    }
    
    /**
     * 字符串转化成集合
     * 
     * @param ids
     *            1,2,3,4,5,6
     * @return
     */
    public static List<String> getListByIds(String ids) {
        List<String> list = new ArrayList<String>();
        if (!"".equals(ids) && null != ids) {
            String[] s = ids.split(",");
            list = new ArrayList<String>(Arrays.asList(s));
        }
        return list;
    }
    
    
    /**
     * 字符串转化成Long类型的List
     * 
     * @param ids
     *            1,2,3,4,5,6
     * @return
     */
    public static List<Long> getListLongByIds(String ids) {
        List<Long> list = new ArrayList<Long>();
        if (!"".equals(ids) && null != ids) {
            String[] s = ids.split(",");
            for (String str : s) {
                if (str != null && str.length() > 0) {
                    list.add(Long.valueOf(str));
                }
            }
        }
        return list;
    }
    
    public static <T> String getIdsByList(Collection<T> list) {
        if (list != null) {
            StringBuffer sb = new StringBuffer();
            for (T s : list) {
                if (s!=null
                        && StringUtils.isNotBlank(s.toString())) {
                    sb.append(s.toString() + ",");
                }
            }
            if (sb.length() > 0) {
                return sb.substring(0, sb.length() - 1);
            } else {
                return "";
            }
        } else {
            return null;
        }
    }
    

    
    public static <T> String getIdsByList(List<T> list, String key) {
        if (null != list && list.size() > 0) {
        	try {
        		List<Map<String, Object>> beanList = BeanToMapForList(list, new String[]{key});
            	List<String> flagList = new ArrayList<String>();
                StringBuffer sb = new StringBuffer();
                for (Map<String, Object> record : beanList) {
                    if (record.get(key) != null&&!flagList.contains(record.get(key).toString())) {
                        sb.append(record.get(key).toString() + ",");
                        flagList.add(record.get(key).toString());
                    }
                }
                if (sb.length() > 0) {
                    return sb.substring(0, sb.length() - 1);
                } else {
                    return "";
                }
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
        	return null;
        } else {
            return null;
        }
    }
    
    
    /**
     * @author cyl
     * @Title: getResultListData
     * @Description: TODO(从list中抽取想要返回的数据)
     * @param dataList
     * @param cols
     * @return List<Record> 抽取完成的数据
     */
    public static List<Map<String, Object>> getResultListData(List<Map<String, Object>> dataList, String... cols) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> r : dataList) {
            list.add(getResultData(r, cols));
        }
        return list;
    }
    
    
    /**
     * @author cyl
     * @Title: getResultData
     * @Description: TODO(从Map中抽取想要返回的数据)
     * @param r
     * @param cols
     * @return 抽取完成的数据
     */
    public static Map<String, Object> getResultData(Map<String, Object> r, String... cols) {
    	Map<String, Object> resultData = new HashMap<String, Object>();
        for (String col : cols) {
            resultData.put(col, r.get(col));
        }
        return resultData;
    }
    
    
    /**
     * list集合变成根据list中的key值为键的Map
     * @param list
     * @param key
     * @return
     */
    public static Map<Object, Map<String, Object>> getRecordStringMap(List<Map<String, Object>> list, String key) {
        Map<Object, Map<String, Object>> recordMap = new HashMap<Object, Map<String, Object>>();
        if(null != list && list.size() > 0){
        	for (Map<String, Object> r : list) {
                recordMap.put(r.get(key), r);
            }
        }
        
        return recordMap;
    }
    
    /**
	 * 从Map<String,Object>转化为Map<String, String>
	 * @author cyl
	 * @param map
	 * @return
	 */
	public static Map<String, String> getMapString(Map<String,Object> map){
		Map<String, String> stringMap=new HashMap<String,String>();
		Set<String> keys = map.keySet();
		for(String key:keys){
			Object value = map.get(key);
			if(value!=null){
				if(value instanceof Date){
					stringMap.put(key, ((Date)value).getTime()+"");
				} else{
					stringMap.put(key, value.toString());
				}
			}
		}
		return stringMap;
	}
	
	
	/**
     * @author cyl
     * @Title: mapValuesToList
     * @Description: 按照某个顺序将一个Map的values转为List
     * @param map 需要转换的Map
     * @param order 标记顺序的集合
     * @return
     */
    public static <K, V> List<V> mapValuesToList(Map<K,V> map,List<K> order){
        List<V> list=new ArrayList<V>();
        for(K k:order){
            list.add(map.get(k));
        }
        return list;
    }
    
    
    /**
	 * 对象列表转换成Map列表
	 * @param list
	 * @param fileds 保留的字段
	 * @return
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 */
	public static <T> List<Map<String,Object>> BeanToMapForList(List<T> list,String[] fileds) throws Exception{
		List<Map<String,Object>> result = new ArrayList<>();
		for (T t : list) {
			result.add(BeanToMap(t,fileds));
		}
		return result;
	}
    
	
	/**
	 * 对象转换成Map
	 * @param t
	 * @param fileds 保留的字段
	 * @return
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 */
	public static <T> Map<String,Object> BeanToMap(T t,String[] fileds) throws Exception{
		Map<String,Object> result = new HashMap<>();
		if(fileds!=null&&t!=null){
			for (String filed : fileds) {
				Field f = t.getClass().getDeclaredField(filed);
				f.setAccessible(true);
				result.put(filed,f.get(t));
			}
		}
		return result;
	}
    
}
