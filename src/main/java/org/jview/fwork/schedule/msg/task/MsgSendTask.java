package org.jview.fwork.schedule.msg.task;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.apache.commons.lang3.time.DateUtils;
import org.jview.fwork.basedata.model.LogMsgPO;
import org.jview.fwork.basedata.service.ILogMsgDetailService;
import org.jview.fwork.basedata.service.ILogMsgService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import ai.yc.common.core.data.RetResult;
import ai.yc.common.core.util.CommAutoSum;
import ai.yc.common.core.util.CommUtil;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;

/**
 * 
 * date 2016-07-01
 * @author cjh
 *
 */
//@Service("msgSendTask")
public class MsgSendTask  implements Job{
	private static final Log logger = LogFactory.getLog(MsgSendTask.class);
	
	private String fromMail="ycstest@126.com";
	private Integer msgType=2;//消息类型：1短信，2邮件，默认邮件
	private String isLogMailDetail="true";//是否记录邮件发送明细，默认不记录
	private Integer runningCount=20;
	
	@Autowired
	private ILogMsgService logMsgService;
	
	@Autowired
	private ILogMsgDetailService logMsgDetailService;
	
//	@Autowired
//	private ILogDbService logDbService;
	
	@Resource(name = "mailSender")
	private MailSender sendMail;
	
	@Resource(name = "taskExecutorSchedule")
	private TaskExecutor taskExecutorSchedule;
	
	public static enum TASK_TYPES{
		MSG_SMS("MSG_SMS", "短信"),
		MSG_MAIL("MSG_MAIL", "郵件");
		private String key;
		private String name;
		TASK_TYPES(String key, String name){
			this.key=key;
			this.name=name;
		}
		public String getKey() {
			return key;
		}
		public String getName() {
			return name;
		}
		
		public static boolean isExist(String key){
			MsgSendTask.TASK_TYPES[] types=MsgSendTask.TASK_TYPES.values();
			for(MsgSendTask.TASK_TYPES type:types){
				if(type.getKey().equals(key)){
					return true;
				}
			}
			return false;
		}
		
	}
	
	//待處理的數據
	private static Map<String, Map<Long, Object>> toDoMap=new ConcurrentHashMap<>();
	//正在處理的數據，處理一個，扣除一個，直到處理完成
	private static Map<String, Map<Long, Object>> runningMap=new ConcurrentHashMap<>();
	//动态累加器,用于输出邮件拆分后邮件发送的进度
	private static CommAutoSum autoSum=new CommAutoSum();
	
	public static void addData(String key, Object obj){
		Map<Long, Object> dataMap=toDoMap.get(key);
		Map<Long, Object> runningDataMap=runningMap.get(key);
		if(dataMap==null){
			dataMap=new ConcurrentHashMap<Long, Object>();
		}
		if(obj instanceof LogMsgPO){
			LogMsgPO logMsg=(LogMsgPO)obj;
			if(!runningDataMap.containsKey(logMsg.getCid())){//如果任务已正在运行，则忽略
				dataMap.put(logMsg.getCid(), logMsg);
			}
		}
		toDoMap.put(key, dataMap);
	}

	public void setTaskExecutorSchedule(TaskExecutor taskExecutor) {
		this.taskExecutorSchedule = taskExecutor;
	}
	
