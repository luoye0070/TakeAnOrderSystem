<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 13-11-9
  Time: 下午11:38
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="customer_mobile_template"/>
    <title>菜品详情</title>
    <style>
    </style>
</head>

<body>

<div class="panel panel-default">
    <div class="panel-heading">
        <h3 class="panel-title">
        <a href="${params.backUrl?:createLink(controller: "customer",action: "getOrCreateOrder")}">返回</a>
        </h3>
    </div>
    <div class="panel-body">
        <div class="col-sm-12" style="margin-top: 5px;margin-bottom: 5px;">
            <div  class="col-xs-8">
                ${foodInfo?.name}
            </div>
            <div  class="col-xs-4">
                <label id="priceLabel">￥${fieldValue(bean: foodInfo, field: 'price')}</label>
                <g:if test="${foodInfo?.originalPrice}">
                    &nbsp;&nbsp;
                    <label id="originalPriceLabel"
                           style="font-size:12px;text-decoration:line-through">￥${fieldValue(bean: foodInfo, field: 'originalPrice')}</label>
                </g:if>
            </div>
        </div>
        <div class="col-sm-12">
            <g:if test="${foodInfo.description}">
                ${foodInfo.description}
            </g:if>
            <g:else>
                <img  class="form-control"  style="height: auto;"
                     src="${createLink(controller: "imageShow", action: "downloadThumbnail", params: [imgUrl: foodInfo.image, width: 140, height: 120])}"/>
            </g:else>
        </div>
    </div>
</div>


