<%@ page import="lj.FormatUtil" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main_template"/>
    <title>菜单管理</title>
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

        %{--<g:render template="../layouts/shopMenu"/>--}%
        <g:render template="../layouts/msgs_and_errors"></g:render>

        <a href="${createLink(controller: "foodManage", action: "editFoodInfo")}" class="btn btn-primary">新增菜单</a>

<g:if test="${foodList}">
    <table class="table table-striped table-bordered table-condensed">
        <thead>
        <tr>
            <g:sortableColumn property="name" title="${message(code: 'foodInfo.name.label', default: 'Name')}"/>
            <g:sortableColumn property="price" title="${message(code: 'foodInfo.price.label', default: 'Price')}"/>
            <g:sortableColumn property="originalPrice"
                              title="${message(code: 'foodInfo.originalPrice.label', default: 'Original Price')}"/>
            <g:sortableColumn property="image" title="${message(code: 'foodInfo.image.label', default: 'Image')}"/>
            %{--<g:sortableColumn property="canTakeOut"--}%
                              %{--title="${message(code: 'foodInfo.canTakeOut.label', default: 'Can Take Out')}"/>--}%
            <g:sortableColumn property="enable"
                              title="${message(code: 'foodInfo.enabled.label', default: 'enabled')}"/>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <g:each in="${foodList}" status="i" var="foodInfoInstance">
            <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                <td>${fieldValue(bean: foodInfoInstance, field: "name")}</td>

                <td>${fieldValue(bean: foodInfoInstance, field: "price")}</td>

                <td>${fieldValue(bean: foodInfoInstance, field: "originalPrice")}</td>

                <td>
                    <img
                            src="${createLink(controller: "imageShow", action: "downloadThumbnail", params: [imgUrl: fieldValue(bean: foodInfoInstance, field: "image")])}">
                </td>

                %{--<td><g:formatBoolean boolean="${foodInfoInstance.canTakeOut}"/></td>--}%

                <td>${FormatUtil.boolFormat(foodInfoInstance.enabled)}</td>

                <td><a href="${createLink(controller: "foodManage", action: "editFoodInfo", params: [id: foodInfoInstance.id])}" class="btn btn-link">编辑</a>
                    <a href="${createLink(controller: "foodManage", action: "delFoodInfo", params: [ids: foodInfoInstance.id])}" class="btn btn-link">删除</a>
                </td>

            </tr>
        </g:each>

        </tbody>
    </table>
    <taos:paginate total="${totalCount}" prev="&larr;" next="&rarr;" params="${params}" max="1"/>
    </div>
</div>
</g:if>
<g:else>

    <br/>
    <span class="label">没有菜单记录</span>

</g:else>
</body>
</html>