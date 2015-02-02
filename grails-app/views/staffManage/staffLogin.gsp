<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 13-11-5
  Time: 下午8:54
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main_template"/>
    <title>工作人员登录</title>
    <style type="text/css">
    .mc_main {
        width: 1000px;
        height: auto;
        margin: 0px 50px;
        background-color: #FFFFFF;
        float: left;
    }
    #login_name {
        font-size: 20px;
        font-weight: bold;
        padding: 10px 0px 10px 20px;
        margin-top: 20px;
    }

    #login_banner {
        width: 1000px;
        height: 4px;
        background: url('${resource(dir:"images",file:"login_banner.gif")}');
        margin: 0px auto;
        margin-bottom: 30px;
    }
    .login_btn {
        width: 172px;
        height: 37px;
        background: url('${resource(dir:"images",file:"login_btn.gif")}');
        color: #fff;

    }
    </style>
</head>

<body>

<div class="mc_main">
    <div id="legend">
        <div id="login_name"><g:message code='staffInfo.login.label'/></div>

        <div id="login_banner"></div>
    </div>

    <g:render template="../layouts/msgs_and_errors"></g:render>

    <div class="span11">
        <g:form class="form-horizontal" method="post" action="staffLogin">
            <fieldset>


                <div class="control-group">
                    <label class="control-label"><g:message code="staffInfo.restaurantId.label" default="restaurantId"/>
                        <span class="required-indicator"></span></label>

                    <div class="controls">
                        <g:textField name="restaurantId" maxlength="32" required="" value="${params.restaurantId}"/>
                    </div>
                </div>


                <div class="control-group">
                    <label class="control-label"><g:message code="staffInfo.loginName.label" default="Login Name"/>
                        <span class="required-indicator"></span></label>

                    <div class="controls">
                        <g:textField name="loginName" maxlength="32" required="" value="${params.loginName}"/>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label" for="passWord"><g:message code="staffInfo.passWord.label"
                                                                           default="Pass Word"/>
                        <span class="required-indicator"></span></label>

                    <div class="controls">
                        <g:passwordField name="passWord" maxlength="128" required="" value=""/>
                    </div>
                </div>

                <div class="control-group">
                    <!-- Button -->
                    <div class="controls">

                        <g:submitButton name="login"
                                        value="${message(code: 'default.button.login.label', default: 'login')}"
                                        class="login_btn" style="width: 172px;font-weight: bold;font-size: 14px;"/>
                    </div>
                </div>
            </fieldset>
        </g:form>

    </div>
</div>

</body>
</html>