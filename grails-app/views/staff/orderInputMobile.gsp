<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 13-11-23
  Time: 下午6:24
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="staff_mobile_template"/>
    <title>创建订单</title>
</head>

<body>
<!--提示消息-->
%{--<g:set var="errors" value="测试一个错误"/>--}%
<g:render template="/layouts/staff_mobile_msgs_and_errors"></g:render>
<div class="panel panel-default">
    <div class="panel-heading">
        <h3 class="panel-title">创建订单</h3>
    </div>

    <div class="panel-body">
        <form class="form-horizontal" action="${createLink(controller: "staff", action: "createOrder")}">
            <div class="form-group">
                <label class="col-sm-2 control-label" style="margin-top: 15px;">
                    桌位：
                </label>

                <div class="col-sm-8" style="margin-top: 15px;">
                    <select name="code" class="form-control">
                        <g:each in="${tableList}" var="tableInfo">
                            <g:if test="${tableInfo?.canUse}">
                                <option value="${tableInfo?.tableInfo.code}" ${params.code == tableInfo?.tableInfo.code.toString() ? "selected='selected'" : ""}>${tableInfo?.tableInfo.name}</option>
                            </g:if>
                        </g:each>
                    </select>
                </div>

                <div class="col-sm-2" style="margin-top: 15px;">
                    <input type="submit"
                           value="${message(code: 'default.button.createOrder.label', default: 'createOrder')}"
                           class="form-control btn btn-primary"/>
                </div>
            </div>
        </form>
    </div>
</div>
</body>
</html>