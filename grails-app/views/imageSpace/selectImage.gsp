<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 13-12-9
  Time: 上午3:49
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <link rel="stylesheet" href="${resource(dir: 'js/JessicaWhite/css', file: 'bootstrap.css')}">
    <link rel="stylesheet" href="${resource(dir: 'js/JessicaWhite/css', file: 'theme.css')}">
    <link rel="stylesheet" href="${resource(dir: 'js/JessicaWhite/css/skins/tango/', file: 'skin.css')}">
    <link rel="stylesheet" href="${resource(dir: 'js/JessicaWhite/css', file: 'bootstrap-responsive.css')}">
    <!--[if lt IE 9]>
    <g:javascript src="html5.js"/>
    <![endif]-->
    <!--script-->
    <g:javascript src="jquery-1.8.2.min.js"/>
    <g:javascript src="JessicaWhite/js/jquery.easing.1.3.js"/>
    <g:javascript src="JessicaWhite/js/jquery.mobile.customized.min.js"/>
    <g:javascript src="JessicaWhite/js/bootstrap.js"/>
    <g:javascript src="JessicaWhite/js/jquery.scrollUp.min.js"/>
    <title></title>
    <script type="text/javascript">
        function selectImage(imgObj){
            var imgUrl=$(imgObj).attr("data");
            parent.${params.callBack}(imgUrl);
        }
    </script>
</head>

<body>
<div class="span8">
    <g:render template="../layouts/msgs_and_errors"></g:render>
    <br/>
    类别：
    <g:each in="${imgClassList}">
        <a href="${createLink(controller: "imageSpace", action: "selectImage", params: [classId: it.id])}"
            <g:if test="${params.classId == it.id.toString() || (!(params.classId) && it.id == 0)}">
                class="btn btn-danger"
            </g:if>
            <g:else>
                class="btn btn-link "
            </g:else>>${it.id ? it.name : "全部"}</a><!---图片空间-->

    </g:each>

    <g:if test="${imageList}">
        <ul class="thumbnails">
            <g:each in="${imageList}" status="i" var="imageInfoInstance">
                <li class="span2">
                    <div class="thumbnail"><img onclick="selectImage(this)" data="${imageInfoInstance?.url}"
                            src="${createLink(controller: "imageShow", action: "downloadThumbnail", params: [imgUrl: fieldValue(bean: imageInfoInstance, field: "url")])}">
                             <label>${fieldValue(bean: imageInfoInstance, field: "fileName")}</label>
                    </div>
                </li>
            </g:each>
        </ul>
        <taos:paginate total="${totalCount}" prev="&larr;" next="&rarr;" params="${params}" max="12"/>
    </g:if>
    <g:else>
        <br/>
        <span class="label">没有图片</span>
    </g:else>
</div>
</body>
</html>