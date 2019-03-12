/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.oa.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.modules.infc.entity.DataStatusList;
import com.thinkgem.jeesite.modules.oa.entity.OaTask;
import com.thinkgem.jeesite.modules.oa.service.OaTaskService;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.oa.entity.OaTaskRecord;
import com.thinkgem.jeesite.modules.oa.service.OaTaskRecordService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 人员任务回复详情Controller
 * @author ctt
 * @version 2019-01-10
 */
@Controller
@RequestMapping(value = "${adminPath}/oa/oaTaskRecord")
public class OaTaskRecordController extends BaseController {

	@Autowired
	private OaTaskRecordService oaTaskRecordService;
	@Autowired
	private OaTaskService oaTaskService;

	@ModelAttribute
	public OaTaskRecord get(@RequestParam(required=false) String id) {
		OaTaskRecord entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = oaTaskRecordService.get(id);
		}
		if (entity == null){
			entity = new OaTaskRecord();
		}
		return entity;
	}
	
	@RequiresPermissions("oa:oaTaskRecord:view")
	@RequestMapping(value = {"list", ""})
	public String list(OaTaskRecord oaTaskRecord, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<OaTaskRecord> page = oaTaskRecordService.findPage(new Page<OaTaskRecord>(request, response), oaTaskRecord); 
		model.addAttribute("page", page);
		return "modules/oa/oaTaskRecordList";
	}

	@RequiresPermissions("oa:oaTaskRecord:view")
	@RequestMapping(value = "form")
	public String form(OaTaskRecord oaTaskRecord, Model model) {
		model.addAttribute("oaTaskRecord", oaTaskRecord);
		return "modules/oa/oaTaskRecordForm";
	}

	@RequiresPermissions("oa:oaTaskRecord:edit")
	@RequestMapping(value = "save")
	public String save(OaTaskRecord oaTaskRecord, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, oaTaskRecord)){
			return form(oaTaskRecord, model);
		}
		oaTaskRecordService.save(oaTaskRecord);
		addMessage(redirectAttributes, "保存回复信息成功");
		return "redirect:"+Global.getAdminPath()+"/oa/oaTaskRecord/?repage";
	}
	
	@RequiresPermissions("oa:oaTaskRecord:edit")
	@RequestMapping(value = "delete")
	public String delete(OaTaskRecord oaTaskRecord, RedirectAttributes redirectAttributes) {
		oaTaskRecordService.delete(oaTaskRecord);
		addMessage(redirectAttributes, "删除回复信息成功");
		return "redirect:"+Global.getAdminPath()+"/oa/oaTaskRecord/?repage";
	}



}