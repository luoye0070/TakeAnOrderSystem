<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main_template">
    <title>点菜界面</title>
    <style type="text/css">
    .mc_main {
        width: 1000px;
        height: auto;
        margin: 0px 50px;
        background-color: #FFFFFF;
        float: left;
    }

    .mcm_top {
        width: 960px;
        height: 80px;
        margin: 20px;
        margin-top: 0px;
        border-bottom: 4px solid #FF9833;
        text-indent: 1em;
        line-height: 80px;
        font-size: 20px;
        font-weight: bolder;
    }

    .mcm_content {
        width: 960px;
        height: auto;
        margin: 20px;
    }

    .mcmc_ssl {
        width: 960px;
        margin-top: 10px;
        margin-bottom: 10px;
        font-size: 14px;
    }

    .mcmcs_field {
        width: 320px;
        float: left;
    }

    .mcmcsf_input {
        width: 120px;
    }

    .mcmcs_field_middle {
        width: 160px;
        float: left;
    }

    .mcmcsf_input_middle {
        width: 80px;
    }

    .mcmcs_field_small {
        width: 80px;
        float: left;
    }

    .mcmcsf_input_small {
        width: 40px;
    }

    .m_list {
        width: 960px;
        height: auto;
    }

    .m_list li {
        width: 182px;
        margin: 5px;
    }

    .ml_row_img {
        width: 140px;
        height: 120px;
        overflow: hidden;
        margin: 0px auto;
    }

    .ml_row_txt {
        width: 140px;
        height: 30px;
        line-height: 30px;
        margin: 0px auto;
        overflow: hidden;
    }
    </style>
</head>

<body>

<div class="mc_main">
    <div class="mcm_top">
        订单${params.orderId}-点菜
    </div>

    <div class="mcm_content">

        <g:render template="../layouts/msgs_and_errors"></g:render>
    <!--订单信息-->
        <div class="mcmc_ssl">
            <form class="well form-inline">
                <g:if test="${orderInfoInstance}">
                    <g:if test="${orderInfoInstance?.id}">
                        <g:message code="orderInfo.id.label" default="id"/> :<g:fieldValue bean="${orderInfoInstance}"
                                                                                           field="id"/>
                    </g:if>

                    &nbsp;&nbsp;
                    <g:if test="${orderInfoInstance?.restaurantId}">
                        <g:message code="orderInfo.restaurantId.label"
                                   default="Restaurant Id"/>:<g:fieldValue
                            bean="${orderInfoInstance}" field="restaurantId"/>
                    </g:if>
                    &nbsp;&nbsp;
                    <g:if test="${orderInfoInstance?.tableId}">
                        <g:message code="orderInfo.tableId.label"
                                   default="Table Id"/>:<g:fieldValue
                            bean="${orderInfoInstance}"
                            field="tableId"/>
                    </g:if>
                    &nbsp;&nbsp;
                    <g:if test="${orderInfoInstance?.waiterId}">
                        <g:message code="orderInfo.waiterId.label"
                                   default="Waiter Id"/>:<g:fieldValue
                            bean="${orderInfoInstance}"
                            field="waiterId"/>
                    </g:if>
                    &nbsp;&nbsp;
                    <g:if test="${orderInfoInstance?.partakeCode}">
                        <g:message code="orderInfo.partakeCode.label"
                                   default="Partake Code"/>：<g:fieldValue
                            bean="${orderInfoInstance}" field="partakeCode"/>
                    </g:if>
                    &nbsp;&nbsp;
                </g:if>

                <a href="${params.backUrl ?: createLink(controller: "staff", action: "orderList")}"
                   class="btn btn-link">返回</a>
            </form>
        </div>


        <!--菜谱列表-->
        <!--菜谱列表-->
        <div class="m_list">
            <g:if test="${foodList}">
                <ul class="thumbnails" style="margin: 0px auto;">
                    <g:each in="${foodList}" status="i" var="foodInfoInstance">
                        <li>
                            <div class="thumbnail" style="background-color: #ffffff">
                                <!--图片-->
                                <div class="ml_row_img">
                                    <img id="imageLabel" width="120"
                                         src="${createLink(controller: "imageShow", action: "downloadThumbnail", params: [imgUrl: foodInfoInstance?.image, width: 140, height: 120])}"/>
                                </div>

                                <div class="ml_row_txt">
                                    <label id="nameLabel"
                                           style="float: left;font-size: 14px;overflow: hidden;height: 30px;line-height: 30px;">
                                        <a target="_parent" title="${foodInfoInstance?.name}"
                                           href="${createLink(controller: "infoShow", action: "foodShow", params: [id: foodInfoInstance.id])}">${foodInfoInstance?.name}</a>
                                    </label>
                                </div>

                                <div class="ml_row_txt">
                                    <label id="priceLabel"
                                           style="float: left;font-size: 14px;">￥${fieldValue(bean: foodInfoInstance, field: 'price')}</label>
                                    <g:if test="${foodInfoInstance?.originalPrice}">
                                        <label id="originalPriceLabel"
                                               style="float: right;font-size:12px;text-decoration:line-through">￥${fieldValue(bean: foodInfoInstance, field: 'originalPrice')}</label>
                                    </g:if>
                                </div>

                                <div class="ml_row_txt">
                                    %{--<g:if test="${foodInfoInstance?.canTakeOut}">--}%
                                    %{--<a style="float: left;" href="#"--}%
                                    %{--restaurantId="${foodInfoInstance?.restaurantId}"--}%
                                    %{--foodId="${foodInfoInstance?.id}">--}%
                                    %{--加入外卖餐车</a>--}%
                                    %{--</g:if>--}%
                                    <a href="${createLink(controller: "staff", action: "addDishes", params: [orderId: orderInfoInstance?.id, foodIds: foodInfoInstance?.id, counts: 1, partakeCode: orderInfoInstance?.partakeCode])}"
                                       class="">点一个</a>
                                    %{--<a style="float: left;" href="#"--}%
                                    %{--addToOrder="true"--}%
                                    %{--restaurantId="${foodInfoInstance?.restaurantId}"--}%
                                    %{--foodId="${foodInfoInstance?.id}">--}%
                                    %{--加入订单</a>--}%
                                    %{--<a style="float: right" href="#"--}%
                                    %{--onclick="foodAddToFavorite('${createLink(controller: "user",action: "addFavorite",params: [type:"food",foodId:foodInfoInstance?.id])}')">收藏</a>--}%
                                </div>
                            </div>
                        </li>
                    </g:each>
                </ul>
                <white:paginate total="${totalCount ?: 0}" prev="&larr;" next="&rarr;" params="${params}"/>
            </g:if>
            <g:else>
                <div style="margin: 0px auto;">
                    <label style="text-align: center">没有搜索到记录</label>
                </div>
            </g:else>
        </div>
    </div>
</div>
</body>
</html>