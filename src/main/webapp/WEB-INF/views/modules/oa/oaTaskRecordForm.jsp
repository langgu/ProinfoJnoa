<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>回复信息管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
		});
		function addRow(list, idx, tpl, row){
			$(list).append(Mustache.render(tpl, {
				idx: idx, delBtn: true, row: row
			}));
			$(list+idx).find("select").each(function(){
				$(this).val($(this).attr("data-value"));
			});
			$(list+idx).find("input[type='checkbox'], input[type='radio']").each(function(){
				var ss = $(this).attr("data-value").split(',');
				for (var i=0; i<ss.length; i++){
					if($(this).val() == ss[i]){
						$(this).attr("checked","checked");
					}
				}
			});
		}
		function delRow(obj, prefix){
			var id = $(prefix+"_id");
			var delFlag = $(prefix+"_delFlag");
			if (id.val() == ""){
				$(obj).parent().parent().remove();
			}else if(delFlag.val() == "0"){
				delFlag.val("1");
				$(obj).html("&divide;").attr("title", "撤销删除");
				$(obj).parent().parent().addClass("error");
			}else if(delFlag.val() == "1"){
				delFlag.val("0");
				$(obj).html("&times;").attr("title", "删除");
				$(obj).parent().parent().removeClass("error");
			}
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/oa/oaTaskRecord/">回复信息列表</a></li>
		<li class="active"><a href="${ctx}/oa/oaTaskRecord/form?id=${oaTaskRecord.id}">回复信息<shiro:hasPermission name="oa:oaTaskRecord:edit">${not empty oaTaskRecord.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="oa:oaTaskRecord:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="oaTaskRecord" action="${ctx}/oa/oaTaskRecord/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">附件：</label>
			<div class="controls">
				<form:input path="files" htmlEscape="false" maxlength="2000" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">接受人：</label>
			<div class="controls">
				<sys:treeselect id="receUser" name="receUser.id" value="${oaTaskRecord.receUser.id}" labelName="receUser.name" labelValue="${oaTaskRecord.receUser.name}"
					title="用户" url="/sys/office/treeData?type=3" cssClass="" allowClear="true" notAllowSelectParent="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注信息：</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">任务完成标记，&lsquo;0&rsquo;，未完成，&lsquo;1&rsquo;完成：</label>
			<div class="controls">
				<form:input path="completeFlag" htmlEscape="false" maxlength="1" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">年：</label>
			<div class="controls">
				<form:input path="year" htmlEscape="false" maxlength="10" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">月：</label>
			<div class="controls">
				<form:input path="month" htmlEscape="false" maxlength="10" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">日：</label>
			<div class="controls">
				<form:input path="day" htmlEscape="false" maxlength="10" class="input-xlarge "/>
			</div>
		</div>
			<div class="control-group">
				<label class="control-label">任务回复状态：</label>
				<div class="controls">
					<table id="contentTable" class="table table-striped table-bordered table-condensed">
						<thead>
							<tr>
								<th class="hide"></th>
								<th>发送人</th>
								<th>接受人</th>
								<th>备注信息</th>
								<th>回复状态(完成/未完成/无法完成)</th>
								<th>回复内容(完成无需回复)</th>
								<th>回复时间</th>
								<th>年</th>
								<th>月</th>
								<th>日</th>
								<shiro:hasPermission name="oa:oaTaskRecord:edit"><th width="10">&nbsp;</th></shiro:hasPermission>
							</tr>
						</thead>
						<tbody id="oaTaskReplyList">
						</tbody>
						<shiro:hasPermission name="oa:oaTaskRecord:edit"><tfoot>
							<tr><td colspan="11"><a href="javascript:" onclick="addRow('#oaTaskReplyList', oaTaskReplyRowIdx, oaTaskReplyTpl);oaTaskReplyRowIdx = oaTaskReplyRowIdx + 1;" class="btn">新增</a></td></tr>
						</tfoot></shiro:hasPermission>
					</table>
					<script type="text/template" id="oaTaskReplyTpl">//<!--
						<tr id="oaTaskReplyList{{idx}}">
							<td class="hide">
								<input id="oaTaskReplyList{{idx}}_id" name="oaTaskReplyList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
								<input id="oaTaskReplyList{{idx}}_delFlag" name="oaTaskReplyList[{{idx}}].delFlag" type="hidden" value="0"/>
							</td>
							<td>
								<input id="oaTaskReplyList{{idx}}_sendUser" name="oaTaskReplyList[{{idx}}].sendUser.id" type="text" value="{{row.sendUser.id}}" maxlength="64" class="input-small "/>
							</td>
							<td>
								<input id="oaTaskReplyList{{idx}}_receUser" name="oaTaskReplyList[{{idx}}].receUser.id" type="text" value="{{row.receUser.id}}" maxlength="64" class="input-small "/>
							</td>
							<td>
								<textarea id="oaTaskReplyList{{idx}}_remarks" name="oaTaskReplyList[{{idx}}].remarks" rows="4" maxlength="255" class="input-small ">{{row.remarks}}</textarea>
							</td>
							<td>
								<select id="oaTaskReplyList{{idx}}_replyFlag" name="oaTaskReplyList[{{idx}}].replyFlag" data-value="{{row.replyFlag}}" class="input-small ">
									<option value=""></option>
									<c:forEach items="${fns:getDictList('reply')}" var="dict">
										<option value="${dict.value}">${dict.label}</option>
									</c:forEach>
								</select>
							</td>
							<td>
								<input id="oaTaskReplyList{{idx}}_replyContent" name="oaTaskReplyList[{{idx}}].replyContent" type="text" value="{{row.replyContent}}" maxlength="2000" class="input-small "/>
							</td>
							<td>
								<input id="oaTaskReplyList{{idx}}_replyDate" name="oaTaskReplyList[{{idx}}].replyDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
									value="{{row.replyDate}}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
							</td>
							<td>
								<input id="oaTaskReplyList{{idx}}_year" name="oaTaskReplyList[{{idx}}].year" type="text" value="{{row.year}}" maxlength="10" class="input-small "/>
							</td>
							<td>
								<input id="oaTaskReplyList{{idx}}_month" name="oaTaskReplyList[{{idx}}].month" type="text" value="{{row.month}}" maxlength="10" class="input-small "/>
							</td>
							<td>
								<input id="oaTaskReplyList{{idx}}_day" name="oaTaskReplyList[{{idx}}].day" type="text" value="{{row.day}}" maxlength="10" class="input-small "/>
							</td>
							<shiro:hasPermission name="oa:oaTaskRecord:edit"><td class="text-center" width="10">
								{{#delBtn}}<span class="close" onclick="delRow(this, '#oaTaskReplyList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
							</td></shiro:hasPermission>
						</tr>//-->
					</script>
					<script type="text/javascript">
						var oaTaskReplyRowIdx = 0, oaTaskReplyTpl = $("#oaTaskReplyTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
						$(document).ready(function() {
							var data = ${fns:toJson(oaTaskRecord.oaTaskReplyList)};
							for (var i=0; i<data.length; i++){
								addRow('#oaTaskReplyList', oaTaskReplyRowIdx, oaTaskReplyTpl, data[i]);
								oaTaskReplyRowIdx = oaTaskReplyRowIdx + 1;
							}
						});
					</script>
				</div>
			</div>
		<div class="form-actions">
			<shiro:hasPermission name="oa:oaTaskRecord:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>