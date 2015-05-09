<%@ page import="lj.enumCustom.CustomerRelationsType" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main_template"/>
    <title>客户关系管理</title>
    <style type="text/css">
    .mc_main {
        width: 1000px;
        height: auto;
        margin: 0px 50px;
        background-color: #FFFFFF;
        float: left;
    }
    </style>
</head>
<body>
<div class="mc_main">
    <div  class="span10" style="margin-left: 10px;margin-top: 10px;">

        <g:render template="../layouts/shopMenu"/>

        <g:render template="../layouts/msgs_and_errors"></g:render>

        <a href="${createLink(controller: "customerRelations", action: "editCr")}" class="btn btn-primary">新增客户关系</a>

        <g:if test="${customerRelationsList}" >
            <table class="table table-striped table-bordered table-condensed">
                <thead>
                <tr>

                    <g:sortableColumn property="restaurantId"
                                      title="${message(code: 'customerRelations.restaurantId.label', default: 'Restaurant Id')}"/>

                    <g:sortableColumn property="customerClientId"
                                      title="${message(code: 'customerRelations.customerClientId.label', default: 'Customer User Id')}"/>

                    <g:sortableColumn property="customerUserName"
                                      title="${message(code: 'customerRelations.customerUserName.label', default: 'Customer User Name')}"/>

                    <g:sortableColumn property="type"
                                      title="${message(code: 'customerRelations.type.label', default: 'Type')}"/>
                     <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <g:each in="${customerRelationsList}" status="i" var="customerRelationsInstance">
                    <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                        <td><g:link action="show"
                                    id="${customerRelationsInstance.id}">${fieldValue(bean: customerRelationsInstance, field: "restaurantId")}</g:link></td>

                        <td>${fieldValue(bean: customerRelationsInstance, field: "customerClientId")}</td>

                        <td>${fieldValue(bean: customerRelationsInstance, field: "customerUserName")}</td>

                        <td>${CustomerRelationsType.getCustomerRelationsType(customerRelationsInstance?.type ?: 0).label}</td>

                        <td><a href="${createLink(controller: "customerRelations", action: "editCr", params: [id: customerRelationsInstance.id])}" class="btn btn-link">编辑</a>
                        <a href="${createLink(controller: "customerRelations", action: "delCr", params: [ids: customerRelationsInstance.id])}" class="btn btn-link">删除</a>
                        </td>
                    </tr>
                </g:each>
                </tbody>
            </table>

            <div class="pagination">
                <g:paginate total="${totalCount ?: 0}"/>
            </div>
        </g:if>
        <g:else>
            <span class="label">还没有客户关系记录</span>
        </g:else>
    </div>
</div>
</body>
</html>