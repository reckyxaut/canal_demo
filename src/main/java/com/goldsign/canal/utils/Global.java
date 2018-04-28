package com.goldsign.canal.utils;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;


public class Global {
	public static final Logger LOGGER = LoggerFactory.getLogger(Global.class);
	private static Map<String, String> map = Maps.newHashMap();
	private static PropertiesLoader loader = new PropertiesLoader("app.properties");
	private static Map<String, PropertiesLoader> map_loader = Maps.newHashMap();
	
	public static String getConfig(String key) {
		String value = map.get(key);
		if (value == null){
			value = loader.getProperty(key);
			map.put(key, value != null ? value : StringUtils.EMPTY);
		}
		return value;
	}
	
	
	public static String getExtendConfig(String key,String prop,String configFile){
		String value = map.get(key);
		if (value == null){
			if(map_loader.get(prop)==null){
				map_loader.put(prop, new PropertiesLoader(configFile));
			}
			value = map_loader.get(prop).getProperty(key);
			map.put(key, value != null ? value : StringUtils.EMPTY);
		}
		return value;
	}
	
	
}
