package com.thinkgem.jeesite.modules.infc.web;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.UploadUtils;
import com.thinkgem.jeesite.common.web.Servlets;
import com.thinkgem.jeesite.modules.infc.entity.DataStatus;
import org.springframework.ui.Model;
import com.thinkgem.jeesite.common.utils.IdGen;
import com.thinkgem.jeesite.common.web.BaseController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;

@CrossOrigin
@Controller
@RequestMapping(value = "${adminPath}/infc/fileUpload")
public class FileUploadController extends BaseController {
    /**
     * 上传照片
	 * @return
     */
    @RequestMapping(value="/uploadPhoto",method=RequestMethod.POST)
    @ResponseBody
    public String uploadPhoto(HttpServletRequest request,HttpServletResponse response){
        DataStatus resultJson = new DataStatus();
        //创建一个通用的多部分解析器.
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        //设置编码
        commonsMultipartResolver.setDefaultEncoding("utf-8");
        //判断 request 是否有文件上传,即多部分请求..
        if (commonsMultipartResolver.isMultipart(request)){
            try {
                //将request转换成多分解请求
//                MultipartHttpServletRequest mulReq = commonsMultipartResolver.resolveMultipart(request);
                MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) request;
                //用getFiles("file")的方式来处理多个文件。返回的是一个list.
                List<MultipartFile> files=mulReq.getFiles("uploadfile"); //获取多张图片
                for(MultipartFile file : files){
                    String imgName = file.getOriginalFilename();  //获取图片的原始文件名
                    String suffix = imgName.substring(imgName.lastIndexOf(".")+1,imgName.length()).toLowerCase();//获取文件后缀名是否为jpg
                    if(!"jpg".equals(suffix)){
                        resultJson.setStatusMessage("上传格式不正确");
                        return this.renderString(response,resultJson);
                    }
                    String year = DateUtils.getYear();
                    String month =DateUtils.getMonth();
                    //创建文件保存目录
                    String saveDir = Global.getUserfilesBaseDir() + Global.USERFILES_BASE_URL+"1/images/img/"+year+"/"+month+"/";
                    File dir = new File(saveDir);
                    if (!dir.exists()) {  //如果目录不存在则创建
                        dir.mkdirs();
                    }
                    File localFile = new File(dir +"/"+ file.getOriginalFilename());
                    try {
                        file.transferTo(localFile);  //通过MultipartFile的transferTo(File dest)这个方法来直接转存文件到指定的路径。
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                resultJson.setStatusMessage("上传成功");
                resultJson.setStatusMessage("true");
            } catch (Exception e) {
                resultJson.setStatusMessage("上传失败");
                logger.error(e.getMessage());
            }
        }else{
            resultJson.setStatusMessage("上传照片不能为空");
        }
        return this.renderString(response,resultJson);
    }
}
