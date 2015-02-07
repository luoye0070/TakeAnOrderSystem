<%@ page import="lj.FormatUtil" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main_template"/>
    <title>店铺信息</title>

    %{--<g:javascript src="jquery-1.8.2.min.js"></g:javascript>--}%
    %{--<g:javascript src="shop/shopSome.js"/>--}%
    <script type="text/javascript" src="${resource(dir:"js/common",file:"cart.js")}"></script>
    <link href="${resource(dir: "css",file: "cart.css")}" rel="stylesheet"/>
    <script type="text/javascript">
        $(function () {
            $("#foodList").load(function () {
                var mainheight = this.contentWindow.window.document.body.offsetHeight + 30;
                //alert(mainheight);
                $(this).height(mainheight);
            });

            $("#appraiseList").load(function () {
                var mainheight = this.contentWindow.window.document.body.offsetHeight + 30;
                //alert(mainheight);
                $(this).height(mainheight);
            });
            //设置点击事件
            $("#foodListTab").click(function(event){
                $("#foodList").attr("src","${createLink(controller:"search",action:"searchFood", params: [ sort: "id", order: "desc", inShop: "true"])}");
            });
            $("#appraiseListTab").click(function(event){
                $("#appraiseList").attr("src","${createLink(controller:"infoShow",action:"appraiseList", params: [ sort: "id", order: "desc", inShop: "true"])}");
            });

            //初始化餐车
            initCart({
                addToCartUrl: "${createLink(controller: "cartOfCustomerAjax",action: "addFoodToCart")}",
                cartsAndDishesUrl:"${createLink(controller: "cartOfCustomerAjax",action: "getCartsAndDishes")}",
                checkOutUrl:"${createLink(controller: "cartOfCustomer",action: "cartCheckout")}",
                imgUrl:"${createLink(controller: "imageShow", action: "downloadThumbnail", params: [width: 70,height: 70])}",
                delDishFromCartUrl:"${createLink(controller: "cartOfCustomerAjax",action: "delDish")}",
                updateDishOfCartUrl:"${createLink(controller: "cartOfCustomerAjax",action: "updateDish")}"
            });
        });
    </script>

    <style type="text/css">
        .mc_main{
            width: 1000px;
            height: auto;
            margin: 0px 50px;
            background-color: #FFFFFF;
            float: left;
        }
        .mcm_top{
            width: 960px;
            height: auto;
            margin: 20px;
            float: left;
        }
        .mcmt_left{
            width: 300px;
            height: 300px;
            float: left;
            overflow: hidden;
        }
        .mcmt_right{
            width: 650px;
            height: auto;
            margin-left:10px ;
            float: left;
        }
        .mcmtr_row_h1{
            width: 650px;
            height: 30px;
            line-height: 30px;
            font-size: 24px;
            font-weight: bolder;
            overflow: hidden;
            float: left;
        }
        .mcmtr_row_h2{
            width: 650px;
            height: 30px;
            line-height: 30px;
            font-size: 14px;
            font-weight: bold;
            overflow: hidden;
            float: left;
        }
        .mcmtr_row_h3{
            width: 650px;
            height: 20px;
            line-height: 20px;
            font-size: 12px;
            overflow: hidden;
            float: left;
        }
        .mcmtr_row_content{
            width: 650px;
            height: auto;
            line-height: 20px;
            font-size: 12px;
            float: left;
            text-indent:2em;
        }
        .mcmtr_row_bottons{
            width: 650px;
            height: 40px;
            line-height: 40px;
            font-size: 12px;
            margin-top: 20px;
            float: left;
        }
        .mcmtrr_label{
            float: left;
            font-size: 12px;
            line-height: 25px;
            color: #c2bfbf;
        }
        .mcmtrr_info{
            float: left;
            line-height: 25px;
            overflow: hidden;
            font-size: 12px;
        }

        .mcm_bottom{
            width: 960px;
            height: auto;
            margin: 20px;
            float: left;
        }
    </style>

</head>

