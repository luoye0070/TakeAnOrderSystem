<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main_template"/>
    <title>图片上传</title>
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
    <div class="mcm_top">
        %{--<div class="mcm_top_name"><g:message code='restaurantInfo.update.label'/></div>--}%

        %{--<div class="mcm_top_banner"></div>--}%
        <g:render template="../layouts/shopMenu"></g:render>
    </div>
    <div  class="span10" style="margin-left: 10px;margin-top: 10px;">

        %{--<g:render template="../layouts/shopMenu"/>--}%
        <div class="span2">
            <ul class="nav nav-list">
                    <li><a href="<g:createLink action="imageList" controller="imageSpace"
                                               params="[isDel: false]"/> ">图片空间</a></li>
                    <li class="active"><a href="#">上传图片</a></li>
                    <li><a href="<g:createLink action="imageClassList" controller="imageSpace"/>">图片分类</a></li>
                    <li><a href="<g:createLink action="imageList" controller="imageSpace"
                                               params="[isDel: true]"/>">回收站</a></li>
                </ul>

        </div>

        <div class="span7">
            <g:render template="../layouts/msgs_and_errors"></g:render>


            <form method="post" action="upload" enctype="multipart/form-data" class="form-horizontal">

                <div class="control-group">
                    <label class="control-label" for="classId">上传到
                        <span class="required-indicator">*</span>
                    </label>

                    <div class="controls">
                        <select name="classId" id="classId" value="">
                            <g:each in="${imgClassList}" var="ic" status="i">
                                <option value="${ic.id}">${ic.name}</option>
                            </g:each>
                        </select>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label" for="imgfile">文件
                        <span class="required-indicator">*</span>
                    </label>

                    <div class="controls">
                        <input type="file" name="imgfile" id="imgfile"/>
                    </div>
                </div>
                :

                <br/>

                <div class="form-actions">
                    <a href="${createLink(controller: "imageSpace", action: "imageList")}"
                       class="btn btn-primary">取消</a>

                    <input type="submit" value="${message(code: 'default.button.create.label', default: 'Create')}"
                           class="btn btn-primary"/>
                </div>
            </form>
        </div>
    </div>
</div>

</body>
</html>