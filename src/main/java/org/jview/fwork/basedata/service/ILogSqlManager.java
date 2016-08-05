package org.jview.fwork.basedata.service;

import java.util.Date;
import java.util.HashMap;

public interface ILogSqlManager {
	
	
	public void addLogSqlAsync(Date startTime, String sqlId, String sql, long runTime, Exception exp, HashMap<String, Object> paramMap);
	
	public void addLogServiceAsync(Date startTime, long runTime, Exception exp, HashMap<String, Object> paramMap);
	
	
}
