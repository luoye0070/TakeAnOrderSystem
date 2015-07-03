<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 14-1-10
  Time: 下午9:39
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta name="layout" content="main1"/>
<title>打印桌位标贴</title>
<style type="text/css">
* {
    border: 0px;
    margin: 0px;
    padding: 0px;
}

.main {
    width: 1000px;
    height: auto;
    margin: 0px auto;
}

ul, li {
    list-style: none;
}

.main li {
    width: 1000px;
    height: 300px;
    margin: 9px 0px;
    border: 5px solid #CCCCFF;
    overflow: hidden;
    /*background-color: #f89406;*/
}

.ml_top {
    width: 1000px;
    height: 80px;
    overflow: hidden;
    /*background-color: #df8505;*/
}

.mlt_left {
    width: auto;
    max-width: 800px;
    height: 80px;
    overflow: hidden;
    /*background-color: #ccccff;*/
    float: left;
}

.mltl_top {
    width: auto;
    max-width: 760px;
    height: auto;
    max-height: 54px;
    margin: 10px 20px;
    line-height: 27px;
    font-size: 20px;
    font-weight: bolder;
    overflow: hidden;
    /*background-color: #a4c001;*/
    /*float: left;*/
}

.mltl_bottom {
    width: auto;
    max-width: 760px;
    height: auto;
    max-height: 26px;
    margin: 5px 20px;
    line-height: 26px;
    font-size: 20px;
    overflow: hidden;
    /*background-color: #b94a48;*/
    /*float: left;*/
}

.mlt_right {
    width: 200px;
    height: 120px;
    overflow: hidden;
    /*background-color: #e0362e;*/
    float: left;
}

.mlt_right img {
    width: 100px;
    height: 100px;
    margin: auto 40px;
}

.ml_bottom {
    width: 1000px;
    height: 260px;
    overflow: hidden;
    /*background-color: #a4c001;*/
}

.mlb_left {
    width: 500px;
    height: 260px;
    overflow: hidden;
    /*background-color: #b2d1ff;*/
    float: left;
}

.mlbl_top {
    width: 460px;
    height: 30px;
    line-height: 26px;
    font-size: 20px;
    overflow: hidden;
    margin: 5px 20px;
}
.mlbl_top label{
    width: 460px;
    height: 26px;
    line-height: 26px;
    font-size: 20px;
    margin: 0px;
    overflow: hidden;
}

.mlbl_bottom {
    width: 460px;
    height: 190px;
    line-height: 26px;
    font-size: 20px;
    overflow: hidden;
    margin: 5px 20px;
    word-wrap: break-word;
    word-break: normal;
}

.mlblb_item {
    width: 250px;
    height: 200px;
    overflow: hidden;
    float: left;
}

.mlblb_item label {
    width: 250px;
    height: 30px;
    line-height: 30px;
    font-size: 20px;
    text-align: center;
    overflow: hidden;
    float: left;
}

.mlblb_item img {
    width: 150px;
    height: 150px;
    margin: 10px 50px;
    overflow: hidden;
    float: left;
}

.mlb_right {
    width: 500px;
    height: 260px;
    overflow: hidden;
    /*background-color: #ccccff;*/
    float: left;
}

.mlbr_top {
    width: 460px;
    height: 30px;
    margin: 5px 20px;
    overflow: hidden;
    float: left;
    line-height: 30px;
    font-size: 20px;
}

.mlbr_bottom {
    width: 460px;
    height: 210px;
    margin: 5px 20px;
    overflow: hidden;
    float: left;
}

.mlbrb_left {
    width: 160px;
    height: 210px;
    overflow: hidden;
    float: left;
    line-height: 30px;
    font-size: 20px;
}

.mlbrb_right {
    width: 300px;
    height: 210px;
    overflow: hidden;
    float: left;
}

