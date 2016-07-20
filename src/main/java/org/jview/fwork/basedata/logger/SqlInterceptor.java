package org.jview.fwork.basedata.logger;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

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
		String sql = getSql(configuration, boundSql);
		Object returnValue = null;
		HashMap<String, Object> paramMap=new HashMap<>();
		try {
			paramMap.put("threadId",String.valueOf(Thread.currentThread().getId()));
			returnValue = invocation.proceed();
//			System.out.println("----sql4="+sql);
//			if(returnValue!=null){
//				return returnValue;
//			}
			if(sql.indexOf("INSERT INTO tf_log_db")>=0){
				return returnValue;
			}
			long runTime = (System.currentTimeMillis() - time);
			
			this.logSqlManager.addLogSqlAsync(startTime, sqlId, sql, runTime, null, paramMap);
		}
		catch (Exception e) {
			long runTime = (System.currentTimeMillis() - time);
			this.logSqlManager.addLogSqlAsync(startTime, sqlId, sql, runTime, e, paramMap);
			throw e;
		}
		return returnValue;

	}

	public static String getSql(Configuration configuration, BoundSql boundSql) {

		String sql = showSql(configuration, boundSql);
		StringBuilder str = new StringBuilder(100);
//		str.append(sqlId);
//		str.append(":");
		str.append(sql);
//		str.append(":");
//		str.append(time);
//		str.append("ms");
		return str.toString();

	}

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

	public static String showSql(Configuration configuration, BoundSql boundSql) {
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
					} else if (boundSql.hasAdditionalParameter(propertyName)) {
						Object obj = boundSql.getAdditionalParameter(propertyName);
						sql = sql.replaceFirst("\\?", getParameterValue(obj));
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
	
	public void setProperties(Properties paramProperties){
		
	}

}