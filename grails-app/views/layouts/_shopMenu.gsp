<div class="navbar navbar-inner">
<ul class="nav">
    <li class="${(controllerName=="shop")?"active":""}">
        <g:link controller="shop" action="index">饭店信息</g:link>
    </li>
    <li class="divider-vertical"></li>
    <li class="${(controllerName=="tableManage")?"active":""}">
        <g:link controller="tableManage" action="index">桌位管理</g:link>
    </li>
    <li class="divider-vertical"></li>
    <li class="${(controllerName=="foodClassInfo")?"active":""}">
        <g:link controller="foodClassInfo" action="index">菜品类别</g:link>
    </li>
    <li class="divider-vertical"></li>
    <li class="${(controllerName=="foodManage")?"active":""}">
        <g:link controller="foodManage" action="index">菜品管理</g:link>
    </li>
    <li class="divider-vertical"></li>
    <li class="${(controllerName=="imageSpace")?"active":""}">
        <g:link controller="imageSpace" action="index">图片空间</g:link>
    </li>
    <li class="divider-vertical"></li>
    <li class="${(controllerName=="staffManage")?"active":""}">
        <g:link controller="staffManage" action="index">员工管理</g:link>
    </li>
    <li class="divider-vertical"></li>
    <li class="${(controllerName=="customerRelations")?"active":""}">
        <g:link controller="customerRelations" action="index">客户关系</g:link>
    </li>
</ul>
</div>