<%@ page import="lj.data.FoodClassInfo" %>



<div class="row">
    
%{--<div class="control-group span8 ${hasErrors(bean: foodClassInfoInstance, field: 'restaurantId', 'error')} required">--}%
    %{--<label class="control-label" for="restaurantId">--}%
        %{--<g:message code="foodClassInfo.restaurantId.label" default="Restaurant Id"/>--}%
        %{--<span class="required-indicator">*</span>--}%
    %{--</label>--}%

    %{--<div class="controls">--}%
        %{--<g:field name="restaurantId" type="number" min="1" value="${foodClassInfoInstance.restaurantId}" required="" class="control-text"/>--}%
    %{--</div>--}%
%{--</div>--}%



<div class="control-group span8 ${hasErrors(bean: foodClassInfoInstance, field: 'name', 'error')} required">
    <label class="control-label" for="name">
        <g:message code="foodClassInfo.name.label" default="Name"/>
        <span class="required-indicator">*</span>
    </label>

    <div class="controls">
        <g:textField name="name" maxlength="16" required="" value="${foodClassInfoInstance?.name}" class="control-text"/>
    </div>
</div>



</div>

<div class="row">
    
<div class="control-group span8 ${hasErrors(bean: foodClassInfoInstance, field: 'description', 'error')} ">
    <label class="control-label" for="description">
        <g:message code="foodClassInfo.description.label" default="Description"/>
        
    </label>

    <div class="controls">
        <g:textArea name="description" cols="40" rows="5" maxlength="256" value="${foodClassInfoInstance?.description}" class="control-text"/>
    </div>
</div>



</div>

