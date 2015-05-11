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
        <div class="mcm_top_name"><g:message code='restaurantInfo.create.label'/></div>

        <div class="mcm_top_banner"></div>
    </div>

    <div class="span10" style="margin-left: 10px;margin-top: 0px;">
        <g:render template="../layouts/msgs_and_errors"></g:render>
    </div>

    <div class="span11">
        <form class="form-horizontal" method="POST" id="create_form" action="${createLink(controller: "customer",action: "getOrCreateOrder")}">
            <g:if test="${!isNeedPartakeCode}">
            <div class="control-group">
                <label class="control-label" style="font-size: 16px;font-weight: bolder;">

                </label>

                <div class="controls" style="font-size: 16px;font-weight: bolder;">
                    请输入桌位编码后重试
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="code"><g:message code="tableInfo.code.label"
                                                                   default="Code"/><span
                        class="required-indicator">*</span></label>

                <div class="controls">
                    <input type="text" style="width: 280px;" name="code" id="code"
                           value=""/>
                </div>
            </div>
            </g:if>
            <g:if test="${isNeedPartakeCode}">
                <div class="control-group">
                    <label class="control-label" for="partakeCode"><g:message code="orderInfo.partakeCode.label"
                                                                       default="Code"/><span
                            class="required-indicator">*</span></label>

                    <div class="controls">
                        <input type="text" style="width: 280px;" name="partakeCode" id="partakeCode"
                               value=""/>
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

        </form>
    </div>
</div>
</body>
</html>