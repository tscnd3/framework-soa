package com.xinyue.framework.disconf.util;

import com.baidu.disconf.client.usertools.DisconfDataGetter;

/**
 * disconf动态获取配置文件
 * @author xiangtao
 *
 */
public class GetDisCfgUtil {

	/**
	 * 指定的默认配置文件
	 */
	private static final String DEFAULT_CONFIG = "config.properties";

	/**
	 * 根据key获取value
	 * @param key
	 * @return
	 */
	public static String getValue(String key) {
		return (String) DisconfDataGetter.getByFile(DEFAULT_CONFIG).get(key);
	}

	/**
	 * 指定配置文件和key获取value
	 * @param propertiesName
	 * @param key
	 * @return
	 */
	public static String getVale(String propertiesName, String key) {
		return (String) DisconfDataGetter.getByFile(propertiesName).get(key);
	}
}
