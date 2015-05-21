<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
    <meta name="layout" content="main_template"/>
    <title>菜谱检索</title>

    %{--<g:javascript src="shop/shopRegister.js"/>--}%
    %{--<g:javascript src="shop/shopSome.js"/>--}%
    <script type="text/javascript" src="${resource(dir:"js/common",file:"doDish.js")}"></script>
    <script type="text/javascript" src="${resource(dir:"js/common",file:"cart.js")}"></script>
    <link href="${resource(dir: "css",file: "cart.css")}" rel="stylesheet"/>
    <style type="text/css">

    .mc_main {
        width: 1100px;
        height: auto;
        margin: 0px auto;
        background-color: #f7f3e7;
    }

    .m_ssl {
        margin-top: 10px;
        margin-bottom: 10px;
    }

    .ms_field {
        width: 180px;
        float: left;
    }

    .ms_field_small {
        width: 80px;
        float: left;
    }

    .msf_label {
        width: 60px;
    }

    .msf_input {
        width: 120px;
    }

    .msf_input_small {
        width: 40px;
    }

    .m_list {
        width: 1100px;
        height: auto;
    }

    .ml_row_img {
        width: 120px;
        height: 100px;
        overflow: hidden;
        margin: 0px auto;
    }

    .ml_row_txt {
        width: 120px;
        height: 30px;
        line-height: 30px;
        margin: 0px auto;
        overflow: hidden;
    }
    </style>
    <script type="text/javascript">
        $(function () {
            //加载区域选择列表;
            var cityId = $("#cityId").val();
            //异步查询城市对应的区域
            var rand = Math.random();
            var url = $("#areaUrl").val() + "?cityId=" + cityId + "&rand=" + rand;
            $.get(url, function (data, status) {
                //alert("Data: " + data.length + "\nStatus: " + status);
                $("#areaId").html("");
                if (data) {
                    var areaId = $("#areaIdValue").val();
                    for (i = 0; i < data.length; i++) {
                        if (data[i].id == areaId)
                            $("#areaId").append("<option selected=selected value='" + data[i].id + "'>" + data[i].area + "</option>");
                        else
                            $("#areaId").append("<option value='" + data[i].id + "'>" + data[i].area + "</option>");
                    }
                }
            });

            //根据val设置被选择项
            var cuisineId = $("#cuisineIdValue").val();
            //alert("cuisineId"+cuisineId);
            $.each($("#cuisineId").children("option"), function (i, obj) {
                //alert($(obj).val());
                if ($(obj).val() == cuisineId) {
                    //alert($(obj).val());
                    $(obj).attr("selected", "selected");
                }
                else {
                    $(obj).removeAttr("selected");
                }
            });

            //注册点菜事件
            $("a[addToOrder]").doDish(
                    {
                        orderListUrl:"${createLink(controller: "customerAjax",action: "getOrdersByRestaurant")}",
                        doDishUrl:"${createLink(controller: "customerAjax",action: "addDishes")}"
                    }
            );

            //初始化餐车
            initCart({
                addToCartUrl: "${createLink(controller: "cartOfCustomerAjax",action: "addFoodToCart")}",
                cartsAndDishesUrl:"${createLink(controller: "cartOfCustomerAjax",action: "getCartsAndDishes")}",
                checkOutUrl:"${createLink(controller: "cartOfCustomer",action: "cartCheckout")}",
                imgUrl:"${createLink(controller: "imageShow", action: "downloadThumbnail", params: [width: 70,height: 70])}",
                delDishFromCartUrl:"${createLink(controller: "cartOfCustomerAjax",action: "delDish")}",
                updateDishOfCartUrl:"${createLink(controller: "cartOfCustomerAjax",action: "updateDish")}"
            });
            //注册加入购餐车事件
            $("a[addToCart]").addFoodToCart(
                    {
                        addToCartUrl: "${createLink(controller: "cartOfCustomerAjax",action: "addFoodToCart")}",
                        //cartListUrl: "${createLink(controller: "cartOfCustomerAjax",action: "getCarts")}",
                        //dishesListUrl:"${createLink(controller: "cartOfCustomerAjax",action: "getDishes")}"
                        cartsAndDishesUrl:"${createLink(controller: "cartOfCustomerAjax",action: "getCartsAndDishes")}",
                        checkOutUrl:"${createLink(controller: "cartOfCustomer",action: "cartCheckout")}",
                        imgUrl:"${createLink(controller: "imageShow", action: "downloadThumbnail", params: [width: 70,height: 70])}",
                        delDishFromCartUrl:"${createLink(controller: "cartOfCustomerAjax",action: "delDish")}",
                        updateDishOfCartUrl:"${createLink(controller: "cartOfCustomerAjax",action: "updateDish")}"
                    }
            );
        });
    </script>
