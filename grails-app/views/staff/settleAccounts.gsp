<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 13-11-25
  Time: 下午11:59
  To change this template use File | Settings | File Templates.
--%>

<%@ page import="lj.enumCustom.OrderValid; lj.enumCustom.OrderStatus; lj.enumCustom.ReserveType; lj.FormatUtil" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main_template"/>
    <title>结账</title>
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
</head>

<body>
<div class="mc_main">
<div class="mcm_top">
    订单${params.orderId}-结账
</div>

<div class="mcm_content">

<!--提示消息-->
<g:render template="../layouts/msgs_and_errors"></g:render>

<!--订单信息-->
<div class="mcmc_detail">
<!--订单信息-->
<div class="mcmcd_title">
    <div class="mcmcdt_ico"></div>

    <div class="mcmcdt_info">订单信息</div>
</div>
<g:if test="${orderInfo?.id}">

    <div class="mcmcd_item">
        <div class="mcmcdi_label">
            <g:message code="orderInfo.id.label" default="id"/>
        </div>

        <div class="mcmcdi_info" style="color: #FE4B1E;">
            <g:fieldValue bean="${orderInfo}" field="id"/>
        </div>
    </div>
</g:if>

<g:if test="${orderInfo?.tableInfo}">
    <div class="mcmcd_item">
        <div class="mcmcdi_label">
            <g:message code="orderInfo.tableInfo.label" default="Table Id"/>
        </div>

        <div class="mcmcdi_info" style="color: #FE4B1E;">
            <g:fieldValue bean="${orderInfo}" field="tableInfo"/>
        </div>
    </div>
</g:if>

<g:if test="${orderInfo?.partakeCode}">
    <div class="mcmcd_item">
        <div class="mcmcdi_label">
            <g:message code="orderInfo.partakeCode.label" default="Partake Code"/>
        </div>

        <div class="mcmcdi_info" style="color: #FE4B1E;">
            <g:fieldValue bean="${orderInfo}" field="partakeCode"/>
        </div>
    </div>
</g:if>

<g:if test="${orderInfo?.restaurantName}">
    <div class="mcmcd_item">
        <div class="mcmcdi_label">
            <g:message code="orderInfo.restaurantName.label" default="restaurantName"/>
        </div>

        <div class="mcmcdi_info">
            <g:fieldValue bean="${orderInfo}" field="restaurantName"/>
        </div>
    </div>
</g:if>

<g:if test="${orderInfo?.tableName}">
    <div class="mcmcd_item">
        <div class="mcmcdi_label">
            <g:message code="orderInfo.tableName.label" default="tableName"/>
        </div>

        <div class="mcmcdi_info">
            <g:fieldValue bean="${orderInfo}" field="tableName"/>
        </div>
    </div>
</g:if>

<g:if test="${orderInfo?.clientInfo}">
    <div class="mcmcd_item">
        <div class="mcmcdi_label">
            <g:message code="orderInfo.clientInfo.label" default="Client Id"/>
        </div>

        <div class="mcmcdi_info">
            <g:fieldValue bean="${orderInfo}" field="clientInfo"/>
        </div>
    </div>
</g:if>

<g:if test="${orderInfo?.createTime}">
    <div class="mcmcd_item">
        <div class="mcmcdi_label">
            <g:message code="orderInfo.createTime.label" default="createTime"/>
        </div>

        <div class="mcmcdi_info">
            ${FormatUtil.dateTimeFormat(orderInfo?.createTime)}
        </div>
    </div>
</g:if>

%{--<g:if test="${orderInfo?.reserveType}">--}%
    %{--<div class="mcmcd_item">--}%
        %{--<div class="mcmcdi_label">--}%
            %{--<g:message code="orderInfo.reserveType.label" default="Reserve Type"/>--}%
        %{--</div>--}%

        %{--<div class="mcmcdi_info">--}%
            %{--${ReserveType.getLabel(orderInfo?.reserveType)}--}%
        %{--</div>--}%
    %{--</div>--}%
%{--</g:if>--}%

<g:if test="${orderInfo?.status}">
    <div class="mcmcd_item">
        <div class="mcmcdi_label">
            <g:message code="orderInfo.status.label" default="Status"/>
        </div>

        <div class="mcmcdi_info">
            ${OrderStatus.getLable(orderInfo?.status)}
        </div>
    </div>
</g:if>

<g:if test="${orderInfo?.valid != null}">
    <div class="mcmcd_item">
        <div class="mcmcdi_label">
            <g:message code="orderInfo.valid.label" default="Valid"/>
        </div>

        <div class="mcmcdi_info">
            ${OrderValid.getLable(orderInfo?.valid)}
        </div>
    </div>
</g:if>

