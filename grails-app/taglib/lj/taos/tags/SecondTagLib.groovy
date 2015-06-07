package lj.taos.tags

import lj.data.AppraiseInfo
import lj.data.TableInfo
import lj.enumCustom.ReCode

class SecondTagLib {
    static namespace = "taos";
    def shopService;
    def tableQRCode = { attr, body ->
        String htmlTag = "";
        try {
            long tableId = attr.tableId as long;
            TableInfo tableInfo=TableInfo.get(tableId);
            String urlStr="";
            String baseUrl = null;
            def reInfo = shopService.getShopInfo();
            if (ReCode.OK == reInfo.recode) {
                baseUrl = reInfo.restaurantInfo?.baseUrl;
            }
            if (baseUrl == null) {
                baseUrl = grailsApplication.config.grails.baseurls.baseUrl;
            }
            urlStr=createLink(controller: "customer",action: "getOrCreateOrder",params: [code:tableInfo?.code,mobile:"true"],absolute: true,base: baseUrl);

            htmlTag += "<img src='" + createLink(controller: "imageShow",action: "showQRCode",params: [str:urlStr]) + "'/>";
        }
        catch (Exception ex) {
            htmlTag += "<font color='RED'>" + ex.message + "</font>";
        }
        out << htmlTag;
    }
    //店铺总体评价星星
    def restaurantStars={ attr, body ->
        String htmlTag = "";
        try {
            //根据饭店ID查询出评价信息
            def appraiseList=AppraiseInfo.list();
            if(appraiseList){
                int count=0;
                float totalWhole=0;//总体评分
                float totalTaste=0;//味道评分
                float totalServiceAttitude=0;//服务评分
                float totalHygienicQuality=0;//环境评分
                appraiseList.each {
                    count++;
                    totalWhole+=it.whole;
                    totalTaste+=it.taste;
                    totalServiceAttitude+=it.serviceAttitude;
                    totalHygienicQuality+=it.hygienicQuality;
                }
                if(count==0){
                    count=1;
                }
                float averageWhole=totalWhole/count/2;
                float averageTaste=totalTaste/count;
                float averageServiceAttitude=totalServiceAttitude/count;
                float averageHygienicQuality=totalHygienicQuality/count;
                for(int i=0;i<Math.rint(averageWhole).intValue();i++){
                    htmlTag += "<img src='" + resource(dir: "images",file: "star1.gif") + "'/>&nbsp;";
                }
                for(int i=0;i<5-Math.rint(averageWhole).intValue();i++){
                    htmlTag += "<img src='" + resource(dir: "images",file: "star2.gif") + "'/>&nbsp;";
                }
                htmlTag+=((int)averageWhole*100)/100+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                htmlTag+="味道："+((int)averageTaste*100)/100+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                htmlTag+="环境："+((int)averageHygienicQuality*100)/100+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                htmlTag+="服务："+((int)averageServiceAttitude*100)/100+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
            }
            else{
                for(int i=0;i<5;i++)
                    htmlTag += "<img src='" + resource(dir: "images",file: "star2.gif") + "'/>&nbsp;";
                htmlTag+="0.0&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                htmlTag+="味道：0.0&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                htmlTag+="环境：0.0&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                htmlTag+="服务：0.0&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
            }
        }
        catch (Exception ex) {
            htmlTag += "<font color='RED'>" + ex.message + "</font>";
        }
        out << htmlTag;
    }
    //店铺总体评价星星
    def appraiseStars={ attr, body ->
        String htmlTag = "";
        try {
            //根据饭店ID查询出评价信息
            long appraiseId=lj.Number.toLong(attr.appraiseId);//评价ID
            AppraiseInfo appraiseInfo=AppraiseInfo.findById(appraiseId);
            if(appraiseInfo){
                for(int i=0;i<appraiseInfo.whole;i++){
                    htmlTag += "<img src='" + resource(dir: "images",file: "star1.gif") + "'/>&nbsp;";
                }
                for(int i=0;i<10-appraiseInfo.whole;i++){
                    htmlTag += "<img src='" + resource(dir: "images",file: "star2.gif") + "'/>&nbsp;";
                }
                htmlTag+=appraiseInfo.whole+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                htmlTag+="味道："+appraiseInfo.taste+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                htmlTag+="环境："+appraiseInfo.hygienicQuality+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                htmlTag+="服务："+appraiseInfo.serviceAttitude+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
            }
            else{
                for(int i=0;i<10;i++)
                    htmlTag += "<img src='" + resource(dir: "images",file: "star2.gif") + "'/>&nbsp;";
                htmlTag+="0&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                htmlTag+="味道：0&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                htmlTag+="环境：0&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                htmlTag+="服务：0&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
            }
        }
        catch (Exception ex) {
            htmlTag += "<font color='RED'>" + ex.message + "</font>";
        }
        out << htmlTag;
    }

}
