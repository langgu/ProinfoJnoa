/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.oa.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.oa.entity.OaTask;

/**
 * 任务信息DAO接口
 * @author ctt
 * @version 2019-01-10
 */
@MyBatisDao
public interface OaTaskDao extends CrudDao<OaTask> {

//    更新标记
    public int  updateFlag(OaTask oaTask);
}