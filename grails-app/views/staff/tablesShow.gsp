<%@ page import="lj.enumCustom.ReserveType" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main_template"/>
    <title>桌位预定</title>
    <style type="text/css">
    .mc_main {
        width: 1000px;
        height: auto;
        margin: 0px 50px;
        background-color: #FFFFFF;
        float: left;
    }

    .mcm_top {
        width: 960px;
        height: auto;
        margin: 20px;
        margin-bottom: 0px;
        /*border-bottom: 4px solid #FF9833;
        text-indent: 1em;
        line-height: 80px;
        font-size: 20px;
        font-weight: bolder;*/
    }

    .mcm_content {
        width: 960px;
        height: auto;
        margin: 20px;
    }

    .mcmc_ssl {
        width: 960px;
        margin-top: 10px;
        margin-bottom: 10px;
    }

    .mcmcs_field {
        width: 220px;
        float: left;
    }

    .mcmcsf_input {
        width: 120px;
    }
    </style>
    <link rel="stylesheet" href="${resource(dir: "js/timePicker", file: "timePicker.css")}" type="text/css"
          media="screen"/>
    <script type="text/javascript" src="${resource(dir: "js/timePicker", file: "jquery.timePicker.min.js")}"></script>
    <link rel="stylesheet" href="${resource(dir: "js/Datepicker", file: "datepicker.css")}" type="text/css"
          media="screen"/>
    <script type="text/javascript" src="${resource(dir: "js/Datepicker", file: "bootstrap-datepicker.js")}"></script>
    <script type="text/javascript">
        $(function () {
            //根据上次选择选择桌号
            var tableIds =${params.tableId.toString()};
            if (isNaN(tableIds)) {
                for (i = 0; i < tableIds.length; i++) {
                    //alert(tableIds[i]);
                    $("input[name='tableId'][value='" + tableIds[i] + "']").attr("checked", "checked");
                }
            }
            else {
                //alert(tableIds);
                $("input[name='tableId'][value='" + tableIds + "']").attr("checked", "checked");
            }

            //时间选择器
            $("#time").timePicker({step: 15});
            $("#time").change(function () {
                var timeV = $("#time").val();
                if (timeV.length > 0) {
                    $("#time").val(timeV + ":00");
                }
            });
            //日期选择器
            $("#date").datepicker({format: "yyyy-mm-dd"});
        });
    </script>
</head>

<body>

<div class="mc_main">
    <div class="mcm_top">
        <g:render template="../layouts/staffMenu"></g:render>
    </div>
    %{--<div class="mcm_top">--}%
        %{--${params.restaurantName}--}%
    %{--</div>--}%

    <div class="mcm_content">

    <g:render template="../layouts/msgs_and_errors"></g:render>

        <div class="mcmc_ssl">
            <form class="well form-inline" action="${createLink(controller: "staff", action: "tablesShow")}">
                <input type="hidden" name="restaurantId" value="${params.restaurantId}"/>
                <input type="hidden" name="restaurantName" value="${params.restaurantName}"/>

                <div class="mcmcs_field">
                    <label>
                        预定日期：
                    </label>
                    <input id="date" name="date" type="text"
                           value="${params.date ?: lj.FormatUtil.dateFormat(new Date())}" class="mcmcsf_input"/>
                </div>

                <div class="mcmcs_field">
                    <label>
                        用餐类型：
                    </label>
                    <select name="reserveType" class="mcmcsf_input">
                        <g:each in="${lj.enumCustom.ReserveType.reserveTypes}">
                            <option value="${it.code}" ${(lj.Number.toInteger(params.reserveType) == it.code) ? "selected='selected'" : ""}>${it.label}</option>
                        </g:each>
                    </select>
                </div>

                <div class="ms_field_small">
                    <input type="submit" value="${message(code: 'default.button.search.label', default: 'search')}"
                           class="btn btn-primary"/>
                </div>
            </form>
        </div>

        <!--查询列表-->
        <form method="post" action="${createLink(controller: "infoShow", action: "tablesShow")}">

        </form>

        <g:if test="${tableList}">

            <form class="form-horizontal" action="${createLink(controller: "staff", action: "reserveTable")}">
                <input type="hidden" name="restaurantId" value="${params.restaurantId}"/>
                <input type="hidden" name="restaurantName" value="${params.restaurantName}"/>
                <input type="hidden" name="date" value="${params.date ?: lj.FormatUtil.dateFormat(new Date())}"/>
                <input type="hidden" name="reserveType" value="${params.reserveType ?: "1"}"/>
                <input type="hidden" name="reserveTypeTimes" value="${params.reserveTypeTimes}"/>

                <fieldset>

                    <div class="control-group">
                        <label class="control-label">预定日期</label>

                        <div class="controls">
                            <input class="input-xlarge focused" type="text" disabled="true"
                                   value="${params.date ?: lj.FormatUtil.dateFormat(new Date())}"/>
                        </div>
                    </div>

                    <div class="control-group">
                        <label class="control-label">用餐类型</label>

                        <div class="controls">
                            <input class="input-xlarge focused" type="text" disabled="true"
                                   value="${ReserveType.getLabel(lj.Number.toInteger(params.reserveType) ?: 1)} ${params.reserveTypeTimes}"/>
                        </div>
                    </div>

                    <div class="control-group">
                        <label class="control-label">预计到店时间</label>

                        <div class="controls">
                            <input class="input-xlarge focused" type="text" name="time" id="time"
                                   value="${params.time ?: lj.FormatUtil.timeFormat(new Date())}"/>
                        </div>
                    </div>

                    <div class="control-group">
                        <label class="control-label">联系电话</label>

                        <div class="controls">
                            <input class="input-xlarge focused" type="text" name="phone" value="${params.phone ?: ""}"/>
                        </div>
                    </div>

                    <div class="control-group">
                        <label class="control-label">每个桌位大概人数</label>

                        <div class="controls">
                            <input class="input-xlarge focused" type="text" name="personCount"
                                   value="${params.personCount ?: ""}"/>
                        </div>
                    </div>


                    <label class="control-label">选择桌位</label>

                    <div class="control-group">
                        <g:each in="${tableList}">
                            <div class="controls" style="padding-left: 20px">
                                <label class="checkbox">
                                    <input type="checkBox" name="tableId"
                                           value="${it.tableInfo.id}" ${it.canUse ? "" : "disabled=true"}/>
                                    桌位ID：${it.tableInfo.id}&nbsp;
                                    桌位名称：${it.tableInfo.name}&nbsp;
                                    最少人数：${it.tableInfo.minPeople}&nbsp;最多人数：${it.tableInfo.maxPeople}

                                </label>
                            </div>
                        </g:each>
                    </div>

                    <div class="form-actions">
                        <input type="submit"
                               value="${message(code: 'default.button.reserve.label', default: 'reserve')}"
                               class="btn btn-primary"/>
                    </div>
                </fieldset>
            </form>

        </g:if>
        <g:else>
            <div style="margin: 0px auto;">
                <label style="text-align: center">没有合适的桌位</label>
            </div>
        </g:else>

    </div>

</div>
</body>
</html>