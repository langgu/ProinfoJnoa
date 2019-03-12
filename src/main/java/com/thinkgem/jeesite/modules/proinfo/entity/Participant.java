/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.proinfo.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 项目信息Entity
 * @author 孙通
 * @version 2019-02-12
 */
public class Participant extends DataEntity<Participant> {
	
	private static final long serialVersionUID = 1L;
	private Proinfo proinfo;		// 父表id 父类
	private String name;		// 姓名
	private String position;		// 职位
	private String contact;		// 联系方式
	
	public Participant() {
		super();
	}

	public Participant(String id){
		super(id);
	}

	public Participant(Proinfo proinfo){
		this.proinfo = proinfo;
	}

	@Length(min=1, max=64, message="父表id长度必须介于 1 和 64 之间")
	public Proinfo getProinfo() {
		return proinfo;
	}

	public void setProinfo(Proinfo proinfo) {
		this.proinfo = proinfo;
	}
	
	@Length(min=1, max=64, message="姓名长度必须介于 1 和 64 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Length(min=0, max=64, message="职位长度必须介于 0 和 64 之间")
	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}
	
	@Length(min=0, max=64, message="联系方式长度必须介于 0 和 64 之间")
	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}
	
}