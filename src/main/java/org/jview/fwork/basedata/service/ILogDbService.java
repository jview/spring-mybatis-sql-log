package org.jview.fwork.basedata.service;

import org.jview.fwork.basedata.model.LogDbPO;

import ai.yc.common.core.page.PageVO;
import ai.yc.common.core.service.IBaseService;

public interface ILogDbService extends IBaseService<LogDbPO> {
	public void info(Object obj, String title, String descs, Long runTime);
	

	
	public LogDbPO findLogDbById(Long id);
	
	public PageVO<LogDbPO> findPageByLogDb(LogDbPO logDb, PageVO<LogDbPO> page);
	
	public void log(LogDbPO logDb);
}
