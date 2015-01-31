<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main_template"/>
    <title>图片类别编辑</title>
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


        <div class="span2">
            <ul class="nav nav-list">
                <li><a href="<g:createLink action="imageList" controller="imageSpace"
                                           params="[isDel: false]"/> ">图片空间</a></li>
                <li><a href="<g:createLink action="upload" controller="imageSpace"/>">上传图片</a></li>
                <li class="active"><a href="#">图片分类</a></li>
                <li><a href="<g:createLink action="imageList" controller="imageSpace" params="[isDel: true]"/>">回收站</a>
                </li>
            </ul>

        </div>

        <div class="span7">
            <g:render template="../layouts/msgs_and_errors"></g:render>



            <form method="post" action="editImageClass" class="form-horizontal">
                <input type="hidden" name="classId" id="id" value="${imageClassInfo?.id}"/>

                <div class="control-group">
                    <label class="control-label" for="name">类别名
                        <span class="required-indicator">*</span>
                    </label>

                    <div class="controls">
                        <input type="text" name="name" id="name" value="${imageClassInfo?.name}"/>
                    </div>
                </div>

                <div class="form-actions">
                    <a href="${createLink(controller: "imageSpace", action: "imageClassList")}"
                       class="btn btn-primary">取消</a>
                    <input type="submit" class="btn btn-primary"
                           value="${message(code: 'default.button.create.label', default: 'Create')}"/>
                </div>
            </form>
        </div>
    </div>
</div>

</body>
</html>