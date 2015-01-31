<style type="text/css">
.top{
    width: 100%;
    height: 150px;
    /*background: #ff8888;*/
}
    .t_menu{
        width: 100%;
        width: 1100px;
        height: 30px;
        margin: 0px auto;
        background: #FAFAFA;
    }
    .tm_real{
        width: 1100px;
        height: 30px;
        margin: 0px auto;
        /*background: #df8505;*/
    }
    .tmr_left{
        width: 530px;
        height: 30px;
        margin-left: 20px;
        line-height: 30px;
        float: left;
        text-align: left;
    }
.tmr_right{
    width: 530px;
    height: 30px;
    margin-right: 20px;
    line-height: 30px;
    float: right;
    text-align: right;
}

    .t_content{
        /*width: 100%;*/
        width: 1100px;
        height: 120px;
        margin: 0px auto;
        background: #F7F3E7;
        background: #FFFFFF;
    }
.tc_real{
    width: 1100px;
    height: 120px;
    margin: 0px auto;
    /*background: #df8505;*/
}
    .tcr_logo{
        width: 300px;
        height: 120px;
        float: left;
    }
.tcr_logo img{
    width: 190px;
    height: 60px;
    margin: 30px 55px;
    /*width: 280px;
    height: 88px;
    margin: 16px 10px;*/
    border: 0px;
}
    .tcr_search{
        width: 500px;
        height: 100px;
        margin: 10px 0px;
        float: left;
        /*background: #df8505;*/
    }
    .tcrs_tab{
        width: 480px;
        height: 40px;
        float: left;
        padding-left: 2px;
        /*background: #df8505;*/
    }
    .tcrst_item{
        width: 80px;
        height: 30px;
        margin-top: 10px;
        line-height: 30px;
        text-align: center;
        font-size: 14px;
        float: left;
        cursor: pointer;
        /*background: #F5F5F5;*/
    }
    .tcrst_item_selected{
        width: 80px;
        height: 30px;
        margin-top: 10px;
        line-height: 30px;
        text-align: center;
        font-size: 14px;
        color: #ffffff;
        float: left;
        background: #D00B01;
        border-color: #CF0000 #CF0000 rgba(0, 0, 0, 0);
        border-image: none;
        border-style: solid;
        border-width: 3px;
    }

    .tcrs_content{
        width: 500px;
        height: 60px;
        float: left;
        /*background: #f2dede;*/
    }
.tcrsc_field {
    width: 380px;
    height: 60px;
    float: left;
}
.tcrsc_field_small {
    width: 80px;
    height: 60px;
    float: left;
}
.tcrscf_label {
    width: 60px;
}
.tcrscf_input {
    width: 350px;
}
.tcrscf_input_small {
    width: 60px;
}

    .tcr_download{
        width: 300px;
        height: 120px;
        float: left;
        /*background: #f5c2bf;*/
    }
    .tcrd_item{
        width: 150px;
        height: 120px;
        float: left;
    }
    .tcrdi_txt{
        width: 150px;
        height: 30px;
        text-align: center;
        line-height: 30px;
        float: left;
    }
.tcrdi_img{
    width: 150px;
    height: 90px;
    text-align: center;
    float: left;
}



</style>
<script type="text/javascript">
     $(function(){
         $("#searchFoodTab").click(function(event){
            $("#searchForm").attr("action","${createLink(controller: "search",action: "searchFood")}");
             $("#keyWord").css("display","block");
             $("#name").css("display","none");
             $("#searchFoodTab").attr("class","tcrst_item_selected");
             $("#searchShopTab").attr("class","tcrst_item");
         });
         $("#searchShopTab").click(function(event){
             $("#searchForm").attr("action","${createLink(controller: "search",action: "searchShop")}");
             $("#keyWord").css("display","none");
             $("#name").css("display","block");
             $("#searchFoodTab").attr("class","tcrst_item");
             $("#searchShopTab").attr("class","tcrst_item_selected");
         });
     });
</script>
<!--top-->
<div class="top">
     <div class="t_menu">
     <div class="tm_real">
        <div class="tmr_left">
            <span>欢迎来到餐萌订餐网！</span>
            <g:if test="${request.getSession(false)?.clientId}">
                <a href="${createLink(controller: "user",action: "viewUserInfo")}">${lj.data.ClientInfo.get(session?.clientId)?.userName}</a>
            </g:if>
            <g:else>
                <a href="${createLink(controller: "user",action: "login")}">请登录</a>
            </g:else>
                 |
                <a href="${createLink(controller: "user",action:"register")}">免费注册</a>
        </div>
         <div class="tmr_right">
             <img src="${resource(dir: "images",file: "setzhuye.png")}" style="margin-top: 10px;margin-right: 2px;"/>
             <a href="#">设为首页</a> |
             <img src="${resource(dir: "images",file: "downshoujiapp.png")}" style="margin-top: 8px;margin-right: 2px;"/>
             <a href="#">手机点餐</a> |
             <img src="${resource(dir: "images",file: "weibo.png")}" style="margin-top: 10px;margin-right: 2px;"/>
             <a href="#">微博关注</a>
         </div>
     </div>
     </div>
     <div class="t_content">
     <div class="tc_real">
        <div class="tcr_logo">
            <img src="${resource(dir: "images",file: "logo.png")}"/>
        </div>
         <div class="tcr_search">
             <div class="tcrs_tab">
                 <div id="searchFoodTab" class="tcrst_item_selected">菜谱</div>
                 <div id="searchShopTab" class="tcrst_item">饭店</div>
             </div>
             <div class="tcrs_content">
                 <form action="${createLink(controller: "search",action: "searchFood")}" method="post" class="" id="searchForm">
                     <input type="hidden" name="showPlace" value="page"/>
                     <!--菜谱名称条件-->
                     <div class="tcrsc_field">
                         <input id="keyWord" name="keyWord" type="text" class="tcrscf_input" style="height:25px;border: 2px solid #D00B01"
                                placeholder="请输入菜名" value="${params?.keyWord}"/>
                         <input id="name" name="name" type="text" class="tcrscf_input" style="display: none;height:25px;border: 2px solid #D00B01"
                            placeholder="请输入餐厅名" value="${params?.name}"/>
                     </div>

                     <div class="tcrsc_field_small">
                         <input type="submit" value="${message(code: 'default.button.search.label', default: 'Create')}"
                                class="btn btn-danger" style="width: 50px;height: 36px;"/>
                     </div>
                 </form>
             </div>
         </div>
         <div class="tcr_download">
             <div class="tcrd_item">
                   <div class="tcrdi_img">
                       <img src="${createLink(controller: "imageShow", action: "showQRCode",
                               params: [height:90, str: resource(dir: "uploadFile",file: "CmCustomer.apk",absolute: true)])}"
                            alt=""/>
                   </div>
                 <div class="tcrdi_txt">
                     <a href="${resource(dir: "uploadFile",file: "CmCustomer.apk")}">顾客手机端下载</a>
                 </div>
             </div>
             <div class="tcrd_item">
                 <div class="tcrdi_img">
                     <img src="${createLink(controller: "imageShow", action: "showQRCode",
                             params: [height:90, str: resource(dir: "uploadFile",file: "CmStaff.apk",absolute: true)])}"
                          alt=""/>
                 </div>
                 <div class="tcrdi_txt">
                     <a href="${resource(dir: "uploadFile",file: "CmStaff.apk")}">饭店手机端下载</a>
                 </div>
             </div>
         </div>
     </div>
     </div>
</div>