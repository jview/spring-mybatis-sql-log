package org.jview.fwork.basedata.mapper;

import org.jview.fwork.basedata.model.ModelPO;
import tk.mybatis.mapper.common.Mapper;

public interface ModelMapper extends Mapper<ModelPO> {
	public void insertModel(ModelPO model);
}