.mlbrb_right img {
    width: 160px;
    height: 160px;
    margin: 10px 35px;
    overflow: hidden;
    float: left;
}
</style>
%{--<g:javascript src="jquery.jqprint-0.3.js"/>--}%
<script type="text/javascript">
    $(function () {
        $("#printBtn").click(function () {
            //$.ajaxSetup({ async: false});
//            $("#printContent").jqprint({
//                // 如果是true则可以显示iframe查看效果（iframe默认高和宽都很小，可以再源码中调大），默认是false
//                debug: false,
//                // true表示引进原来的页面的css，默认是true。（如果是true，先会找$("link[media=print]")，若没有会去找$("link")中的css文件）
//                importCSS: true,
//                // 表示如果原来选择的对象必须被纳入打印（注意：设置为false可能会打破你的CSS规则）
//                printContainer: true,
//                // 表示如果插件也必须支持歌opera浏览器，在这种情况下，它提供了建立一个临时的打印选项卡。默认是true
//                operaSupport: true
//            });
            window.print();
        });
    });
</script>
</head>

<body>
<g:form action="printTable" style="margin-left: 100px;">
    <input type="text" name="max" value="${params.max}"/>
    <input type="submit" value="查询"/>
</g:form>

<div class="main" id="printContent">
    <g:if test="${tableList}">
        <ul>
            <g:each in="${tableList}" status="i" var="tableInfoInstance">
                <li>
                    <div class="ml_top">
                        <div class="mlt_left">
                            <div class="mltl_top">${restaurantInfo?.name}欢迎您</div>

                            <div class="mltl_bottom">
                                您所在的桌位是：${tableInfoInstance?.name}
                            </div>
                        </div>

                        %{--<div class="mlt_right">--}%
                            %{--<img src="${createLink(controller: "imageShow", action: "showQRCode",--}%
                                    %{--params: [str: createLink(controller: "infoShow", action: "shopShow", absolute: true, params: [id: restaurantInfo?.id])])}"--}%
                                 %{--alt=""/>--}%
                        %{--</div>--}%
                    </div>

                    <div class="ml_bottom">
                        <div class="mlb_left">
                            <div class="mlbl_top">
                                <label>在浏览器输入桌位地址来创建订单和点菜</label>
                                %{--<label>安卓下载：http://www.canmeng.com/appdown</label>--}%
                                %{--<label>苹果下载：http://www.canmeng.com/appdown</label>--}%
                            </div>

                            <div class="mlbl_bottom">
                                ${createLink(controller: "customer",action: "getOrCreateOrder",params: [code:tableInfoInstance?.code,mobile:"true"],absolute: true,base: baseUrl)}
                                %{--<div class="mlblb_item">--}%
                                    %{--<label>安卓下载</label>--}%
                                    %{--<img src="${createLink(controller: "imageShow", action: "showQRCode", params: [width: 150, str: "www.canmeng.com/shopShow/4"])}"--}%
                                         %{--alt=""/>--}%
                                %{--</div>--}%

                                %{--<div class="mlblb_item">--}%
                                    %{--<label>苹果下载</label>--}%
                                    %{--<img src="${createLink(controller: "imageShow", action: "showQRCode", params: [width: 150, str: "www.canmeng.com/shopShow/4"])}"--}%
                                         %{--alt=""/>--}%
                                %{--</div>--}%
                            </div>
                        </div>

                        <div class="mlb_right">
                            <div class="mlbr_top">扫描饭桌位地址二维码来创建订单和点菜</div>

                            <div class="mlbr_bottom">
                                %{--<div class="mlbrb_left">--}%
                                    %{--桌位地址：${createLink(controller: "customer",action: "getOrCreateOrder",params: [code:tableInfoInstance?.code,mobile:"true"],absolute: true,base: baseUrl)}--}%
                                %{--</div>--}%

                                <div class="mlbrb_right">
                                    %{--<img src="${createLink(controller: "imageShow", action: "showQRCode",--}%
                                            %{--params: [width: 230, str: "|" + tableInfoInstance?.id])}"--}%
                                         %{--alt=""/>--}%
                                    <taos:tableQRCode tableId="${tableInfoInstance.id}" />
                                </div>
                            </div>
                        </div>
                    </div>
                </li>
            </g:each>
        </ul>
    </g:if>
</div>
<taos:paginate total="${totalCount ?: 0}" prev="&larr;" next="&rarr;" params="${params}" max="12"/>
<a id="printBtn" href="#" class="btn btn-primary" style="float: right;margin: 0px 100px;">打印</a>
</body>
</html>