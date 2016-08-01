package org.jview.fwork.basedata.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import ai.yc.common.core.entity.BaseEntity;

@Table(name = "tf_log_db")
public class LogDbPO extends BaseEntity {
    @Id
    @SequenceGenerator(name="",sequenceName="select _nextval('_tf_logDb_cid_seq')")
    private Long cid;

    /**
     * 产品id
     */
    @Column(name = "project_id")
    private Long projectId;

    /**
     * 服务器id
     */
    @Column(name = "service_id")
    private Long serviceId;

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 用户账号
     */
    @Column(name = "user_name")
    private String userName;

    /**
     * 模块名对应的id，添加日志时自动检查
model表中是否没有，如果没有就自动在model表中加数据
     */
    @Column(name = "model_id")
    private Long modelId;

    /**
     * 日志级别
     */
    @Column(name = "log_level")
    private Integer logLevel;

    /**
     * 标题
     */
    private String title;

    /**
     * 参数json，当时操作功能的参数
     */
    @Column(name = "json_param")
    private String jsonParam;

    /**
     * 参数optId
     */
    @Column(name = "json_param_opt_id")
    private Long jsonParamOptId;

    /**
     * json返回报文
     */
    @Column(name = "json_ret")
    private String jsonRet;

    /**
     * 返回报文opt_id
     */
    @Column(name = "json_ret_opt_id")
    private Long jsonRetOptId;

    /**
     * 返回标志0失败，1成功，2异常
     */
    private String flag;

    /**
     * 描述信息
     */
    private String descs;

    /**
     * 运行时间,毫秒
     */
    @Column(name = "run_time")
    private Long runTime;
    
    private Integer rows;
    
    /**
     * 操作类型(insert,select,update等)
     */
    @Column(name = "oper_type")
    private String operType;
    
    private String author;
    
    /**
     * 调用的方法名，多个以“,”隔开，用于知道调用关系
     */
    private String calls;
    /**
     * 程序执行的线程id号
     */
    @Column(name = "thread_id")
    private Long threadId;
    
    private String ip;

    /**
     * 备注
     */
    private String remark;

    /**
     * @return cid
     */
    public Long getCid() {
        return cid;
    }

    /**
     * @param cid
     */
    public void setCid(Long cid) {
        this.cid = cid;
    }

    /**
     * 获取产品id
     *
     * @return project_id - 产品id
     */
    public Long getProjectId() {
        return projectId;
    }

    /**
     * 设置产品id
     *
     * @param projectId 产品id
     */
    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    /**
     * 获取服务器id
     *
     * @return service_id - 服务器id
     */
    public Long getServiceId() {
        return serviceId;
    }

    /**
     * 设置服务器id
     *
     * @param serviceId 服务器id
     */
    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    /**
     * 获取用户id
     *
     * @return user_id - 用户id
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 设置用户id
     *
     * @param userId 用户id
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * 获取用户账号
     *
     * @return user_name - 用户账号
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 设置用户账号
     *
     * @param userName 用户账号
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 获取模块名对应的id，添加日志时自动检查
model表中是否没有，如果没有就自动在model表中加数据
     *
     * @return model_id - 模块名对应的id，添加日志时自动检查
model表中是否没有，如果没有就自动在model表中加数据
     */
    public Long getModelId() {
        return modelId;
    }

    /**
     * 设置模块名对应的id，添加日志时自动检查
model表中是否没有，如果没有就自动在model表中加数据
     *
     * @param modelId 模块名对应的id，添加日志时自动检查
model表中是否没有，如果没有就自动在model表中加数据
     */
    public void setModelId(Long modelId) {
        this.modelId = modelId;
    }

    /**
     * 获取日志级别
     *
     * @return log_level - 日志级别
     */
    public Integer getLogLevel() {
        return logLevel;
    }

    /**
     * 设置日志级别
     *
     * @param logLevel 日志级别
     */
    public void setLogLevel(Integer logLevel) {
        this.logLevel = logLevel;
    }

    /**
     * 获取标题
     *
     * @return title - 标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置标题
     *
     * @param title 标题
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取参数json，当时操作功能的参数
     *
     * @return json_param - 参数json，当时操作功能的参数
     */
    public String getJsonParam() {
        return jsonParam;
    }

    /**
     * 设置参数json，当时操作功能的参数
     *
     * @param jsonParam 参数json，当时操作功能的参数
     */
    public void setJsonParam(String jsonParam) {
        this.jsonParam = jsonParam;
    }

    /**
     * 获取参数optId
     *
     * @return json_param_opt_id - 参数optId
     */
    public Long getJsonParamOptId() {
        return jsonParamOptId;
    }

    /**
     * 设置参数optId
     *
     * @param jsonParamOptId 参数optId
     */
    public void setJsonParamOptId(Long jsonParamOptId) {
        this.jsonParamOptId = jsonParamOptId;
    }

    /**
     * 获取json返回报文
     *
     * @return json_ret - json返回报文
     */
    public String getJsonRet() {
        return jsonRet;
    }

    /**
     * 设置json返回报文
     *
     * @param jsonRet json返回报文
     */
    public void setJsonRet(String jsonRet) {
        this.jsonRet = jsonRet;
    }

