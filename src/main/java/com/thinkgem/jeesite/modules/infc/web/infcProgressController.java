package com.thinkgem.jeesite.modules.infc.web;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.utils.IdGen;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.infc.entity.DataStatus;
import com.thinkgem.jeesite.modules.infc.entity.DataStatusList;
import com.thinkgem.jeesite.modules.proinfo.dao.ProgressDao;
import com.thinkgem.jeesite.modules.proinfo.dao.ProinfoDao;
import com.thinkgem.jeesite.modules.proinfo.entity.Progress;
import com.thinkgem.jeesite.modules.proinfo.entity.Proinfo;
import com.thinkgem.jeesite.modules.proinfo.service.ProinfoService;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@CrossOrigin
@Controller
@RequestMapping(value = "${adminPath}/infc/infcProgress")
public class infcProgressController extends BaseController {
    @Autowired
    private ProinfoService proinfoService;
    @Autowired
    private ProinfoDao proinfoDao;
    @Autowired
    private ProgressDao progressDao;

    //传入项目id查找到Proname，leader，money，planning和progress列表
    /*查找详情，以及进度列表*/
    @RequestMapping(value = "ProProgress",method = RequestMethod.GET)
    public String ProProgress(HttpServletRequest request, HttpServletResponse response) {
        String proInfoId = request.getParameter("ProInfoId");//获取当前项目id
        Proinfo proinfo=proinfoService.get(proInfoId);
        DataStatus dataStatus = new DataStatus();
        if(proinfo == null) {
            dataStatus.setStatusMessage("无法获取该任务详情！");
        } else {
            Map<String,Object> map=Maps.newHashMap();
            map.put("id",proinfo.getId());
            map.put("proname",proinfo.getProname());
            User user=UserUtils.get(proinfo.getLeader().getId());
            map.put("leader",user.getName());
            map.put("money",proinfo.getMoney());
            map.put("planningCycle",proinfo.getPlanningCycle());

            List<Map<String,Object>> list=Lists.newArrayList();
            List<Progress> progressList=proinfo.getProgressList();
            for(int i=0;i<progressList.size();i++) {
                Progress progress=progressList.get(i);
                Map<String,Object> progressMap=Maps.newHashMap();
                progressMap.put("progressId",progress.getId());
                progressMap.put("reportProgress",progress.getReportProgress());
                progressMap.put("isSuitable",progress.getIsSuitable());
                progressMap.put("problem",progress.getProblem());
                list.add(progressMap);
            }
            map.put("progressList",list);

            dataStatus.setSuccess("true");
            dataStatus.setData(map);
            if(list.size()>0){
                dataStatus.setStatusMessage("ok");
            } else{
                dataStatus.setStatusMessage("暂无进度数据");
            }
        }
        return this.renderString(response,dataStatus);
    }

    @RequestMapping(value = "addProgress",produces = MediaType.APPLICATION_JSON_VALUE,method = RequestMethod.POST)
    public String addProInfo(@RequestBody Progress progress, HttpServletRequest request, HttpServletResponse response) {

        DataStatus status = new DataStatus();
        String reportProgress = progress.getReportProgress();//进度
        if (reportProgress != null  ) {
            progress.setId(IdGen.uuid());
            progressDao.insert(progress);
            /*存储主表的isEnd字段*/
            Proinfo proinfo=new Proinfo(progress.getProinfo().getId());
            proinfo.setIsEnd(progress.getIsEnd());
            proinfoDao.updateIsEnd(proinfo);
            status.setStatusMessage("ok");
            status.setSuccess("true");
        } else {
            status.setStatusMessage("新增失败");
        }
        return this.renderString(response, status);
    }
}