<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 13-11-26
  Time: 上午2:30
  To change this template use File | Settings | File Templates.
--%>

<%@ page import="lj.enumCustom.DishesStatus; lj.enumCustom.DishesValid" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main_template"/>
    <title>点菜列表-厨师用</title>
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
    }

    .mcmcs_field {
        width: 280px;
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
    </style>
</head>

<body>

<div class="mc_main">
    <div class="mcm_top">
        <g:render template="../layouts/staffMenu"></g:render>
    </div>
    <div class="mcm_content">
    <!--提示消息-->
        <g:render template="../layouts/msgs_and_errors"></g:render>

    <!--搜索条件-->
    %{--<div class="mcmc_ssl">--}%
    %{--<form class="well form-inline" action="${createLink(controller: "staff", action: "dishList")}">--}%


    %{--</form>--}%
    %{--</div>--}%


        <g:if test="${dishList}">
            <table class="table table-striped table-bordered table-condensed">
                <thead>
                <tr>

                    <g:sortableColumn property="order.id"
                                      title="${message(code: 'dishesInfo.order.label', default: 'Order Id')}"
                                      params="${params}"/>

                    <g:sortableColumn property="foodId"
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

                    <g:sortableColumn property="cancelReason"
                                      title="${message(code: 'dishesInfo.cancelReason.label', default: 'Cancel Reason')}"
                                      params="${params}"/>

                    <g:sortableColumn property="remark"
                                      title="${message(code: 'dishesInfo.remark.label', default: 'Remark')}"
                                      params="${params}"/>

                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <g:each in="${dishList}" status="i" var="dishesInfoInstance">
                    <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                        <td>${fieldValue(bean: dishesInfoInstance, field: "order")}</td>

                        <td>
                            <g:if test="${dishesInfoInstance?.foodName}">
                                ${dishesInfoInstance?.foodName}
                            </g:if>
                            <g:else>
                                菜谱&nbsp;${dishesInfoInstance?.foodInfo.id}
                            </g:else>
                        </td>

                        <td>${fieldValue(bean: dishesInfoInstance, field: "num")}</td>

                        <td>${DishesStatus.getLable(dishesInfoInstance?.status)}</td>

                        <td>${DishesValid.getLable(dishesInfoInstance?.valid)}</td>

                        <td>${fieldValue(bean: dishesInfoInstance, field: "cancelReason")}</td>

                        <td>${fieldValue(bean: dishesInfoInstance, field: "remark")}</td>

                        <td><taos:staffDishesOperation dishesId="${dishesInfoInstance?.id}"
                             backUrl="${createLink(controller:"staff",action:  "dishList",params: params,absolute: true)}"></taos:staffDishesOperation></td>
                    </tr>
                </g:each>
                </tbody>
            </table>
            <!--分页-->
            <taos:paginate action="dishList" total="${totalCount ?: 0}" prev="&larr;" next="&rarr;" params="${params}"/>
        </g:if>
        <g:else>
            <div style="margin: 0px auto;">
                <label style="text-align: center">还没有点菜哦</label>
            </div>
        </g:else>
    </div>
</div>
</body>
</html>