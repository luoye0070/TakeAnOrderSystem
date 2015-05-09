<div class="navbar navbar-inner">
<ul class="nav">
    <g:if test="${controllerName=="reserveCustomer" && actionName in ["reserveDinnerTimeInput","reserveTables","createReserveOrder","dishOfReserveOrder"]}">
        <li class="active"><a href="#">桌位预定</a></li>
    </g:if>
    <g:else>
        <li><g:link controller="reserveCustomer" action="reserveDinnerTimeInput">桌位预定</g:link></li>
    </g:else>
    <li class="divider-vertical"></li>
    <g:if test="${controllerName=="reserveCustomer" && actionName in ["reserveOrderList","reserveOrderDetail"]}">
        <li class="active"><g:link controller="reserveCustomer" action="reserveOrderList">预定列表</g:link></li>
    </g:if>
    <g:else>
        <li><g:link controller="reserveCustomer" action="reserveOrderList">预定列表</g:link></li>
    </g:else>
</ul>
</div>