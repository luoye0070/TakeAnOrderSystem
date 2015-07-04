
<%@ page import="lj.data.FoodClassInfo" %>
<!DOCTYPE html>
<html>
	<head>
        <meta name="layout" content="main_template"/>
		<g:set var="entityName" value="${message(code: 'foodClassInfo.label', default: 'foodClassInfo')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
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
    <g:render template="../layouts/shopMenu"/>
        </div>
        <div  class="span10" style="margin-left: 10px;margin-top: 10px;">
            <g:render template="../layouts/msgs_and_errors"></g:render>

		    <div class="button-group" style="margin: 9px 0px 9px 40px;">
			<ul>
				<li><g:link   class="btn btn-primary" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>


			<table  class="table table-striped table-bordered table-condensed" cellspacing="0">
				<thead>
					<tr>
					
						%{--<g:sortableColumn property="restaurantId" title="${message(code: 'foodClassInfo.restaurantId.label', default: 'Restaurant Id')}" />--}%
					
						<g:sortableColumn property="name" title="${message(code: 'foodClassInfo.name.label', default: 'Name')}" />
					
						<g:sortableColumn property="description" title="${message(code: 'foodClassInfo.description.label', default: 'Description')}" />
					
                        <th>操作</th>
					</tr>
				</thead>
				<tbody>
				<g:each in="${foodClassInfoInstanceList}" status="i" var="foodClassInfoInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						%{--<td>${fieldValue(bean: foodClassInfoInstance, field: "restaurantId")}</td>--}%
					
						<td>
                            <g:link action="show" id="${foodClassInfoInstance.id}">
                                ${fieldValue(bean: foodClassInfoInstance, field: "name")}
                            </g:link>
                        </td>
					
						<td>${fieldValue(bean: foodClassInfoInstance, field: "description")}</td>
					

                        <td>
                            <g:form method="post" action="update" >
                                <g:hiddenField name="id" value="${foodClassInfoInstance?.id}" />
                                <g:hiddenField name="version" value="${foodClassInfoInstance?.version}" />
                                <g:actionSubmit  class="btn btn-link" action="edit" value="${message(code: 'default.button.edit.label', default: 'edit')}" formnovalidate="" />
                                <g:actionSubmit  class="btn btn-link" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" formnovalidate="" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
                            </g:form>
                        </td>
					</tr>
				</g:each>
				</tbody>
			</table>
                    </div>
			<div class="pagination pull-right">
				<taos:paginate total="${foodClassInfoInstanceTotal?:0}" />
			</div>
            </div>
	</body>
</html>
