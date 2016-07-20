package org.jview.fwork.basedata.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import ai.yc.common.core.entity.BaseEntity;

@Table(name = "tf_log_msg_detail")
public class LogMsgDetailPO extends BaseEntity {
    @Id
    @SequenceGenerator(name="",sequenceName="select _nextval('_tf_logMsgDetail_cid_seq')")
    private Long cid;

    @Column(name = "log_msg_id")
    private Long logMsgId;

    /**
     * 收件人
     */
    private String tos;

    /**
     * 发送时间
     */
    @Column(name = "send_date")
    private Date sendDate;
    
    @Column(name = "run_time")
    private Long runTime;

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
     * @return log_msg_id
     */
    public Long getLogMsgId() {
        return logMsgId;
    }

    /**
     * @param logMsgId
     */
    public void setLogMsgId(Long logMsgId) {
        this.logMsgId = logMsgId;
    }

    /**
     * 获取收件人
     *
     * @return tos - 收件人
     */
    public String getTos() {
        return tos;
    }

    /**
     * 设置收件人
     *
     * @param tos 收件人
     */
    public void setTos(String tos) {
        this.tos = tos;
    }

    /**
     * 获取发送时间
     *
     * @return send_date - 发送时间
     */
    public Date getSendDate() {
        return sendDate;
    }

    /**
     * 设置发送时间
     *
     * @param sendDate 发送时间
     */
    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
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
    
    

    public Long getRunTime() {
		return runTime;
	}

	public void setRunTime(Long runTime) {
		this.runTime = runTime;
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
        LogMsgDetailPO other = (LogMsgDetailPO) that;
        return (this.getCid() == null ? other.getCid() == null : this.getCid().equals(other.getCid()))
            && (this.getLogMsgId() == null ? other.getLogMsgId() == null : this.getLogMsgId().equals(other.getLogMsgId()))
            && (this.getTos() == null ? other.getTos() == null : this.getTos().equals(other.getTos()))
            && (this.getSendDate() == null ? other.getSendDate() == null : this.getSendDate().equals(other.getSendDate()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getCid() == null) ? 0 : getCid().hashCode());
        result = prime * result + ((getLogMsgId() == null) ? 0 : getLogMsgId().hashCode());
        result = prime * result + ((getTos() == null) ? 0 : getTos().hashCode());
        result = prime * result + ((getSendDate() == null) ? 0 : getSendDate().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        return result;
    }
}