<body>
<div class="mc_main">
    %{--<div class="span11 offset1">--}%
    <g:render template="../layouts/msgs_and_errors"></g:render>

        <g:if test="${restaurantInfo}">
            <!--店铺信息-->
            <div class="mcm_top">
                <div class="mcmt_left img-rounded">
                    <img class="" src="${createLink(controller: "imageShow", action: "downloadThumbnail", params: [imgUrl: restaurantInfo.image,width:300,height:300])}" width="300"/>
                </div>
                <div class="mcmt_right">
                    <div class="mcmtr_row_h1">${restaurantInfo.name}</div>
                    <div class="mcmtr_row_h3">
                        <div style="width: 650px;margin: 4px 0px;height: 12px;line-height: 12px;">
                            <taos:restaurantStars/>
                            消费水平：
                            <g:if test="${restaurantInfo?.averageConsume}">
                                ￥${restaurantInfo?.averageConsume}
                            </g:if>
                            <g:else>
                                暂时没有统计
                            </g:else>
                        </div>
                    </div>
                    <div class="mcmtr_row_h3">
                        <label class="mcmtrr_label">地址：</label>
                        <label class="mcmtrr_info">
                        ${restaurantInfo?.address}
                        </label>
                    </div>
                    <div class="mcmtr_row_h3">
                        <label class="mcmtrr_label">菜系：</label>
                        <label class="mcmtrr_info">${restaurantInfo.cuisineName}</label>
                    </div>

                    <div class="mcmtr_row_h2">餐厅简介</div>
                    <div class="mcmtr_row_content">
                        <g:if test="${restaurantInfo?.description}">
                            ${restaurantInfo?.description}
                        </g:if>
                        <g:else>
                            暂时没有简介
                        </g:else>
                    </div>

                    <div class="mcmtr_row_h2">其他信息</div>
                    <div class="mcmtr_row_h3">
                        <label class="mcmtrr_label">营业时间：</label>
                        <label class="mcmtrr_info">
                            ${FormatUtil.timeFormat(restaurantInfo?.shopHoursBeginTime)}-
                            ${FormatUtil.timeFormat(restaurantInfo?.shopHoursEndTime)}
                        </label>
                    </div>
                    <div class="mcmtr_row_h3">
                        <label class="mcmtrr_label">电话：</label>
                        <label class="mcmtrr_info">
                            ${restaurantInfo?.phone}
                        </label>
                    </div>

                    <div class="mcmtr_row_bottons">
                    <a class="btn btn-success" href="${createLink(controller: "infoShow", action: "tablesShow", params: [restaurantId: restaurantInfo.id,restaurantName:URLEncoder.encode(restaurantInfo.name,"utf-8")])}"
                       target="_blank" style="color:#ffffff">进入预定桌位</a>
                        &nbsp;  &nbsp; &nbsp; &nbsp;
                    %{--<a class="btn btn-success" href="${createLink(controller: "infoShow", action: "orderInput", params: [restaurantId: restaurantInfo.id])}"--}%
                       %{--target="_blank" style="color:#ffffff">进入创建订单</a>--}%
                        %{--<a href="#"--}%
                           %{--onclick="shopAddToFavorite('${createLink(controller: "user",action: "addFavorite",params: [type:"shop",restaurantId:restaurantInfo?.id])}')"--}%
                           %{--style="">收藏饭店</a>--}%
                    </div>

                </div>
            </div>

            <!--菜谱和评价信息-->
            <div class="mcm_bottom">
                <div class="tabbable" style="margin-bottom: 9px;">
                    <div>
                    <ul class="nav nav-tabs">
                        <li id="foodListTab" class="active"><a href="#1" data-toggle="tab">
                            <!--店内菜谱列表-->
                            店内菜谱列表
                        </a></li>
                        <li id="appraiseListTab"><a href="#2" data-toggle="tab">
                            <!--评价列表-->
                            饭店评价列表
                        </a></li>

                    </ul>
                    </div>
                    <div class="tab-content">
                    <div class="tab-pane active" id="1">
                        <iframe id="foodList" width='100%'  height=300 style="border: 0px" frameborder="no" border="0" marginwidth="0"
                                marginheight="0"
                                src="${createLink(controller: "search", action: "searchFood", params: [restaurantId: restaurantInfo.id, sort: "id", order: "desc", inShop: "true"])}">
                        </iframe>
                        %{--${createLink(controller: "search", action: "searchFood", params: [restaurantId: restaurantInfo.id, sort: "id", order: "desc", inShop: "true"],absolute: true).toString().toURL().getText()}--}%
                    </div>
                    <div class="tab-pane" id="2">
                        <iframe id="appraiseList" width='100%'  height=300 style="border: 0px" frameborder="no" border="0"
                                marginwidth="0"
                                marginheight="0"
                                src="${createLink(controller: "infoShow", action: "appraiseList", params: [restaurantId: restaurantInfo.id, sort: "id", order: "desc", inShop: "true"])}">
                        </iframe>
                    </div>

                </div>
                </div>
            </div>


        </g:if>
    %{--</div>--}%
</div>
</body>
</html>