package com.xinyue.framework.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class StrUtils {
	
	
	public static String getStrSort(String string){
		String returnJson ="";
		if(string.indexOf(",")>0){
			String[] sList = string.split(",");
			List<String> anonyList = new ArrayList<>();
			for(String str : sList){
				anonyList.add(str);
			}
			//按匿名名称排序
			Collections.sort(anonyList,new Comparator<String>() {
				@Override
				public int compare(String o1, String o2) {
					return o1.compareTo(o2);
				}
			});
			for(String params : anonyList){
				returnJson +=  params + ",";
			}
			returnJson = returnJson.substring(0, returnJson.lastIndexOf(","));
			return returnJson;
		}
		return null;
	}
	
	public static void main(String[] args) {
		System.out.println(getStrSort("123asd1,sdg45,sddf21,"));
	}
}
