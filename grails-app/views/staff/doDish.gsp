<%@ page import="lj.enumCustom.DishesValid; lj.enumCustom.DishesStatus" contentType="text/html;charset=UTF-8" %>
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

    <script type="text/javascript">
        function doDish(obj,foodId,orderId){
            var doDishUrl="${createLink(controller: "staffAjax",action: "addDishes")}";
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
                data:"orderId="+orderId+"&"+countName+"="+countVal+"&"+remarkName+"="+remarkVal+"&foodIds="+foodId,
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
            var delDishUrl="${createLink(controller: "staffAjax",action: "delDish")}";
            $.ajax({
                context:this,
                url:delDishUrl,
                async:false,
                type:'post',
                //data:{'orderId':orderId,countName:counts,remarkName:foodIds,'remarks':remarks},
                data:"dishIds="+dishId,//+"&orderId="+orderId,
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
        订单${params.orderId}-点菜
    </div>

    <div class="mcm_content">

        <g:render template="../layouts/msgs_and_errors"></g:render>
        <div name='info'></div>
    <!--订单信息-->
        <div class="mcmc_ssl">
            <form class="well form-inline">
                <g:if test="${orderInfoInstance}">
                    <g:if test="${orderInfoInstance?.id}">
                        <g:message code="orderInfo.id.label" default="id"/> :<g:fieldValue bean="${orderInfoInstance}"
                                                                                           field="id"/>
                    </g:if>

                    &nbsp;&nbsp;
                    <g:if test="${orderInfoInstance?.tableInfo}">
                        <g:message code="orderInfo.tableInfo.label"
                                   default="Table Id"/>:<g:fieldValue
                            bean="${orderInfoInstance}"
                            field="tableInfo"/>
                    </g:if>
                    &nbsp;&nbsp;
                    <g:if test="${orderInfoInstance?.waiter}">
                        <g:message code="orderInfo.waiter.label"
                                   default="Waiter Id"/>:<g:fieldValue
                            bean="${orderInfoInstance}"
                            field="waiter"/>
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


    <!--菜品类别列表-->
    <div>
        <ul class="breadcrumb">
            <li class="active"><a href="${createLink(controller: "customer",action: "getOrCreateOrder",params: [foodClassId:0]<<params)}">全部</a></li>
            <g:each in="${foodClassInfoInstanceList}" status="i" var="foodClassInfoInstance">
                <li><a href="${createLink(controller: "customer",action: "getOrCreateOrder",params: [foodClassId:foodClassInfoInstance.id]<<params)}">${foodClassInfoInstance.name}</a></li>
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
                                <input id="remarks${foodInfoInstance?.id}" name="remarks" type="text" class="msf_input" style="width: 16px;" value="1"/>
                            </div>

                            <div class="ml_row_txt">
                                %{--<g:if test="${foodInfoInstance?.canTakeOut}">--}%
                                %{--<a style="float: left;" href="#"--}%
                                %{--restaurantId="${foodInfoInstance?.restaurantId}"--}%
                                %{--foodId="${foodInfoInstance?.id}">--}%
                                %{--加入外卖餐车</a>--}%
                                %{--</g:if>--}%
                                <button onclick="doDish(this,${foodInfoInstance?.id},${orderInfoInstance?.id})"
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
            <taos:paginate total="${totalCount ?: 0}" prev="&larr;" next="&rarr;" params="${params}" action="getOrCreateOrder"/>
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
        <g:if test="${dishes?.dishList}">
        %{--<div>--}%
            <table class="table table-striped table-bordered table-condensed">
                <thead>
                <tr>

                    <g:sortableColumn property="orderId"
                                      title="${message(code: 'dishesInfo.orderId.label', default: 'Order Id')}"
                                      params="${params}"/>

                    <g:sortableColumn property="foodId"
                                      title="${message(code: 'dishesInfo.foodId.label', default: 'Food Id')}"
                                      params="${params}"/>

                    <g:sortableColumn property="foodName"
                                      title="${message(code: 'dishesInfo.foodName.label', default: 'Food Name')}"
                                      params="${params}"/>

                    <g:sortableColumn property="num" title="${message(code: 'dishesInfo.num.label', default: 'num')}"
                                      params="${params}"/>

                    <g:sortableColumn property="status"
                                      title="${message(code: 'dishesInfo.status.label', default: 'Status')}"
                                      params="${params}"/>

                    <g:sortableColumn property="valid" title="${message(code: 'dishesInfo.valid.label', default: 'Valid')}"
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
                <g:each in="${dishes?.dishList}" status="i" var="dishesInfoInstance">
                    <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                        <td>${fieldValue(bean: dishesInfoInstance, field: "orderId")}</td>

                        <td>${fieldValue(bean: dishesInfoInstance, field: "foodId")}</td>

                        <td>${fieldValue(bean: dishesInfoInstance, field: "foodName")}</td>

                        <td>${fieldValue(bean: dishesInfoInstance, field: "num")}</td>

                        <td>${DishesStatus.getLable(dishesInfoInstance?.status)}</td>

                        <td>${DishesValid.getLable(dishesInfoInstance?.valid)}</td>

                        %{--<td>${fieldValue(bean: dishesInfoInstance, field: "cancelReason")}</td>--}%

                        <td>${fieldValue(bean: dishesInfoInstance, field: "remark")}</td>

                        %{--<td><g:customerDishesOperation--}%
                        %{--dishesId="${dishesInfoInstance?.id}"></g:customerDishesOperation></td>--}%
                        <td>
                            %{--<g:if test="${isOwner}">--}%
                                <button onclick="delDish(${dishesInfoInstance?.id},${orderInfoInstance?.id})"
                                        class="">删除</button>
                    %{--<taos:staffDishesOperation dishesId="${dishesInfoInstance?.id}"--}%
                                               %{--backUrl="${createLink(controller:"staff",action:  "doDish",params: params,absolute: true)}"/>--}%
                            %{--</g:if>--}%
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
</div>
</body>
</html>