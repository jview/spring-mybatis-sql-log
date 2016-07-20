package org.jview.fwork.basedata.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import ai.yc.common.core.data.IMailVO;
import ai.yc.common.core.data.ISmsVO;
import ai.yc.common.core.entity.BaseEntity;

@Table(name = "tf_log_msg")
public class LogMsgPO extends BaseEntity {
	public void load(ISmsVO sms){
		this.msgType=1;
		this.sendType=0;
		this.sendFlag=0;
		this.content=sms.getContent();
		this.tos=sms.getTos();
		this.sendTime=sms.getSendDate();
		this.template=sms.getTemplate();
		this.setCreateUser(sms.getSenderId());
		
	}
	public void load(IMailVO mail){
		this.msgType=2;
		this.sendType=0;
		this.sendFlag=0;
		this.title=mail.getTitle();
		this.content=mail.getContent();
		this.tos=mail.getTos();
		this.ccs=mail.getCcs();
		this.bccs=mail.getBccs();
		this.template=mail.getTemplate();
		this.sendTime=mail.getSendDate();
		this.setCreateUser(mail.getSenderId());

	}
	
	
	
    @Id
    @SequenceGenerator(name="",sequenceName="select _nextval('_tf_logMsg_cid_seq')")
    private Long cid;

    /**
     * 邮件编码，可用lookup,
邮件用tf_send_mail_code，
短信用tf_send_sms_code,
推送用tf_send_push_code,
系统消息用tf_send_sys_code
     */
    @Column(name = "msg_code")
    private String msgCode;

    /**
     * 产品id
     */
    @Column(name = "project_id")
    private Long projectId;

    /**
     * 如果没有用户，则user_id=0
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 消息类型：1短信，2邮件，3系统消息，4推送消息
     */
    @Column(name = "msg_type")
    private Integer msgType;

    /**
     * 发送时间
     */
    @Column(name = "send_time")
    private Date sendTime;

    /**
     * 发送状态：0发送开始，1全部发送成功，3发送异常
     */
    @Column(name = "send_flag")
    private Integer sendFlag;

    /**
     * 邮件发送类型:1一起发送，2分开发送(分开发送，全部按收件人发，不区分抄送、密送）,3拒收
     */
    @Column(name = "send_type")
    private Integer sendType;

    /**
     * 邮箱个数，去除重复后
     */
    @Column(name = "mail_count")
    private Integer mailCount;

    /**
     * 发送成功的邮件个数
     */
    @Column(name = "send_count")
    private Integer sendCount;

    /**
     * 收件人或手机号
     */
    private String tos;

    /**
     * 抄送人
     */
    private String ccs;

    /**
     * 密送人
     */
    private String bccs;

    /**
     * 失败的邮箱，在需要时可人工对失败的邮件进行重发，重发方式与配置一样，如抄送仍然发到抄送
     */
    @Column(name = "fail_mails")
    private String failMails;

    /**
     * 失败或异常信息，如：记录哪些邮件发送失败
     */
    private String exceptions;

    /**
     * 邮件标题
     */
    private String title;

    /**
     * 邮件正文，一般只存一部份，最大不超过4000，测试多一些，生产不超过1000
     */
    private String content;
    
    private String template;

    /**
     * 邮件附件,多个以“,”隔开
     */
    @Column(name = "attach_path")
    private String attachPath;

    /**
     * 结束时间
     */
    @Column(name = "end_time")
    private Date endTime;
    
    @Column(name = "run_time")
    private Long runTime;

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
     * 获取邮件编码，可用lookup,
邮件用tf_send_mail_code，
短信用tf_send_sms_code,
推送用tf_send_push_code,
系统消息用tf_send_sys_code
     *
     * @return msg_code - 邮件编码，可用lookup,
邮件用tf_send_mail_code，
短信用tf_send_sms_code,
推送用tf_send_push_code,
系统消息用tf_send_sys_code
     */
    public String getMsgCode() {
        return msgCode;
    }