    /**
     * 获取返回报文opt_id
     *
     * @return json_ret_opt_id - 返回报文opt_id
     */
    public Long getJsonRetOptId() {
        return jsonRetOptId;
    }

    /**
     * 设置返回报文opt_id
     *
     * @param jsonRetOptId 返回报文opt_id
     */
    public void setJsonRetOptId(Long jsonRetOptId) {
        this.jsonRetOptId = jsonRetOptId;
    }

    /**
     * 获取返回标志0失败，1成功，2异常
     *
     * @return flag - 返回标志0失败，1成功，2异常
     */
    public String getFlag() {
        return flag;
    }

    /**
     * 设置返回标志0失败，1成功，2异常
     *
     * @param flag 返回标志0失败，1成功，2异常
     */
    public void setFlag(String flag) {
        this.flag = flag;
    }

    /**
     * 获取描述信息
     *
     * @return descs - 描述信息
     */
    public String getDescs() {
        return descs;
    }

    /**
     * 设置描述信息
     *
     * @param descs 描述信息
     */
    public void setDescs(String descs) {
        this.descs = descs;
    }

    /**
     * 获取运行时间,毫秒
     *
     * @return run_time - 运行时间,毫秒
     */
    public Long getRunTime() {
        return runTime;
    }

    /**
     * 设置运行时间,毫秒
     *
     * @param runTime 运行时间,毫秒
     */
    public void setRunTime(Long runTime) {
        this.runTime = runTime;
    }

    /**
     * 获取备注
     *
     * @return remark - 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注
     *
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }
    
    
    

    public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
	

	public String getOperType() {
		return operType;
	}

	public void setOperType(String operType) {
		this.operType = operType;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
	
	

	public String getCalls() {
		return calls;
	}

	public void setCalls(String calls) {
		this.calls = calls;
	}
	
	

	public Long getThreadId() {
		return threadId;
	}

	public void setThreadId(Long threadId) {
		this.threadId = threadId;
	}
	
	

	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	@Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        LogDbPO other = (LogDbPO) that;
        return (this.getCid() == null ? other.getCid() == null : this.getCid().equals(other.getCid()))
            && (this.getProjectId() == null ? other.getProjectId() == null : this.getProjectId().equals(other.getProjectId()))
            && (this.getServiceId() == null ? other.getServiceId() == null : this.getServiceId().equals(other.getServiceId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getUserName() == null ? other.getUserName() == null : this.getUserName().equals(other.getUserName()))
            && (this.getModelId() == null ? other.getModelId() == null : this.getModelId().equals(other.getModelId()))
            && (this.getCreateDate() == null ? other.getCreateDate() == null : this.getCreateDate().equals(other.getCreateDate()))
            && (this.getLogLevel() == null ? other.getLogLevel() == null : this.getLogLevel().equals(other.getLogLevel()))
            && (this.getTitle() == null ? other.getTitle() == null : this.getTitle().equals(other.getTitle()))
            && (this.getJsonParam() == null ? other.getJsonParam() == null : this.getJsonParam().equals(other.getJsonParam()))
            && (this.getJsonParamOptId() == null ? other.getJsonParamOptId() == null : this.getJsonParamOptId().equals(other.getJsonParamOptId()))
            && (this.getJsonRet() == null ? other.getJsonRet() == null : this.getJsonRet().equals(other.getJsonRet()))
            && (this.getJsonRetOptId() == null ? other.getJsonRetOptId() == null : this.getJsonRetOptId().equals(other.getJsonRetOptId()))
            && (this.getFlag() == null ? other.getFlag() == null : this.getFlag().equals(other.getFlag()))
            && (this.getDescs() == null ? other.getDescs() == null : this.getDescs().equals(other.getDescs()))
            && (this.getRunTime() == null ? other.getRunTime() == null : this.getRunTime().equals(other.getRunTime()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getCid() == null) ? 0 : getCid().hashCode());
        result = prime * result + ((getProjectId() == null) ? 0 : getProjectId().hashCode());
        result = prime * result + ((getServiceId() == null) ? 0 : getServiceId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getUserName() == null) ? 0 : getUserName().hashCode());
        result = prime * result + ((getModelId() == null) ? 0 : getModelId().hashCode());
        result = prime * result + ((getCreateDate() == null) ? 0 : getCreateDate().hashCode());
        result = prime * result + ((getLogLevel() == null) ? 0 : getLogLevel().hashCode());
        result = prime * result + ((getTitle() == null) ? 0 : getTitle().hashCode());
        result = prime * result + ((getJsonParam() == null) ? 0 : getJsonParam().hashCode());
        result = prime * result + ((getJsonParamOptId() == null) ? 0 : getJsonParamOptId().hashCode());
        result = prime * result + ((getJsonRet() == null) ? 0 : getJsonRet().hashCode());
        result = prime * result + ((getJsonRetOptId() == null) ? 0 : getJsonRetOptId().hashCode());
        result = prime * result + ((getFlag() == null) ? 0 : getFlag().hashCode());
        result = prime * result + ((getDescs() == null) ? 0 : getDescs().hashCode());
        result = prime * result + ((getRunTime() == null) ? 0 : getRunTime().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        return result;
    }
}