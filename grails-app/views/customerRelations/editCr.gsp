<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main_template"/>
    <title>编辑客户关系</title>
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

        <form method="post" action="editCr" class="form-horizontal">
            <g:hiddenField name="crId" value="${customerRelationsInstance?.id}"/>
            <g:hiddenField name="version" value="${customerRelationsInstance?.version}"/>
            <fieldset class="form">
                <g:render template="form"/>
            </fieldset>
            <fieldset class="form-actions">
                <a href="${createLink(controller: "customerRelations", action: "crList")}" class="btn send_btn">取消</a>
                <g:submitButton name="create"
                                value="${message(code: 'default.button.create.label', default: 'Create')}"
                                class="btn send_btn"/>
            </fieldset>
        </form>
    </div>
</div>
</body>
</html>