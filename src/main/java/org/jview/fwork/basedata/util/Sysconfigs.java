package org.jview.fwork.basedata.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * 限服务层使用
 * @author jinhe.chen
 *
 */
public class Sysconfigs {
	private static Logger logger = Logger.getLogger(Sysconfigs.class);
	private Properties properties;
	private static Map envMap=new HashMap();
	

	public static synchronized Map<String, Object> getEnvMap(){
		
		return envMap;
	}



	public void setProperties(Properties properties) {
		this.properties = properties;
		Set<Map.Entry<Object,Object>> sets=this.properties.entrySet();
		for(Map.Entry<Object,Object> entry:sets){
			if("sql.ignoreSqlId".equals(entry.getKey())){
				String v=(String)entry.getValue();
				envMap.put(entry.getKey(), this.getString2Set(v));
			}
			else if("sql.ignoreKey".equals(entry.getKey())){
				String v=(String)entry.getValue();
				envMap.put(entry.getKey(), this.getString2Set(v));
			}
			else{
				envMap.put((String)entry.getKey(), entry.getValue());
			}
		}
	}
	
	private Set<String> getString2Set(String lines){
		Set<String> sets=new HashSet<String>();
		if(!StringUtils.isEmpty(lines)){
			lines=lines.trim();
			String[] sqlIds=lines.split("\n");
			for(String sqlId:sqlIds){
				sqlId=sqlId.trim();
				if(!"".equals(sqlId)){
					sets.add(sqlId);
				}
			}
		}
		
		return sets;
	}
	
	
}