	public MsgSendTask() {
		
	}
	
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info(simpleDateFormat.format(new Date()) + ":" 
				+ context.getTrigger().getKey().getGroup() + "," 
				+ context.getTrigger().getKey().getName());
		
	}
	
	private void initMap(String key){
		Map<Long, Object> dataMap=toDoMap.get(key);
		if(dataMap==null){
			dataMap=new ConcurrentHashMap<Long, Object>();
			toDoMap.put(key, dataMap);
		}
		
		dataMap=runningMap.get(key);
		if(dataMap==null){
			dataMap=new ConcurrentHashMap<Long, Object>();
			runningMap.put(key, dataMap);
		}
	}
	
	
	/**
	 * 截入數據功能，一般5分鐘一次
	 */
	public void loadMsgData(){
		String key=null;
		if(msgType.intValue()==2){
			key=TASK_TYPES.MSG_MAIL.key;
		}
		else if(msgType.intValue()==1){
			key=TASK_TYPES.MSG_SMS.key;
		}
		
		this.initMap(key);
		
		Date date = new Date();
		date=DateUtils.addHours(date, -1);
		
		Map<Long, Object> toDoDataMap=toDoMap.get(key);
		Map<Long, Object> runningDataMap=runningMap.get(key);
		
		logger.info("------loadMsgData--msgType="+msgType+" key="+key+" sendTime=" + simpleDateFormat.format(date)
			+" toDoSize="+toDoDataMap.size()+"/runningSize="+runningDataMap.size());
		
		Integer sendFlag=0;//未發送
		RetResult<LogMsgPO> ret=this.logMsgService.findLogMsgList(date, msgType, sendFlag);
		if(ret.getSize()>0)
			logger.info("------loadMsgData--retFlag="+ret.getFlag()+" size="+ret.getSize());
		if(ret.isSuccess()){
			Collection<LogMsgPO> list=ret.getDataList();
			for(LogMsgPO logMsg:list){
				addData(key, logMsg);
			}
		}
		else{
			logger.warn("------loadMsgData--errorInfo="+ret.getMsgCode()+","+ret.getMsgBody());
		}
	}
	
	/**
	 * 執行郵件處理功能，一般10秒一次，使功能以接近準實時的方式執行
	 * 
	 */
	public void sendMsg(){
		String key=null;
		if(msgType.intValue()==2){
			key=TASK_TYPES.MSG_MAIL.key;
		}
		else if(msgType.intValue()==1){
			key=TASK_TYPES.MSG_SMS.key;
		}
		logger.debug("------sendMsg-----msgType="+msgType+" key="+key+" runningCount="+runningCount);
		
		this.initMap(key);
		Map<Long, Object> toDoDataMap=toDoMap.get(key);
		Map<Long, Object> runningDataMap=runningMap.get(key);
	
		if(toDoDataMap.size()>0 || runningDataMap.size()>0){
			logger.info("-----sendMsg key="+key+" progress="+runningDataMap.size()+"/"+toDoDataMap.size());
		}
		
		
		
		if(runningDataMap.size()<this.runningCount.intValue()){
			if(toDoDataMap==null||toDoDataMap.isEmpty()){//如果沒有待處理,就不處理了
				return;
			}
			Iterator<Long> it=toDoDataMap.keySet().iterator();;
			Object obj=null;
			int count=0;
			Long cid=null;
			while(it.hasNext()){
				cid=it.next();
				obj=toDoDataMap.get(cid);
				if(obj instanceof LogMsgPO){
					count++;
					LogMsgPO logMsg=(LogMsgPO)obj;
					if(logMsg.getSendType()!=null && logMsg.getSendType().intValue()==1){
						this.sendMail(logMsg);
					}
//					else if(logMsg.getSendType()!=null && logMsg.getSendType().intValue()==2){
//						this.sendMailSplit(logMsg);
//					}
					runningDataMap.put(cid, logMsg);//加到正在处理的列表中
					it.remove();//从待处理列表移除
					if(count+runningDataMap.size()>this.runningCount){
						break;
					}
				}
			}
			runningMap.put(key, runningDataMap);
			
		}

	}
	
	public void sendMail(final LogMsgPO logMsg) {
		final String key=TASK_TYPES.MSG_MAIL.key;
		taskExecutorSchedule.execute(new Runnable() {
			public void run() {
				long time=System.currentTimeMillis();
				Map<Long, Object> runningDataMap=runningMap.get(key);
				try {
					//处理前，再次检查状态
					LogMsgPO logMsgCheck=logMsgService.selectByKey(logMsg.getCid());
					if(logMsgCheck==null || logMsgCheck.getSendFlag().intValue()!=0){
						logger.warn("-----sendMail--cid="+logMsg.getCid()+"数据状态不是0待发送,取消---");
						runningDataMap.remove(logMsg.getCid());//处理过，或状态无效，则移除不用处理
						return;
					}
					SimpleMailMessage mail = new SimpleMailMessage(); // <span style="color:
					// #ff0000;">注意SimpleMailMessage只能用来发送text格式的邮件</span>
					mail.setTo(CommUtil.split(logMsg.getTos(), ","));// 接受者
					mail.setCc(CommUtil.split(logMsg.getCcs(), ","));
					mail.setBcc(CommUtil.split(logMsg.getBccs(), ","));
					mail.setFrom(fromMail);// 发送者,这里还可以另起Email别名，不用和xml里的username一致
					mail.setSubject(logMsg.getTitle());// 主题
					mail.setText(logMsg.getContent());// 邮件内容
					sendMail.send(mail);
					logMsg.setEndTime(new Date());
					logMsg.setSendFlag(1);//發送完成標志
					logMsg.setSendCount(1);
					
				} catch (Exception e) {
					logMsg.setSendFlag(3);
					logMsg.setExceptions(e.getMessage());
					logger.error("----saveMailAsync title="+logMsg.getTitle(), e);
				}
				logMsg.setRunTime(System.currentTimeMillis()-time);//执行时间
				RetResult<Long> ret=logMsgService.update(logMsg);

				
				//處理完,從正在處理的列表中移除
				runningDataMap.remove(logMsg.getCid());
//				runningMap.put(key, runningDataMap);
				logger.info("----sendMsg--cid="+logMsg.getCid()+" tos="+logMsg.getTos()+" isSuccess="+ret.isSuccess()+" runningSize="+runningDataMap.size());
			}
		});
	}
	
