package org.jview.fwork.basedata.mapper;

import java.util.List;

import org.jview.fwork.basedata.model.LogMsgPO;
import tk.mybatis.mapper.common.Mapper;

public interface LogMsgMapper extends Mapper<LogMsgPO> {
	public void insertBatch(List<LogMsgPO> list);
	
	public void updateBatch(List<LogMsgPO> list);
	
	public void insertLogMsg(LogMsgPO logMsg);
}