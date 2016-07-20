package org.jview.fwork.basedata.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.jview.fwork.basedata.model.LogMsgPO;
import org.jview.fwork.basedata.service.ILogMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import ai.yc.common.core.data.IMailVO;
import ai.yc.common.core.data.ISmsVO;
import ai.yc.common.core.data.RetResult;

/**
 * 消息异步处理
 * @author cjh
 *
 */
@Service("logMsgManagerAsyncService")
public class LogMsgManagerAsyncService  {
	private static final Logger logger = Logger.getLogger(LogMsgManagerAsyncService.class);

	@Autowired
	private ILogMsgService logMsgService;

	@Resource(name = "taskExecutor")
	private TaskExecutor taskExecutor;

	public void setTaskExecutor(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}


	public void saveMail(final String msgCode, final IMailVO mail, final int sendType) {
		taskExecutor.execute(new Runnable() {
			public void run() {
				try {
					LogMsgPO msg = new LogMsgPO();
					msg.load(mail);
					msg.setSendType(sendType);
					msg.setMsgCode(msgCode);
					RetResult<Long> ret=logMsgService.create(msg);
					if(!ret.isSuccess()){
						logger.warn(ret.getMsgCode()+","+ret.getMsgBody());
					}
				} catch (Exception e) {
					logger.error("----saveMailAsync title="+mail.getTitle(), e);
				}
			}
		});
	}


	public void saveMailBatch(final String msgCode, final List<IMailVO> mailList, final int sendType) {
		taskExecutor.execute(new Runnable() {
			public void run() {
				try {
					List<LogMsgPO> list=new ArrayList<LogMsgPO>();
					LogMsgPO msg =null;
					for(IMailVO mail:mailList){
						msg = new LogMsgPO();
						msg.load(mail);
						msg.setMsgCode(msgCode);
						msg.setSendType(sendType);
						list.add(msg);
					}
					logMsgService.insertBatch(list);
				} catch (Exception e) {
					logger.error("----saveMailBatch, error:"+e.getMessage(), e);
				}
			}
		});
		
	}


	public void saveSms(final String msgCode, ISmsVO sms) {
		taskExecutor.execute(new Runnable() {
			public void run() {
				try {
					LogMsgPO msg = new LogMsgPO();
					msg.load(sms);
					msg.setMsgCode(msgCode);
					RetResult<Long> ret=logMsgService.create(msg);
					if(!ret.isSuccess()){
						logger.warn(ret.getMsgCode()+","+ret.getMsgBody());
					}
				} catch (Exception e) {
					logger.error("----saveSms tos="+sms.getTos(), e);
				}
			}
		});
		
	}


	public void saveSmsBatch(final String msgCode, List<ISmsVO> smsList) {
		taskExecutor.execute(new Runnable() {
			public void run() {
				try {
					List<LogMsgPO> list=new ArrayList<LogMsgPO>();
					LogMsgPO msg =null;
					for(ISmsVO sms:smsList){
						msg = new LogMsgPO();
						msg.setMsgCode(msgCode);
						msg.load(sms);
						list.add(msg);
					}
					logMsgService.insertBatch(list);
				} catch (Exception e) {
					logger.error("----saveSmsBatch, error:"+e.getMessage(), e);
				}
			}
		});
		
	}
}