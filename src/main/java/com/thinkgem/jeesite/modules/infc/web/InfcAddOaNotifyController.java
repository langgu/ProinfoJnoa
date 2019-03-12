package com.thinkgem.jeesite.modules.infc.web;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.IdGen;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.infc.entity.DataStatus;
import com.thinkgem.jeesite.modules.oa.entity.OaNotify;
import com.thinkgem.jeesite.modules.oa.service.OaNotifyService;
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
import java.io.InputStreamReader;
import java.util.Date;
import java.util.UUID;

/**
* @Description:    新建通知通告接口
* @Author:         wfp
* @CreateDate:     2019/1/9 1:10
*/
@CrossOrigin
@Controller
@RequestMapping(value = "${adminPath}/infc/infcAddOaNotify")
public class InfcAddOaNotifyController extends BaseController {

    @Autowired
    private OaNotifyService oaNotifyService;

    //请求方法为post,返回数据为json格式
   // produces属性可以设置返回数据的类型以及编码
    //@RequestBody   请求对象实体类 通过@requestBody可以将请求体中的JSON字符串绑定到相应的bean上
    @RequestMapping(value = "addOaNotify",produces = MediaType.APPLICATION_JSON_VALUE,method = RequestMethod.POST)
    public String addOaNotify(@RequestBody OaNotify oaNotify, HttpServletRequest request, HttpServletResponse response){

        DataStatus status = new DataStatus();
        String title = oaNotify.getTitle();    //标题
        String content = oaNotify.getContent();   //内容
        String files = oaNotify.getFiles();    //附件
        String createBy = oaNotify.getSendUserId();  //发送人
        String userIds = oaNotify.getReceUserIds();  //接收人
        String urgentFlag = oaNotify.getUrgentFlag();   //是否为紧急通知
        String year = DateUtils.getYear();
        String month = DateUtils.getMonth();
        String saveDir = "/jnoa"+ Global.USERFILES_BASE_URL+"1/images/img/"+year+"/"+month+"/";
        if(oaNotify.getImgPath1()!=null){
            String path = oaNotify.getImgPath1();
            String imgName = path.substring(path.lastIndexOf("/")+1,path.length());
            oaNotify.setImgPath1(saveDir+imgName);

        }
        if(oaNotify.getImgPath2()!=null){
            String path = oaNotify.getImgPath2();
            String imgName = path.substring(path.lastIndexOf("/")+1,path.length());
            oaNotify.setImgPath2(saveDir+imgName);
        }
        if(oaNotify.getImgPath3()!=null){
            String path = oaNotify.getImgPath3();
            String imgName = path.substring(path.lastIndexOf("/")+1,path.length());
            oaNotify.setImgPath3(saveDir+imgName);
        }
        if(title != null && content != null && createBy != null && userIds!= null){
            User user = UserUtils.get(createBy);
            oaNotify.setCreateBy(user);
            oaNotify.setCreateDate(new Date());
            oaNotifyService.saveByInfc(oaNotify, userIds);
            status.setStatusMessage("ok");
            status.setSuccess("true");
        }
        else{
            status.setStatusMessage("新增失败");
        }
        return this.renderString(response,status);
    }
}
