package com.xinyue.framework.mogo.helper;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.xinyue.framework.mogo.annotation.QueryField;

@Component
public class MogoBaseDao<T>{
	
private Logger LOGGER = LoggerFactory.getLogger(MogoBaseDao.class);
	
	@Autowired
	public MongoTemplate mongoTemplate;
	
	/**
	 * 保存
	 * @param collectionName
	 * @param t
	 * @return
	 */
	public  boolean save(T t,String collectionName) {
		boolean bool = false;
		try {
			if (t != null && StringUtils.isNotBlank(collectionName)) {
				mongoTemplate.save(t, collectionName);
				bool = true;
			} else {
				LOGGER.error("添加到mogo中异常->  缓存对象不能为空");
			}
		} catch (Exception e) {
			LOGGER.error("添加到mogo中异常->", e);
		}
		return bool;
	}
	
	
	
	/**
	 * 保存
	 * @param collectionName
	 * @param t
	 * @return
	 */
	public  boolean insert(String collectionName, T t) {
		boolean bool = false;
		try {
			if (t != null && StringUtils.isNotBlank(collectionName)) {
				mongoTemplate.insert(t, collectionName);
				bool = true;
			} else {
				LOGGER.error("添加到mogo中异常->  缓存对象不能为空");
			}
		} catch (Exception e) {
			LOGGER.error("添加到mogo中异常->", e);
		}
		return bool;
	}
	
	
	/**
	 * 根据id删除对象 
	 * @param query
	 * @param collectionName
	 * return  受影响的行数
	 */
	public  Integer deleteById(T t,final String collectionName){
		Integer num=0;
		DeleteResult result=mongoTemplate.remove(t, collectionName);
		if(result!=null){
			num=Integer.valueOf(String.valueOf(result.getDeletedCount()));
		}
		return num;
	}
	
