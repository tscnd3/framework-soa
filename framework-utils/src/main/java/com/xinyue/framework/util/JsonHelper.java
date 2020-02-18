package com.xinyue.framework.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class JsonHelper {
	/**
	 * 判断是JsonObject
	 * @param obj
	 * @return
	 */
	public static boolean isJsonObject(Object obj){
		String content = obj.toString();
	    try {
	        JSONObject.parseObject(content);
	        if (content.startsWith("{")) {
	        	return  true;
			}else {
				return  false;
			}
	   } catch (Exception e) {
	        return false;
	  }
	}
	
	/**
	 * 判断是JsonArray
	 * @param obj
	 * @return
	 */
	public static boolean isJsonArray(Object obj){
		String content = obj.toString();
	    try {
	        JSONArray.parseArray(content);
	        if (content.startsWith("[")) {
	        	return  true;
			}else {
				return  false;
			}
	   } catch (Exception e) {
	        return false;
	  }
	}
	
	
	/**
	 * 读取json文件，返回json串
	 * 
	 * @param fileName
	 * @return
	 */
	public static String readJsonFile(String fileName) {
		String jsonStr = "";
		try {
			File jsonFile = new File(fileName);
			FileReader fileReader = new FileReader(jsonFile);
			Reader reader = new InputStreamReader(new FileInputStream(jsonFile), "utf-8");
			int ch = 0;
			StringBuffer sb = new StringBuffer();
			while ((ch = reader.read()) != -1) {
				sb.append((char) ch);
			}
			fileReader.close();
			reader.close();
			jsonStr = sb.toString();
			return jsonStr;
		} catch (IOException e) {
			System.out.println(String.format("read jsonfile {} fail",fileName));
			return null;
		}
	}
	
}
