package com.thinkgem.jeesite.modules.infc.web;

import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.infc.entity.DataStatus;
import com.thinkgem.jeesite.modules.oa.entity.OaTask;
import com.thinkgem.jeesite.modules.oa.entity.OaTaskRecord;
import com.thinkgem.jeesite.modules.oa.entity.OaTaskReply;
import com.thinkgem.jeesite.modules.oa.service.OaTaskRecordService;
import com.thinkgem.jeesite.modules.oa.service.OaTaskService;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
* @Description:    新增任务接口
* @Author:         wfp
* @CreateDate:     2019/1/10 18:19
*/
@CrossOrigin
@Controller
@RequestMapping(value = "${adminPath}/infc/infcAddTask")
public class InfcAddTaskController extends BaseController {

    @Autowired
    private OaTaskService oaTaskService;

    @Autowired
    private OaTaskRecordService oaTaskRecordService;

    @RequestMapping(value = "addTask",produces = MediaType.APPLICATION_JSON_VALUE,method = RequestMethod.POST)
    public String addTask(@RequestBody OaTask oaTask, HttpServletRequest request, HttpServletResponse response){
        DataStatus status = new DataStatus();
        String title = oaTask.getTitle();    //标题
        String content = oaTask.getContent();   //内容
        String files = oaTask.getFiles();    //附件
        String createBy = oaTask.getSendUserId();  //发送人
        String userIds = oaTask.getReceUserIds();  //接收人
        String forwardFlag = oaTask.getForwardFlag();  //是否转发标记
        if(title != null && content != null && createBy != null && userIds!= null && forwardFlag !=null){
            User user = UserUtils.get(createBy);
            oaTask.setCreateBy(user);
            oaTask.setCreateDate(new Date());
            oaTask.setCompleteFlag("0");  //未完成
            oaTask.setYear(DateUtils.getYear());
            oaTask.setMonth(DateUtils.getMonth());
            oaTask.setDay(DateUtils.getDay());
            oaTaskService.saveByInfc(oaTask, userIds);
            status.setStatusMessage("ok");
            status.setSuccess("true");
        }
        else{
            status.setStatusMessage("新增失败");
        }
        return this.renderString(response,status);
    }

    @RequestMapping(value = "addTaskReply",produces = MediaType.APPLICATION_JSON_VALUE,method = RequestMethod.POST)
    public String addReply(@RequestBody OaTaskReply oaTaskReply, HttpServletRequest request, HttpServletResponse response){
        DataStatus status = new DataStatus();
        String oaTaskRecordId = oaTaskReply.getOaTaskRecordId();  //任务记录表id
        String replyFlag = oaTaskReply.getReplyFlag();    //回复状态(完成/未完成/无法完成)
        String replyContent = oaTaskReply.getReplyContent();   //回复内容(完成无需回复)
        String sendUser = oaTaskReply.getSendUser();  //发送人
        if(oaTaskRecordId!= null && replyFlag != null && replyContent != null && sendUser != null){
            OaTaskRecord oaTaskRecord = oaTaskRecordService.get(oaTaskRecordId);
            oaTaskReply.setOaTask(oaTaskRecord.getOaTask());
            oaTaskReply.setOaTaskRecord(oaTaskRecord);
            oaTaskReply.setReceUser(oaTaskRecord.getSendUser().getId());
            oaTaskReply.setReplyDate(new Date());  //回复时间
            oaTaskReply.setYear(DateUtils.getYear());
            oaTaskReply.setMonth(DateUtils.getMonth());
            oaTaskReply.setDay(DateUtils.getDay());
            oaTaskRecordService.saveByInfcReply(oaTaskReply);
            status.setStatusMessage("ok");
            status.setSuccess("true");
        }
        else{
            status.setStatusMessage("新增失败");
        }
        return this.renderString(response,status);
    }

}
