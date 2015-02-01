<%@ page import="lj.data.FoodClassInfo" %>
<!DOCTYPE html>
<html>
	<head>
        <meta name="layout" content="main_template"/>
		<g:set var="entityName" value="${message(code: 'foodClassInfo.label', default: 'foodClassInfo')}" />
		<title><g:message code="default.edit.label" args="[entityName]" /></title>
	</head>
	<body>

    %{--<g:render template="../layouts/shopMenu"/>--}%

    <div id="edit-foodClassInfo" class="content scaffold-edit" role="main" style="width: 800px;">
        <h1 style="margin: 9px 0px 9px 40px;" class="breadcrumb"><g:message code="default.edit.label" args="[entityName]" /></h1>
        <g:render template="../layouts/msgs_and_errors"></g:render>

        <g:form method="post" action="update" class="form-horizontal well" style="margin: 9px 0px 9px 40px;"  >
        <g:hiddenField name="id" value="${foodClassInfoInstance?.id}" />
        <g:hiddenField name="version" value="${foodClassInfoInstance?.version}" />

            <g:render template="form"/>

            <div class="row">
                <div class="form-actions offset3">
                    <g:actionSubmit class="button button-primary" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" />
                    <g:actionSubmit class="button" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" formnovalidate="" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
                </div>
            </div>
        </g:form>
    </div>
	</body>
</html>
