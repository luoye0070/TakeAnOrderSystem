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

    <link rel="stylesheet" href="${resource(dir: 'js/JessicaWhite/css', file: 'bootstrap.css')}">
    <link rel="stylesheet" href="${resource(dir: 'js/JessicaWhite/css', file: 'theme.css')}">
    <link rel="stylesheet" href="${resource(dir: 'js/JessicaWhite/css/skins/tango/', file: 'skin.css')}">
    <link rel="stylesheet" href="${resource(dir: 'js/JessicaWhite/css', file: 'bootstrap-responsive.css')}">
    <!--[if lt IE 9]>
    <g:javascript src="html5.js"/>
    <![endif]-->
    <!--script-->
    <g:javascript src="jquery-1.8.2.min.js"/>
    <g:javascript src="JessicaWhite/js/jquery.easing.1.3.js"/>
    <g:javascript src="JessicaWhite/js/jquery.mobile.customized.min.js"/>
    <g:javascript src="JessicaWhite/js/bootstrap.js"/>
    <g:javascript src="JessicaWhite/js/jquery.scrollUp.min.js"/>

    <style type="text/css">
    * {
        margin: 0px;
        border: 0px;
        padding: 0px;
    }

    ul, li {
        list-style: none;
        padding: 0px;
        margin: 0px;
    }
     body{
         background: #bfbfbf;
     }
    /*a:link{*/
        /*color: #96C76B;*/
    /*}*/
    /*a:active{*/

    /*}*/
    /*a:visited{*/

    /*}*/
    /*a:hover{*/
        /*color: #D00B01;*/
    /*}*/

    .main {
        width: 1100px;
        height: auto;
        margin: 0px auto;
        background: #f7f3e7;
    }

    .m_head {
        width: 1100px;
        height: 50px;
        /*height: auto;*/
        margin: 0px auto;
        background: url('${resource(dir:"images",file:"main_head.png")}');
    }

    .mh_menu {
        width: 1100px;
        height: 50px;
        float: left;
        line-height: 50px;
        background: #000000;
        background-repeat: repeat;
        FILTER: Alpha(opacity=60); /*IE下半透明*/
        -moz-opacity: 0.6; /*FF下半透明*/
        opacity: 0.6; /* 支撑CSS3的阅读器（FF 1.5也支持）透明度20%*/
    }

    .mhm_city {
        width: 200px;
        height: 50px;
        float: left;
    }

    .mhm_city label {
        line-height: 50px;
        font-size: 20px;
        margin-left: 20px;
        margin-right: 5px;
        float: left;
        color: #ffffff;
        font-weight: 800;
    }

    .mhm_city a {
        float: left;
    }

    .mh_menu ul, .mh_menu li {
        float: left;
    }

    .mh_menu li {
        height: 50px;
        line-height: 50px;
        margin: 0px 5px 0px 5px;
        color: #ffffff;
        font-family: 'Open Sans', sans-serif;
        font-weight: 800;
        font-size: 14px;
    }

    .mh_menu .current a {
        color: #c5c5c5;
    }

    .mh_menu li a:hover {
        color: #c5c5c5;
        text-decoration: none;
    }

    .m_content{
        width: 1100px;
        height: auto;
        background-color: #f7f3e7;
        overflow: hidden;
        float: left;
    }
    .m_footer{
        width: 1100px;
        height: auto;
        background-color: #000;
        overflow: hidden;
        float: left;
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

    <g:layoutHead/>
    <r:layoutResources/>
</head>

<body>
<g:render template="/layouts/top"></g:render>
<div class="main">
    <!--header-->
    <div class="m_head">
        <div class="mh_menu">
            <div class="mhm_city">
                <label>${cityIdAndName?.cityName?:"乌鲁木齐"}</label>
                <a href="${createLink(controller: "citySelect",action: "cityList",params: params<<[co:controllerName,ac:actionName])}">切换城市</a>
            </div>
            <ul style="float: right;margin-right: 20px;">
                <% String url=request.getRequestURL();
                url=url.substring(url.lastIndexOf("/")+1);
                %>
                <li ${url==""?'class="current"':""}><g:link url="/canmeng"> 餐萌首页</g:link></li>
                <li  ${controllerName=="user"?'class="current"':""}><g:link controller="user" action="viewUserInfo">我的餐萌</g:link></li>
                <li  ${controllerName=="customer"?'class="current"':""}><g:link controller="customer" action="orderList">我的订单</g:link></li>
                <li  ${controllerName=="shop"?'class="current"':""}><g:link controller="shop" action="shopInfo">我的饭店</g:link></li>
                <li  ${url=="c"?'class="current"':""}><a href="about.html">联系餐萌</a></li>
                <li ${(controllerName=="staff"||(controllerName=="staffManage"&&actionName=="staffLogin"))?"class='current'":""}><g:link controller="staff" action="index">工作人员入口</g:link></li>
                <g:if test="${session && (session.clientId||session.staffInfo)}">
                    <li><g:link controller="user" action="logout">退出餐萌</g:link></li>
                </g:if>
                <g:else>
                    <li><g:link controller="user" action="login">登录餐萌</g:link></li>
                </g:else>
            </ul>
        </div>
        %{--<div class="mh_center"></div>--}%
        %{--<div class="mh_tab">--}%
            %{--<ul>--}%
                %{--<li id="foodListTab" class="mht_selected">美食</li>--}%
                %{--<li id="resListTab" class="mht_unselected">餐厅</li>--}%
            %{--</ul>--}%
        %{--</div>--}%
    </div>
    <!--//header-->

    <!--content-->
    <div class="m_content">
    <g:layoutBody/>
    </div>

    <!--footer-->
    <div class="m_footer">

            <div class="wrap" style="margin: 10px 20px;">
                <div class="container">
                    <div class="row">
                        <div class="span5">
                            <div class="foot_logo"><g:img dir="js/JessicaWhite/img" file="foot_logo.png" /></div>
                            <div class="copyright">&copy; 2020 餐萌版权所有</div>
                        </div>

                    </div>
                </div>
            </div>

    </div>
    <!--//footer-->
 </div>
</body>
</html>