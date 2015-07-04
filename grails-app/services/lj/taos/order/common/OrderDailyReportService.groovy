package lj.taos.order.common

import groovy.json.JsonSlurper
import lj.data.OrderInfo
import lj.enumCustom.OrderStatus
import lj.enumCustom.OrderValid
import lj.enumCustom.ReCode
import lj.internet.AppConstant
import lj.internet.HttpConnectionHelper

import java.lang.reflect.Field
import java.text.SimpleDateFormat

class OrderDailyReportService {
    def grailsApplication;
    def shopService;

    def dailyReport() {
        def reInfo=shopService.getShopInfo();
        if(reInfo.recode!=ReCode.OK){
            return;
        }

        long rId=reInfo.restaurantInfo.remoteId;

        Date now=new Date();
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(now);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        Date dateBegin=calendar.getTime();
        calendar.set(Calendar.HOUR_OF_DAY,23);
        calendar.set(Calendar.MINUTE,59);
        calendar.set(Calendar.SECOND,59);
        calendar.set(Calendar.MILLISECOND,999);
        Date dateEnd=calendar.getTime();
        int totalCount=OrderInfo.countByCreateTimeBetween(dateBegin,dateEnd);
        int availableCount=OrderInfo.countByCreateTimeBetweenAndValid(dateBegin,dateEnd,OrderValid.EFFECTIVE_VALID.code);
        int completeCount=OrderInfo.countByCreateTimeBetweenAndStatus(dateBegin,dateEnd,OrderStatus.CHECKOUTED_STATUS.code);



        String url = grailsApplication.config.grails.daily.report.url;//"http://localhost/TaosServer/restaurant/update";
        log.info("url->"+url);
        //String scope="";//：非必须参数。以空格分隔的权限列表，
        ArrayList<HashMap<String, String>> paramList = new ArrayList<HashMap<String, String>>();

        HashMap<String, String> param = new HashMap<String, String>();
        param.put(AppConstant.HttpParamRe.PARAM_NAME, "totalCount");
        param.put(AppConstant.HttpParamRe.PARAM_VALUE, totalCount+"");
        paramList.add(param);

        HashMap<String, String> param1 = new HashMap<String, String>();
        param1.put(AppConstant.HttpParamRe.PARAM_NAME, "availableCount");
        param1.put(AppConstant.HttpParamRe.PARAM_VALUE, availableCount+"");
        paramList.add(param1);

        HashMap<String, String> param2 = new HashMap<String, String>();
        param2.put(AppConstant.HttpParamRe.PARAM_NAME, "completeCount");
        param2.put(AppConstant.HttpParamRe.PARAM_VALUE, completeCount+"");
        paramList.add(param2);

        HashMap<String, String> param3 = new HashMap<String, String>();
        param3.put(AppConstant.HttpParamRe.PARAM_NAME, "rId");
        param3.put(AppConstant.HttpParamRe.PARAM_VALUE, rId+"");
        paramList.add(param3);

        HttpConnectionHelper hch = new HttpConnectionHelper();
        String responseStr = hch.getResponseStr(url, paramList);
        log.info("responseStr->"+responseStr);

        //def slurper = new JsonSlurper();
        //def responseObj=slurper.parseText(responseStr);

    }
}
