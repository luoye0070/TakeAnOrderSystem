<%@ page import="lj.data.FoodClassInfo" %>
<!DOCTYPE html>
<html>
	<head>
        <meta name="layout" content="main_template"/>
		<g:set var="entityName" value="${message(code: 'foodClassInfo.label', default: 'foodClassInfo')}" />
		<title><g:message code="default.create.label" args="[entityName]" /></title>
	</head>

	<body>

    %{--<g:render template="../layouts/shopMenu"/>--}%

    <div id="create-foodClassInfo" class="content scaffold-create" role="main" style="width: 800px;">
        <h1 style="margin: 9px 0px 9px 40px;" class="breadcrumb"><g:message code="default.create.label" args="[entityName]" /></h1>
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
	</body>
</html>
