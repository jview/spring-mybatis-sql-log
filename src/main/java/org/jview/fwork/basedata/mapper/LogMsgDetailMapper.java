package org.jview.fwork.basedata.mapper;

import java.util.List;

import tk.mybatis.mapper.common.Mapper;
import org.jview.fwork.basedata.model.LogMsgDetailPO;

public interface LogMsgDetailMapper extends Mapper<LogMsgDetailPO> {
	public void insertBatch(List<LogMsgDetailPO> list);
	
	public void updateBatch(List<LogMsgDetailPO> list);
	
	public void insertLogMsgDetail(LogMsgDetailPO logMsg);
	
}