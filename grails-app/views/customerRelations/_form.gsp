<%@ page import="lj.data.CustomerRelations" %>



%{--<div class="fieldcontain ${hasErrors(bean: customerRelationsInstance, field: 'restaurantId', 'error')} required">--}%
	%{--<label for="restaurantId">--}%
%{--<g:message code="customerRelations.restaurantId.label" default="Restaurant Id"/>--}%
%{--<span class="required-indicator">*</span>--}%
%{--</label>--}%
%{--<g:field name="restaurantId" type="number" value="${customerRelationsInstance?.restaurantId}" required=""/>--}%
%{--</div>--}%

<div class="control-group">
    <label class="control-label" for="customerClientId"><g:message code="customerRelations.customerClient.label"
                                                                 default="Customer Client"/>
        <span class="required-indicator"></span>
    </label>

    <div class="controls">
        <g:select name="customerClientId" from="${userList}" optionKey="clientId" optionValue="userName" noSelection="${['0':'请选择']}"
            value="${customerRelationsInstance?.customerClient?.id}"></g:select>
    </div>
</div>


<div class="control-group">
    <label class="control-label" for="type"><g:message code="customerRelations.type.label" default="Type"/>
        <span class="required-indicator">*</span>
    </label>

    <div class="controls">
        <g:select name="type" from="${customerRelationsTypes}" optionKey="code" optionValue="label"
                  value="${customerRelationsInstance?.type}"></g:select>
    </div>
</div>


<div class="control-group">
    <label class="control-label" for="customerUserName"> <g:message code="customerRelations.customerUserName.label"
                                                                   default="Customer User Name"/>
</label>
<div class="controls">
    <g:textField name="customerUserName" maxlength="32" value="${customerRelationsInstance?.customerClient?.clientMark}"/>
</div>
</div>