    /**
     * 设置邮件编码，可用lookup,
邮件用tf_send_mail_code，
短信用tf_send_sms_code,
推送用tf_send_push_code,
系统消息用tf_send_sys_code
     *
     * @param msgCode 邮件编码，可用lookup,
邮件用tf_send_mail_code，
短信用tf_send_sms_code,
推送用tf_send_push_code,
系统消息用tf_send_sys_code
     */
    public void setMsgCode(String msgCode) {
        this.msgCode = msgCode;
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
     * 获取如果没有用户，则user_id=0
     *
     * @return user_id - 如果没有用户，则user_id=0
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 设置如果没有用户，则user_id=0
     *
     * @param userId 如果没有用户，则user_id=0
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * 获取消息类型：1短信，2邮件，3系统消息，4推送消息
     *
     * @return msg_type - 消息类型：1短信，2邮件，3系统消息，4推送消息
     */
    public Integer getMsgType() {
        return msgType;
    }

    /**
     * 设置消息类型：1短信，2邮件，3系统消息，4推送消息
     *
     * @param msgType 消息类型：1短信，2邮件，3系统消息，4推送消息
     */
    public void setMsgType(Integer msgType) {
        this.msgType = msgType;
    }

    /**
     * 获取发送时间
     *
     * @return send_time - 发送时间
     */
    public Date getSendTime() {
        return sendTime;
    }

    /**
     * 设置发送时间
     *
     * @param sendTime 发送时间
     */
    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    /**
     * 获取发送状态：0发送开始，1全部发送成功，3发送异常
     *
     * @return send_flag - 发送状态：0发送开始，1全部发送成功，3发送异常
     */
    public Integer getSendFlag() {
        return sendFlag;
    }

    /**
     * 设置发送状态：0发送开始，1全部发送成功，2拆分时的部份发送成功，3发送异常
     *
     * @param sendFlag 发送状态：0发送开始，1全部发送成功,2拆分时的部份发送成功，3发送异常
     */
    public void setSendFlag(Integer sendFlag) {
        this.sendFlag = sendFlag;
    }

    /**
     * 获取邮件发送类型:1一起发送，2分开发送(分开发送，全部按收件人发，不区分抄送、密送）,3拒收
     *
     * @return send_type - 邮件发送类型:1一起发送，2分开发送(分开发送，全部按收件人发，不区分抄送、密送）,3拒收
     */
    public Integer getSendType() {
        return sendType;
    }

    /**
     * 设置邮件发送类型:1一起发送，2分开发送(分开发送，全部按收件人发，不区分抄送、密送）,3拒收
     *
     * @param sendType 邮件发送类型:1一起发送，2分开发送(分开发送，全部按收件人发，不区分抄送、密送）,3拒收
     */
    public void setSendType(Integer sendType) {
        this.sendType = sendType;
    }

    /**
     * 获取邮箱个数，去除重复后
     *
     * @return mail_count - 邮箱个数，去除重复后
     */
    public Integer getMailCount() {
        return mailCount;
    }

    /**
     * 设置邮箱个数，去除重复后
     *
     * @param mailCount 邮箱个数，去除重复后
     */
    public void setMailCount(Integer mailCount) {
        this.mailCount = mailCount;
    }

    /**
     * 获取发送成功的邮件个数
     *
     * @return send_count - 发送成功的邮件个数
     */
    public Integer getSendCount() {
        return sendCount;
    }

    /**
     * 设置发送成功的邮件个数
     *
     * @param sendCount 发送成功的邮件个数
     */
    public void setSendCount(Integer sendCount) {
        this.sendCount = sendCount;
    }

    /**
     * 获取收件人或手机号
     *
     * @return tos - 收件人或手机号
     */
    public String getTos() {
        return tos;
    }

    /**
     * 设置收件人或手机号
     *
     * @param tos 收件人或手机号
     */
    public void setTos(String tos) {
        this.tos = tos;
    }

    /**
     * 获取抄送人
     *
     * @return ccs - 抄送人
     */
    public String getCcs() {
        return ccs;
    }

    /**
     * 设置抄送人
     *
     * @param ccs 抄送人
     */
    public void setCcs(String ccs) {
        this.ccs = ccs;
    }

    /**
     * 获取密送人
     *
     * @return bccs - 密送人
     */
    public String getBccs() {
        return bccs;
    }

    /**
     * 设置密送人
     *
     * @param bccs 密送人
     */
    public void setBccs(String bccs) {
        this.bccs = bccs;
    }

    /**
     * 获取失败的邮箱，在需要时可人工对失败的邮件进行重发，重发方式与配置一样，如抄送仍然发到抄送
     *
     * @return fail_mails - 失败的邮箱，在需要时可人工对失败的邮件进行重发，重发方式与配置一样，如抄送仍然发到抄送
     */
    public String getFailMails() {
        return failMails;
    }

    /**
     * 设置失败的邮箱，在需要时可人工对失败的邮件进行重发，重发方式与配置一样，如抄送仍然发到抄送
     *
     * @param failMails 失败的邮箱，在需要时可人工对失败的邮件进行重发，重发方式与配置一样，如抄送仍然发到抄送
     */
    public void setFailMails(String failMails) {
        this.failMails = failMails;
    }

    /**
     * 获取失败或异常信息，如：记录哪些邮件发送失败
     *
     * @return exceptions - 失败或异常信息，如：记录哪些邮件发送失败
     */
    public String getExceptions() {
        return exceptions;
    }

    /**
     * 设置失败或异常信息，如：记录哪些邮件发送失败
     *
     * @param exceptions 失败或异常信息，如：记录哪些邮件发送失败
     */
    public void setExceptions(String exceptions) {
        this.exceptions = exceptions;
    }

    /**
     * 获取邮件标题
     *
     * @return title - 邮件标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置邮件标题
     *
     * @param title 邮件标题
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取邮件正文，一般只存一部份，最大不超过4000，测试多一些，生产不超过1000
     *
     * @return content - 邮件正文，一般只存一部份，最大不超过4000，测试多一些，生产不超过1000
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置邮件正文，一般只存一部份，最大不超过4000，测试多一些，生产不超过1000
     *
     * @param content 邮件正文，一般只存一部份，最大不超过4000，测试多一些，生产不超过1000
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 获取邮件附件,多个以“,”隔开
     *
     * @return attach_path - 邮件附件,多个以“,”隔开
     */
    public String getAttachPath() {
        return attachPath;
    }

    /**
     * 设置邮件附件,多个以“,”隔开
     *
     * @param attachPath 邮件附件,多个以“,”隔开
     */
    public void setAttachPath(String attachPath) {
        this.attachPath = attachPath;
    }

    /**
     * 获取结束时间
     *
     * @return end_time - 结束时间
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * 设置结束时间
     *
     * @param endTime 结束时间
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
    
    

    public Long getRunTime() {
		return runTime;
	}
	public void setRunTime(Long runTime) {
		this.runTime = runTime;
	}
	public String getTemplate() {
		return template;
	}
	public void setTemplate(String template) {
		this.template = template;
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
        LogMsgPO other = (LogMsgPO) that;
        return (this.getCid() == null ? other.getCid() == null : this.getCid().equals(other.getCid()))
            && (this.getMsgCode() == null ? other.getMsgCode() == null : this.getMsgCode().equals(other.getMsgCode()))
            && (this.getProjectId() == null ? other.getProjectId() == null : this.getProjectId().equals(other.getProjectId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getMsgType() == null ? other.getMsgType() == null : this.getMsgType().equals(other.getMsgType()))
            && (this.getSendTime() == null ? other.getSendTime() == null : this.getSendTime().equals(other.getSendTime()))
            && (this.getSendFlag() == null ? other.getSendFlag() == null : this.getSendFlag().equals(other.getSendFlag()))
            && (this.getSendType() == null ? other.getSendType() == null : this.getSendType().equals(other.getSendType()))
            && (this.getMailCount() == null ? other.getMailCount() == null : this.getMailCount().equals(other.getMailCount()))
            && (this.getSendCount() == null ? other.getSendCount() == null : this.getSendCount().equals(other.getSendCount()))
            && (this.getTos() == null ? other.getTos() == null : this.getTos().equals(other.getTos()))
            && (this.getCcs() == null ? other.getCcs() == null : this.getCcs().equals(other.getCcs()))
            && (this.getBccs() == null ? other.getBccs() == null : this.getBccs().equals(other.getBccs()))
            && (this.getFailMails() == null ? other.getFailMails() == null : this.getFailMails().equals(other.getFailMails()))
            && (this.getExceptions() == null ? other.getExceptions() == null : this.getExceptions().equals(other.getExceptions()))
            && (this.getTitle() == null ? other.getTitle() == null : this.getTitle().equals(other.getTitle()))
            && (this.getContent() == null ? other.getContent() == null : this.getContent().equals(other.getContent()))
            && (this.getAttachPath() == null ? other.getAttachPath() == null : this.getAttachPath().equals(other.getAttachPath()))
            && (this.getEndTime() == null ? other.getEndTime() == null : this.getEndTime().equals(other.getEndTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getCid() == null) ? 0 : getCid().hashCode());
        result = prime * result + ((getMsgCode() == null) ? 0 : getMsgCode().hashCode());
        result = prime * result + ((getProjectId() == null) ? 0 : getProjectId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getMsgType() == null) ? 0 : getMsgType().hashCode());
        result = prime * result + ((getSendTime() == null) ? 0 : getSendTime().hashCode());
        result = prime * result + ((getSendFlag() == null) ? 0 : getSendFlag().hashCode());
        result = prime * result + ((getSendType() == null) ? 0 : getSendType().hashCode());
        result = prime * result + ((getMailCount() == null) ? 0 : getMailCount().hashCode());
        result = prime * result + ((getSendCount() == null) ? 0 : getSendCount().hashCode());
        result = prime * result + ((getTos() == null) ? 0 : getTos().hashCode());
        result = prime * result + ((getCcs() == null) ? 0 : getCcs().hashCode());
        result = prime * result + ((getBccs() == null) ? 0 : getBccs().hashCode());
        result = prime * result + ((getFailMails() == null) ? 0 : getFailMails().hashCode());
        result = prime * result + ((getExceptions() == null) ? 0 : getExceptions().hashCode());
        result = prime * result + ((getTitle() == null) ? 0 : getTitle().hashCode());
        result = prime * result + ((getContent() == null) ? 0 : getContent().hashCode());
        result = prime * result + ((getAttachPath() == null) ? 0 : getAttachPath().hashCode());
        result = prime * result + ((getEndTime() == null) ? 0 : getEndTime().hashCode());
        return result;
    }
}