<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main_template"/>
    <title>图片类别</title>
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

        <div class="span2">
            <ul class="nav nav-list">
            <li><a href="<g:createLink action="imageList" controller="imageSpace"  params="[isDel: false]" /> ">图片空间</a></li>
            <li><a href="<g:createLink action="upload" controller="imageSpace"/>">上传图片</a></li>
            <li class="active"><a href="#">图片分类</a></li>
            <li><a href="<g:createLink action="imageList" controller="imageSpace" params="[isDel: true]"/>">回收站</a></li>
        </ul>

        </div>

        <div class="span7">

        <a href="${createLink(controller: "imageSpace", action: "editImageClass")}" class="btn btn-primary">新增图片类别</a>
        <g:if test="${classList}">
            <table class="table table-striped table-bordered table-condensed">
                <thead>
                <tr>
                    <th>类别名</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <g:each in="${classList}">
                    <tr><td>${it.name}</td>
                        <td>
                            <a href="${createLink(controller: "imageSpace", action: "editImageClass", params: [id: it.id])}"
                               class="btn btn-link">编辑</a>
                            <a href="${createLink(controller: "imageSpace", action: "delImageClass", params: [ids: it.id])}"
                               class="btn btn-link">删除</a>
                        </td></tr>
                </g:each>
                </tbody>
            </table>

        </g:if>
        <g:else>
            没有图片类别
        </g:else>
      </div>
    </div>
</div>
</body>
</html>