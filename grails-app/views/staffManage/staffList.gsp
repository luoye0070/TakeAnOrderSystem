<%@ page import="lj.enumCustom.PositionType; lj.FormatUtil" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main_template"/>
    <title>工作人员列表</title>
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

        <a href="${createLink(controller: "staffManage", action: "editStaffInfo")}" class="btn btn-primary">新增员工</a>


        <g:if test="${staffFullList}">
            <table class="table table-striped table-bordered table-condensed">
                <thead>
                <tr>

                    <g:sortableColumn property="loginName"
                                      title="${message(code: 'staffInfo.loginName.label', default: 'Login Name')}"/>

                    <g:sortableColumn property="name"
                                      title="${message(code: 'staffInfo.name.label', default: 'Name')}"/>

                    <g:sortableColumn property="passWord"
                                      title="${message(code: 'staffInfo.staffPositionInfo.label', default: 'staffPositionInfo')}"/>

                    <g:sortableColumn property="isOnline"
                                      title="${message(code: 'staffInfo.isOnline.label', default: 'Is Online')}"/>
                    <th>操作</th>

                </tr>
                </thead>
                <tbody>
                <g:each in="${staffFullList}" status="i" var="staffFull">
                    <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                        <td>${staffFull?.staffInfo?.loginName}</td>

                        <td>${staffFull?.staffInfo?.name}</td>

                        <td><g:each in="${staffFull?.positionFullList}">${it.label}|</g:each></td>

                        <td>${FormatUtil.boolFormat(staffFull?.staffInfo?.isOnline)}</td>
                        <g:if test="${!([code: PositionType.SHOPKEEPER.code, label: PositionType.SHOPKEEPER.label] in staffFull?.positionFullList)}">
                            <td><a href="${createLink(controller: "staffManage", action: "editStaffInfo", params: [id: staffFull?.staffInfo?.id])}" class="btn btn-link">编辑</a>
                            <a href="${createLink(controller: "staffManage", action: "delStaff", params: [ids: staffFull?.staffInfo?.id])}" class="btn btn-link">删除</a>
                            </td>
                        </g:if>
                    </tr>
                </g:each>
                </tbody>
            </table>

            <div class="pagination">
                <g:paginate total="${totalCount}"/>
            </div>
        </g:if>
        <g:else>

        </g:else>

    </div>
</div>
</body>
</html>