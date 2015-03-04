<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 15-2-10
  Time: 下午11:12
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
        function delDish(dishId,orderId){
            var delDishUrl="${createLink(controller: "customer",action: "delDishesAjax")}";
            $.ajax({
                context:this,
                url:delDishUrl,
                async:false,
                type:'post',
                //data:{'orderId':orderId,countName:counts,remarkName:foodIds,'remarks':remarks},
                data:"dishIds="+dishId,
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
        <div class="mcm_top_name">订单${orderInfo?.orderNum}-点菜确认</div>

        <div class="mcm_top_banner"></div>
    </div>

    <div class="span10" style="margin-left: 10px;margin-top: 0px;">
        <g:render template="../layouts/msgs_and_errors"></g:render>
        <div name="info"></div>
    </div>
    <!--订单简要信息-->
    <div class="mcmc_ssl" style="margin-left: 10px;margin-top: 0px;">
        <form class="well form-inline">
            <g:if test="${orderInfo}">
                <g:if test="${orderInfo?.orderNum}">
                    <g:message code="orderInfo.orderNum.label" default="Order Num"/> :
                    ${orderInfo?.orderNum}
                </g:if>

                &nbsp;&nbsp;
                <g:if test="${orderInfo?.tableInfo}">
                    <g:message code="orderInfo.tableInfo.label"
                               default="Table Name"/>:
                    ${orderInfo.tableInfo.name}
                </g:if>
                &nbsp;&nbsp;
                <g:if test="${orderInfo?.waiter}">
                    <g:message code="orderInfo.waiter.label"
                               default="Waiter"/>:
                    ${orderInfo.waiter.name}
                </g:if>
            %{--&nbsp;&nbsp;--}%
            %{--<g:if test="${orderInfo?.partakeCode}">--}%
            %{--<g:message code="orderInfo.partakeCode.label"--}%
            %{--default="Partake Code"/>：<g:fieldValue--}%
            %{--bean="${orderInfo}" field="partakeCode"/>--}%
            %{--</g:if>--}%
                &nbsp;&nbsp;
            </g:if>

            %{--<a href="${params.backUrl ?: createLink(controller: "customer", action: "orderList")}"--}%
            %{--class="btn btn-link">返回</a>--}%
        </form>
    </div>

    <div>
        请确认你的点菜，点击退回点菜，将退回到点菜，如果你确认了你的点菜就点击确认点菜，你确认后，我们将开始为你做菜
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

                    <g:sortableColumn property="valid"
                                      title="${message(code: 'dishesInfo.valid.label', default: 'Valid')}"
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
                        <button onclick="delDish(${dishesInfoInstance?.id},${orderInfo?.id})"
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
    <!--完成点菜-->
    <div class="span11">
        <form class="form-horizontal" method="POST" id="back_to_dish_form" action="backToDish">
            <input type="hidden" name="orderId" value="${orderInfo?.id}"/>
            <input type="hidden" name="code" value="${params.code}"/>
            <div class="control-group">
                <label class="control-label"></label>

                <div class="controls">
                    <input type="submit"
                           value="${message(code: 'default.button.backToDish.label', default: 'Back To Dish')}"
                           class="btn send_btn"/>
                </div>
            </div>
        </form>
    <form class="form-horizontal" method="POST" id="dishConfirm_form" action="orderConfirm">
        <input type="hidden" name="orderId" value="${orderInfo?.id}"/>
        <input type="hidden" name="code" value="${params.code}"/>
        <div class="control-group">
            <label class="control-label"></label>

            <div class="controls">
                <input type="submit"
                       value="${message(code: 'default.button.dishConfirm.label', default: 'Dish Confirm')}"
                       class="btn send_btn"/>
            </div>
        </div>
    </form>
    </div>
</div>
</body>
</html>