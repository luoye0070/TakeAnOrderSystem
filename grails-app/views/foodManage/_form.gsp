<%@ page import="lj.data.FoodInfo" %>

<div class="control-group">
    <label class="control-label" for="name"><g:message code="foodInfo.foodClassInfo.label" default="Food Class" />
        <span class="required-indicator">*</span>
    </label>
    <div class="controls">
        <select id="classId" name="foodClassInfo.id">
            <option value="0" ${foodInfoInstance?.foodClassInfo==null?"selected=\"selected\"":""}>请选择</option>
            <g:if test="${foodClassList}">
                <g:each in="${foodClassList}" var="foodClass">
                    <option value="${foodClass.id}" ${foodInfoInstance?.foodClassInfo?.id==foodClass.id?"selected=\"selected\"":""}>${foodClass.name}</option>
                </g:each>
            </g:if>
        </select>
    </div>
</div>

<div class="control-group">
    <label class="control-label" for="name"><g:message code="foodInfo.name.label" default="Name" />
        <span class="required-indicator">*</span>
    </label>
    <div class="controls">
        <g:textField name="name" maxlength="32" required="" value="${foodInfoInstance?.name}"/>
    </div>
</div>

<div class="control-group">
    <label class="control-label" for="price"><g:message code="foodInfo.price.label" default="Price" />
        <span class="required-indicator">*</span>
    </label>
    <div class="controls">
        <g:textField name="price" value="${fieldValue(bean: foodInfoInstance, field: 'price')}" required=""/>
    </div>
</div>

<div class="control-group">
    <label class="control-label" for="enabled"><g:message code="foodInfo.enabled.label" default="Enabled" />
    </label>
    <div class="controls">
        <g:checkBox name="enabled" value="${foodInfoInstance?.enabled}" />
    </div>
</div>

%{--<div class="control-group">--}%
    %{--<label class="control-label" for="countLimit"><g:message code="foodInfo.countLimit.label" default="Count Limit" />--}%
        %{--<span class="required-indicator">*</span>--}%
    %{--</label>--}%
    %{--<div class="controls">--}%
        %{--<g:field name="countLimit" type="number" min="0" value="${foodInfoInstance?.countLimit}" required=""/>--}%
    %{--</div>--}%
%{--</div>--}%

%{--<div class="control-group">--}%
    %{--<label class="control-label" for="isSetMeal"><g:message code="foodInfo.isSetMeal.label" default="Is Set Meal" />--}%
        %{--<span class="required-indicator">*</span>--}%
    %{--</label>--}%
    %{--<div class="controls">--}%
        %{--<g:checkBox name="isSetMeal" value="${foodInfoInstance?.isSetMeal}" />--}%
    %{--</div>--}%
%{--</div>--}%

<div class="control-group">
    <label class="control-label" for="originalPrice"><g:message code="foodInfo.originalPrice.label" default="Original Price" />
    </label>
    <div class="controls">
        <g:textField name="originalPrice" value="${fieldValue(bean: foodInfoInstance, field: 'originalPrice')}"/>
    </div>
</div>

<div class="control-group">
    <label class="control-label" for="isReady"><g:message code="foodInfo.isReady.label" default="isReady" />
    </label>
    <div class="controls">
        <g:checkBox name="isReady" value="${foodInfoInstance?.isReady}" />
    </div>
</div>

<div class="control-group">
    <label class="control-label" for="originalPrice"><g:message code="foodInfo.image.label" default="Image" />
    </label>
    <div class="controls">
        <input type="hidden" id="imageHidden" name="image" value="${foodInfoInstance?.image}"/>
        <img src="${createLink(controller: "imageShow", action: "downloadThumbnail", params: [imgUrl: foodInfoInstance?.image])}" id="imageShow"/>
        <input type="button" value="从图片空间选择一张图片" onclick="selectImage()"/>
    </div>
</div>

%{--<div class="control-group">--}%
    %{--<label class="control-label" for="canTakeOut"><g:message code="foodInfo.canTakeOut.label" default="Can Take Out" />--}%
    %{--</label>--}%
    %{--<div class="controls">--}%
        %{--<g:checkBox name="canTakeOut" value="${foodInfoInstance?.canTakeOut}" />--}%
    %{--</div>--}%
%{--</div>--}%

<div class="control-group" style="width: 900px;height: 600px">
    <label class="control-label" for="description"><g:message code="foodInfo.description.label" default="Description" />
    </label>
    <div class="controls" style="width: 800px;height: 500px">
        <g:textArea name="description"  style="width: 800px;height: 500px" cols="40" rows="5" maxlength="1024" value="${foodInfoInstance?.description}"/>
    </div>
</div>