//	private Set<String> getReceiveSet(String tos){
//		Set<String> receiveSet = new HashSet<String>();
//		String[] strs=null;
//		if (tos != null) {
//			tos = tos.trim();
//			strs = tos.split(",");
//			for(String str:strs){
//				str=str.trim();
//				receiveSet.add(str);
//			}
//		}
//		return receiveSet;
//	}
//	
//	private Set<String> getMsgReceiveSet(final LogMsgPO logMsg) {
//		Set<String> receiveSet = new HashSet<String>();
//		Set<String> sets=null;
//		
//		sets=this.getReceiveSet(logMsg.getTos());
//		receiveSet.addAll(sets);
//		sets=this.getReceiveSet(logMsg.getCcs());
//		receiveSet.addAll(sets);
//		sets=this.getReceiveSet(logMsg.getBccs());
//		receiveSet.addAll(sets);
//		return receiveSet;
//	}
//	
//	private void sendMailSplit(final LogMsgPO logMsg) {
//		final String key=TASK_TYPES.MSG_MAIL.key;
//		Map<Long, Object> runningDataMap=runningMap.get(key);
//		LogMsgPO logMsgCheck=logMsgService.selectByKey(logMsg.getCid());
//		if(logMsgCheck==null || logMsgCheck.getSendFlag().intValue()!=0){
//			logger.warn("-----cid="+logMsg.getCid()+" 数据状态不是0待发送,取消---");
//			runningDataMap.remove(logMsg.getCid());
//			return;
//		}
//	
//		long timeStart=System.currentTimeMillis();//用于计算整体任务执行时间
//		Set<String> receiveSet = getMsgReceiveSet(logMsg);
//		logMsg.setSendCount(receiveSet.size());
//		logMsg.setSendFlag(2);//开始发送，在检查全部发送完成后，会重新改回1
//		RetResult<Long> ret=logMsgService.update(logMsg);
//		
//		for(String receive:receiveSet){
//			this.sendMailSplitDetail(logMsg, receive, receiveSet.size(), timeStart);
//		}
//		
//		
//		logger.info("----sendMsgSplit--cid="+logMsg.getCid()+" tos="+logMsg.getTos()+" isSuccess="+ret.isSuccess()+" runningSize="+runningDataMap.size());
//	}
//
//	
//	
//	private void sendMailSplitDetail(final LogMsgPO logMsg, final String to, final int totalCount, final long timeStart) {
//		final String key=TASK_TYPES.MSG_MAIL.key;
//		taskExecutorSchedule.execute(new Runnable() {
//			public void run() {
//				long time=System.currentTimeMillis();
//				Map<Long, Object> runningDataMap=runningMap.get(key);
//				LogMsgPO logMsgCheck=logMsgService.selectByKey(logMsg.getCid());
//				if(logMsgCheck==null || logMsgCheck.getSendFlag().intValue()!=2){//拆分
//					logger.warn("-----sendMailSplitDetail--cid="+logMsg.getCid()+" 数据状态不是2，拆分后待发送---");
//					return;
//				}
//				
//				
//				LogMsgDetailPO msgDetail=new LogMsgDetailPO();
//				msgDetail.setLogMsgId(logMsg.getCid());
//				msgDetail.setTos(to);
//				msgDetail.setSendDate(new Date());
//				try {
//					SimpleMailMessage mail = new SimpleMailMessage(); // <span style="color:
//					// #ff0000;">注意SimpleMailMessage只能用来发送text格式的邮件</span>
//
//					mail.setTo(to);// 接受者
//					mail.setFrom(fromMail);// 发送者,这里还可以另起Email别名，不用和xml里的username一致
//					mail.setSubject(logMsg.getTitle());// 主题
//					mail.setText(logMsg.getContent());// 邮件内容
//					sendMail.send(mail);
//				} catch (Exception e) {
//					msgDetail.setRemark(e.getMessage());
//					logger.error("----saveMailAsync to="+to, e);
//				}
//				autoSum.count(logMsg.getCid());
//				msgDetail.setRunTime(System.currentTimeMillis()-time);//执行时间
//				RetResult<Long> ret=logMsgDetailService.create(msgDetail);
//				logger.info("----sendMsgDetail--cid="+logMsg.getCid()+" sending="+autoSum.getValue(logMsg.getCid()).intValue()+"/"+totalCount+" tos="+logMsg.getTos()+" isSuccess="+ret.isSuccess());
//				
//				if(totalCount==autoSum.getValue(logMsg.getCid()).intValue()){//全部发送完成
//					logMsg.setSendFlag(1);
//					logMsg.setRunTime(System.currentTimeMillis()-timeStart);//计算所有邮件发送完成后的时间
//					logMsgService.update(logMsg);
//					//處理完,從正在處理的列表中移除
//					runningDataMap.remove(logMsg.getCid());
//					logger.info("----sendMsgSplit--cid="+logMsg.getCid()+" tos="+logMsg.getTos()+" isSuccess="+ret.isSuccess()+" runningSize="+runningDataMap.size());
//				}
//				
//			}
//		});
//	}

	public void setFromMail(String fromMail) {
		this.fromMail = fromMail;
	}

	public void setMsgType(Integer msgType) {
		this.msgType = msgType;
	}

	public void setRunningCount(Integer runningCount) {
		this.runningCount = runningCount;
	}

	public void setIsLogMailDetail(String isLogMailDetail) {
		this.isLogMailDetail = isLogMailDetail;
	}
	
	
	
}

