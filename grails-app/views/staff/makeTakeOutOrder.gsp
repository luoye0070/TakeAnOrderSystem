<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main_template">
    <title>外卖界面</title>
    <link rel="stylesheet" href="${resource(dir: "js/timePicker", file: "timePicker.css")}" type="text/css"
          media="screen"/>
    <script type="text/javascript" src="${resource(dir: "js/timePicker", file: "jquery.timePicker.min.js")}"></script>
    <link rel="stylesheet" href="${resource(dir: "js/Datepicker", file: "datepicker.css")}" type="text/css"
          media="screen"/>
    <script type="text/javascript" src="${resource(dir: "js/Datepicker", file: "bootstrap-datepicker.js")}"></script>

    <script type="text/javascript" src="${resource(dir: "js/common", file: "cart.js")}"></script>
    <link href="${resource(dir: "css", file: "cart_page.css")}" rel="stylesheet"/>
    <script type="text/javascript">
        $(function () {
            //初始化餐车
            initCart({
                addToCartUrl: "${createLink(controller: "staffAjax",action: "addFoodToCart")}",
                cartsAndDishesUrl: "${createLink(controller: "staffAjax",action: "getCartsAndDishes")}",
                imgUrl: "${createLink(controller: "imageShow", action: "downloadThumbnail", params: [width: 70,height: 70])}",
                delDishFromCartUrl: "${createLink(controller: "staffAjax",action: "delDishFromCart")}",
                updateDishOfCartUrl: "${createLink(controller: "staffAjax",action: "updateDishOfCart")}",
                parentTag: "#checkout_carts",
                headDisplay: "none"
            });
            //注册加入购餐车事件
            $("a[addToCart]").addFoodToCart(
                    {
                        addToCartUrl: "${createLink(controller: "staffAjax",action: "addFoodToCart")}",
                        cartsAndDishesUrl:"${createLink(controller: "staffAjax",action: "getCartsAndDishes")}",
                        imgUrl:"${createLink(controller: "imageShow", action: "downloadThumbnail", params: [width: 70,height: 70])}",
                        delDishFromCartUrl:"${createLink(controller: "staffAjax",action: "delDishFromCart")}",
                        updateDishOfCartUrl:"${createLink(controller: "staffAjax",action: "updateDishOfCart")}",
                        parentTag: "#checkout_carts",
                        headDisplay: "none"
                    }
            );
            //日期选择器
            $("#date").datepicker({format: "yyyy-mm-dd"});
            //时间选择器
            $("#time").timePicker({step: 15});
            $("#time").change(function () {
                var timeV = $("#time").val();
                if (timeV.length > 0) {
                    $("#time").val(timeV + ":00");
                }
            });
        });
    </script>
    <g:javascript src="common/address.js"/>
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
        height: auto;
        margin: 20px;
        margin-bottom: 0px;
        /*border-bottom: 4px solid #FF9833;
        text-indent: 1em;
        line-height: 80px;
        font-size: 20px;
        font-weight: bolder;*/
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

    .address, .option, .operation,.titleLabel {
        width: 840px;
        margin: 0px 40px;
        height: auto;
        /*background:#066;*/
        display: block;
        float: left;
    }

    .operation {
        text-align: right;
    }

    .operation input {
        width: 100px;
    }

    .address table {
        width: 880px;
    }

    .control-group {
        width: 328px;
        MARGIN: 0px 45px;
        margin-bottom: 10px;
        height: auto;
        float: left;
    }
    </style>
    <script type="text/javascript">
        function setSelectText(element, text) {

            var count = $("#" + element + " option").length;

            for (var i = 0; i < count; i++) {
                if ($("#" + element).get(0).options[i].text == text) {
                    $("#" + element).get(0).options[i].selected = true;
                    $("#" + element).change();
                    break;
                }
            }
        }

        $(function(){
            if("${params.province?:""}"!=""){
                setSelectText("province", "${params.province?:""}");
                setTimeout(function(){
                    setSelectText("city", "${params.city?:""}");
                    setTimeout(function(){
                        setSelectText("area", "${params.area?:""}");
                    },500);
                },500);
            }
            $("#linkManName,#phone").blur(function(){
                var linkManName=$("#linkManName").val();
                var phone=$("#phone").val();
                $.getJSON("${createLink(controller: "staffAjax",action: "getAddress")}",{linkManName:linkManName,phone:phone},function(data){
                    if(data.recode.code==0){
                        //alert(data.addressInfo.id);
                        var addressInfo=data.addressInfo;
                        setSelectText("province", addressInfo.province);
                        setTimeout(function(){
                            setSelectText("city", addressInfo.city);
                            setTimeout(function(){
                                setSelectText("area", addressInfo.area);
                            },500);
                        },500);
                        $("#linkManName").val(addressInfo.linkManName);
                        $("#phone").val(addressInfo.phone);
                        $("#street").val(addressInfo.street);
                    }
                });
            });
        });
    </script>
</head>

<body>

