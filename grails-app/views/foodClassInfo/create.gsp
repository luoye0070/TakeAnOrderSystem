<%@ page import="lj.data.FoodClassInfo" %>
<!DOCTYPE html>
<html>
	<head>
        <meta name="layout" content="main_template"/>
		<g:set var="entityName" value="${message(code: 'foodClassInfo.label', default: 'foodClassInfo')}" />
		<title><g:message code="default.create.label" args="[entityName]" /></title>
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

    <div id="create-foodClassInfo" class="content scaffold-create" role="main" style="width: 800px;">
        <g:render template="../layouts/msgs_and_errors"></g:render>
        <g:form action="save" class="form-horizontal well" style="margin: 9px 0px 9px 40px;"  >

            <g:render template="form"/>

            <div class="row">
                <div class="form-actions offset3">
                    <g:submitButton name="create" class="button button-primary" value="${message(code: 'default.button.create.label', default: 'Create')}" />
                </div>
            </div>
        </g:form>
    </div>

            </div>
	</body>
</html>
