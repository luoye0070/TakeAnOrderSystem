<%@ page import="lj.data.TableInfo" %>



<div class="control-group">
    <label class="control-label" for="name"><g:message code="tableInfo.name.label" default="Name" />
        <span class="required-indicator">*</span>
    </label>
    <div class="controls">
        <g:textField name="name" maxlength="32" required="" value="${tableInfoInstance?.name}"/>
    </div>
</div>

<div class="control-group">
    <label class="control-label" for="minPeople"><g:message code="tableInfo.minPeople.label" default="Min People" />
        <span class="required-indicator">*</span>
    </label>
    <div class="controls">
        <g:field name="minPeople" type="number" min="1" value="${tableInfoInstance?.minPeople}" required=""/>
    </div>
</div>

<div class="control-group">
    <label class="control-label" for="maxPeople"><g:message code="tableInfo.maxPeople.label" default="Max People" />
        <span class="required-indicator">*</span>
    </label>
    <div class="controls">
        <g:field name="maxPeople" type="number" min="1" value="${tableInfoInstance?.maxPeople}" required=""/>
    </div>
</div>

<div class="control-group">
    <label class="control-label" for="canMultiOrder"><g:message code="tableInfo.canMultiOrder.label" default="Can Multi Order" />
    </label>
    <div class="controls">
        <g:checkBox name="canMultiOrder" value="${tableInfoInstance?.canMultiOrder}" />
    </div>
</div>

<div class="control-group">
    <label class="control-label" for="canReserve"><g:message code="tableInfo.canReserve.label" default="Can Reserve" />
    </label>
    <div class="controls">
        <g:checkBox name="canReserve" value="${tableInfoInstance?.canReserve}" />
    </div>
</div>

<div class="control-group">
    <label class="control-label" for="enabled"><g:message code="tableInfo.enabled.label" default="Enabled" />
    </label>
    <div class="controls">
        <g:checkBox name="enabled" value="${tableInfoInstance?.enabled}" />
    </div>
</div>

<div class="control-group">
    <label class="control-label" for="description"><g:message code="tableInfo.description.label" default="Description" />
    </label>
    <div class="controls">
        <g:textArea name="description" cols="40" rows="5" maxlength="1024" value="${tableInfoInstance?.description}"/>
    </div>
</div>