</head>

<body>
<div class="mc_main">
    <g:render template="../layouts/msgs_and_errors"></g:render>
<!--搜索栏-->
    <div class="m_ssl">
        <!--搜索条件-->
        <g:form action="searchFood" method="post" class="well form-inline">
        <input type="hidden" name="showPlace" value="page"/>
            <!--地区条件-->
            <div class="ms_field">
                <label for="areaId">
                    区域：
                </label>
                <input type="hidden" name="cityId" id="cityId" value="${cityIdAndName?.cityId ?: "1"}">
                <input type="hidden" id="areaIdValue" value="${params?.areaId ?: "0"}">
                <select id="areaId" name="areaId" class="msf_input">
                    <option value="0">请选择</option>
                </select>
                <input type="hidden" id="areaUrl"
                       value="${createLink(controller: "areaParam", action: "getAreaList")}"/>
            </div>
            <!--菜系-->
            <div class="ms_field">
                <label for="cuisineId">
                    菜系：
                </label>
                <input type="hidden" id="cuisineIdValue" value="${params?.cuisineId ?: "0"}"/>
                <select id="cuisineId" name="cuisineId" class="msf_input">
                    <g:each in="${cuisineList}" var="cuisine" status="i">
                        <option value="${cuisine.id}">${cuisine.name}</option>
                    </g:each>
                </select>
            </div>

            <div class="ms_field">
                <label for="cuisineId">
                    价格：
                </label>
                <input name="priceLow" type="text" class="msf_input_small" value="${params?.priceLow}"/>-
                <input name="priceHigh" type="text" class="msf_input_small" value="${params?.priceHigh}"/>
            </div>
            %{--<!--是否支持外卖-->--}%
            %{--<div class="ms_field_small">--}%
                %{--<g:checkBox name="canTakeOut" value="${params?.canTakeOut}"/><label>&nbsp;&nbsp;外卖</label>--}%
            %{--</div>--}%
            <!--菜谱名称条件-->
            <div class="ms_field">
                <input name="keyWord" type="text" class="msf_input" style="width: 160px;"
                       placeholder="请输入菜名" value="${params?.keyWord}"/>
            </div>

            <div class="ms_field_small">
                <input type="submit" value="${message(code: 'default.button.search.label', default: 'Create')}"
                       class="btn send_btn"/>
            </div>
        </g:form>
    </div>


    <!--菜谱列表-->
    <div class="m_list">
        <g:if test="${foodList}">
            <ul class="thumbnails">
                <g:each in="${foodList}" status="i" var="foodInfoInstance">
                    <li class="span2" style="width: 158px">
                        <div class="thumbnail" style="background-color: #ffffff">
                            <!--图片-->
                            <div class="ml_row_img">
                                <img id="imageLabel" width="120"
                                     src="${createLink(controller: "imageShow", action: "downloadThumbnail", params: [imgUrl: foodInfoInstance?.image,width:120])}"/>
                            </div>

                            <div class="ml_row_txt">
                                <label id="nameLabel" style="float: left;font-size: 14px;overflow: hidden;height: 30px;line-height: 30px;">
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
                                <g:if test="${foodInfoInstance?.canTakeOut}">
                                    <a style="float: left;" href="#"
                                       addToCart="true"
                                       foodId="${foodInfoInstance?.id}">
                                        加入外卖餐车</a>
                                </g:if>
                                <a style="float: left;" href="#"
                                   addToOrder="true"
                                   restaurantId="${foodInfoInstance?.restaurantId}"
                                   foodId="${foodInfoInstance?.id}">
                                    加入订单</a>
                                <a style="float: right" href="#"
                                   onclick="foodAddToFavorite('${createLink(controller: "user",action: "addFavorite",params: [type:"food",foodId:foodInfoInstance?.id])}')">收藏</a>
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
</body>
</html>