%{--<div id="food_main">--}%
    %{--<g:render template="../layouts/msgs_and_errors"></g:render>--}%
    %{--<div id="food_view">--}%
        %{--<!--菜单信息-->--}%
        %{--<!--图片-->--}%
        %{--<div id="picArea">--}%
            %{--<img id="imageLabel"--}%
                 %{--src="${createLink(controller: "imageShow", action: "downloadThumbnail", params: [imgUrl: foodInfo.image, width: 300, height: 250])}--}%
                 %{--"/>--}%
        %{--</div>--}%

        %{--<div id="fooddecribe">--}%
            %{--<!--名称-->--}%
            %{--<div class="fieldcontain">--}%
                %{--<span id="foodname">${foodInfo?.name}</span>--}%
            %{--</div>--}%
            %{--<!--价格-->--}%
            %{--<div class="fieldcontain">--}%
                %{--<g:message code="foodInfo.price.label" default="Price"/>：--}%
                %{--<span class="priceLabel">￥${fieldValue(bean: foodInfo, field: 'price')}</span>--}%
            %{--</div>--}%
            %{--<g:if test="${fieldValue(bean: foodInfo, field: 'originalPrice')}">--}%
                %{--<!--原价-->--}%
                %{--<div class="fieldcontain">--}%
                    %{--<g:message code="foodInfo.originalPrice.label" default="Original Price"/>：--}%
                    %{--<span class="priceLabel"--}%
                          %{--id="old_priceLabel">￥${fieldValue(bean: foodInfo, field: 'originalPrice')}</span>--}%
                %{--</div>--}%
            %{--</g:if>--}%
            %{--<div class="fieldcontain">售出总量：${foodInfo?.totalSellCount}份</div>--}%

            %{--<div class="fieldcontain">我 要 点：<input type="text" value="1" style="width: 40px;"> 份（剩余量：${foodInfo?.countLimit-foodInfo?.sellCount}份）</div>--}%
            %{--<div class="fieldcontain">今日剩余：${foodInfo?.countLimit - foodInfo?.sellCount}份</div>--}%
            %{--<g:if test="${foodInfo?.canTakeOut}"><div id="btn_jrcc">--}%
                %{--<a addToCart="true"--}%
                   %{--foodId="${foodInfo?.id}"--}%
                   %{--href="#">加入餐车</a>--}%
            %{--</div></g:if>--}%

            %{--<div>--}%
                %{--<g:if test="${foodInfo?.countLimit - foodInfo?.sellCount}">--}%
                    %{--<a addToOrder="true"--}%
                       %{--foodId="${foodInfo?.id}"--}%
                       %{--href="#">加入预定订单</a>&nbsp;&nbsp;--}%
                %{--</g:if>--}%
                %{--<a href="#"--}%
                   %{--onclick="foodAddToFavorite('${createLink(controller: "user",action: "addFavorite",params: [type:"food",foodId:foodInfo?.id])}')">收藏</a>--}%
            %{--</div>--}%

            %{--<div>--}%
            %{--<a href="#"onclick="foodAddToFavorite('${createLink(controller: "user",action: "addFavorite",params: [type:"food",foodId:foodInfo?.id])}')">收藏</a>--}%
            %{--</div>--}%
        %{--</div>--}%
    %{--</div>--}%

    %{--<div id="food_detail">--}%
        %{--<!--详细信息-->--}%
        %{--<div id="detail_left">--}%
            %{--<div id="detail_left_title"></div>--}%

            %{--<div id="detail_left_main">--}%
                %{--<g:if test="${foodInfo.description}">--}%
                    %{--${foodInfo.description}--}%
                %{--</g:if>--}%
                %{--<g:else>--}%
                    %{--<img width="671"--}%
                         %{--src="${createLink(controller: "imageShow", action: "downloadThumbnail", params: [imgUrl: foodInfo.image, width: 671, height: 671])}--}%
                         %{--"/>--}%
                %{--</g:else>--}%
            %{--</div>--}%
        %{--</div>--}%

        %{--<div id="detail_right">--}%
            %{--<div class="detail_restour">--}%
                %{--<div class="detail_restour_title">${restaurantInfo?.name}</div>--}%

                %{--<div class="detail_restour_main">--}%
                    %{--<g:message code="restaurantInfo.image.label" default="image"/>--}%
                    %{--<g:if test="${restaurantInfo?.image}">--}%
                        %{--<img id="imageLabel"--}%
                             %{--src="${createLink(controller: "imageShow", action: "downloadThumbnail", params: [imgUrl: restaurantInfo?.image])}"/>--}%
                    %{--</g:if>--}%
                %{--<!--店铺名称-->--}%
                    %{--<div>--}%
                        %{--<g:message code="restaurantInfo.name.label" default="name"/>--}%
                        %{--${restaurantInfo?.name}--}%
                    %{--</div>--}%
                    %{--<!--店铺地址-->--}%
                    %{--<div>--}%
                        %{--<g:message code="restaurantInfo.street.label" default="Street"/>--}%
                        %{--${restaurantInfo?.address}--}%
                    %{--</div>--}%

                    %{--<div>--}%
                        %{--<a href="${createLink(controller: "infoShow", action: "shopShow")}">进入饭店</a>--}%
                    %{--</div>--}%
                %{--</div>--}%
            %{--</div>--}%

            %{--<div class="detail_restour" id="detail_other_food">--}%
                %{--<div class="detail_restour_title">本店还有哪些美食<span id="wenhao">？</span></div>--}%

                %{--<div class="detail_restour_main">--}%
                    %{--<g:if test="${foodList}">--}%
                        %{--<ul>--}%
                            %{--<g:each in="${foodList}" var="foodInfo">--}%
                                %{--<li><a href="${createLink(controller: "infoShow", action: "foodShow", params: [id: foodInfo.id])}">${foodInfo.name}</a>--}%
                                %{--</li>--}%
                            %{--</g:each>--}%
                        %{--</ul>--}%
                    %{--</g:if>--}%
                %{--</div>--}%
            %{--</div>--}%
        %{--</div>--}%
    %{--</div>--}%
%{--</div>--}%


<!--购物车浮动模块-->
%{--<div class="cart" id="cart">--}%
    %{--<div class="head">--}%
        %{--<div class="jsmenu">--}%
            %{--<div class="shopping-amount">--}%
                %{--<div class="sa-left"></div>--}%
                %{--<div class="sa-right">10</div>--}%
            %{--</div>--}%
            %{--<div class="jsmenu-real">--}%
                %{--<div class="jsr-text"><a href="#">去餐车结算</a></div><div class="b"></div>--}%
            %{--</div>--}%
        %{--</div>--}%
    %{--</div>--}%
    %{--<div class="content">--}%
        %{--<div class="prompt" style="display: none" id="cart_nothing">--}%
            %{--<div class="nogoods"><b></b>餐车中还没有商品，赶紧选购吧！</div>--}%
        %{--</div>--}%
        %{--<div class="outerList" id="cart_list">--}%
            %{--<ul>--}%
                %{--<li>--}%
                   %{--<div class="top">--}%
                       %{--<div class="title">--}%
                           %{--落叶的测试饭店--}%
                       %{--</div>--}%
                       %{--<div class="subtotal">--}%
                           %{--小计：￥124.98--}%
                       %{--</div>--}%
                   %{--</div>--}%
                    %{--<div class="innerList">--}%
                        %{--<ul>--}%
                            %{--<li>--}%
                               %{--<div class="img">--}%
                                   %{--<img src="${createLink(controller: "imageShow", action: "downloadThumbnail", params: [imgUrl: foodInfo?.image,width: 70,height: 70])}"/>--}%
                               %{--</div>--}%
                                %{--<div class="detail">--}%
                                    %{--<div class="dtop">--}%
                                        %{--<div class="dtlable">鱼香肉丝</div>--}%
                                        %{--<div class="dtclosebt"><input type="button" value="X"/></div>--}%
                                    %{--</div>--}%
                                    %{--<div class="dbottom">--}%
                                        %{--<div class="dbleft">￥12.09</div>--}%
                                        %{--<div class="dbright">--}%
                                            %{--<input type="button" value="─"/>--}%
                                            %{--<input type="text" value="2"/>--}%
                                            %{--<input type="button" value="+">--}%
                                        %{--</div>--}%
                                    %{--</div>--}%
                                %{--</div>--}%
                            %{--</li>--}%
                            %{--<li>--}%
                                %{--<div class="img">--}%
                                    %{--<img src="${createLink(controller: "imageShow", action: "downloadThumbnail", params: [imgUrl: foodInfo?.image,width: 70,height: 70])}"/>--}%
                                %{--</div>--}%
                                %{--<div class="detail">--}%
                                    %{--<div class="dtop">--}%
                                        %{--<div class="dtlable">鱼香肉丝</div>--}%
                                        %{--<div class="dtclosebt"><input type="button" value="X"/></div>--}%
                                    %{--</div>--}%
                                    %{--<div class="dbottom">--}%
                                        %{--<div class="dbleft">￥12.09</div>--}%
                                        %{--<div class="dbright">--}%
                                            %{--<input type="button" value="─"/>--}%
                                            %{--<input type="text" value="2"/>--}%
                                            %{--<input type="button" value="+">--}%
                                        %{--</div>--}%
                                    %{--</div>--}%
                                %{--</div>--}%
                            %{--</li>--}%
                            %{--<li>--}%
                                %{--<div class="img">--}%
                                    %{--<img src="${createLink(controller: "imageShow", action: "downloadThumbnail", params: [imgUrl: foodInfo?.image,width: 70,height: 70])}"/>--}%
                                %{--</div>--}%
                                %{--<div class="detail">--}%
                                    %{--<div class="dtop">--}%
                                        %{--<div class="dtlable">鱼香肉丝</div>--}%
                                        %{--<div class="dtclosebt"><input type="button" value="X"/></div>--}%
                                    %{--</div>--}%
                                    %{--<div class="dbottom">--}%
                                        %{--<div class="dbleft">￥12.09</div>--}%
                                        %{--<div class="dbright">--}%
                                            %{--<input type="button" value="─"/>--}%
                                            %{--<input type="text" value="2"/>--}%
                                            %{--<input type="button" value="+">--}%
                                        %{--</div>--}%
                                    %{--</div>--}%
                                %{--</div>--}%
                            %{--</li>--}%
                        %{--</ul>--}%
                    %{--</div>--}%
                %{--</li>--}%
                %{--<li>--}%
                    %{--<div class="top">--}%
                        %{--<div class="title">--}%
                            %{--落叶的测试饭店--}%
                        %{--</div>--}%
                        %{--<div class="subtotal">--}%
                            %{--小计：￥124.98--}%
                        %{--</div>--}%
                    %{--</div>--}%
                    %{--<div class="innerList">--}%
                        %{--<ul>--}%
                            %{--<li>--}%
                                %{--<div class="img">--}%
                                    %{--<img src="${createLink(controller: "imageShow", action: "downloadThumbnail", params: [imgUrl: foodInfo?.image,width: 70,height: 70])}"/>--}%
                                %{--</div>--}%
                                %{--<div class="detail">--}%
                                    %{--<div class="dtop">--}%
                                        %{--<div class="dtlable">鱼香肉丝</div>--}%
                                        %{--<div class="dtclosebt"><input type="button" value="X"/></div>--}%
                                    %{--</div>--}%
                                    %{--<div class="dbottom">--}%
                                        %{--<div class="dbleft">￥12.09</div>--}%
                                        %{--<div class="dbright">--}%
                                            %{--<input type="button" value="─"/>--}%
                                            %{--<input type="text" value="2"/>--}%
                                            %{--<input type="button" value="+">--}%
                                        %{--</div>--}%
                                    %{--</div>--}%
                                %{--</div>--}%
                            %{--</li>--}%
                            %{--<li>--}%
                                %{--<div class="img">--}%
                                    %{--<img src="${createLink(controller: "imageShow", action: "downloadThumbnail", params: [imgUrl: foodInfo?.image,width: 70,height: 70])}"/>--}%
                                %{--</div>--}%
                                %{--<div class="detail">--}%
                                    %{--<div class="dtop">--}%
                                        %{--<div class="dtlable">鱼香肉丝</div>--}%
                                        %{--<div class="dtclosebt"><input type="button" value="X"/></div>--}%
                                    %{--</div>--}%
                                    %{--<div class="dbottom">--}%
                                        %{--<div class="dbleft">￥12.09</div>--}%
                                        %{--<div class="dbright">--}%
                                            %{--<input type="button" value="─"/>--}%
                                            %{--<input type="text" value="2"/>--}%
                                            %{--<input type="button" value="+">--}%
                                        %{--</div>--}%
                                    %{--</div>--}%
                                %{--</div>--}%
                            %{--</li>--}%
                            %{--<li>--}%
                                %{--<div class="img">--}%
                                    %{--<img src="${createLink(controller: "imageShow", action: "downloadThumbnail", params: [imgUrl: foodInfo?.image,width: 70,height: 70])}"/>--}%
                                %{--</div>--}%
                                %{--<div class="detail">--}%
                                    %{--<div class="dtop">--}%
                                        %{--<div class="dtlable">鱼香肉丝</div>--}%
                                        %{--<div class="dtclosebt"><input type="button" value="X"/></div>--}%
                                    %{--</div>--}%
                                    %{--<div class="dbottom">--}%
                                        %{--<div class="dbleft">￥12.09</div>--}%
                                        %{--<div class="dbright">--}%
                                            %{--<input type="button" value="─"/>--}%
                                            %{--<input type="text" value="2"/>--}%
                                            %{--<input type="button" value="+">--}%
                                        %{--</div>--}%
                                    %{--</div>--}%
                                %{--</div>--}%
                            %{--</li>--}%
                        %{--</ul>--}%
                    %{--</div>--}%
                %{--</li>--}%
            %{--</ul>--}%
            %{--<div class="totalInfo" id="cart_list_total">--}%
                %{--总计：￥123.98--}%
            %{--</div>--}%
        %{--</div>--}%

    %{--</div>--}%
%{--</div>--}%


</body>
</html>