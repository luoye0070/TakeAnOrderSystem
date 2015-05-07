<%@ page import="lj.data.CustomerRelations" %>


<!--
<div class="fieldcontain ${hasErrors(bean: customerRelationsInstance, field: 'restaurantId', 'error')} required">
	<label for="restaurantId">
<g:message code="customerRelations.restaurantId.label" default="Restaurant Id"/>
<span class="required-indicator">*</span>
</label>
<g:field name="restaurantId" type="number" value="${customerRelationsInstance?.restaurantId}" required=""/>
</div>
-->
<div class="control-group">
    <label class="control-label" for="customerClientId"><g:message code="customerRelations.customerClientId.label"
                                                                 default="Customer Client Id"/>
        <span class="required-indicator">*</span>
    </label>

    <div class="controls">
        <g:field name="customerClientId" type="number" value="${customerRelationsInstance?.customerClientId}" required=""/>
    </div>
</div>


<div class="control-group">
    <label class="control-label" for="type"><g:message code="customerRelations.type.label" default="Type"/>
        <span class="required-indicator">*</span>
    </label>

    <div class="controls">
        <select name="type" id="type" value="${customerRelationsInstance?.type}">
            <g:each in="${customerRelationsTypes}">
                <option value="${it.code}">${it.label}</option>
            </g:each>
        </select>
    </div>
</div>


<div class="control-group">
    <label class="control-label" for="customerUserName"> <g:message code="customerRelations.customerUserName.label"
                                                                   default="Customer User Name"/>
</label>
<div class="controls">
    <g:textField name="customerUserName" maxlength="32" value="${customerRelationsInstance?.customerUserName}"/>
</div>
</div>



