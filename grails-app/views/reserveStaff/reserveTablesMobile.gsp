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
    <meta name="layout" content="staff_mobile_template"/>
    <title></title>
    <style type="text/css">
    </style>
</head>

<body>

<!--提示消息-->
%{--<g:set var="errors" value="测试一个错误"/>--}%
<g:render template="/layouts/staff_mobile_msgs_and_errors"></g:render>
<div class="panel panel-default">
    <div class="panel-heading">
        <h3 class="panel-title">第二步-选择预定桌位和输入预定信息</h3>
    </div>

    <div class="panel-body">
       <div class="col-sm-12">
           <a href="${createLink(controller: "reserveStaff", action: "reserveDinnerTimeInput", params: [dinnerTime: params.dinnerTime])}">返回时间选择</a>
       </div>

        <g:form class="form-horizontal" method="POST" id="create_form" action="createReserveOrder">
            <g:if test="${tableInfoList}">
                <input type="hidden" name="dinnerTime" id="dinnerTime"
                       value="${params.dinnerTime}"/>

                <div class="form-group">
                    <label class="col-sm-3 control-label" style="margin-top: 15px;">
                        桌位<span>*</span></label>
                    <div class="col-sm-9">
                    <g:each in="${tableInfoList}" var="tableInfo">
                        <g:if test="${tableInfo.canUse}">
                            <div class="col-sm-3" style="margin-top: 15px;">
                                <div class="form-control">
                                <input class="" type="radio" name="tableId" id="tableId${tableInfo.tableInfo?.id}"
                                       value="${tableInfo.tableInfo?.id}" ${(lj.Number.toLong(params.tableId) == tableInfo.tableInfo?.id) ? "checked='checked'" : ""}/>
                                <label for="tableId${tableInfo.tableInfo?.id}">
                                ${tableInfo.tableInfo?.name}(${tableInfo.tableInfo?.minPeople}-${tableInfo.tableInfo?.maxPeople}人)
                                </label>
                                </div>
                            </div>
                        </g:if>
                    </g:each>
                    </div>

                    <label class="col-sm-3 control-label" for="personCount" style="margin-top: 15px;">
                        用餐人数<span>*</span></label>
                    <div class="col-sm-9" style="margin-top: 15px;">
                        <input type="number" name="personCount" id="personCount" class="form-control"
                               value="${params.personCount}"/>
                    </div>

                    <label class="col-sm-3 control-label" for="phone" style="margin-top: 15px;">
                        联系电话<span>*</span></label>
                    <div class="col-sm-9" style="margin-top: 15px;">
                        <input type="number" name="phone" id="phone" class="form-control"
                               value="${params.phone}"/>
                    </div>

                    <label class="col-sm-3 control-label" for="customerName" style="margin-top: 15px;">
                        联系人<span></span></label>
                    <div class="col-sm-9" style="margin-top: 15px;">
                        <input type="text" name="customerName" id="customerName" class="form-control"
                               value="${params.customerName}"/>
                    </div>

                    <label class="col-sm-3 control-label" for="remark" style="margin-top: 15px;">
                        备注<span></span></label>
                    <div class="col-sm-9" style="margin-top: 15px;">
                        <input type="text" name="remark" id="remark" class="form-control"
                               value="${params.remark}"/>
                    </div>

                    <div class="col-sm-12" style="margin-top: 15px;">
                        <input type="submit" value="${message(code: 'default.button.next.label', default: 'Next')}" class="form-control btn btn-primary"/>
                    </div>

                </div>
            </g:if>
        </g:form>

    </div>
</div>

</body>
</html>