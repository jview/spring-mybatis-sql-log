package org.jview.fwork.basedata.service;

import org.jview.fwork.basedata.model.ModelPO;

import ai.yc.common.core.data.RetResult;
import ai.yc.common.core.page.PageVO;
import ai.yc.common.core.service.IBaseService;

public interface IModelService  extends IBaseService<ModelPO> {
	public ModelPO findModelById(Long id);
	
	public RetResult<ModelPO> findModelByCode(String className, String funcCode);

	public PageVO<ModelPO> findPageByModel(ModelPO search, PageVO<ModelPO> page);
	
	public RetResult<Long> update(ModelPO record);
	
//	public RetResult<ModelPO> create(ModelPO record);
	
	public RetResult<ModelPO> insertModel(ModelPO record);
	
	
}
