package org.jview.fwork.basedata.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.time.DateUtils;
import org.jview.fwork.basedata.mapper.LogMsgDetailMapper;
import org.jview.fwork.basedata.model.LogMsgDetailPO;
import org.jview.fwork.basedata.service.ILogMsgDetailService;
import org.jview.fwork.schedule.msg.task.IScheduleToDoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import tk.mybatis.mapper.entity.Example;
import ai.yc.common.core.data.RetResult;
import ai.yc.common.core.page.PageVO;
import ai.yc.common.core.service.impl.BaseService;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 
 * 消息明细发送结果处理
 * @author cjh
 *
 */
@Service("logMsgDetailService")
public class LogMsgDetailService extends BaseService<LogMsgDetailPO> implements ILogMsgDetailService {
	private static final Log logger = LogFactory.getLog(LogMsgDetailService.class);
	// logLevel:1 debug, 2 info, 3 warn, 4 error, 5 fatal.
	

	@Autowired
	private LogMsgDetailMapper mapper;
	
	@Autowired
	private IScheduleToDoService scheduleToDoService;
	

	public void setScheduleQueueService(IScheduleToDoService scheduleToDoService) {
		this.scheduleToDoService = scheduleToDoService;
	}

	@Override
	public LogMsgDetailMapper getMapper() {
		return mapper;
	}
	
	public RetResult<LogMsgDetailPO> findLogMsgDetailList(Date sendTime, Integer msgType, Integer sendFlag) {
		logger.info("----findLogMsgDetailList--sendTime="+sendTime+" msgType="+msgType+" sendFlag="+sendFlag);
		RetResult<LogMsgDetailPO> ret = new RetResult<LogMsgDetailPO>();
		if (msgType == null) {
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "msgType is null", null);
			return ret;
		}
		if (sendFlag == null) {
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "sendFlag is null", null);
			return ret;
		}
		if (sendTime == null) {
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "sendTime is null", null);
			return ret;
		}
		Example example = new Example(LogMsgDetailPO.class);
		Example.Criteria criteria = example.createCriteria();

		criteria.andEqualTo("msgType", msgType);
		criteria.andEqualTo("sendFlag", sendFlag);
		criteria.andGreaterThanOrEqualTo("sendTime", sendTime);
		criteria.andLessThanOrEqualTo("endTime", new Date());//小于当前时间
		
		List<LogMsgDetailPO> list = selectByExample(example);
		ret.setDataList(list);
		return ret;

	}

	@Override
	public PageVO<LogMsgDetailPO> findPageByLogMsgDetail(LogMsgDetailPO logMsgDetail, PageVO<LogMsgDetailPO> page) {
		logger.info("----findPageByLogMsgDetail--");
		Example example = new Example(LogMsgDetailPO.class);
//		  example.selectProperties("nameCn","nameEn","code","cid");
		Example.Criteria criteria = example.createCriteria();
//		criteria.andEqualTo("ifDel", 0);
        if (logMsgDetail.getSendDate() != null) {
            criteria.andGreaterThanOrEqualTo("sendDate", logMsgDetail.getSendDate());
        }
 
    
        
        example.setOrderByClause("send_date desc");
		
		// 分页查询
		PageHelper.startPage(page.getCurrentPage(), page.getPageSize());
		List<LogMsgDetailPO> list = selectByExample(example);
		PageInfo<LogMsgDetailPO> pageInfo = new PageInfo<LogMsgDetailPO>(list);
		PageVO<LogMsgDetailPO> pageRet = new PageVO<LogMsgDetailPO>(pageInfo);
		return pageRet;
	}
	
	public boolean checkEmail(String email){
        boolean flag = false;
        try{
                String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
                Pattern regex = Pattern.compile(check);
                Matcher matcher = regex.matcher(email);
                flag = matcher.matches();
            }catch(Exception e){
                flag = false;
            }
        return flag;
    }
     
    /**
     * 验证手机号码
     * @param mobiles
     * @return
     */
    public boolean checkMobileNumber(String mobileNumber){
        boolean flag = false;
        try{
                Pattern regex = Pattern.compile("^(((13[0-9])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8})|(0\\d{2}-\\d{8})|(0\\d{3}-\\d{7})$");
                Matcher matcher = regex.matcher(mobileNumber);
                flag = matcher.matches();
            }catch(Exception e){
                flag = false;
            }
        return flag;
    }
    
 
	
	public RetResult<Long> create(LogMsgDetailPO record){
		logger.info("----create--");
		RetResult<Long> ret = new RetResult<>();
		if(StringUtils.isEmpty(record.getTos())){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "tos="+record.getTos()+" is empty", null);
			logger.warn("create="+ret.getMsgBody());
			return ret;
		}
		
		
		Date date_after_5 = DateUtils.addMinutes(new Date(), 5);//5分钟后
		Date date_before_5 = DateUtils.addMinutes(new Date(), -5);//5分钟前



		boolean isSend=false;
		

		record.initCreate();
		this.getMapper().insertLogMsgDetail(record);
		ret.setData(record.getCid());
		return ret;
	}
	

	
	public void insertBatch(List<LogMsgDetailPO> msgList) throws Exception{
		int batchSize=800;
		List<LogMsgDetailPO> cacheList=new ArrayList<>();
		RetResult<Long> ret=null;
		for(LogMsgDetailPO msg:msgList){
			msg.initCreate();

			cacheList.add(msg);
			if(cacheList.size()>batchSize){
				this.mapper.insertBatch(cacheList);
				cacheList.clear();
			}
		}
		if(cacheList.size()>0){
			this.mapper.insertBatch(cacheList);
		}
		
	}
	
	public void updateBatch(List<LogMsgDetailPO> msgList) throws Exception{
		int batchSize=800;
		List<LogMsgDetailPO> cacheList=new ArrayList<>();
		RetResult<Long> ret=null;
		for(LogMsgDetailPO msg:msgList){
			msg.initUpdate();
			cacheList.add(msg);
			if(cacheList.size()>batchSize){
				this.mapper.updateBatch(cacheList);
				cacheList.clear();
			}
		}
		if(cacheList.size()>0){
			this.mapper.updateBatch(cacheList);
		}
		
	}
	
	public RetResult<Long> update(LogMsgDetailPO record){
		logger.info("----update--");
		RetResult<Long> ret = new RetResult<>();
		if(record.getCid()==null||record.getCid().longValue()==0){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "cid="+record.getCid()+" is empty", null);
			return ret;
		}
		record.initUpdate();
		record.setIfDel(0);
		this.mapper.updateByPrimaryKey(record);
		ret.setData(record.getCid());
		return ret;
	}
	

}
