package org.jview.fwork.basedata.service;

import java.util.Date;
import java.util.List;

import org.jview.fwork.basedata.model.LogMsgPO;

import ai.yc.common.core.data.RetResult;
import ai.yc.common.core.page.PageVO;
import ai.yc.common.core.service.IBaseService;

public interface ILogMsgService extends IBaseService<LogMsgPO> {
	public PageVO<LogMsgPO> findPageByLogMsg(LogMsgPO logDb, PageVO<LogMsgPO> page);
	
	public RetResult<LogMsgPO> findLogMsgList(Date sendTime, Integer msgType, Integer sendFlag);
	
	public void insertBatch(List<LogMsgPO> msgList)throws Exception;
	
	public void updateBatch(List<LogMsgPO> msgList)throws Exception;
	
	public RetResult<Long> create(LogMsgPO record);
	
	public RetResult<Long> update(LogMsgPO record);
	
	
}
