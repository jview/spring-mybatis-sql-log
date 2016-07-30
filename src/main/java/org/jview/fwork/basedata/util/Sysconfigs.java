package org.jview.fwork.basedata.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * 限服务层使用
 * @author jinhe.chen
 *
 */
public class Sysconfigs {
	private static Logger logger = Logger.getLogger(Sysconfigs.class);
	private Properties properties;
	private static Map<String, Object> envMap=new HashMap<String, Object>();
	

	public static synchronized Map<String, Object> getEnvMap(){
		
		return envMap;
	}



	public void setProperties(Properties properties) {
		this.properties = properties;
		Set<Map.Entry<Object,Object>> sets=this.properties.entrySet();
		for(Map.Entry<Object,Object> entry:sets){
			envMap.put((String)entry.getKey(), entry.getValue());
		}
	}
	
	
}
