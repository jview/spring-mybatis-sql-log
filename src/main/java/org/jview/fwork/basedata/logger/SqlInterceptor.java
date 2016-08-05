package org.jview.fwork.basedata.logger;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.apache.log4j.Logger;
import org.jview.fwork.basedata.service.ILogSqlManager;
import org.jview.fwork.basedata.util.Sysconfigs;

import com.google.gson.Gson;

import ai.yc.common.core.data.RetResult;
import ai.yc.common.core.page.PageVO;

@Intercepts({
		@Signature(type = Executor.class, method = "update", args = {
				MappedStatement.class, Object.class }),
		@Signature(type = Executor.class, method = "query", args = {
				MappedStatement.class, Object.class,
				RowBounds.class, ResultHandler.class })
		})
public class SqlInterceptor implements Interceptor {
	private static final Logger logger = Logger.getLogger(SqlInterceptor.class);
	private ILogSqlManager logSqlManager;
	private static Map configMap=new HashMap();

	
	public Object intercept(Invocation invocation) throws Throwable {
		final long time = System.currentTimeMillis();
		Date startTime= new Date();
		
		MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
		Object parameter = null;
		
		if (invocation.getArgs().length > 1) {
			parameter = invocation.getArgs()[1];
		}

		String sqlId = mappedStatement.getId();
		BoundSql boundSql = mappedStatement.getBoundSql(parameter);
		Configuration configuration = mappedStatement.getConfiguration();
		HashMap<String, Object> pMap=new HashMap<>();
		String sql = getSql(configuration, boundSql, pMap);
		Object returnValue = null;
		HashMap<String, Object> paramMap=new HashMap<>();
		try {
			paramMap.put("threadId",String.valueOf(Thread.currentThread().getId()));
			paramMap.put("args", gson.toJson(pMap));
			returnValue = invocation.proceed();
			long runTime = (System.currentTimeMillis() - time);
//			if(returnValue!=null){
//				return returnValue;
//			}
			//取得数据行数
			Integer rows=this.getReturnRows(returnValue);
			paramMap.put("rows", rows);
			
			//忽略指定的查询类型
			String ignoreSqlType=(String)configMap.get("sql.logSqlIgnore");
			String operType=this.getOperTypeBySql(sql);
			if(isIgnore(operType, sql, ignoreSqlType)){
				logger.debug("-----ignoreSqlType sqlId="+sqlId+":"+runTime+"ms"+":"+sql);
				return returnValue;
			}
			
			//忽略mybatis的mapperId的对应的查询日志
			Set<String> ignoreSqlIds=(Set<String>)configMap.get("sql.ignoreSqlId");
			if(this.isIgnoreSqlId(sqlId, ignoreSqlIds)){
				logger.debug("-----ignoreSqlId sqlId="+sqlId+":"+runTime+"ms"+":"+sql);
				return returnValue;
			}
			
			//忽略sql含有指定关键字的查询日志
			Set<String> ignoreSqlKey=(Set<String>)configMap.get("sql.ignoreKey");
			if(this.isContainIgnoreSqlKey(sql, ignoreSqlKey)){
				logger.debug("-----ignoreKey sqlId="+sqlId+":"+runTime+"ms"+":"+sql);
				return returnValue;
			}
			
			Integer logInfo=this.getConfigValue("sql.logInfo", 0);
			if(runTime<logInfo){
				logger.debug("-----logInfo env sqlId="+sqlId+":"+runTime+"ms"+":"+sql);
				return returnValue;
			}
			
			//忽略指定的查询类型
			String logSqlIgnore=(String)Sysconfigs.getEnvMap().get("sql.logSqlIgnore");
			if(isIgnore(operType, sql, logSqlIgnore)){
				logger.debug("-----logSqlIgnore env sqlId="+sqlId+":"+runTime+"ms"+":"+sql);
				return returnValue;
			}

			//忽略mybatis的mapperId的对应的查询日志
			ignoreSqlIds=(Set<String>)Sysconfigs.getEnvMap().get("sql.ignoreSqlId");
			if(this.isIgnoreSqlId(sqlId, ignoreSqlIds)){
				logger.debug("-----ignoreSqlId env sqlId="+sqlId+":"+runTime+"ms"+":"+sql);
				return returnValue;
			}
			
			
			//忽略sql含有指定关键字的查询日志
			ignoreSqlKey=(Set<String>)Sysconfigs.getEnvMap().get("sql.ignoreKey");
			if(this.isContainIgnoreSqlKey(sql, ignoreSqlKey)){
				logger.debug("-----ignoreSqlKey env sqlId="+sqlId+":"+runTime+"ms"+":"+sql);
				return returnValue;
			}
			
//			long runTime2 = (System.currentTimeMillis() - time);
//			System.out.println("----runTime="+runTime+"/"+runTime2);
			this.logSqlManager.addLogSqlAsync(startTime, sqlId, sql, runTime, null, paramMap);
		}
		catch (Exception e) {
			long runTime = (System.currentTimeMillis() - time);
			this.logSqlManager.addLogSqlAsync(startTime, sqlId, sql, runTime, e, paramMap);
			throw e;
		}
		return returnValue;

	}
	
