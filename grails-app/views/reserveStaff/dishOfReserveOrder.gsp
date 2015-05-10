<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 15-2-10
  Time: 下午11:11
  To change this template use File | Settings | File Templates.
--%>

<%@ page import="lj.enumCustom.DishesValid; lj.enumCustom.DishesStatus" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main_template"/>
    <title></title>
    <style type="text/css">
    .mc_main {
        width: 1000px;
        height: auto;
        margin: 0px 50px;
        background-color: #FFFFFF;
        float: left;
    }
    .mcm_top {
        margin-top: 20px;
    }

    .mcm_top_name {
        font-size: 20px;
        font-weight: bold;
        padding: 10px 0px 10px 20px;
    }

    .mcm_top_banner {
        width: 100%;
        height: 4px;
        background: url('${resource(dir:"images",file:"login_banner.gif")}');
        margin: 0px auto;
        margin-bottom: 30px;
    }
    .mcmc_ssl {
        width: 960px;
        margin-top: 10px;
        margin-bottom: 10px;
        font-size: 14px;
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

    .mcmc_detail {
        width: 900px;
        height: auto;
        margin-left: 30px;
        margin-right: 30px;
        float: left;
        border-bottom: 2px solid #DDDDDD;
        margin-bottom: 20px;
    }

    .mcmcd_title {
        width: 900px;
        height: 30px;
        line-height: 30px;
        float: left;
        overflow: hidden;
    }

    .mcmcdt_ico {
        border-left: 7px solid #FF9833;
        border-right: 7px solid #FFF;
        border-top: 7px solid #FFF;
        border-bottom: 7px solid #FFF;
        margin-top: 8px;
        margin-bottom: 8px;
        width: 0;
        height: 0;
        float: left;
    }

    .mcmcdt_info {
        color: #FF9833;
        float: left;
        font-size: 15px;
    }

    .mcmcd_item {
        width: 300px;
        height: 30px;
        line-height: 30px;
        float: left;
        overflow: hidden;
    }

    .mcmcdi_label {
        width: 100px;
        height: 30px;
        float: left;
        color: #C2BFBF;
        overflow: hidden;
    }

    .mcmcdi_info {
        width: 200px;
        height: 30px;
        float: left;
        overflow: hidden;
    }
    </style>
    <script type="text/javascript">
        function doDish(obj,foodId,orderId){
            var doDishUrl="${createLink(controller: "reserveStaff",action: "dishAjax")}";
            var selectVar="#counts"+foodId;
            var countVal=$(obj).parent().parent().find("input[name='counts']").val();
            selectVar="#remarks"+foodId;
            var remarkVal= $(obj).parent().parent().find("input[name='remarks']").val();
            var countName="counts"+foodId;
            var remarkName= "remarks"+foodId;
            $.ajax({
                context:this,
                url:doDishUrl,
                async:false,
                type:'post',
                //data:{'orderId':orderId,countName:counts,remarkName:foodIds,'remarks':remarks},
                data:"reserveOrderId="+orderId+"&"+countName+"="+countVal+"&"+remarkName+"="+remarkVal+"&foodIds="+foodId,
                dataType: 'json',
                success:function(data){
                    $("div[name='info']").html("");
                    if(data.recode.code==0){
                        //$("div[name='info']").html("<label style='height: 50px;line-height:50px;color: green'>点菜"+data.recode.label+"</label>");
                        location.reload();
                    }else{
                        if(data.recode.code==5)
                            $("div[name='info']").html("<label style='height: 50px;line-height:50px;color: red'>点菜不成功："+data.failedList[0].msg+"</label>");
                        else
                            $("div[name='info']").html("<label style='height: 50px;line-height:50px;color: red'>点菜不成功："+data.recode.label+"</label>");
                    }
                },
                error:function(data){
                    $("div[name='info']").html("<label style='height: 50px;line-height:50px;color: red'>"+"未知错误"+"</label>");
                }
            });

        }

        function delDish(dishId,orderId){
            var delDishUrl="${createLink(controller: "reserveStaff",action: "delDishAjax")}";
            $.ajax({
                context:this,
                url:delDishUrl,
                async:false,
                type:'post',
                //data:{'orderId':orderId,countName:counts,remarkName:foodIds,'remarks':remarks},
                data:"dishIds="+dishId+"&reserveOrderId="+orderId,
                dataType: 'json',
                success:function(data){
                    $("div[name='info']").html("");
                    if(data.recode.code==0){
                        //$("div[name='info']").html("<label style='height: 50px;line-height:50px;color: green'>点菜"+data.recode.label+"</label>");
                        location.reload();
                    }else{
                        if(data.recode.code==5)
                            $("div[name='info']").html("<label style='height: 50px;line-height:50px;color: red'>不成功："+data.failedList[0].msg+"</label>");
                        else
                            $("div[name='info']").html("<label style='height: 50px;line-height:50px;color: red'>不成功："+data.recode.label+"</label>");
                    }
                },
                error:function(data){
                    $("div[name='info']").html("<label style='height: 50px;line-height:50px;color: red'>"+"未知错误"+"</label>");
                }
            });

        }
    </script>
</head>
<body>
<div class="mc_main">
<div class="mcm_top">
    %{--<div class="mcm_top_name">订单${reserveOrderInfo?.numInRestaurant}-点菜</div>--}%

    %{--<div class="mcm_top_banner"></div>--}%
    <g:render template="../layouts/staffMenu"></g:render>
</div>

<div class="span10" style="margin-left: 10px;margin-top: 0px;">
    <g:render template="../layouts/msgs_and_errors"></g:render>
    <div name="info"></div>
</div>
<!--订单简要信息-->
<div class="mcmc_ssl" style="margin-left: 10px;margin-top: 0px;">
    <form class="well form-inline">
        <g:if test="${reserveOrderInfo}">
            <g:if test="${reserveOrderInfo?.numInRestaurant}">
                <g:message code="reserveOrderInfo.numInRestaurant.label" default="Number In Restaurant"/> :
                ${reserveOrderInfo?.numInRestaurant}
            </g:if>

            &nbsp;&nbsp;
            <g:if test="${reserveOrderInfo?.tableInfo}">
                <g:message code="reserveOrderInfo.tableInfo.label"
                           default="Table Name"/>:
                ${reserveOrderInfo.tableInfo.name}
            </g:if>
            &nbsp;&nbsp;
            <g:if test="${reserveOrderInfo?.waiter}">
                <g:message code="reserveOrderInfo.waiter.label"
                           default="Waiter"/>:
                ${reserveOrderInfo.waiter.name}
            </g:if>
            &nbsp;&nbsp;
        </g:if>

        %{--<a href="${params.backUrl ?: createLink(controller: "customer", action: "orderList")}"--}%
        %{--class="btn btn-link">返回</a>--}%
    </form>
</div>
<!--菜品类别列表-->
<div>
    <ul class="breadcrumb">
        <li class="active"><a href="${createLink(controller: "reserveStaff",action: "dishOfReserveOrder",params: params<<[foodClassId:0])}">全部</a></li>
        <g:each in="${foodClassInfoInstanceList}" status="i" var="foodClassInfoInstance">
            <li><a href="${createLink(controller: "reserveStaff",action: "dishOfReserveOrder",params: params<<[foodClassId:foodClassInfoInstance.id])}">${foodClassInfoInstance.name}</a></li>
        </g:each>
    </ul>
</div>

<!--菜品列表-->
<div class="m_list" style="margin-left: 10px;margin-top: 0px;">
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
                            <label id="counts${foodInfoInstance?.id}"
                                   style="float: left;font-size: 14px;">数量:</label>
                            <input id="counts${foodInfoInstance?.id}" name="counts" type="text" class="msf_input" style="width: 16px;" value="1"/>
                        </div>

                        <div class="ml_row_txt">
                            <label id="remarks${foodInfoInstance?.id}"
                                   style="float: left;font-size: 14px;">备注:</label>
                            <input id="remarks${foodInfoInstance?.id}" name="remarks" type="text" class="msf_input" style="width: 16px;" value=""/>
                        </div>

                        <div class="ml_row_txt">
                            %{--<g:if test="${foodInfoInstance?.canTakeOut}">--}%
                            %{--<a style="float: left;" href="#"--}%
                            %{--restaurantId="${foodInfoInstance?.restaurantId}"--}%
                            %{--foodId="${foodInfoInstance?.id}">--}%
                            %{--加入外卖餐车</a>--}%
                            %{--</g:if>--}%
                            <button onclick="doDish(this,${foodInfoInstance?.id},${reserveOrderInfo?.id})"
                                    class="">点一个</button>
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
        <taos:paginate total="${totalCount ?: 0}" prev="&larr;" next="&rarr;" params="${params}" action="dishOfReserveOrder"/>
    </g:if>
    <g:else>
        <div style="margin: 0px auto;">
            <label style="text-align: center">没有搜索到记录</label>
        </div>
    </g:else>
</div>
<!--点菜列表-->
<div class="mcmc_detail">
    <!--订单对应的点菜信息-->
    <div class="mcmcd_title">
        <div class="mcmcdt_ico"></div>

        <div class="mcmcdt_info">点菜信息</div>
    </div>
    <g:if test="${reserveOrderInfo?.dishes}">
    %{--<div>--}%
        <table class="table table-striped table-bordered table-condensed">
            <thead>
            <tr>

                <g:sortableColumn property="foodId"
                                  title="${message(code: 'dishesInfo.food.label', default: 'Food')}"
                                  params="${params}"/>

                <g:sortableColumn property="foodName"
                                  title="${message(code: 'dishesInfo.foodName.label', default: 'Food Name')}"
                                  params="${params}"/>

                <g:sortableColumn property="num" title="${message(code: 'dishesInfo.num.label', default: 'num')}"
                                  params="${params}"/>

                %{--<g:sortableColumn property="cancelReason"--}%
                %{--title="${message(code: 'dishesInfo.cancelReason.label', default: 'Cancel Reason')}"--}%
                %{--params="${params}"/>--}%

                <g:sortableColumn property="remark"
                                  title="${message(code: 'dishesInfo.remark.label', default: 'Remark')}"
                                  params="${params}"/>

                <th>操作</th>
            </tr>
            </thead>
            <tbody>
            <g:each in="${reserveOrderInfo?.dishes}" status="i" var="reserveDishesInfo">
                <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                    <td>${fieldValue(bean: reserveDishesInfo, field: "foodId")}</td>

                    <td>${fieldValue(bean: reserveDishesInfo, field: "foodName")}</td>

                    <td>${fieldValue(bean: reserveDishesInfo, field: "num")}</td>

                    %{--<td>${fieldValue(bean: reserveDishesInfo, field: "cancelReason")}</td>--}%

                    <td>${fieldValue(bean: reserveDishesInfo, field: "remark")}</td>

                    %{--<td><g:customerDishesOperation--}%
                    %{--dishesId="${reserveDishesInfo?.id}"></g:customerDishesOperation></td>--}%
                    <td>
                            <button onclick="delDish(${reserveDishesInfo?.id},${reserveOrderInfo?.id})"
                                    class="">删除</button>
                    </td>
                </tr>
            </g:each>
            </tbody>
        </table>

    %{--<taos:paginate action="orderShow" total="${totalCount ?: 0}" prev="&larr;" next="&rarr;" params="${params}"/>--}%

    %{--</div>--}%
    </g:if>
    <g:else>
        <div class="mcmcd_item" style="width: 900px;">
            还没有点菜，点菜吧
        </div>
    </g:else>
</div>
</div>
</body>
</html>