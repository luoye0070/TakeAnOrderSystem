<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 13-11-20
  Time: 下午11:16
  To change this template use File | Settings | File Templates.
--%>

<%@ page import="java.text.SimpleDateFormat; lj.enumCustom.OrderStatus; lj.enumCustom.OrderValid; lj.enumCustom.ReserveType; lj.FormatUtil" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="staff_mobile_template">
    <title>订单列表</title>
    <link type="text/css" href="${resource(dir: "js/bootstrap-3.3.4/css", file: "bootstrap-datetimepicker.min.css")}" rel="stylesheet" />
    <script type="text/javascript" src="${resource(dir: "js/bootstrap-3.3.4/js", file: "bootstrap-datetimepicker.min.js")}" charset="UTF-8"></script>
    <script type="text/javascript" src="${resource(dir: "js/bootstrap-3.3.4/js/locales", file: "bootstrap-datetimepicker.zh-CN.js")}" charset="UTF-8"></script>
    <script type="text/javascript">
        $(function () {
            $('#beginDate').datetimepicker({
                language:  'zh-CN',
                autoclose: 1,
                todayBtn:  1,
                format: 'yyyy-mm-dd hh:ii:ss'
            });
            $('#endDate').datetimepicker({
                language:  'zh-CN',
                autoclose: 1,
                todayBtn:  1,
                format: 'yyyy-mm-dd hh:ii:ss'
            });
        });
    </script>
</head>

<body>
<div class="row" style="margin-bottom: 10px;">
<div class="col-xs-8">
<h4 style="margin-top: 0px;">订单列表</h4>
</div>
<div class="col-xs-4">
<button class="btn" type="button" data-toggle="collapse" data-target="#collapseExample" aria-expanded="false" aria-controls="collapseExample">
    %{--<span class="caret"></span>--}%
    展开/收起
</button>
    </div>
</div>
<!--提示消息-->
%{--<g:set var="errors" value="测试一个错误"/>--}%
<g:render template="/layouts/staff_mobile_msgs_and_errors"></g:render>

<form style="margin-top: 0px;" class="well form-horizontal collapse"  id="collapseExample" action="${createLink(controller: "staff", action: "orderList")}">
    <div class="form-group">
        <%
            Date now=new Date();
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar calendar=Calendar.getInstance();
            String beginTimeStr=params.beginTime;
            if(beginTimeStr==null){
                calendar.setTime(now);
                calendar.add(Calendar.DATE,-1);
                calendar.set(Calendar.HOUR_OF_DAY,0);
                calendar.set(Calendar.MINUTE,0);
                calendar.set(Calendar.SECOND,0);
                beginTimeStr=simpleDateFormat.format(calendar.getTime());
            }
            String endTimeStr=params.endTime;
                if(endTimeStr==null){
                calendar.setTime(now);
                calendar.add(Calendar.DATE,1);
                calendar.set(Calendar.HOUR_OF_DAY,0);
                calendar.set(Calendar.MINUTE,0);
                calendar.set(Calendar.SECOND,0);
                endTimeStr=simpleDateFormat.format(calendar.getTime());
            }
         %>
        <div class="col-sm-4" style="margin-top: 15px;">
            <select name="tableId" class="form-control">
                <option value="0" ${params.tableId == "0" ? "selected='selected'" : ""}>全部</option>
                <g:each in="${lj.data.TableInfo.list()}" var="tableInfo">
                        <option value="${tableInfo?.id}" ${params.tableId == tableInfo?.id.toString() ? "selected='selected'" : ""}>${tableInfo.name}</option>
                </g:each>
            </select>
        </div>

        <div class="col-sm-4" style="margin-top: 15px;">
            <select name="valid" class="form-control">
                <%
                    def paramsT=params.clone();
                    if(paramsT.valid==null){
                        paramsT.valid=OrderValid.EFFECTIVE_VALID.code+"";
                    }
                %>
                <option value="-1" ${paramsT.valid == "-1" ? "selected='selected'" : ""}>全部</option>
                <g:each in="${lj.enumCustom.OrderValid.valids}">
                    <option value="${it.code}" ${paramsT.valid == it.code.toString() ? "selected='selected'" : ""}>${it.label}</option>
                </g:each>
            </select>
        </div>

        <div class="col-sm-4" style="margin-top: 15px;">
            <select name="status" class="form-control">
                <option value="-1" ${params.status == "-1" ? "selected='selected'" : ""}>全部</option>
                <g:each in="${lj.enumCustom.OrderStatus.statuses}">
                    <option value="${it.code}" ${params.status == it.code.toString() ? "selected='selected'" : ""}>${it.label}</option>
                </g:each>
            </select>
        </div>

        <div class="col-sm-4" style="margin-top: 15px;">
            %{--<input id="beginDate" name="beginTime"  type="text" readonly="" class="form-control"--}%
                   %{--value="${beginTimeStr}" placeholder="请输入查询开始时间"/>--}%

            <div id="beginDate" style="padding: 0px;" class="form-control input-group date form_datetime col-md-5" data-date="${beginTimeStr}" data-link-field="dtp_input1">
                <input class="form-control" size="16" type="text" value="${beginTimeStr}" readonly>
                <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
                <span class="input-group-addon"><span class="glyphicon glyphicon-th"></span></span>
            </div>
            <input type="hidden" id="dtp_input1" value="${beginTimeStr}"  name="beginTime" />
        </div>
        <div class="col-sm-4" style="margin-top: 15px;">
            %{--<input id="endDate" name="endTime" type="text" readonly=""  class="form-control"--}%
                   %{--value="${endTimeStr}" placeholder="请输入查询结束时间"/>--}%
            <div id="endDate" style="padding: 0px;" class="form-control input-group date form_datetime col-md-5" data-date="${endTimeStr}" data-link-field="dtp_input2">
                <input class="form-control" size="16" type="text" value="${endTimeStr}" readonly>
                <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
                <span class="input-group-addon"><span class="glyphicon glyphicon-th"></span></span>
            </div>
            <input type="hidden" id="dtp_input2" value="${endTimeStr}"  name="endTime" />
        </div>

        <div class="col-sm-4" style="margin-top: 15px;">
            <input type="submit"
                   value="${message(code: 'default.button.search.label', default: 'search')}"
                   class="form-control btn btn-primary"/>
        </div>
    </div>
