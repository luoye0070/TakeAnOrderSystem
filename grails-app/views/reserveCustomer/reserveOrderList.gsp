<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 15-2-10
  Time: 下午11:11
  To change this template use File | Settings | File Templates.
--%>

<%@ page import="lj.enumCustom.ReserveOrderStatus; lj.enumCustom.OrderValid; lj.FormatUtil; lj.enumCustom.DishesValid; lj.enumCustom.DishesStatus" contentType="text/html;charset=UTF-8" %>
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
</head>
<body>
<div class="mc_main">
    <div class="mcm_top">
        <div class="mcm_top_name">订单列表</div>

        <div class="mcm_top_banner"></div>
    </div>

    <div class="span10" style="margin-left: 10px;margin-top: 0px;">
        <g:render template="../layouts/msgs_and_errors"></g:render>
        <div name="info"></div>
    </div>
    <!--订单列表-->
    <div class="mcmc_detail">
    <g:if test="${reserveOrderInfoList}">
        <!--订单列表-->
        <table class="table table-striped table-bordered table-condensed">
            <thead>
            <tr>
                <g:sortableColumn property="id" title="${message(code: 'reserveOrderInfo.id.label', default: 'id')}"  params="${params}"/>
                %{--<g:sortableColumn property="restaurantId"--}%
                %{--title="${message(code: 'reserveOrderInfo.restaurantId.label', default: 'Restaurant Id')}"/>--}%

                <g:sortableColumn property="clientInfo.id" title="${message(code: 'reserveOrderInfo.userName.label', default: 'User Name')}"  params="${params}"/>

                <g:sortableColumn property="tableInfo.id"
                                  title="${message(code: 'reserveOrderInfo.tableName.label', default: 'Table Name')}" params="${params}"/>



                <g:sortableColumn property="createTime"
                                  title="${message(code: 'reserveOrderInfo.ctreateTime.label', default: 'Time')}" params="${params}"/>

                <g:sortableColumn property="valid" title="${message(code: 'reserveOrderInfo.valid.label', default: 'Valid')}" params="${params}"/>

                <g:sortableColumn property="status" title="${message(code: 'reserveOrderInfo.status.label', default: 'Status')}" params="${params}"/>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
            <g:each in="${reserveOrderInfoList}" status="i" var="reserveOrderInfoInstance">
                <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
                    <td>${reserveOrderInfoInstance.id}</td>
                    %{--<td><g:link action="show"--}%
                    %{--id="${reserveOrderInfoInstance.id}">${fieldValue(bean: reserveOrderInfoInstance, field: "restaurantId")}</g:link></td>--}%

                    <td>
                        <g:if test="${reserveOrderInfoInstance.clientInfo}">
                            <g:if test="${reserveOrderInfoInstance.clientInfo?.nickname}">
                                用户&nbsp;${reserveOrderInfoInstance.clientInfo?.nickname}
                            </g:if>
                            <g:else>
                                用户&nbsp;${reserveOrderInfoInstance.clientInfo?.clientMark}
                            </g:else>
                        </g:if>
                        <g:else>
                            服务员
                        </g:else>
                    </td>

                    <td style="max-width: 100px">
                        <g:if test="${reserveOrderInfoInstance.tableName}">
                            ${fieldValue(bean: reserveOrderInfoInstance, field: "tableName")}
                        </g:if>
                        <g:else>
                            桌位&nbsp;${reserveOrderInfoInstance.tableInfo.id}
                        </g:else>
                    </td>



                    <td>${FormatUtil.dateTimeFormat(reserveOrderInfoInstance.createTime)}</td>

                    <td>${OrderValid.getLable(reserveOrderInfoInstance.valid)}</td>

                    <td>${ReserveOrderStatus.getLable(reserveOrderInfoInstance.status)}</td>

                    %{--<td>${ReserveType.getLabel(reserveOrderInfoInstance.reserveType)}</td>--}%
                    <td><a href="${createLink(controller: "reserveCustomer", action: "reserveOrderDetail", params: [reserveOrderId: reserveOrderInfoInstance.id])}">订单详情</a>
                    </td>
                    <td>
                        %{--<a href="${createLink(controller: "reserveCustomer", action: "reserveOrderCancel", params: [reserveOrderId: reserveOrderInfoInstance.id])}">取消</a>--}%
                        <taos:customerReserveOrderOperation reserveOrderId="${reserveOrderInfoInstance.id}"
                         backUrl="${createLink(controller: "reserveCustomer", action: "reserveOrderList",params: params,absolute: true)}"/>
                    </td>
                </tr>
            </g:each>
            </tbody>
        </table>
        <!--分页-->
        <taos:paginate action="reserveOrderList" total="${totalCount ?: 0}" prev="&larr;" next="&rarr;" params="${params}"/>
    </g:if>
    <g:else>
        <div style="margin: 0px auto;">
            <label style="text-align: center">还没有预定订单哦</label>
        </div>
    </g:else>
    </div>
</div>
</body>
</html>