package lj.taos.order.customer

import lj.data.OrderInfo
import lj.data.ReserveOrderInfo
import lj.data.RestaurantInfo
import lj.data.TableInfo
import lj.enumCustom.OrderStatus
import lj.enumCustom.OrderValid
import lj.enumCustom.ReCode
import lj.enumCustom.ReserveOrderStatus

import java.text.SimpleDateFormat

class CustomerReserveService {
    def shopService;

    def serviceMethod() {

    }
    //根据预定信息查询出相应的可预定桌位
    def getReserveTable(def params){
        SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String dinnerTimeStr=params.dinnerTime;
        Date dinnerTime=new Date();
        try { dinnerTime = sdfDateTime.parse(dinnerTimeStr); } catch (Exception ex) {}

        //查询出所有桌位
        List<TableInfo> tableInfoListTemp=TableInfo.getAll();
        if(tableInfoListTemp==null||tableInfoListTemp.size()==0){
            return [recode:ReCode.NO_RESULT];
        }

        //从店铺属性中获取订单直接间隔
        RestaurantInfo restaurantInfo=null;
        int intervalTime=60;//默认60分钟，单位分钟
        def reInfo=shopService.getShopEnabled();
        if(reInfo.recode!=ReCode.OK){
            return reInfo;
        }
        restaurantInfo=reInfo.restaurantInfo;
        if(restaurantInfo.intervalTime){
            intervalTime=restaurantInfo.intervalTime;
        }

        Calendar calendar=Calendar.getInstance();
        calendar.setTime(dinnerTime);
        calendar.add(Calendar.MINUTE,intervalTime*-1)
        Date lowerDateTime=calendar.getTime();
        println("lowerDateTime-->"+lowerDateTime.toLocaleString());
        calendar.setTime(dinnerTime);
        calendar.add(Calendar.MINUTE,intervalTime)
        Date upperDateTime=calendar.getTime();
        println("upperDateTime-->"+upperDateTime.toLocaleString());
        def tableInfoList=tableInfoListTemp.collect {//滤掉已经占用的桌位
           boolean canUse=true;
           //查询是否有预定
            ReserveOrderInfo reserveOrderInfo=ReserveOrderInfo.findByTableInfoAndDinnerTimeBetweenAndValidAndStatus(it,lowerDateTime,upperDateTime,OrderValid.EFFECTIVE_VALID.code,ReserveOrderStatus.ORIGINAL_STATUS.code);
            if(reserveOrderInfo){
                canUse=false;
            }
            //查询是否有订单
            OrderInfo orderInfo=OrderInfo.findByTableInfoAndCreateTimeBetweenAndValidAndStatusLessThan(it,lowerDateTime,upperDateTime,OrderValid.EFFECTIVE_VALID.code, OrderStatus.CHECKOUTED_STATUS.code);
            if(orderInfo){
                canUse=false;
            }
            [canUse: canUse, tableInfo: it];
        }

        return [recode: ReCode.OK, tableInfoList: tableInfoList, params: params];
    }
}
