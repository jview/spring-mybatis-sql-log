package org.jview.fwork.basedata.service.impl;

import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.jview.fwork.basedata.mapper.LogDbMapper;
import org.jview.fwork.basedata.model.LogDbPO;
import org.jview.fwork.basedata.service.ILogDbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;
import ai.yc.common.core.data.RetResult;
import ai.yc.common.core.entity.BaseEntity;
import ai.yc.common.core.page.PageVO;
import ai.yc.common.core.service.impl.BaseService;
import ai.yc.common.core.util.CommUtil;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("logDbService")
public class LogDbService extends BaseService<LogDbPO> implements ILogDbService {
	 private static final Log logger = LogFactory.getLog(LogDbService.class);
	//logLevel:1 debug, 2 info, 3 warn, 4 error, 5 fatal.

	 @Autowired
	 private LogDbMapper mapper;
	 
	 
	@Override
	public LogDbMapper getMapper() {
		return mapper;
	}
	
	public void info(Object obj, String title, String descs, Long runTime){
		int logLevel=2;
		this.log(logLevel, obj, title, descs, runTime, null, null);
	}

	
	private void log(int logLevel, Object obj, String title, String descs, Long runTime, Exception e, RetResult ret){
		String userName="";
		String remark=null;
		if(e!=null){
			descs=e.getMessage()+"\n"+descs;
			remark=ExceptionUtils.getFullStackTrace(e);
			if(remark.length()>2000){
				remark=remark.substring(0, 2000);
			}
		}
		LogDbPO logDb = new LogDbPO();
		if(logDb.getCreateDate()==null){
			logDb.initCreate();
		}
		if(obj!=null){
			if(obj instanceof BaseEntity){
				BaseEntity entity=(BaseEntity)obj;
				logDb.setUserId(entity.getModifyUser());
				try {
					String json=JSONObject.fromObject(obj).toString();
					json=CommUtil.getStringLimit(json, 2000);
					logDb.setJsonParam(json);
				} catch (Exception e1) {
					logger.warn("json convert", e1);
				}
			}
			else{
				logDb.setJsonParam(""+obj);
			}
		}
		
		if(ret!=null){
			try {
				String json=JSONObject.fromObject(ret).toString();
				json=CommUtil.getStringLimit(json, 2000);
				logDb.setJsonParam(json);
			} catch (Exception e1) {
				logger.warn("json convert", e1);
			}
			logDb.setFlag(""+ret.getFlag());
		}
		if(logDb.getFlag()==null){
			logDb.setFlag("0");
		}
		logDb.setUserName(userName);
		logDb.setTitle(title);
		logDb.setDescs(descs);
		logDb.setRemark(remark);
		logDb.setRunTime(runTime);
		logDb.setLogLevel(logLevel);
		this.getMapper().insert(logDb);
	}
	
	@Override
	public void log(LogDbPO logDb){
		if(logDb.getCreateDate()==null){
			logDb.initCreate();
		}
		if(logDb.getFlag()==null){
			logDb.setFlag("0");
		}
		String json=CommUtil.getStringLimit(logDb.getJsonRet(), 2000);
		logDb.setJsonRet(json);
		
		json=CommUtil.getStringLimit(logDb.getJsonParam(), 2000);
		logDb.setJsonParam(json);
		this.mapper.insert(logDb);
	}

	@Override
	public LogDbPO findLogDbById(Long id) {
		logger.info("----findLogDbById--id="+id);
		return this.getMapper().selectByPrimaryKey(id);
	}
	
	@Override
	public PageVO<LogDbPO> findPageByLogDb(LogDbPO logDb, PageVO<LogDbPO> page) {
		logger.info("----findPageByLogDb--");
		Example example = new Example(LogDbPO.class);
//		  example.selectProperties("nameCn","nameEn","code","cid");
		Example.Criteria criteria = example.createCriteria();
//		criteria.andEqualTo("ifDel", 0);
		if (StringUtil.isNotEmpty(logDb.getTitle())) {
            criteria.andLike("title", "%" + logDb.getTitle() + "%");
        }
		if (StringUtil.isNotEmpty(logDb.getDescs())) {
            criteria.andLike("descs", "%" + logDb.getDescs() + "%");
        }
//        if (StringUtil.isNotEmpty(logDb.getCode())) {
//            criteria.andLike("itemCode", "%" + logDb.get() + "%");
//        }
        if (logDb.getLogLevel() != null) {
            criteria.andEqualTo("cid", logDb.getLogLevel());
        }
        
        if (logDb.getUserId() != null) {
            criteria.andEqualTo("userId", logDb.getUserId());
        }
        
        if (logDb.getLogLevel() != null) {
            criteria.andEqualTo("logLevel", logDb.getLogLevel());
        }
        
        if (logDb.getModelId() != null) {
            criteria.andEqualTo("modelId", logDb.getModelId());
        }
        
        if (logDb.getProjectId() != null) {
            criteria.andEqualTo("projectId", logDb.getProjectId());
        }
        
        example.setOrderByClause("create_date desc");
		
		// 分页查询
		PageHelper.startPage(page.getCurrentPage(), page.getPageSize());
		List<LogDbPO> list = selectByExample(example);
		PageInfo<LogDbPO> pageInfo = new PageInfo<LogDbPO>(list);
		PageVO<LogDbPO> pageRet = new PageVO<LogDbPO>(pageInfo);
		return pageRet;
	}


	
}