	private boolean isIgnoreSqlId(String sqlId, Set<String> ignoreSqlIdSet){
		if(ignoreSqlIdSet==null||ignoreSqlIdSet.isEmpty()){
			return false;
		}
		
		String method=sqlId.substring(sqlId.lastIndexOf(".")+1);
		String className=sqlId.substring(0, sqlId.lastIndexOf("."));
		className=className.substring(className.lastIndexOf(".")+1);
		String classMethod=className+"."+method;
		if(ignoreSqlIdSet.contains(classMethod)){
			return true;
		}
		
		return false;
	}
	
	private boolean isContainIgnoreSqlKey(final String sql, final Set<String> ignoreSqlKeySet){
		if(ignoreSqlKeySet==null){
			return false;
		}
//		System.out.println(ignoreSqlKeySet);
		for(String ignoreSqlKey:ignoreSqlKeySet){
			if(ignoreSqlKey.length()>0 && sql.indexOf(ignoreSqlKey)>=0){
				logger.debug("----ignore:"+ignoreSqlKey+" --sql:"+sql);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 获取返回的数据行数
	 * @param retValue
	 * @return
	 */
	private Integer getReturnRows(final Object retValue){
		if(retValue==null){
			return 0;
		}
		if(retValue instanceof RetResult){
			RetResult ret=(RetResult)retValue;
			return ret.getSize();
		}
		else if(retValue instanceof PageVO){
			PageVO page=(PageVO)retValue;
			if(page.getDatas()!=null)
				return page.getDatas().size();
		}
		else if(retValue instanceof List){
			return ((List)retValue).size();
		}
//		应该不需要数组，因为一般情况下只会返回list
//		else if(retValue.getClass().isArray()){
//			return ((Object[])retValue).length;
//		}
		return 0;
	}

	public static String getSql(Configuration configuration, BoundSql boundSql, Map<String, Object> paramMap) {

		String sql = showSql(configuration, boundSql, paramMap);
		StringBuilder str = new StringBuilder(100);
//		str.append(sqlId);
//		str.append(":");
		str.append(sql);
//		str.append(":");
//		str.append(time);
//		str.append("ms");
		return str.toString();

	}
	
	/**
	 * 获取数据库操作类型
	 * @param sql
	 * @return
	 */
	private String getOperTypeBySql(String sql){
		String operType=null;
		String oper=sql;
		if(sql.length()>6){
			oper=sql.substring(0, 6);
		}
		operType=oper.toLowerCase();
		if(operType.equals("select")){
			if(sql.indexOf("count(0)")>0){
				operType+="_count";
			}
			else if(sql.indexOf(" _nextval(")>0){
				operType+="_seq";
			}
		}
		return operType;
	}
	
	/**
	 * 是否忽略
	 * @param operType
	 * @param sql
	 * @param logSqlIgnore
	 * @return
	 */
	private boolean isIgnore(final String operType, final String sql, final String logSqlIgnore){
		if(logSqlIgnore==null){
			return false;
		}
		String[] ignores=logSqlIgnore.split(",");
		for(String ignore:ignores){
			ignore=ignore.trim();
			if(StringUtils.isEmpty(ignore)){
				continue;
			}
			if(operType.equals(ignore)||sql.indexOf(ignore)>=0){
				logger.debug("----ignore:"+ignore+" --sql:"+sql);
				return true;
			}
		}
		return false;
	}
	private static  Gson gson = new Gson();
	
	private static String getParameterValue(Object obj) {
		String value = null;

		if (obj instanceof String) {
			value = "'" + obj.toString() + "'";
		} else if (obj instanceof Date) {
			DateFormat formatter = DateFormat.getDateTimeInstance(
					DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
			value = "'" + formatter.format((Date)obj) + "'";//日期时间问题处理
		} else {
			if (obj != null) {
				value = obj.toString();
			} else {
				value = "null";
			}
		}
		return value;
	}

	public static String showSql(Configuration configuration, BoundSql boundSql, Map<String, Object> paramMap) {
		Object parameterObject = boundSql.getParameterObject();
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
		String sql = boundSql.getSql().replaceAll("[\\s]+", " ");

		if (parameterMappings.size() > 0 && parameterObject != null) {
			TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
			if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
				sql = sql.replaceFirst("\\?",getParameterValue(parameterObject));
			} else {
				MetaObject metaObject = configuration.newMetaObject(parameterObject);
				for (ParameterMapping parameterMapping : parameterMappings) {
					String propertyName = parameterMapping.getProperty();
					if (metaObject.hasGetter(propertyName)) {
						Object obj = metaObject.getValue(propertyName);
						sql = sql.replaceFirst("\\?", getParameterValue(obj));
						paramMap.put(propertyName, obj);
					} else if (boundSql.hasAdditionalParameter(propertyName)) {
						Object obj = boundSql.getAdditionalParameter(propertyName);
						sql = sql.replaceFirst("\\?", getParameterValue(obj));
						paramMap.put(propertyName, obj);
					}
				}
			}
		}
		return sql;

	}

	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}


	public void setLogSqlManager(ILogSqlManager logSqlManager) {
		this.logSqlManager = logSqlManager;
	}
	
	public void setProperties(Properties properties){
		Set<Map.Entry<Object,Object>> sets=properties.entrySet();
		String key=null;
		for(Map.Entry<Object,Object> entry:sets){
			key=(String)entry.getKey();
			if("sql.ignoreSqlId".equals(entry.getKey())){
				String v=(String)entry.getValue();
				configMap.put(entry.getKey(), this.getString2Set(key, v));
			}
			else if("sql.ignoreKey".equals(entry.getKey())){
				String v=(String)entry.getValue();
				configMap.put(entry.getKey(), this.getString2Set(key, v));
			}
			else{
				configMap.put((String)entry.getKey(), entry.getValue());
			}
		}
		logger.info("sqlIntercepter.properties="+configMap);
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
			String[] sqlIds=lines.split("\n");
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
	
	private Integer getConfigValue(final String key, final Integer defaultValue){
		Object v=Sysconfigs.getEnvMap().get(key);
		if(v==null){
//			logger.warn(" key="+key+" is null");
			return defaultValue;
		}
		
		Integer logInfo=0;
		if(v instanceof Integer){
			logInfo=(Integer)v;
		}
		else{
			logInfo=Integer.parseInt(""+v);
		}
		return logInfo;
		
	}

}