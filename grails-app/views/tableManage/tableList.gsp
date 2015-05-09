
<%@ page import="lj.FormatUtil" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main_template"/>
  <title>桌位列表</title>
<style type="text/css">
    .mc_main {
        width: 1000px;
        height: auto;
        margin: 0px 50px;
        background-color: #FFFFFF;
        float: left;
    }
    </style>
</head>
<body>
<div class="mc_main">
<div class="mcm_top">
    %{--<div class="mcm_top_name"><g:message code='restaurantInfo.update.label'/></div>--}%

    %{--<div class="mcm_top_banner"></div>--}%
    <g:render template="../layouts/shopMenu"></g:render>
</div>
    <div  class="span10" style="margin-left: 10px;margin-top: 10px;">

        %{--<g:render template="../layouts/shopMenu"/>--}%
        <g:render template="../layouts/msgs_and_errors"></g:render>

        <a href="${createLink(controller: "tableManage", action: "editTableInfo")}" class="btn btn-primary">新增桌位</a>
        <a href="${createLink(controller: "tableManage", action: "printTable")}" target="_blank" class="btn btn-primary">打印桌位标贴</a>

<g:if test="${tableList}">
    <table class="table table-striped table-bordered table-condensed">
        <thead>
        <tr>
            <g:sortableColumn property="name" title="${message(code: 'tableInfo.name.label', default: 'Name')}" />
            <g:sortableColumn property="minPeople" title="${message(code: 'tableInfo.minPeople.label', default: 'Min People')}" />
            <g:sortableColumn property="maxPeople" title="${message(code: 'tableInfo.maxPeople.label', default: 'Max People')}" />
            <g:sortableColumn property="code" title="${message(code: 'tableInfo.code.label', default: 'Code')}" />
            <g:sortableColumn property="canReserve" title="${message(code: 'tableInfo.canReserve.label', default: 'Can Reserve')}" />
            <th>桌位二维码</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <g:each in="${tableList}" status="i" var="tableInfoInstance">
            <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                <td>${fieldValue(bean: tableInfoInstance, field: "name")}</td>

                <td>${fieldValue(bean: tableInfoInstance, field: "minPeople")}</td>

                <td>${fieldValue(bean: tableInfoInstance, field: "maxPeople")}</td>

                <td>${tableInfoInstance?.code}</td>

                <td>${FormatUtil.boolFormat(tableInfoInstance.canReserve)}</td>

                <td>
                <taos:tableQRCode tableId="${tableInfoInstance.id}" />
                </td>

                <td><a href="${createLink(controller: "tableManage",action: "editTableInfo",params: [id:tableInfoInstance.id])}" class="btn btn-link">编辑</a>
                    <a href="${createLink(controller: "tableManage",action: "delTableInfo",params: [ids:tableInfoInstance.id])}" class="btn btn-link">删除</a></td>

            </tr>
        </g:each>
        </tbody>
    </table>

    <taos:paginate total="${totalCount?:0}" prev="&larr;" next="&rarr;" params="${params}" max="12"/>

    </div>
 </div>

</g:if>
<g:else>
    还没有桌位，赶快去添加吧
</g:else>
</body>
</html>