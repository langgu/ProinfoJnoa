/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.infc.web;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.infc.entity.DataStatus;
import com.thinkgem.jeesite.modules.infc.entity.DataStatusList;
import com.thinkgem.jeesite.modules.oa.dao.OaTaskDao;
import com.thinkgem.jeesite.modules.oa.dao.OaTaskReplyDao;
import com.thinkgem.jeesite.modules.oa.entity.OaTask;
import com.thinkgem.jeesite.modules.oa.entity.OaTaskRecord;
import com.thinkgem.jeesite.modules.oa.entity.OaTaskReply;
import com.thinkgem.jeesite.modules.oa.service.OaTaskRecordService;
import com.thinkgem.jeesite.modules.oa.service.OaTaskService;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * 任务信息Controller
 * @author ctt
 * @version 2019-01-10
 */
@Controller
@RequestMapping(value = "${adminPath}/infc/infcOaTask")
public class InfcOaTaskController extends BaseController {

	@Autowired
	private OaTaskService oaTaskService;

	@Autowired
	private OaTaskRecordService oaTaskRecordService;

	@Autowired
	private OaTaskReplyDao oaTaskReplyDao;

	@ModelAttribute
	public OaTask get(@RequestParam(required=false) String id) {
		OaTask entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = oaTaskService.get(id);
		}
		if (entity == null){
			entity = new OaTask();
		}
		return entity;
	}


	/**
	 * 查询我的待办/已办任务
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "tbdTaskList",method = RequestMethod.GET)
	public String list(HttpServletRequest request, HttpServletResponse response) {
		//手机端传送userid
		//然后根据userId和完成标记查询出本用户对应的任务信息列表
		String userId = request.getParameter("userId");
		String flag = request.getParameter("completeFlag");
		OaTaskRecord oaTaskRecord = new OaTaskRecord();
		User user = UserUtils.get(userId);
		oaTaskRecord.setReceUser(user);
		oaTaskRecord.setCompleteFlag(flag);
		if(flag.equals("1")){
			oaTaskRecord.setNotFlag("2");
		}
		List<OaTaskRecord> recordList = oaTaskRecordService.findList(oaTaskRecord);
		DateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
		List<Map<String, Object>> data = Lists.newArrayList();
		for(OaTaskRecord oaTaskRecord2: recordList){
			Map<String, Object> map = Maps.newHashMap();
			map.put("recordId",oaTaskRecord2.getId());
			map.put("title",oaTaskRecord2.getTitle());
			map.put("sendUser",oaTaskRecord2.getSendUser().getName());
			map.put("forwardFlag",oaTaskRecord2.getForwardFlag());
			map.put("sendDate",format.format(oaTaskRecord2.getSendDate()));
			data.add(map);
		}
		DataStatusList status = new DataStatusList();
		status.setSuccess("true");
		if (recordList.size()>0){
			status.setStatusMessage("ok");
		}else {
			status.setStatusMessage("暂无数据");
		}
		status.setData(data);
		return this.renderString(response,status);
	}



	/**
	 * 查询当前用户已发布的任务
	 */
	@ResponseBody
	@RequestMapping(value = "publishTaskList",method = RequestMethod.GET)
	public String ownlist(OaTask oaTask, HttpServletRequest request, HttpServletResponse response){
		//手机端传送userid
		String userId = request.getParameter("userId");
		User user = UserUtils.get(userId);
		//获取创建者为当前id的通知通告列表
		oaTask.setCreateBy(user);
		List<Map<String, Object>> data = Lists.newArrayList();
		List<OaTask> list = oaTaskService.findList(oaTask);
		for (OaTask oaTask1:list){
			//OaTask oaTask1 = oaTaskService.get(list.get(i).getId());
			Map<String, Object> data_detail = Maps.newHashMap();
			data_detail.put("id",oaTask1.getId());
			data_detail.put("title",oaTask1.getTitle());
			data_detail.put("CompleteFlag",oaTask1.getCompleteFlag());
			data_detail.put("forwardFlag",oaTask1.getForwardFlag());
			DateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
			String reTime = format.format(oaTask1.getCreateDate());
			data_detail.put("sendDate",reTime);
			data.add(data_detail);
		}
		DataStatusList status = new DataStatusList();
		status.setSuccess("true");
		if (list.size()>0){
			status.setStatusMessage("ok");
		}else {
			status.setStatusMessage("暂无数据");
		}
		status.setData(data);
		return this.renderString(response,status);

	}


   /**
   * @Description:    查询待办/已办任务详情及回复列表
   * @Author:         wfp
   * @CreateDate:     2019/1/13 22:24
   */
	@RequestMapping(value = "tbdTaskDetails",method = RequestMethod.GET)
	public String task_detail(HttpServletRequest request, HttpServletResponse response) {
		String recordId = request.getParameter("recordId");
		OaTaskRecord oaTaskRecord = oaTaskRecordService.get(recordId);
		DataStatus dataStatus = new DataStatus();
		if(oaTaskRecord == null){
			dataStatus.setStatusMessage("无法获取该任务详情！");
		} else{
			Map<String, Object> map = Maps.newHashMap();
			map.put("recordId", oaTaskRecord.getId());
			map.put("title", oaTaskRecord.getTitle());
			map.put("content", oaTaskRecord.getContent());
			map.put("sendUser",oaTaskRecord.getSendUser().getName());
			DateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
			String reTime = format.format(oaTaskRecord.getSendDate());
			map.put("sendDate",reTime);
			map.put("forwardFlag",oaTaskRecord.getForwardFlag());
			map.put("files",oaTaskRecord.getFiles());

			//该任务的个人回复列表
			List<Map<String, Object>> data = Lists.newArrayList();
			List<OaTaskReply> replyList = oaTaskRecord.getOaTaskReplyList();
			for(int i=0;i<replyList.size(); i++){
				OaTaskReply reply =replyList.get(i);
				Map<String, Object> map2 = Maps.newHashMap();
//				map2.put("receUser",UserUtils.get(reply.getReceUser()).getName());
				map2.put("sendUser",UserUtils.get(reply.getSendUser()).getName());
				map2.put("replyContent",reply.getReplyContent());
				map2.put("replyDate",format.format(reply.getReplyDate()));
				map2.put("replyFlag",reply.getReplyFlag());
				data.add(map2);
			}
			map.put("replyList",data);
			dataStatus.setSuccess("true");
			dataStatus.setData(map);
			if(data.size()>0){
				dataStatus.setStatusMessage("ok");
			} else{
				dataStatus.setStatusMessage("暂无回复数据");
			}
		}
		return this.renderString(response,dataStatus);
	}

	/**
	 * 查询任务详情以及回复列表
	 */
	@ResponseBody
	@RequestMapping(value = "publishTaskDetails",method = RequestMethod.GET)
	public String task_reply_detail( HttpServletRequest request, HttpServletResponse response){
		String oaTaskId = request.getParameter("taskId");
		OaTask oaTask = null;
		if (StringUtils.isNotBlank(oaTaskId)) {
			oaTask = oaTaskService.get(oaTaskId);
		}
		if (oaTask == null) {
			oaTask = new OaTask();
		}
		Map<String, Object> map = Maps.newHashMap();
		//任务详情
		map.put("title", oaTask.getTitle());
		map.put("content", oaTask.getContent());
		map.put("files", oaTask.getFiles());
		map.put("forwardFlag",oaTask.getForwardFlag());
		DateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
		String reTime = format.format(oaTask.getCreateDate());
		map.put("sendDate",reTime);
		map.put("taskId",oaTask.getId());

		String receNames = "";
		//任务回复列表
		List<OaTaskRecord> list = oaTask.getOaTaskRecordList();
		List<Map<String, Object>> data = Lists.newArrayList();
		for (int i = 0; i<list.size();i++) {
			OaTaskRecord oaTaskRecord = list.get(i);
			if (i == list.size() - 1) {
				receNames += oaTaskRecord.getReceUser().getName();
			} else {
				receNames += oaTaskRecord.getReceUser().getName() + ",";
			}
		}
//			得到本项目的第I位接收人的回复列表
			List<OaTaskReply> replylist = oaTaskRecordService.getOaTaskReplyList(oaTask);
			for (int j=0;j<replylist.size();j++)
			{
				String id2 = replylist.get(j).getId();
				OaTaskReply oaTaskReply = oaTaskReplyDao.get(id2);
				Map<String, Object> map1 = Maps.newHashMap();
				User user = UserUtils.get(oaTaskReply.getSendUser());
				map1.put("sendUser",user.getName());
				map1.put("content",oaTaskReply.getReplyContent());
				map1.put("replyFlag",oaTaskReply.getReplyFlag());
				String reTime2 = format.format(oaTaskReply.getReplyDate());
				map1.put("replyDate",reTime2);
				data.add(map1);
			}

		map.put("receNames",receNames);
		map.put("replyList",data);
		DataStatus dataStatusList = new DataStatus();
		dataStatusList.setSuccess("true");
		if (data.size()>0){
			dataStatusList.setStatusMessage("ok");
		}else {
			dataStatusList.setStatusMessage("暂无回复数据");
		}
		dataStatusList.setData(map);
		return this.renderString(response,dataStatusList);
	}
}