<div class="mc_main">
    <div class="mcm_top">
        <g:render template="../layouts/staffMenu"></g:render>
    </div>

    <div class="mcm_content">

        <g:render template="../layouts/msgs_and_errors"></g:render>

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
                                    <g:if test="${foodInfoInstance?.canTakeOut}">
                                    <a style="float: left;" href="#" addToCart="true"
                                    restaurantId="${foodInfoInstance?.restaurantId}"
                                    foodId="${foodInfoInstance?.id}">
                                    加入外卖餐车</a>
                                    </g:if>
                                    %{--<a href="${createLink(controller: "staff", action: "addDishes", params: [orderId: orderInfoInstance?.id, foodIds: foodInfoInstance?.id, counts: 1, partakeCode: orderInfoInstance?.partakeCode])}"--}%
                                       %{--class="">点一个</a>--}%
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

        <!--购餐车-->
        <div id="checkout_carts">

        </div>
        <!--订单信息-->
        <div class="mcmc_ssl">
            <form style="float: left;" class="well form-inline"
                  action="${createLink(controller: "staff", action: "makeTakeOutOrder")}" method="post">

                <div class="titleLabel">送餐时间：</div>
                <div class="option">
                    <div class="control-group">
                        <label class="control-label">用餐日期</label>

                        <div class="controls">
                            <input class="input-xlarge focused" type="text" name="date" id="date"
                                   value="${params.date ?: lj.FormatUtil.dateFormat(new Date())}"/>
                        </div>
                    </div>

                    %{--<div class="control-group">--}%
                        %{--<label class="control-label">用餐类型</label>--}%

                        %{--<div class="controls">--}%
                            %{--<select name="reserveType" class="mcmcsf_input">--}%
                                %{--<g:each in="${lj.enumCustom.ReserveType.reserveTypes}">--}%
                                    %{--<option value="${it.code}" ${(lj.Number.toInteger(params.reserveType) == it.code) ? "selected='selected'" : ""}>${it.label}</option>--}%
                                %{--</g:each>--}%
                            %{--</select>--}%
                        %{--</div>--}%
                    %{--</div>--}%

                    <div class="control-group">
                        <label class="control-label">希望送到时间</label>

                        <div class="controls">
                            <input class="input-xlarge focused" type="text" name="time" id="time"
                                   value="${params.time ?: lj.FormatUtil.timeFormat(new Date())}"/>
                        </div>
                    </div>

                    %{--<div class="control-group">--}%
                        %{--<label class="control-label">联系人</label>--}%

                        %{--<div class="controls">--}%
                            %{--<input class="input-xlarge focused" type="text" name="customerName"--}%
                                   %{--value="${params.customerName ?: ""}"/>--}%
                        %{--</div>--}%
                    %{--</div>--}%

                    %{--<div class="control-group">--}%
                        %{--<label class="control-label">联系电话</label>--}%

                        %{--<div class="controls">--}%
                            %{--<input class="input-xlarge focused" type="text" name="phone" value="${params.phone ?: ""}"/>--}%
                        %{--</div>--}%
                    %{--</div>--}%

                </div>

                <div class="titleLabel">送餐地址：</div>
                <div class="address">

                    <div class="control-group">
                        <label class="control-label" for="linkManName">联系人</label>

                        <div class="controls">
                            <input type="text" class="input-xlarge" id="linkManName" name="linkManName"
                                   value="${params.linkManName ?: ""}"/>
                        </div>
                    </div>

                    <div class="control-group">
                        <label class="control-label" for="phone">电话号码</label>

                        <div class="controls">
                            <input type="text" class="input-xlarge" id="phone" name="phone"
                                   value="${params.phone ?: ""}"/>
                        </div>
                    </div>
                    <input type="hidden" id="cityUrl"
                           value="${createLink(controller: "areaParam", action: "getCityList")}"/>
                    <input type="hidden" id="areaUrl"
                           value="${createLink(controller: "areaParam", action: "getAreaList")}"/>

                    <div class="control-group">
                        <label class="control-label" for="province">所在地区</label>

                        <div class="controls">
                            省
                            <select id="province" name="province">
                                <option value="">请选择</option>
                                <g:each in="${provinces}">
                                    <option value="${it.id}">${it.province}</option>
                                </g:each>
                            </select><br/>
                            市
                            <select id="city" name="city">
                                <option value="">请选择</option>
                            </select><br/>
                            区
                            <select id="area" name="area">
                                <option value="">请选择</option>
                            </select>
                        </div>
                    </div>

                    <div class="control-group">
                        <label class="control-label" for="street">街道地址</label>

                        <div class="controls">
                            <textarea class="input-xlarge" id="street" name="street" rows="1"
                                      placeholder="为了您的方便，请填写详细地址">
                                ${params.street ?: ""}
                                      </textarea>
                        </div>
                    </div>
                </div>

                <div class="operation">
                    <input type="submit"
                           value="${message(code: 'default.button.submit.label', default: 'submit')}"
                           class="btn btn-primary"/>
                </div>
            </form>
        </div>

    </div>
</div>
</body>
</html>