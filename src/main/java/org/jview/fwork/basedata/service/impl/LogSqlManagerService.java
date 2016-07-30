package org.jview.fwork.basedata.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jview.fwork.basedata.logger.LogService;
import org.jview.fwork.basedata.model.LogDbPO;
import org.jview.fwork.basedata.model.ModelPO;
import org.jview.fwork.basedata.service.ILogDbService;
import org.jview.fwork.basedata.service.ILogSqlManager;
import org.jview.fwork.basedata.service.IModelService;
import org.jview.fwork.basedata.util.Sysconfigs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import ai.yc.common.core.data.RetResult;
import ai.yc.common.core.util.ServerUtil;

@Service("logSqlManager")
public class LogSqlManagerService implements ILogSqlManager {
	private static final Logger logger = Logger.getLogger(LogSqlManagerService.class);

	@Autowired
	private ILogDbService logDbService;
	
	@Autowired
	@Lazy
	private IModelService modelService;

	@Resource(name = "taskExecutor")
	private TaskExecutor taskExecutor;

	public void setTaskExecutor(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}
	
	private Properties properties;
	private static Map<String, Long> modelIdMap=new HashMap<>();
	private static Map<String, Object> dataMap=null;
	private static void loadProperties(Properties properties){
		dataMap=new HashMap<String, Object>();
		Integer logInfo=0;
		Integer logWarn=2000;
		Set<String> ignoreSqlKey=new HashSet<String>();
		if(properties!=null){
			logInfo=Integer.parseInt(properties.getProperty("sql.logInfo", "500"));
			logWarn=Integer.parseInt(properties.getProperty("sql.logWarn", "2000"));
			String ignoreSql=properties.getProperty("sql.ignoreKey", "");
			if(ignoreSql!=null){
				String[] keys=ignoreSql.split(",");
				for(String key:keys){
					key=key.trim();
					ignoreSqlKey.add(key);
				}
			}
		}
		ignoreSqlKey.add("FROM tf_sysconfig WHERE ( IF_dEL = 0 and STATUS = 1 )");
//		ignoreSqlKey.add("INSERT INTO tf_log_db");
		logger.info("-----logLevel:logInfo="+logInfo+"ms, logWarn="+logWarn+"ms");
		dataMap.put("sql.logInfo", logInfo);
		dataMap.put("sql.logWarn", logWarn);
		dataMap.put("sql.ignoreKey", ignoreSqlKey);
	}

	
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

	
	private void insertOperatorLog(Integer logLevel, Date startDate, String sqlId, String sql, long runTime, Exception exp, HashMap<String, Object> paramMap){
		LogDbPO logDb=this.prepareLogDb(startDate, runTime, paramMap);
		logDb.setLogLevel(logLevel);
		logDb.setFlag("0");
		logDb.setJsonParam((String)paramMap.get("args"));
		ModelPO model=null;
		if(sql!=null){
			model=this.prepareModel(sqlId);
			String operType=this.getOperTypeBySql(sql);
			logDb.setOperType(operType);
			logDb.setJsonRet(sql);
			logDb.setServiceId(-1l);
			String logSqlIgnore=(String)Sysconfigs.getEnvMap().get("app.logSqlIgnore");
			if(exp==null && isIgnore(logDb.getOperType(), sql, logSqlIgnore)){
				return;
			}
		}
		else{
			model=this.prepareModel(paramMap);
			logDb.setServiceId(0l);
			logDb.setJsonRet((String)paramMap.get("returnValue"));
			LogService logService=(LogService)paramMap.get("logService");
			if(logService!=null){
				logDb.setTitle(logService.title());
				logDb.setAuthor(logService.author());
				logDb.setDescs(logService.descs());
				logDb.setCalls(logService.calls());
			}
		}

		
		if(exp!=null){
			String expInfo=ExceptionUtils.getFullStackTrace(exp);
			//把caused by的异常信息提到前面去，以免被日志给截掉
			int causedBy_index=expInfo.indexOf("Caused by:");
			if(causedBy_index>=0){
				String causeBy=expInfo.substring(causedBy_index, causedBy_index+200)+"\n";
				expInfo=causeBy+expInfo;
			}
			expInfo=expInfo.replaceAll("\\$", "");
			logDb.setJsonRet(exp.getMessage()+":\n"+expInfo);
			logDb.setFlag("3");
//			System.out.println("-----exceptions="+logDb.getJsonRet());
		}
		
		
		if(modelService!=null){
			Long modelId=initModelId(model);
			logDb.setModelId(modelId);
		}

		this.logDbService.log(logDb);
	}