</form>

<g:if test="${orderList}">
    <!--订单列表-->
    <table class="table table-striped table-bordered table-condensed">
        <thead>
        <tr>
            <g:sortableColumn property="id" title="${message(code: 'orderInfo.id.label', default: 'id')}"  params="${params}"/>

            <g:sortableColumn property="tableInfo.id"
                              title="${message(code: 'orderInfo.tableName.label', default: 'Table Name')}" params="${params}"/>

            <g:sortableColumn property="createTime"
                              title="${message(code: 'orderInfo.createTime.label', default: 'Time')}" params="${params}"/>

            <g:sortableColumn property="valid" title="${message(code: 'orderInfo.valid.label', default: 'Valid')}" params="${params}"/>

            <g:sortableColumn property="status" title="${message(code: 'orderInfo.status.label', default: 'Status')}" params="${params}"/>

            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <g:each in="${orderList}" status="i" var="orderInfoInstance">
            <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
                <td>${orderInfoInstance?.numInRestaurant}</td>

                <td>
                    <g:if test="${orderInfoInstance?.tableInfo?.name}">
                        ${orderInfoInstance?.tableInfo?.name}
                    </g:if>
                    <g:else>
                        桌位&nbsp;${orderInfoInstance?.tableInfo?.id}
                    </g:else>
                </td>

                <td>${FormatUtil.dateTimeFormat(orderInfoInstance.createTime)}</td>

                <td>${OrderValid.getLable(orderInfoInstance.valid)}</td>

                <td>${OrderStatus.getLable(orderInfoInstance.status)}</td>

                <td><a href="${createLink(controller: "staff", action: "orderShow", params: [orderId: orderInfoInstance.id])}">订单详情</a>
                </td>
            </tr>
        </g:each>
        </tbody>
    </table>
    <!--分页-->
    <taos:paginateForBs3 action="orderList" total="${totalCount ?: 0}" prev="&larr;" next="&rarr;" params="${params}"/>
</g:if>
<g:else>
    <div style="text-align: center">
        <g:if test="${firstLoad}">
            <label style="text-align: center">请点击搜索来查找订单</label>
        </g:if>
        <g:else>
            <label style="text-align: center">没有符合条件的订单哦</label>
        </g:else>
    </div>
</g:else>

</body>
</html>