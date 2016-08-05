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
	

	public static Map<String, Object> getEnvMap(){
		return envMap;
	}



	public void setProperties(Properties properties) {
		this.properties = properties;
		Set<Map.Entry<Object,Object>> sets=this.properties.entrySet();
		String key=null;
		for(Map.Entry<Object,Object> entry:sets){
			key=(String)entry.getKey();
			if("sql.ignoreSqlId".equals(entry.getKey())){
				String v=(String)entry.getValue();
				envMap.put(entry.getKey(), this.getString2Set(key, v));
			}
			else if("sql.ignoreKey".equals(entry.getKey())){
				String v=(String)entry.getValue();
				envMap.put(entry.getKey(), this.getString2Set(key, v));
			}
			else if("service.ignoreIntfKey".equals(entry.getKey())){
				String v=(String)entry.getValue();
				envMap.put(entry.getKey(), this.getString2Set(key, v));
			}
			else{
				envMap.put((String)entry.getKey(), entry.getValue());
			}
		}
		logger.info("sysconfigs.envMap="+envMap);
	}
	
	/**
	 * ModelMapper:create,insert 转成 ModelMapper.create,ModelMapper.insert
	 * @param lines
	 * @return
	 */
	private Set<String> getString2Set(String key, String lines){
		Set<String> sets=new HashSet<String>();
		if(!StringUtils.isEmpty(lines)){
			lines=lines.trim();
			String[] sqlIds=null;
			if(lines.indexOf(";")>0){
				sqlIds=lines.split(";");
			}
			else{
				sqlIds=lines.split("\n");
			}
			for(String sqlId:sqlIds){
				sqlId=sqlId.trim();
				if(!"".equals(sqlId)){
					addAllSqlIds(key, sqlId, sets);
				}
			}
		}
		
		return sets;
	}


	/**
	 * 根据配置的信息生成对应的每个方法的sqlId配置
	 * ModelMapper:create,insert 转成 ModelMapper.create,ModelMapper.insert
	 * @param sets
	 * @param sqlId
	 * @return
	 */
	private void addAllSqlIds(String key, String sqlId, Set<String> sets) {
		String className="";
		if("sql.ignoreSqlId".equals(key) && sqlId.indexOf(":")>0){
			className=sqlId.substring(0, sqlId.indexOf(":"));
			sqlId=sqlId.substring(sqlId.indexOf(":")+1);
		}
		String[] methods=sqlId.split(",");
		for(String method:methods){
			method=method.trim();
			if(className.isEmpty()){
				sets.add(method);
			}
			else{
				sets.add(className+"."+method);
			}
		}
	}
	
	
}
