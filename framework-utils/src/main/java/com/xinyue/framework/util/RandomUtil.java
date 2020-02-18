package com.xinyue.framework.util;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang3.math.NumberUtils;

public class RandomUtil {

	
	/**
     * 获得一个[0,max)之间的整数。
     * @param max
     * @return
     */
    public static int getRandomInt(int intValue) {
    	Random random = new Random();
    	return  random.nextInt(intValue);
    }
     
    /**
     * 获得一个[0,max)之间的整数。
     * @param max
     * @return
     */
    public static long getRandomLong(long longValue) {
    	Random random = new Random();
    	return Math.abs(random.nextInt())%longValue;
    }
     
    /**
     * 从list中随机取得一个元素
     * @param list
     * @return 
     * @return
     */
    public static <E> E getRandomElement(List<E> list){
        return list.get(getRandomInt(list.size()));
    }
     
    /**
     * 从set中随机取得一个元素
     * @param set
     * @return 
     * @return
     */
    public static <E> E getRandomElement(Set<E> set){
        int rn = getRandomInt(set.size());
        int i = 0;
        for (E e : set) {
            if(i==rn){
                return e;
            }
            i++;
        }
        return null;
    }
     
    /**
     * 从map中随机取得一个key
     * @param map
     * @return
     */
    public static <K,V> K getRandomKeyFromMap(Map<K,V> map) {
        int rn = getRandomInt(map.size());
        int i = 0;
        for (K key : map.keySet()) {
            if(i==rn){
                return key;
            }
            i++;
        }
        return null;
    }
     
    /**
     * 从map中随机取得一个value
     * @param map
     * @return
     */
    public static <K,V> V getRandomValueFromMap(Map<K,V> map) {
        int rn = getRandomInt(map.size());
        int i = 0;
        for (V value : map.values()) {
            if(i==rn){
                return value;
            }
            i++;
        }
        return null;
    }
    
    /**
     * 生成min->max之间的数,最小生成的随机数为min，最大生成的随机数为max  
     * @param min
     * @param max
     * @return
     */
    public static int getRandomNumToInt(int min,int max){  
    	//要获取一个[x,y)的int类型的随机数 | 左闭右开
    	int d = min + (int)(Math.random() * (max - min));
    	return d;
//    	要获取一个(x,y]的int类型的随机数 | 左开右闭
//    	int d = y - (int)(Math.random() * (y - x));
//    	要获取一个[x,y]的int类型的随机数 | 左闭右闭
//    	int i = x + (int)(Math.random() * (y - x + 1));
//    	要获取一个(x,y)的int类型的随机数 | 左开右开
//    	int d = (int)((y - Math.random()) % y);
    }  
    
    /**
     * 生成min->max之间的数,最小生成的随机数为min，最大生成的随机数为max 
     * @param min
     * @param max
     * @return 默认保留两位小数
     */
    public static double getRandomNumToDouble(int min,int max){  
    	double s = min + (Math.random() * (max - min));
    	DecimalFormat format = new DecimalFormat("0.00");
		String format2 = format.format(s);
        return Double.parseDouble(format2);
    }
    
    /**
     * 生成min->max之间的数,最小生成的随机数为min，最大生成的随机数为max 
     * @param min
     * @param max
     * @return
     */
    public static double getRandomNumToDouble(double min,double max){  
    	//要获取一个[x,y)的double类型的随机数 | 左闭右开
    	double d = min + Math.random() * (max - min);
    	DecimalFormat format = new DecimalFormat("0.00");
		String format2 = format.format(d);
        return Double.parseDouble(format2);
//    	要获取一个(x,y]的double类型的随机数 | 左开右闭
//    	double d = y - Math.random() * (y - x);
//    	要获取一个[x,y]的double类型的随机数 | 左闭右闭
//    	double d = x + Math.random() * y % (y - x + 1);
//    	要获取一个(x,y)的double类型的随机数 | 左开右开
//    	double d = (y - Math.random()) % y;
    }
    
    public static double getSpecifyBitRandom(int bit,double value){
    	StringBuilder sb = new StringBuilder();
    	if(bit>0){
    		sb.append("0.");
    	}else{
    		sb.append("#.##");
    	}
    	for(int i=0;i<bit;i++){
    		sb.append("0");
    	}
    	DecimalFormat df = new DecimalFormat(sb.toString());
    	return NumberUtils.createDouble(df.format(value));
    }
    
    public static double getAverage(double[] darr){
    	int length = darr.length;
    	double d = 0;
    	for(int i=0;i<length;i++){
    		d = darr[i] + d;
    	}
    	return d;
    }
    
    public static double getAverage(List<Double> list){
    	int size = list.size();
    	double t = 0;
    	for(int i=0;i<size;i++){
    		t = list.get(i) + t;
    	}
    	return t;
    }
    
    public static void main(String[] args) {
       
    	for(int i=0;i<10;i++){
    		double randomNumToDouble = getRandomNumToDouble(0.01, 0.3);
    		System.out.println(randomNumToDouble);
    		/*double randomNumToDouble = getRandomNumToDouble(2, 8);
    		System.out.println(randomNumToDouble);*/
    	}
	}
}
