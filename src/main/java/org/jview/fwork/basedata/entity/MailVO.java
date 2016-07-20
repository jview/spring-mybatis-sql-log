package org.jview.fwork.basedata.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import ai.yc.common.core.data.IMailVO;

public class MailVO implements IMailVO, Serializable {
	private static final long serialVersionUID = 1L;
	private List<Long> userIdList;
	private List<Long> roleIdList;
	private String tos;
	private String ccs;
	private String bccs;
	private String title;
	private String content;
	private Date sendDate;
	private List<String> inlineList;
	private List<String> attachList;
	private String template;
	private Map<String, Object> paramMap;
	private Long senderId;
	public List<Long> getUserIdList() {
		return userIdList;
	}
	public void setUserIdList(List<Long> userIdList) {
		this.userIdList = userIdList;
	}
	public List<Long> getRoleIdList() {
		return roleIdList;
	}
	public void setRoleIdList(List<Long> roleIdList) {
		this.roleIdList = roleIdList;
	}
	public String getTos() {
		return tos;
	}
	public void setTos(String tos) {
		this.tos = tos;
	}
	
	public String getCcs() {
		return ccs;
	}
	public void setCcs(String ccs) {
		this.ccs = ccs;
	}
	public String getBccs() {
		return bccs;
	}
	public void setBccs(String bccs) {
		this.bccs = bccs;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getSendDate() {
		return sendDate;
	}
	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}
	public List<String> getInlineList() {
		return inlineList;
	}
	public void setInlineList(List<String> inlineList) {
		this.inlineList = inlineList;
	}
	public List<String> getAttachList() {
		return attachList;
	}
	public void setAttachList(List<String> attachList) {
		this.attachList = attachList;
	}
	public String getTemplate() {
		return template;
	}
	public void setTemplate(String template) {
		this.template = template;
	}
	public Map<String, Object> getParamMap() {
		return paramMap;
	}
	public void setParamMap(Map<String, Object> paramMap) {
		this.paramMap = paramMap;
	}
	public Long getSenderId() {
		return senderId;
	}
	public void setSenderId(Long senderId) {
		this.senderId = senderId;
	}
	

	
}
