<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>事件处理管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/matter/matter/">事件处理列表</a></li>
		<shiro:hasPermission name="matter:matter:edit"><li><a href="${ctx}/matter/matter/form">事件处理添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="matter" action="${ctx}/matter/matter/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>名称：</label>
				<form:input path="title" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>更新时间</th>
				<th>备注信息</th>
				<th>名称</th>
				<th>事件内容描述</th>
				<th>办公室意见</th>
				<th>部门领导意见</th>
				<th>事件处理意见</th>
				<shiro:hasPermission name="matter:matter:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="matter">
			<tr>
				<td><a href="${ctx}/matter/matter/form?id=${matter.id}">
					<fmt:formatDate value="${matter.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</a></td>
				<td>
					${matter.remarks}
				</td>
				<td>
					${matter.title}
				</td>
				<td>
					${matter.describe}
				</td>
				<td>
					${matter.officeText}
				</td>
				<td>
					${matter.leaderText}
				</td>
				<td>
					${matter.handlingText}
				</td>
				<shiro:hasPermission name="matter:matter:edit"><td>
    				<a href="${ctx}/matter/matter/form?id=${matter.id}">修改</a>
					<a href="${ctx}/matter/matter/delete?id=${matter.id}" onclick="return confirmx('确认要删除该事件处理吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>