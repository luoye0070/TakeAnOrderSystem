<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 13-10-31
  Time: 下午8:08
  To change this template use File | Settings | File Templates.
--%>

<%@ page import="lj.FormatUtil" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main_template"/>
    <title>创建饭店</title>
    <style type="text/css">
    .mc_main {
        width: 1000px;
        height: auto;
        margin: 0px 50px;
        background-color: #FFFFFF;
        float: left;
    }

    .mcm_top {
        margin-top: 20px;
    }

    .mcm_top_name {
        font-size: 20px;
        font-weight: bold;
        padding: 10px 0px 10px 20px;
    }

    .mcm_top_banner {
        width: 100%;
        height: 4px;
        background: url('${resource(dir:"images",file:"login_banner.gif")}');
        margin: 0px auto;
        margin-bottom: 30px;
    }

        /*.mcm_top {*/
        /*width: 960px;*/
        /*height: 80px;*/
        /*margin: 20px;*/
        /*margin-top: 0px;*/
        /*border-bottom: 4px solid #FF9833;*/
        /*text-indent: 1em;*/
        /*line-height: 80px;*/
        /*font-size: 20px;*/
        /*font-weight: bolder;*/
        /*}*/

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
        width: 320px;
        float: left;
    }

    .mcmcsf_input {
        width: 120px;
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

    <link rel="stylesheet" href="${resource(dir: "js/timePicker", file: "timePicker.css")}" type="text/css"
          media="screen"/>
    <script type="text/javascript" src="${resource(dir: "js/timePicker", file: "jquery.timePicker.min.js")}"></script>
    <script type="text/javascript">
        $(function () {

            //时间选择器
            $("#shopHoursBeginTime").timePicker({step: 15});
            $("#shopHoursBeginTime").change(function () {
                var timeV = $("#shopHoursBeginTime").val();
                if (timeV.length > 0) {
                    $("#shopHoursBeginTime").val(timeV + ":00");
                }
            });
            $("#shopHoursEndTime").timePicker({step: 15});
            $("#shopHoursEndTime").change(function () {
                var timeV = $("#shopHoursEndTime").val();
                if (timeV.length > 0) {
                    $("#shopHoursEndTime").val(timeV + ":00");
                }
            });
        });
    </script>
</head>

<body>

<div class="mc_main">
    <div class="mcm_top">
        <div class="mcm_top_name"><g:message code='restaurantInfo.create.label'/></div>

        <div class="mcm_top_banner"></div>
    </div>

    <div class="span10" style="margin-left: 10px;margin-top: 0px;">
        <g:render template="../layouts/msgs_and_errors"></g:render>

        <div class="span11">
            <form class="form-horizontal" method="POST" id="create_form" action="shopCreate">

                <div class="control-group">
                    <label class="control-label" style="font-size: 16px;font-weight: bolder;">
                        饭店基本信息
                    </label>

                    <div class="controls">
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label" for="name"><g:message code="restaurantInfo.name.label"
                                                                       default="Name"/><span
                            class="required-indicator">*</span></label>

                    <div class="controls">
                        <input type="text" style="width: 280px;" name="name" id="name"
                               value="${restaurantInfoInstance?.name}"/>
                    </div>
                </div>

                %{--<div class="control-group">--}%
                    %{--<label class="control-label" for="image"><g:message code="restaurantInfo.image.label"--}%
                                                                        %{--default="Image"/><span--}%
                            %{--class="required-indicator"></span></label>--}%

                    %{--<div class="controls">--}%
                        %{--<input type="text" style="width: 280px;" name="image" id="image"--}%
                               %{--value="${restaurantInfoInstance?.image}"/>--}%
                    %{--</div>--}%
                %{--</div>--}%

                <div class="control-group">
                    <label class="control-label" for="address">
                        <g:message code="restaurantInfo.address.label"
                                   default="Address"/>
                        <span class="required-indicator">*</span>
                    </label>

                    <div class="controls">
                        %{--<textarea class="input-xxlarge" id="street" name="street" rows="1"--}%
                        %{--placeholder="为了您的方便，请填写详细地址">${restaurantInfoInstance?.street}</textarea>--}%
                        <input type="text" class="input-xxlarge" id="address" name="address"
                               placeholder="请填写详细地址" value="${restaurantInfoInstance?.address}"/>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label" for="longitude"><g:message code="restaurantInfo.longitude.label"
                                                                            default="Longitude"/></label>

                    <div class="controls">
                        <input type="text" id="longitude" name="longitude"
                               value="${fieldValue(bean: restaurantInfoInstance, field: 'longitude')}"/>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label" for="latitude"><g:message code="restaurantInfo.latitude.label"
                                                                           default="Latitude"/></label>

                    <div class="controls">
                        <input type="text" id="latitude" name="latitude"
                               value="${fieldValue(bean: restaurantInfoInstance, field: 'latitude')}"/>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label" for="phone"><g:message code="restaurantInfo.phone.label"
                                                                        default="Phone"/><span
                            class="required-indicator">*</span></label>

                    <div class="controls">
                        <input type="text" id="phone" name="phone" maxlength="16" required=""
                               value="${restaurantInfoInstance?.phone}"/>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label" for="shopHoursBeginTime"><g:message
                            code="restaurantInfo.shopHoursBeginTime.label" default="Shop Hours Begin Time"/><span
                            class="required-indicator">*</span></label>

                    <div class="controls">
                        <input type="text" id="shopHoursBeginTime" name="shopHoursBeginTime"
                               value="${FormatUtil.timeFormat(restaurantInfoInstance?.shopHoursBeginTime)}"/>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label" for="shopHoursEndTime"><g:message
                            code="restaurantInfo.shopHoursEndTime.label" default="Shop Hours End Time"/><span
                            class="required-indicator">*</span></label>

                    <div class="controls">
                        <input type="text" id="shopHoursEndTime" name="shopHoursEndTime"
                               value="${FormatUtil.timeFormat(restaurantInfoInstance?.shopHoursEndTime)}"/>

                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label" for="cuisineName"><g:message code="restaurantInfo.cuisineName.label"
                                                                              default="Cuisine Name"/><span
                            class="required-indicator"></span></label>

                    <div class="controls">
                        <input type="text" placeholder="请填写菜系" id="cuisineName" name="cuisineName"
                               value="${restaurantInfoInstance?.cuisineName}"/>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label" for="imageSpaceSize"><g:message
                            code="restaurantInfo.imageSpaceSize.label"
                            default="Image Space Size"/><span
                            class="required-indicator">*</span></label>

                    <div class="controls">
                        <input type="text" placeholder="图片空间大小" id="imageSpaceSize" name="imageSpaceSize"
                               value="${restaurantInfoInstance?.imageSpaceSize}"/>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label" for="description"><g:message code="restaurantInfo.description.label"
                                                                              default="description"/></label>

                    <div class="controls">
                        <textarea class="input-xlarge" id="description" name="description"
                                  rows="1">${restaurantInfoInstance?.description}</textarea>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label" for="baseUrl"><g:message code="restaurantInfo.baseUrl.label"
                                                                          default="Base Url"/><span
                            class="required-indicator"></span></label>

                    <div class="controls">
                        <input type="text" placeholder="店铺URL" id="baseUrl" name="baseUrl"
                               value="${restaurantInfoInstance?.baseUrl}"/>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label" style="font-size: 16px;font-weight: bolder;">
                        店主信息
                    </label>

                    <div class="controls">
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label" for="loginName"><g:message code="staffInfo.loginName.label" default="Login Name" />
                        <span class="required-indicator">*</span>
                    </label>
                    <div class="controls">
                        <g:textField name="loginName" maxlength="32" required="" value="${staffInfo?.loginName}"/>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label" for="passWord"><g:message code="staffInfo.passWord.label" default="Pass Word" />
                        <span class="required-indicator">*</span>
                    </label>
                    <div class="controls">
                        <g:passwordField name="passWord" maxlength="128" required="" value="${staffInfo?.passWord}"/>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label" for="rePassWord"><g:message code="staffInfo.rePassWord.label" default="Re Pass Word" />
                        <span class="required-indicator">*</span>
                    </label>
                    <div class="controls">
                        <g:passwordField name="rePassWord" maxlength="128" required="" value=""/>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label" for="staffName"><g:message code="staffInfo.name.label" default="Name" />
                    </label>
                    <div class="controls">
                        <g:textField name="staffName" maxlength="32" value="${staffInfo?.name}"/>
                    </div>
                </div>


                <div class="control-group">
                    <label class="control-label"></label>

                    <div class="controls">
                        <input type="submit" value="${message(code: 'default.button.create.label', default: 'Create')}"
                               class="btn send_btn"/>
                    </div>
                </div>

            </form>

        </div>
    </div>
</div>
</body>
</html>