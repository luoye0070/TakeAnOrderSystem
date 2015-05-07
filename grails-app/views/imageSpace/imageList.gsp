<%@ page import="lj.FormatUtil" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main_template"/>
    <title>图片列表</title>
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


        <div class="well" style="padding: 8px 0;">
            <ul class="nav nav-list">
                <g:if test="${'true'.equals(params.isDel)}">
                    <li><a href="<g:createLink action="imageList" controller="imageSpace"
                                               params="[isDel: false]"/>">图片空间</a></li>
                    <li><a href="<g:createLink action="upload" controller="imageSpace"/>">上传图片</a></li>
                    <li><a href="<g:createLink action="imageClassList" controller="imageSpace"/>">图片分类</a></li>
                    <li class="active"><a href="#">回收站</a></li>
                </g:if>
                <g:else>
                    <li class="active"><a href="#">图片空间</a></li>
                    <li><a href="<g:createLink action="upload" controller="imageSpace"/>">上传图片</a></li>
                    <li><a href="<g:createLink action="imageClassList" controller="imageSpace"/>">图片分类</a></li>
                    <li><a href="<g:createLink action="imageList" controller="imageSpace"
                                               params="[isDel: true]"/>">回收站</a></li>
                </g:else>
            </ul>
        </div>


        </div>

        <div class="span7">
            <g:render template="../layouts/msgs_and_errors"></g:render>



            <br/>
            类别：
            <g:each in="${imgClassList}">
                <a href="${createLink(controller: "imageSpace", action: "imageList", params: [classId: it.id])}"
                    <g:if test="${params.classId == it.id.toString() || (!(params.classId) && it.id==0)}">
                        class="btn btn-danger"
                    </g:if>
                    <g:else>
                        class="btn btn-link "
                    </g:else> >${it.id ? it.name : "全部"}</a><!---图片空间-->

            </g:each>

        <!--图片空间容量信息-->
            <div>
                总空间：${FormatUtil.byteToGB(restaurantInfo.imageSpaceSize)}
                已用空间：${FormatUtil.byteToKB(restaurantInfo.imageSpaceUsedSize)}
                图片数量：${totalCount ?: 0}
            </div>



            <g:if test="${imageList}">

                <table class="table table-striped table-bordered table-condensed">
                    <thead>
                    <tr>

                        %{--<g:sortableColumn property="restaurantId"--}%
                                          %{--title="${message(code: 'imageInfo.restaurantId.label', default: 'Restaurant Id')}"/>--}%

                        <g:sortableColumn property="url"
                                          title="${message(code: 'imageInfo.url.label', default: 'Url')}"/>

                        <g:sortableColumn property="url"
                                          title="${message(code: 'imageInfo.fileName.label', default: 'File Name')}"/>

                        %{--<g:sortableColumn property="width"--}%
                                          %{--title="${message(code: 'imageInfo.width.label', default: 'Width')}"/>--}%

                        %{--<g:sortableColumn property="height"--}%
                                          %{--title="${message(code: 'imageInfo.height.label', default: 'Height')}"/>--}%

                        %{--<g:sortableColumn property="isDel"--}%
                                          %{--title="${message(code: 'imageInfo.isDel.label', default: 'Is Del')}"/>--}%

                        %{--<g:sortableColumn property="classId"--}%
                                          %{--title="${message(code: 'imageInfo.classId.label', default: 'Class Id')}"/>--}%

                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <g:each in="${imageList}" status="i" var="imageInfoInstance">
                        <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                            %{--<td><g:link action="show"--}%
                                        %{--id="${imageInfoInstance.id}">${fieldValue(bean: imageInfoInstance, field: "restaurantId")}</g:link></td>--}%

                            <td>
                            <a target="_blank" href="${createLink(controller: "imageShow", action: "download", params: [imgUrl: fieldValue(bean: imageInfoInstance, field: "url")])}">
                            <img
                                    src="${createLink(controller: "imageShow", action: "downloadThumbnail", params: [imgUrl: fieldValue(bean: imageInfoInstance, field: "url")])}">
                            </a>
                            </td>

                            <td>${imageInfoInstance?.fileName}</td>

                            %{--<td>${fieldValue(bean: imageInfoInstance, field: "width")}</td>--}%

                            %{--<td>${fieldValue(bean: imageInfoInstance, field: "height")}</td>--}%

                            %{--<td><g:formatBoolean boolean="${imageInfoInstance.isDel}"/></td>--}%

                            %{--<td>${fieldValue(bean: imageInfoInstance, field: "classId")}</td>--}%

                            <td>
                                <g:if test="${params.isDel&&params.isDel=="true"}">
                                    <a href="${createLink(controller: "imageSpace", action: "recoverFromRecycled", params: [ids: imageInfoInstance.id])}"
                                       class="btn btn-link">从回收站恢复</a>
                                </g:if>
                                <g:else>
                                    <a href="${createLink(controller: "imageSpace", action: "recycleToRecycled", params: [ids: imageInfoInstance.id])}"
                                       class="btn btn-link">放入回收站</a>
                                </g:else>
                                <a href="${createLink(controller: "imageSpace", action: "delImage", params: [ids: imageInfoInstance.id])}"
                                   class="btn btn-link">删除图片</a>
                            </td>

                        </tr>
                    </g:each>
                    </tbody>
                </table>
                <taos:paginate total="${totalCount}" prev="&larr;" next="&rarr;" params="${params}" max="12"/>
            </g:if>
            <g:else>
                <br/>
                <span class="label">没有图片</span>
            </g:else>
        </div>
    </div>
</div>

</body>
</html>