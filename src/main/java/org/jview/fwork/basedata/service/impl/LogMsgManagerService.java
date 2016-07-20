package org.jview.fwork.basedata.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jview.fwork.basedata.model.LogMsgPO;
import org.jview.fwork.basedata.service.ILogMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ai.yc.common.core.data.IMailVO;
import ai.yc.common.core.data.ISmsVO;
import ai.yc.common.core.data.RetResult;
import ai.yc.common.core.logger.ILogMsgManager;

/**
 * 
 * 消息同步保存处理
 * @author cjh
 *
 */
@Service("logMsgManager")
public class LogMsgManagerService implements ILogMsgManager {
	private static final Logger logger = Logger.getLogger(LogMsgManagerService.class);

	@Autowired
	private ILogMsgService logMsgService;
	
	@Autowired
	private LogMsgManagerAsyncService logMsgManagerAsyncService;




	@Override
	public void saveMail(String msgCode, IMailVO mail, int sendType, boolean isAsync) {
		if(isAsync){
			this.logMsgManagerAsyncService.saveMail(msgCode, mail, sendType);
			return;
		}
		LogMsgPO msg = new LogMsgPO();
		msg.load(mail);
		msg.setMsgCode(msgCode);
		msg.setSendType(sendType);
		RetResult<Long> ret=this.logMsgService.create(msg);
		if(!ret.isSuccess()){
			logger.warn(ret.getMsgCode()+","+ret.getMsgBody());
		}
	}


	@Override
	public void saveMailBatch(String msgCode, List<IMailVO> mailList, int sendType, boolean isAsync) {
		if(isAsync){
			this.logMsgManagerAsyncService.saveMailBatch(msgCode, mailList, sendType);
			return;
		}
		List<LogMsgPO> list=new ArrayList<LogMsgPO>();
		LogMsgPO msg =null;
		for(IMailVO mail:mailList){
			msg = new LogMsgPO();
			msg.setMsgCode(msgCode);
			msg.load(mail);
			msg.setSendType(sendType);
			list.add(msg);
		}
		try {
			this.logMsgService.insertBatch(list);
		} catch (Exception e) {
			logger.error("----saveMailBatch--", e);
		}
		
	}


	@Override
	public void saveSms(String msgCode, ISmsVO sms, boolean isAsync) {
		if(isAsync){
			this.logMsgManagerAsyncService.saveSms(msgCode, sms);
			return;
		}
		LogMsgPO msg = new LogMsgPO();
		msg.setMsgCode(msgCode);
		msg.load(sms);
		RetResult<Long> ret=this.logMsgService.create(msg);
		if(!ret.isSuccess()){
			logger.warn(ret.getMsgCode()+","+ret.getMsgBody());
		}
		
	}


	@Override
	public void saveSmsBatch(String msgCode, List<ISmsVO> smsList, boolean isAsync) {
		if(isAsync){
			this.logMsgManagerAsyncService.saveSmsBatch(msgCode, smsList);
			return;
		}
		List<LogMsgPO> list=new ArrayList<LogMsgPO>();
		LogMsgPO msg =null;
		for(ISmsVO sms:smsList){
			msg = new LogMsgPO();
			msg.setMsgCode(msgCode);
			msg.load(sms);
			list.add(msg);
		}
		try {
			this.logMsgService.insertBatch(list);
		} catch (Exception e) {
			logger.error("----saveMailBatch--", e);
		}
	}
}