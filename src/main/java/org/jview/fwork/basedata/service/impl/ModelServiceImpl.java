package org.jview.fwork.basedata.service.impl;

import java.util.List;

import org.jview.fwork.basedata.mapper.ModelMapper;
import org.jview.fwork.basedata.model.ModelPO;
import org.jview.fwork.basedata.service.ILogDbService;
import org.jview.fwork.basedata.service.IModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;
import ai.yc.common.core.data.RetResult;
import ai.yc.common.core.page.PageVO;
import ai.yc.common.core.service.ISelfInject;
import ai.yc.common.core.service.impl.BaseService;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("modelService")
public class ModelServiceImpl extends BaseService<ModelPO> implements IModelService,  ISelfInject{
	 private static final Log logger = LogFactory.getLog(ModelServiceImpl.class);
	 
	 public ModelServiceImpl(){
		 
	 }
	 
	 @Autowired
	 private ModelMapper mapper;
	 
	@Override
	public ModelMapper getMapper() {
		return mapper;
	}
	
	private IModelService _self;

	public void setSelf(Object proxyBean) { // 通过InjectBeanSelfProcessor注入自己（目标对象）的AOP代理对象
		this._self = (IModelService) proxyBean;
	}
	
	 @Autowired
	 private ILogDbService logDbService;
	
	
	@Override
	public ModelPO findModelById(Long id) {
		logger.info("----findModelById--id="+id);
		return mapper.selectByPrimaryKey(id);
	}
	
	/**
	 * 建议不要对RetResult的数据进行缓存，因为会造成空数据也被缓存
	 * @param funcCode
	 * @param className
	 * @return
	 */
	
	public RetResult<ModelPO> findModelByCode(String packageName, String className, String funcCode){
		RetResult<ModelPO> ret = new RetResult<>();
		Example example = new Example(ModelPO.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("packageName", packageName);
		criteria.andEqualTo("className", className);
		criteria.andEqualTo("funcCode", funcCode);
		List<ModelPO> list=this.selectByExample(example);
//		System.out.println("--------findModelByCode-----"+className+" "+funcCode);
		ret.setDataList(list);
		return ret;
	}
	

	public PageVO<ModelPO> findPageByModel(ModelPO model, PageVO<ModelPO> page) {
		logger.info("----findModelById--id="+model.getCid());
		Example example = new Example(ModelPO.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtil.isNotEmpty(model.getClassName())) {
			criteria.andLike("className", "%" + model.getClassName() + "%");
		}
		if (StringUtil.isNotEmpty(model.getFuncCode())) {
			criteria.andLike("funcCode", "%" + model.getFuncCode() + "%");
		}
		if (model.getCid() != null) {
			criteria.andEqualTo("cid", model.getCid());
		}
		
		// 分页查询
		PageHelper.startPage(page.getCurrentPage(), page.getPageSize());
		List<ModelPO> list = selectByExample(example);
		PageInfo<ModelPO> pageInfo = new PageInfo<ModelPO>(list);
		PageVO<ModelPO> pageRet = new PageVO<ModelPO>(pageInfo);
		return pageRet;
	}
	

	public RetResult<ModelPO> create(ModelPO record){
		logger.info("----create--");
		long time=System.currentTimeMillis();
		RetResult<ModelPO> ret = new RetResult<>();
		if(StringUtils.isEmpty(record.getCode())||StringUtils.isEmpty(record.getName())){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "code="+record.getCode()+" or name="+record.getName()+" is empty", null);
			logger.warn("create="+ret.getMsgBody());
			return ret;
		}
		record.initCreate();
		this.getMapper().insert(record);
		ret.setData(record);
		this.logDbService.info(record, "create", "cid="+record.getCid(), System.currentTimeMillis()-time);
		return ret;
	}
	
//	@CacheEvict(value = "basedata",key = "'findModelByCode'+#className+'.'+#funcCode")
	public RetResult<ModelPO> insertModel(ModelPO record){
		logger.info("----insertModel--");
		long time=System.currentTimeMillis();
		RetResult<ModelPO> ret = new RetResult<>();
		if(StringUtils.isEmpty(record.getClassName())||StringUtils.isEmpty(record.getFuncCode())){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "className="+record.getClassName()+" or funcCode="+record.getFuncCode()+" is empty", null);
			logger.warn("create="+ret.getMsgBody());
			return ret;
		}
		record.initCreate();
		this.getMapper().insertModel(record);
		ret.setData(record);
		this.logDbService.info(record, "create", "cid="+record.getCid(), System.currentTimeMillis()-time);
		return ret;
	}
	
	public RetResult<Long> update(ModelPO record){
		logger.info("----update--");
		long time=System.currentTimeMillis();
		RetResult<Long> ret = new RetResult<>();
		if(record.getCid()==null||record.getCid().longValue()==0){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "cid="+record.getCid()+" is empty", null);
			logger.warn("update="+ret.getMsgBody());
			return ret;
		}
		record.initUpdate();
		record.setIfDel(0);
		this.mapper.updateByPrimaryKey(record);
		ret.setData(record.getCid());
		this.logDbService.info(record, "update", "cid="+record.getCid(), System.currentTimeMillis()-time);
		return ret;
	}
	
	public RetResult<Integer> deleteVirtual(Long operUserId, Long cid){
		logger.info("----update--");
		long time=System.currentTimeMillis();
		RetResult<Integer> ret = new RetResult<>();
		if(cid==null||cid==0){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "cid="+cid+" is empty", null);
			logger.warn("deleteVirtual="+ret.getMsgBody());
			return ret;
		}
		ModelPO model = new ModelPO();
		model.setCid(cid);
		model.setModifyUser(operUserId);
		int v=0;
		try {
			v = this.deleteVirtual(model);
		} catch (Exception e) {
			ret.initError(RetResult.msg_codes.ERR_UNKNOWN, "cid="+cid+",error="+e.getMessage(), e);
		}
		ret.setData(v);
		this.logDbService.info(cid, "deleteVirtual", "cid="+cid, System.currentTimeMillis()-time);
		return ret;
	}

}
