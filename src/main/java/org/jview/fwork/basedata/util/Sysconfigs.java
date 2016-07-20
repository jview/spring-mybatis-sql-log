package org.jview.fwork.basedata.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * 限服务层使用
 * @author jinhe.chen
 *
 */
public class Sysconfigs {
	private static Logger logger = Logger.getLogger(Sysconfigs.class);
	
	

	public static synchronized Map<String, Object> getEnvMap(){
		
		Map<String, Object> envMap=new HashMap<>();
//		envMap.put("app.logSqlIgnore", "select,select_count");
		
		return envMap;
	}
}
