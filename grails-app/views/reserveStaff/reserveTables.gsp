<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 15-2-10
  Time: 下午11:11
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main_template"/>
    <title></title>
    <style type="text/css">
    .mc_main {
        width: 1000px;
        height: auto;
        margin: 0px 50px;
        background-color: #FFFFFF;
        float: left;
    }

    .mcm_top {
        margin-top: 20px;
    }

    .mcm_top_name {
        font-size: 20px;
        font-weight: bold;
        padding: 10px 0px 10px 20px;
    }

    .mcm_top_banner {
        width: 100%;
        height: 4px;
        background: url('${resource(dir:"images",file:"login_banner.gif")}');
        margin: 0px auto;
        margin-bottom: 30px;
    }
    </style>
</head>

<body>
<div class="mc_main">
    <div class="mcm_top">
        %{--<div class="mcm_top_name"><g:message code='restaurantInfo.create.label'/></div>--}%

        %{--<div class="mcm_top_banner"></div>--}%
        <g:render template="../layouts/staffMenu"></g:render>
    </div>

    <div class="span10" style="margin-left: 10px;margin-top: 0px;">
        <g:render template="../layouts/msgs_and_errors"></g:render>
    </div>

    <a href="${createLink(controller: "reserveStaff", action: "reserveDinnerTimeInput", params: [dinnerTime: params.dinnerTime])}">返回时间选择</a>

    <div class="span11">
        <g:form class="form-horizontal" method="POST" id="create_form" action="createReserveOrder">
            <g:if test="${tableInfoList}">
                <input type="hidden" name="dinnerTime" id="dinnerTime"
                       value="${params.dinnerTime}"/>

                <div class="control-group">
                    <label class="control-label">
                        桌位<span
                            class="required-indicator">*</span></label>
                    <g:each in="${tableInfoList}" var="tableInfo">
                        <g:if test="${tableInfo.canUse}">
                            <div class="controls">
                                <input type="radio" name="tableId" id="tableId${tableInfo.tableInfo?.id}"
                                       value="${tableInfo.tableInfo?.id}" ${(lj.Number.toLong(params.tableId) == tableInfo.tableInfo?.id) ? "checked='checked'" : ""}/> ${tableInfo.tableInfo?.name}
                            </div>
                        </g:if>
                    </g:each>
                </div>

                <div class="control-group">
                    <label class="control-label" for="personCount">
                        用餐人数<span
                            class="required-indicator">*</span></label>

                    <div class="controls">
                        <input type="text" style="width: 280px;" name="personCount" id="personCount"
                               value="${params.personCount}"/>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label" for="phone">
                        联系电话<span
                            class="required-indicator">*</span></label>

                    <div class="controls">
                        <input type="text" style="width: 280px;" name="phone" id="phone"
                               value="${params.phone}"/>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label" for="customerName">
                        联系人<span
                            class="required-indicator"></span></label>

                    <div class="controls">
                        <input type="text" style="width: 280px;" name="customerName" id="customerName"
                               value="${params.customerName}"/>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label" for="remark">
                        备注<span
                            class="required-indicator">*</span></label>

                    <div class="controls">
                        <input type="text" style="width: 280px;" name="remark" id="remark"
                               value="${params.remark}"/>
                    </div>
                </div>
            </g:if>
            <div class="control-group">
                <label class="control-label"></label>

                <div class="controls">
                    <input type="submit" value="${message(code: 'default.button.next.label', default: 'Next')}"
                           class="btn send_btn"/>
                </div>
            </div>

        </g:form>
    </div>
</div>
</body>
</html>