	/**
	 * @param logDb
	 * @param model
	 */
	private Long initModelId(ModelPO model) {
		String key=model.getPackageName()+"."+model.getClassName()+"."+model.getFuncCode();
		Long modelId=modelIdMap.get(key);
		if(modelId==null){
			RetResult<ModelPO> ret=this.modelService.findModelByCode(model.getClassName(), model.getFuncCode());
			if(ret.isSuccess() && ret.getSize()>0){
				modelId=ret.getFirstData().getCid();
			}
			else{
				this.modelService.insertModel(model);
				modelId=model.getCid();
			}
			modelIdMap.put(key, modelId);
		}
			
		return modelId;
		
	}
	

	
	private ModelPO prepareModel(Map<String, Object> paramMap){
		String target=(String)paramMap.get("target");
		int find=target.indexOf("@");
		String className=target;
		if(find>0){
			className=target.substring(0,  find);
		}
		String packageName=null;
		find=className.lastIndexOf(".");
		if(find>0){
			packageName=className.substring(0, find);
			className=className.substring(find+1);
		}
		ModelPO model=new ModelPO();
		model.setFuncCode((String)paramMap.get("method"));
		model.setClassName(className);
		model.setPackageName(packageName);
		model.setCreateDate(new Date());
		return model;
	}
	
	private ModelPO prepareModel(String sqlId){
		ModelPO model=new ModelPO();
		String funcCode=sqlId.substring(sqlId.lastIndexOf(".")+1);
		String className=sqlId.substring(0, sqlId.lastIndexOf("."));
		String packageName=className;
		int found=className.lastIndexOf(".");
		if(found>0){
			packageName=className.substring(0, found);
			className=className.substring(found+1);
		}
		model.setFuncCode(funcCode);
		model.setClassName(className);
		model.setPackageName(packageName);
		return model;
	}
	
