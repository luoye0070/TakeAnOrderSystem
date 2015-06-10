<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 13-12-13
  Time: 上午4:20
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!--[if lt IE 9]>
    <g:javascript src="html5.js"/>
    <![endif]-->
    <!--script-->
    <!-- 新 Bootstrap 核心 CSS 文件 -->
    <link rel="stylesheet" href="${resource(dir: "js/bootstrap-3.3.4/css",file: "bootstrap.min.css")}">
    <!-- 可选的Bootstrap主题文件（一般不用引入） -->
    <link rel="stylesheet" href="${resource(dir: "js/bootstrap-3.3.4/css",file: "bootstrap-theme.min.css")}">
    %{--<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->--}%
    <script src="${resource(dir: "js",file: "jquery-1.11.3.min.js")}"></script>
    <!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
    <script src="${resource(dir: "js/bootstrap-3.3.4/js",file: "bootstrap.min.js")}"></script>
    <g:javascript src="JessicaWhite/js/jquery.scrollUp.min.js"/>

    <style type="text/css">
    body {
        padding-top: 70px;
        padding-bottom: 30px;
    }
    #scrollUp {
        bottom: 20px;
        right: 20px;
        height: 38px; /* Height of image */
        width: 38px; /* Width of image */
        background: url("${resource(dir:'js/JessicaWhite/img',file:'top.png')}") no-repeat;
    }
    </style>
    <script type="text/javascript">
        $(document).ready(function(){
            $.scrollUp({
                scrollName: 'scrollUp', // Element ID
                topDistance: '300', // Distance from top before showing element (px)
                topSpeed: 300, // Speed back to top (ms)
                animation: 'fade', // Fade, slide, none
                animationInSpeed: 200, // Animation in speed (ms)
                animationOutSpeed: 200, // Animation out speed (ms)
                scrollText: '', // Text for element
                activeOverlay: false  // Set CSS color to display scrollUp active point, e.g '#00FFFF'
            });
        });
    </script>
    <script type="text/javascript">
        <g:if test="${orderInfo}">
            function sendMsg(content){
                var tableName='${orderInfo?.tableInfo?.name}';
                var fullContent="来自【"+tableName+"】的消息："+content;
                var sendMsgUrl="${createLink(controller: "message",action: "sendMsgByCustomer")}";
                $.ajax({
                    context:this,
                    url:sendMsgUrl,
                    async:false,
                    type:'post',
                    //data:{'orderId':orderId,countName:counts,remarkName:foodIds,'remarks':remarks},
                    data:"content="+fullContent,
                    dataType: 'json',
                    success:function(data){
                        if(data.recode.code==0){
                            alert("消息已经成功发送!");
                        }
                    },
                    error:function(data){

                    }
                });
            }
        </g:if>
    </script>
    <g:layoutHead/>
    <r:layoutResources/>
</head>

<body role="document">
    <!--header-->
    <nav class="navbar navbar-inverse navbar-fixed-top">
    <div class="navbar-header">
        <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
        </button>
        <a class="navbar-brand" href="#">餐萌点菜系统-顾客端</a>
    </div>
    <div id="navbar" class="navbar-collapse collapse">
        <ul class="nav navbar-nav">
            <g:if test="${controllerName=="customer"}">
                <li class="active"><g:link controller="customer" action="getOrCreateOrder">用餐点菜</g:link></li>
            </g:if>
            <g:else>
                <li><g:link controller="customer" action="getOrCreateOrder">用餐点菜</g:link></li>
            </g:else>
            <li class="divider-vertical"></li>
            <g:if test="${controllerName=="reserveCustomer" && actionName in ["reserveOrderList","reserveOrderDetail"]}">
                <li class="active"><g:link controller="reserveCustomer" action="reserveOrderList">桌位预定列表</g:link></li>
            </g:if>
            <g:else>
                <li><g:link controller="reserveCustomer" action="reserveOrderList">桌位预定列表</g:link></li>
            </g:else>
            <li class="divider-vertical"></li>
            <g:if test="${controllerName=="reserveCustomer" && actionName in ["reserveDinnerTimeInput","reserveTables","createReserveOrder","dishOfReserveOrder"]}">
                <li class="active"><g:link controller="reserveCustomer" action="reserveDinnerTimeInput">桌位预定</g:link></li>
            </g:if>
            <g:else>
                <li><g:link controller="reserveCustomer" action="reserveDinnerTimeInput">桌位预定</g:link></li>
            </g:else>
            <li class="divider-vertical"></li>
            <g:if test="${controllerName=="customer"}">
            <li class="dropdown open">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="true">发送消息 <span class="caret"></span></a>
                <ul class="dropdown-menu" role="menu">
                    <li><a href="#" onclick="sendMsg('太口渴了，来点茶水吧！')">需要茶水</a></li>
                    <li><a href="#" onclick="sendMsg('我要点菜，来个服务员吧！')">要点菜</a></li>
                    <li><a href="#" onclick="sendMsg('快上菜，上菜啊！')">催上菜</a></li>
                    <li class="divider"></li>
                    <li><a href="#" onclick="sendMsg('服务员你过来一下，有事找你！')">叫服务员</a></li>
                </ul>
            </li>
            </g:if>
        </ul>
    </div><!--/.nav-collapse -->
    </nav>
    <!--//header-->

    <!--content-->
    <div class="container theme-showcase" role="main">
    <!--提示消息-->
    %{--<g:set var="errors" value="测试一个错误"/>--}%
    <g:render template="/layouts/staff_mobile_msgs_and_errors"></g:render>
    %{--<div class="panel panel-default">--}%
        %{--<div class="panel-body">--}%
            <g:layoutBody/>
        %{--</div>--}%
    %{--</div>--}%
    %{--</div>--}%

    <!--footer-->
    %{--<div class="m_footer">--}%

    %{--</div>--}%
    <!--//footer-->
</body>
</html>