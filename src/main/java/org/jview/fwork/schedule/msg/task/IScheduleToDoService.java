package org.jview.fwork.schedule.msg.task;

import ai.yc.common.core.data.RetResult;

/**
 * 更新待处理
 * date 2016-07-01
 * @author cjh
 *
 */
public interface IScheduleToDoService {
	/**
	 * 把消息直接加到待处理，不用等定时任务去载入数据
	 * @param key
	 * @param obj
	 * @return
	 */
	public RetResult<String> addToDo(String key, Object obj);

}
