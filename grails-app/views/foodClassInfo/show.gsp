
<%@ page import="lj.data.FoodClassInfo" %>
<!DOCTYPE html>
<html>
	<head>
        <meta name="layout" content="main_template"/>
		<g:set var="entityName" value="${message(code: 'foodClassInfo.label', default: 'foodClassInfo')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>

    %{--<g:render template="../layouts/shopMenu"/>--}%

		<div id="show-foodClassInfo" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
        <g:render template="../layouts/msgs_and_errors"></g:render>
			<ol class="property-list foodClassInfo">

			
				<g:if test="${foodClassInfoInstance?.name}">
				<li class="fieldcontain">
					<span id="name-label" class="property-label"><g:message code="foodClassInfo.name.label" default="Name" /></span>
					
						<span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${foodClassInfoInstance}" field="name"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${foodClassInfoInstance?.description}">
				<li class="fieldcontain">
					<span id="description-label" class="property-label"><g:message code="foodClassInfo.description.label" default="Description" /></span>
					
						<span class="property-value" aria-labelledby="description-label"><g:fieldValue bean="${foodClassInfoInstance}" field="description"/></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${foodClassInfoInstance?.id}" />
					<g:link class="edit" action="edit" id="${foodClassInfoInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
