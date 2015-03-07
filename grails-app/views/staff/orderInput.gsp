<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 13-11-23
  Time: 下午6:24
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main_template"/>
  <title>创建订单</title>
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
        width: 240px;
        float: left;
    }

    .mcmcsf_input {
        width: 160px;
    }

    .mcmcs_field_middle {
        width: 160px;
        float: left;
    }

    .mcmcsf_input_middle {
        width: 80px;
    }

    .mcmcs_field_small {
        width: 80px;
        float: left;
    }

    .mcmcsf_input_small {
        width: 40px;
    }
    </style>
</head>
<body>
<div class="mc_main">
    <div class="mcm_top">
        <g:render template="../layouts/staffMenu"></g:render>
    </div>

    <div class="mcm_content">
    <!--提示消息-->
    <g:render template="../layouts/msgs_and_errors"></g:render>

    <!--表单-->
        <div class="mcmc_ssl">
            <form class="form-inline" action="${createLink(controller: "staff",action: "createOrder")}">

                <div class="mcmcs_field">
                    <label>
                        桌位：
                    </label>
                    <select name="tableId" class="mcmcsf_input">
                        <g:each in="${tableList}" var="tableInfo">
                            <g:if test="${tableInfo?.canUse}">
                            <option value="${tableInfo?.tableInfo.id}" ${params.tableId == tableInfo?.tableInfo.id.toString() ? "selected='selected'" : ""}>${tableInfo?.tableInfo.name}</option>
                            </g:if>
                        </g:each>
                    </select>
                </div>

                <div class="ms_field_small">
                    <input type="submit" value="${message(code: 'default.button.createOrder.label', default: 'createOrder')}"
                           class="btn btn-primary"/>
                </div>

            </form>
        </div> <br/><br/><br/><br/><br/><br/><br/>
</div>
</div>
</body>
</html>