	private LogDbPO prepareLogDb(Date startDate, long runTime, HashMap<String, Object> paramMap){
		LogDbPO logDb = new LogDbPO();
//		if(request!=null){
//			logDb.setIp(AppContext.getIpAddr(request));
//		}
////		System.out.println("----prepareLogDb-userName="+AppContext.getUsername());
//		logDb.setUserName(AppContext.getUsername());
//		logDb.setUserId(AppContext.getCurrentUserId());
		if(logDb.getUserName()==null){
			logDb.setUserName("");
		}
		
		logDb.setCreateDate(startDate);
		logDb.setRunTime(runTime);
		logDb.setIp(this.getServerIP());
		String threadCode=(String)paramMap.get("threadId");
		if(threadCode!=null){
			logDb.setThreadId(Long.parseLong(threadCode));
		}
		return logDb;
		
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
	private static String ip=null;
	private static String getServerIP(){
		if(ip==null){
			ip=ServerUtil.getLocalIP();
			if(ip!=null){
				if(ip.startsWith("192.168.")){
					ip=ip.replace("192.168.", "");
				}
//				else if(ip.startsWith("192.168.")){
//					ip=ip.replace("192.168.", "");
//				}
			}
		}
		return ip;
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
	
	private Integer getConfigValue(String key){
		Integer logInfo=(Integer)dataMap.get(key);
		if(logInfo==null){
			logInfo=0;
		}
		Map<String, Object> envMap=Sysconfigs.getEnvMap();
		Object v=envMap.get(key);
		Integer logInfo2=null;
		if(v!=null){
			if(v instanceof Integer){
				logInfo2=(Integer)v;
			}
			else{
				logInfo2=Integer.parseInt(""+v);
			}
		}
		if(logInfo2!=null){
			logInfo=logInfo2;
		}
		return logInfo;
		
	}
	
	@Override
	public void addLogSqlAsync(Date startTime, String sqlId, String sql, long runTime, Exception exp, HashMap<String, Object> paramMap){
		if(dataMap==null){
			loadProperties(this.properties);
		}
		
		Set<String> ignoreKey=(Set<String>)dataMap.get("sql.ignoreKey");
		if(isContainIgnoreSqlKey(sql, ignoreKey)){
			logger.debug(sqlId+":"+sql+":"+runTime+"ms");
			return;
		}
		
		Map<String, Object> envMap=Sysconfigs.getEnvMap();
		ignoreKey=(Set<String>)envMap.get("sql.ignoreKey");
		if(isContainIgnoreSqlKey(sql, ignoreKey)){
			logger.debug(sqlId+":"+sql+":"+runTime+"ms");
			return;
		}
		
		Integer logInfo=this.getConfigValue("sql.logInfo");
		Integer logWarn=this.getConfigValue("sql.logWarn");
		if(logInfo==null){
			logInfo=0;
		}
		if(logWarn==null){
			logWarn=2000;
		}
		
		Integer logLevel=2;
		if(exp!=null){
			logLevel=4;
		}
		else if(runTime>=logWarn){
			logLevel=3;
		}
		else if(runTime>=logInfo){
			logLevel=2;
		}
		
		final Integer level=logLevel;
		ServerUtil.logPoolSize(this.getClass().getSimpleName()+".taskExecutor", taskExecutor, 0);
		taskExecutor.execute(new Runnable() {
			public void run() {
				try {
					insertOperatorLog(level, startTime, sqlId, sql, runTime, exp, paramMap);
				} catch (Exception e) {
					logger.error("----addLogSqlAsync sqlId="+sqlId, e);
				}
			}
		});
	}







	@Override
	public void addLogAsync(Date startTime, long runTime, Exception exp,
			HashMap<String, Object> paramMap) {
		if(dataMap==null){
			loadProperties(this.properties);
		}
		
		Set<String> ignoreKey=(Set<String>)dataMap.get("service.ignoreKey");
		Map<String, Object> envMap=Sysconfigs.getEnvMap();
		Set<String> ignoreKey2=(Set<String>)envMap.get("sql.ignoreKey");
		if(ignoreKey!=null){
			ignoreKey=ignoreKey2;
		}
		
//		if(!isContainIgnoreSqlKey(sql, ignoreKey)){
//			logger.debug(sqlId+":"+sql+":"+runTime+"ms");
//			return;
//		}
		
		
		Integer logInfo=this.getConfigValue("sql.logInfo");
		Integer logWarn=this.getConfigValue("sql.logWarn");
		if(logInfo==null){
			logInfo=0;
		}
		if(logWarn==null){
			logWarn=2000;
		}
		
		Integer logLevel=2;
		if(exp!=null){
			logLevel=4;
		}
		else if(runTime>=logWarn){
			logLevel=3;
		}
		else if(runTime>=logInfo){
			logLevel=2;
		}
		
		final Integer level=logLevel;
		ServerUtil.logPoolSize(this.getClass().getSimpleName()+".taskExecutor", taskExecutor, 0);
		taskExecutor.execute(new Runnable() {
			public void run() {
				try {
					String sqlId=null;
					String sql=null;
					insertOperatorLog(level, startTime, sqlId, sql, runTime, exp, paramMap);
				} catch (Exception e) {
					logger.error("----addLogAsync ", e);
				}
			}
		});
		
	}
	
	public void setProperties(Properties properties0) {
		this.properties = properties0;
	}

	

}