<g:if test="${orderInfo?.cancelReason}">
    <div class="mcmcd_item">
        <div class="mcmcdi_label">
            <g:message code="orderInfo.cancelReason.label" default="Cancel Reason"/>
        </div>

        <div class="mcmcdi_info">
            <g:fieldValue bean="${orderInfo}" field="cancelReason"/>
        </div>
    </div>
</g:if>

<g:if test="${orderInfo?.waiter}">
    <div class="mcmcd_item">
        <div class="mcmcdi_label">
            <g:message code="orderInfo.waiter.label" default="Waiter"/>
        </div>

        <div class="mcmcdi_info">
            <g:fieldValue bean="${orderInfo}" field="waiter"/>
        </div>
    </div>
</g:if>

%{--<g:if test="${orderInfo?.listenWaiterId}">--}%
%{--<div class="mcmcd_item">--}%
%{--<div class="mcmcdi_label">--}%
%{--<g:message code="orderInfo.listenWaiterId.label" default="Listen Waiter Id"/>--}%
%{--</div>--}%

%{--<div class="mcmcdi_info">--}%
%{--<g:fieldValue bean="${orderInfo}" field="listenWaiterId"/>--}%
%{--</div>--}%
%{--</div>--}%
%{--</g:if>--}%

<g:if test="${orderInfo?.cashier}">
    <div class="mcmcd_item">
        <div class="mcmcdi_label">
            <g:message code="orderInfo.cashier.label" default="Cashier"/>
        </div>

        <div class="mcmcdi_info">
            <g:fieldValue bean="${orderInfo}" field="cashier"/>
        </div>
    </div>
</g:if>

<g:if test="${orderInfo?.remark}">
    <div class="mcmcd_item">
        <div class="mcmcdi_label">
            <g:message code="orderInfo.remark.label" default="Remark"/>
        </div>

        <div class="mcmcdi_info">
            <g:fieldValue bean="${orderInfo}" field="remark"/>
        </div>
    </div>
</g:if>

<g:if test="${orderInfo?.numInRestaurant}">
    <div class="mcmcd_item">
        <div class="mcmcdi_label">
            <g:message code="orderInfo.numInRestaurant.label" default="Num In Restaurant"/>
        </div>

        <div class="mcmcdi_info">
            <g:fieldValue bean="${orderInfo}" field="numInRestaurant"/>
        </div>
    </div>
</g:if>

%{--<g:if test="${orderInfo?.orderNum}">--}%
%{--<div class="mcmcd_item">--}%
%{--<div class="mcmcdi_label">--}%
%{--<g:message code="orderInfo.orderNum.label" default="Order Num"/>--}%
%{--</div>--}%

%{--<div class="mcmcdi_info">--}%
%{--<g:fieldValue bean="${orderInfo}" field="orderNum"/>--}%
%{--</div>--}%
%{--</div>--}%
%{--</g:if>--}%

<g:if test="${orderInfo?.totalAccount}">
    <div class="mcmcd_item">
        <div class="mcmcdi_label">
            <g:message code="orderInfo.totalAccount.label" default="Total Account"/>
        </div>

        <div class="mcmcdi_info">
            <g:fieldValue bean="${orderInfo}" field="totalAccount"/>
        </div>
    </div>
</g:if>

<g:if test="${orderInfo?.realAccount}">
    <div class="mcmcd_item">
        <div class="mcmcdi_label">
            <g:message code="orderInfo.realAccount.label" default="Real Account"/>
        </div>

        <div class="mcmcdi_info">
            <g:fieldValue bean="${orderInfo}" field="realAccount"/>
        </div>
    </div>
</g:if>

<div class="mcmcd_item" style="width: 900px;">
    <a href="${params.backUrl ?: createLink(controller: "staff", action: "orderList")}"
       class="btn btn-link">返回</a>
</div>

</div>


<div class="mcmc_detail">
    <!--订单对应的点菜信息-->
    <div class="mcmcd_title">
        <div class="mcmcdt_ico"></div>

        <div class="mcmcdt_info">结账</div>
    </div>
    <!--提交算账的表单-->
    <form method="post" action="${createLink(controller: "staff", action: "settleAccounts",params:[backUrl:params.backUrl])}"
          class="form-horizontal offset1">
        <input type="hidden" name="orderId" value="${orderInfo?.id}"/>
        %{--<g:if test="${orderInfo?.realAccount!=null}">--}%
        <div class="control-group">
            <label for="realAccount" class="control-label">
                <g:message code="orderInfo.realAccount.label" default="Real Account"/>
            </label>

            <div class="controls">
                <input type="text" id="realAccount" name="realAccount" value="${orderInfo?.realAccount}"/>
            </div>

        </div>
        %{--</g:if>--}%
        <div class="control-group">
            <label for="realAccount" class="control-label">

            </label>

            <div class="controls">
                <input type="submit" class="btn btn-primary" value="${message(code: "default.button.submit.label", default: "submit")}"/>
            </div>
        </div>
    </form>
</div>

</div>
</div>
</body>
</html>