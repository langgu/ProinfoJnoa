/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.oa.entity;


import org.hibernate.validator.constraints.Length;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 人员任务回复详情Entity
 * @author ctt
 * @version 2019-01-10
 */
public class OaTaskReply extends DataEntity<OaTaskReply> {
	
	private static final long serialVersionUID = 1L;
	private OaTask oaTask;		// 任务主表ID(主表是oa_task_record)
	private OaTaskRecord oaTaskRecord;  //任务子表ID(主表是oa_task_record)
	private String sendUser;		// 发送人
	private String receUser;		// 接受人
	private String replyFlag;		// 回复状态(完成/未完成/无法完成)
	private String replyContent;		// 回复内容(完成无需回复)
	private Date replyDate;		// 回复时间
	private String year;		// 年
	private String month;		// 月
	private String day;		// 日
	private String oaTaskRecordId;   //任务记录表id
	
	public OaTaskReply() {
		super();
	}

	public OaTaskReply(String id){
		super(id);
	}

	public OaTaskReply(OaTaskRecord oaTaskRecord){
		this.oaTaskRecord = oaTaskRecord;
	}

	public void setOaTask(OaTask oaTask) {
		this.oaTask = oaTask;
	}

	public OaTask getOaTask() {
		return oaTask;
	}

	@Length(min=0, max=64, message="任务ID(主表是oa_task_record)长度必须介于 0 和 64 之间")
	public OaTaskRecord getOaTaskRecord() {
		return oaTaskRecord;
	}

	public void setOaTaskRecord(OaTaskRecord oaTaskRecord) {
		this.oaTaskRecord = oaTaskRecord;
	}

	@Length(min=0, max=64, message="发送人长度必须介于 0 和 64 之间")
	public String getSendUser() {
		return sendUser;
	}

	public void setSendUser(String sendUser) {
		this.sendUser = sendUser;
	}
	
	@Length(min=0, max=64, message="接受人长度必须介于 0 和 64 之间")
	public String getReceUser() {
		return receUser;
	}

	public void setReceUser(String receUser) {
		this.receUser = receUser;
	}
	
	@Length(min=0, max=1, message="回复状态(完成/未完成/无法完成)长度必须介于 0 和 1 之间")
	public String getReplyFlag() {
		return replyFlag;
	}

	public void setReplyFlag(String replyFlag) {
		this.replyFlag = replyFlag;
	}
	
	@Length(min=0, max=2000, message="回复内容(完成无需回复)长度必须介于 0 和 2000 之间")
	public String getReplyContent() {
		return replyContent;
	}

	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getReplyDate() {
		return replyDate;
	}

	public void setReplyDate(Date replyDate) {
		this.replyDate = replyDate;
	}
	
	@Length(min=0, max=10, message="年长度必须介于 0 和 10 之间")
	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}
	
	@Length(min=0, max=10, message="月长度必须介于 0 和 10 之间")
	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}
	
	@Length(min=0, max=10, message="日长度必须介于 0 和 10 之间")
	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getOaTaskRecordId() {
		return oaTaskRecordId;
	}

	public void setOaTaskRecordId(String oaTaskRecordId) {
		this.oaTaskRecordId = oaTaskRecordId;
	}
}