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
    <meta name="layout" content="staff_mobile_template"/>
    <title>工作人员登录</title>
    <script type="text/javascript">
        $(function(){
            <g:if test="${errors}">
            </g:if>
            <g:else>
            //获取用户名密码并进行登录
            if (window.dataHelper) {
                //alert("dataHelper");
                window.dataHelper.autoLogin();
            }else{//测试自动登录
                //autoLogin("dd","111");
            }
            </g:else>
        });
        function autoLogin(userName,passWord){
            $("#logining").css("display","");
            $("#loginForm").css("display","none");
            $("#loginName").val(userName);
            $("#passWord").val(passWord);
            $("#loginForm").submit();
            //alert("submit");
        }
    </script>
</head>

<body>
<!--提示消息-->
%{--<g:set var="errors" value="测试一个错误"/>--}%
<g:render template="/layouts/staff_mobile_msgs_and_errors"></g:render>
<div class="panel panel-default">
    <div class="panel-heading">
        <h3 class="panel-title">工作人员登录</h3>
    </div>

    <div class="panel-body">
        <div id="logining" style="text-align: center;display: none">
            <img src="${resource(dir:"images",file:"loading.jpg")}"/>
            <label>登录中，请稍后...</label>
        </div>
        <g:form class="form-horizontal" method="post" action="staffLogin" name="loginForm">

            <div class="form-group">
                <label class="col-sm-2 control-label"><g:message code="staffInfo.loginName.label" default="Login Name"/>
                    <span class="required-indicator"></span></label>

                <div class="col-sm-10">
                    <g:textField class="form-control" name="loginName" maxlength="32" required=""
                                 value="${params.loginName}"/>
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label" for="passWord"><g:message code="staffInfo.passWord.label"
                                                                                default="Pass Word"/>
                    <span class="required-indicator"></span></label>

                <div class="col-sm-10">
                    <g:passwordField class="form-control" name="passWord" maxlength="128" required="" value=""/>
                </div>
            </div>

            <div class="form-group">
                <!-- Button -->
                <div class="col-sm-offset-2 col-sm-10">

                    <g:submitButton name="login"
                                    value="${message(code: 'default.button.login.label', default: 'login')}"
                                    class="form-control btn btn-primary"/>
                </div>
            </div>
        </g:form>

    </div>

</div>

</body>
</html>