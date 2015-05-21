<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="lj.FormatUtil; lj.enumCustom.VerifyStatus; lj.enumCustom.ReCode" %>
<html>
<head>
    <meta name="layout" content="main_template"/>
    <title>修改饭店资料</title>
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

    </style>
    <script type="text/javascript">
        $(function(){

        });

        function selectImage(){
            $("#imageSelect").attr('src','${createLink(controller: "imageSpace",action: "selectImage",params: [callBack:"showImage"])}');
            $("#ratyService").modal();
        }
        function showImage(imgUrl){
            $("#imageHidden").val(imgUrl);
            $("#imageShow").attr('src','${createLink(controller: "imageShow", action: "downloadThumbnail")}?imgUrl='+imgUrl);
        }
    </script>
</head>

<body>

<div class="mc_main">

    <div class="mcm_top">
        %{--<div class="mcm_top_name"><g:message code='restaurantInfo.update.label'/></div>--}%

        %{--<div class="mcm_top_banner"></div>--}%
        <g:render template="../layouts/shopMenu"></g:render>
    </div>

    <div class="span10" style="margin-left: 10px;margin-top: 10px;">
        %{--<g:render template="../layouts/shopMenu" />--}%

        <g:form class="form-horizontal" method="POST" action="editShopInfo" enctype="multipart/form-data">
            <fieldset>
                <g:render template="../layouts/msgs_and_errors"></g:render>

                <div class="control-group">
                    <label class="control-label" for="name"><g:message code="restaurantInfo.name.label"
                                                                       default="Name"/><span
                            class="required-indicator">*</span></label>

                    <div class="controls">
                        <input type="text" style="width: 280px;" name="name" id="name"
                               value="${restaurantInfoInstance?.name}"/>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label" for="image"><g:message code="restaurantInfo.image.label"
                                                                        default="Image"/><span
                            class="required-indicator"></span></label>
                    <div class="controls">
                        <input type="hidden" id="imageHidden" name="image"/>
                        <img src="${createLink(controller: "imageShow", action: "downloadThumbnail", params: [imgUrl: restaurantInfoInstance?.image])}" id="imageShow"/>
                        <input type="button" value="从图片空间选择一张图片" onclick="selectImage()"/>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label" for="address">
                        <g:message code="restaurantInfo.address.label"
                                   default="Address"/>
                        <span class="required-indicator">*</span>
                    </label>

                    <div class="controls">
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
                    <label class="control-label" for="name"><g:message code="restaurantInfo.enabled.label"
                                                                       default="Enabled"/></label>

                    <div class="controls">
                        <g:checkBox name="enabled" value="${restaurantInfoInstance?.enabled}"/>
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

                <div class="form-actions">
                    <input type="submit" class="btn send_btn"
                           value="${message(code: 'default.button.update.label', default: 'Update')}"/>
                </div>

            </fieldset>
        </g:form>

    </div>

</div>

<div>

</div>

</form>

<!--rating modal's content-->
<div id="ratyService" class="modal hide fade">

    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h3>选择图片</h3>
    </div><!--Modal header-->
    <div class="modal-body">
        <iframe id="imageSelect" src="${createLink(controller: "imageSpace",action: "selectImage",params: [callBack:"insertImage"])}"
                width="500px" height="500px"></iframe>
    </div><!--Modal body-->
    <div class="modal-footer">
        <a href="#" class="btn" data-dismiss="modal" >Close</a>
        %{--<a href="#" class="btn btn-primary">Save changes</a>--}%
    </div><!--Modal footer-->
</div> <!--Modal-->
</body>
</html>