	/**
	 * 根据对象的属性删除  
	 * @param t
	 * @param collectionName
	 * return  受影响的行数
	 */
    public Integer deleteByCondition(T t,final String collectionName) { 
    	Integer num=0;
        Query query = buildBaseQuery(t);  
        DeleteResult result=mongoTemplate.remove(query, getEntityClass(t),collectionName);  
        if(result!=null){
			num=Integer.valueOf(String.valueOf(result.getDeletedCount()));
		}
		return num;
    }  
	
    
    /**
     * 根据条件修改
     * @param query
     * @param update
     * @param collectionName
     */
    public Integer updateMulti(Query query,Update update,final String collectionName){
    	Integer num=0;
    	UpdateResult result=mongoTemplate.updateMulti(query, update, collectionName);
    	if(result!=null){
 			num=Integer.valueOf(String.valueOf(result.getModifiedCount()));
 		}
 		return num;
    }
    
    
	/**
	 * 更新
	 * @param t
	 * @param collectionName
	 * @return
	 */
	public  Integer upsert(T t, String collectionName) {
		Integer num=0;
		try {
			if (t != null && StringUtils.isNotBlank(collectionName)) {
				Update update = new Update();
				Class<? extends Object> clazz = t.getClass();
				Field[] fields = clazz.getDeclaredFields();
				Query query = null;
				String[] pkeys={"_id","id"};
				Map<String,Object> queryConditionMap=new HashMap<>();
				for (Field field : fields) {
					try {
						field.setAccessible(true);
						Object value = field.get(t);
						if (value != null) {
							if ("serialVersionUID".equals(field.getName())) {
								continue;
							}
							if(Arrays.binarySearch(pkeys, field.getName())>0){
								query = new Query();
								queryConditionMap.put(field.getName(), value);
							}else{
								update.set(field.getName(), value);								
							}
						}
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
				if (query != null) {
					if(queryConditionMap.containsKey(pkeys[0])){
						ObjectId objectId=	new ObjectId(queryConditionMap.get(pkeys[0]).toString());
						query.addCriteria(Criteria.where(pkeys[0]).is(objectId));
					}else if (queryConditionMap.containsKey(pkeys[1])) {
						ObjectId objectId=	new ObjectId(queryConditionMap.get(pkeys[1]).toString());
						query.addCriteria(Criteria.where(pkeys[0]).is(objectId));
					}	
					UpdateResult result=mongoTemplate.upsert(query, update, collectionName);
					if(result!=null){
						num=Integer.valueOf(String.valueOf(result.getModifiedCount()));
			 		}
			 		return num;
				}else{
					LOGGER.error("更新到mogo中异常->  query对象不能为空");
				}
			} else {
				LOGGER.error("更新到mogo中异常-> 对象不能为空");
			}
		} catch (Exception e) {
			LOGGER.error("更新到mogo中异常->", e);
		}
		return num;
	}
	
	
	
	
	
	
	/**
	 * 通过一定的条件查询一个实体  
	 * @param query
	 * @param collectionName
	 * @param clazz
	 * @return
	 */
	public  T findOne(Query query, final String collectionName, Class<T> clazz) {
		return mongoTemplate.findOne(query, clazz, collectionName);
	}
	
	
	/**
	 * 通过条件查询实体(集合)
	 * @param query
	 * @param collectionName
	 * @param clazz
	 * @return
	 */
	public  List<T> find(Query query, final String collectionName, Class<T> clazz) {
		return mongoTemplate.find(query, clazz, collectionName);
	}
	
	/**
	 * 通过实体注解条件查询实体(集合)
	 * @param t
	 * @param collectionName
	 * @return
	 */
	public List<T> findByCondition(T t,final String collectionName) {  
        Query query = buildBaseQuery(t);  
        return mongoTemplate.find(query,getEntityClass(t),collectionName);  
    }
	
	
	/**
	 * 支持排序
	 * 通过实体注解条件查询实体(集合)
	 * @param t
	 * @param collectionName
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public List<T> findByCondition(T t,MogSort sort,final String collectionName) {  
        Query query = buildBaseQuery(t); 
        if(sort!=null){
        	Order order=null;
        	if(MogSort.Direction.ASC.getValue().equals(sort.getDirection().getValue())){
        		 order=new Order(Sort.Direction.ASC,sort.getSortField());
        	}else{
        		 order=new Order(Sort.Direction.DESC,sort.getSortField());
        	}
        	if(order!=null){
        		query.with(new Sort(order));
        	}
        }
        return mongoTemplate.find(query,getEntityClass(t),collectionName);  
    }
	
	
	
	@SuppressWarnings("deprecation")
	public PageImpl<T> findPage(MogCondition<T> mogCondition, final String collectionName){
		T t=mogCondition.getT();
		Query query = buildBaseQuery(t); 
		Pageable pageable = new PageRequest(mogCondition.getCurrPage(), mogCondition.getPageSize()); // get 5 profiles on a page
	    query.with(pageable);
	    if(mogCondition.getMogSort()!=null){
	    	Order order=null;
	    	if(MogSort.Direction.ASC.getValue().equals(mogCondition.getMogSort().getDirection().getValue())){
	    		 order=new Order(Sort.Direction.ASC,mogCondition.getMogSort().getSortField());
	    	}else{
	    		order=new Order(Sort.Direction.DESC,mogCondition.getMogSort().getSortField());
	    	}
	    	if(order!=null){
		    	query.with(new Sort(order));	  
	    	}
	    }
	    long count = mongoTemplate.count(query,getEntityClass(t), collectionName);
	    List<T> items = mongoTemplate.find(query, getEntityClass(t), collectionName);
	    PageImpl<T> page=new PageImpl<>(items,pageable,count);
	    return page;
	}
	
	
	
	
	
	/**
	 *  通过ID获取记录,并且指定了集合名
	 * @param id
	 * @param collectionName
	 * @param clazz
	 * @return
	 */
    public T get(String id, String collectionName,Class<T> clazz) {  
        return mongoTemplate.findById(id, clazz, collectionName);  
    } 
	
	
	/**
	 * 通过带自定义注解注解实体获取Query
	 * @param t
	 * @return
	 */
	private Query buildBaseQuery(T t) {  
        Query query = new Query();  
        Field[] fields = t.getClass().getDeclaredFields();  
        for (Field field : fields) {  
            field.setAccessible(true);  
            try {  
                Object value = field.get(t);  
                if (value != null) {  
                    QueryField queryField = field.getAnnotation(QueryField.class);  
                    if (queryField != null) {  
                        query.addCriteria(queryField.type().buildCriteria(queryField, field, value));  
                    }  
                }  
            } catch (Exception e) {
            	LOGGER.error("获取到query条件异常->",e);
            }  
        }  
        return query;  
      }
	
	
	  // 获取需要操作的实体类class  
    @SuppressWarnings("unchecked")  
    protected Class<T> getEntityClass(T t) {  
        return (Class<T>) t.getClass();
    }  
    
    //被继承后可以 获取需要操作的实体类class  
    @SuppressWarnings("unchecked")  
    protected Class<T> getEntityClass() {  
        return ((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);  
    } 
}
