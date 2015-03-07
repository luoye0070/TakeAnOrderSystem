<div class="navbar navbar-inner">
<ul class="nav">
    <g:if test="${controllerName=="staff" && actionName=="orderList"}">
        <li class="active"><a href="#">订单列表</a></li>
    </g:if>
    <g:else>
        <li><g:link controller="staff" action="orderList">订单列表</g:link></li>
    </g:else>
    <li class="divider-vertical"></li>
    <g:if test="${controllerName=="staff" && actionName=="dishList"}">
        <li class="active"><a href="#">点菜列表</a></li>
    </g:if>
    <g:else>
        <li><g:link controller="staff" action="dishList">点菜列表</g:link></li>
    </g:else>
    <li class="divider-vertical"></li>
    <g:if test="${controllerName=="staff" && actionName in ["tablesShow"]}">
        <li class="active"><a href="#">桌位预定</a></li>
    </g:if>
    <g:else>
        <li><g:link controller="staff" action="tablesShow">桌位预定</g:link></li>
    </g:else>
    <li class="divider-vertical"></li>
    <g:if test="${controllerName=="staff" && actionName in ["orderInput"]}">
        <li class="active"><a href="#">订单创建</a></li>
    </g:if>
    <g:else>
        <li><g:link controller="staff" action="orderInput">订单创建</g:link></li>
    </g:else>
    <li class="divider-vertical"></li>
    <g:if test="${controllerName=="staff" && actionName in ["makeTakeOutOrder"]}">
        <li class="active"><a href="#">外卖订单创建</a></li>
    </g:if>
    <g:else>
        <li><g:link controller="staff" action="makeTakeOutOrder">外卖订单创建</g:link></li>
    </g:else>
    <li class="divider-vertical"></li>
</ul>
</div>