package lj.taos.order.common

import lj.data.OrderInfo
import lj.data.ReserveOrderInfo
import lj.enumCustom.DishesValid
import lj.enumCustom.OrderStatus
import lj.enumCustom.OrderValid
import lj.enumCustom.ReCode
import lj.enumCustom.ReserveOrderStatus

import java.text.SimpleDateFormat

class OrderAndReserveService {
    def shopService;
    //标注过期订单
    def markExpireOrder() {
        log.info("标注过期订单");
        Date now=new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.DATE,-1);
        Date date=calendar.getTime();
       //标注过期订单（1天前的订单为过期订单）
        def orderList=OrderInfo.findAllByCreateTimeLessThanAndValidLessThanEqualsAndStatusLessThan(date,OrderValid.EFFECTIVE_VALID.code,OrderStatus.CHECKOUTED_STATUS.code);
        if(orderList){
            orderList.each {
                it.valid=OrderValid.EXPIRE_VALID.code;
                it.save(flush: true);
                log.info("过期订单->"+it);
                //点菜过期
                def dishes=it.dishes;
                if(dishes){
                    dishes.each { dish ->
                        dish.valid=DishesValid.EXPIRE_VALID.code;
                        dish.save();
                    }
                }
            }
        }
    }
    //标注过期预定订单
    def markExpireReserve(){
        log.info("标注过期预定订单");
        int intervalTime=60;//默认60分钟，单位分钟
        def reInfo=shopService.getShopEnabled();
        if(reInfo.recode==ReCode.OK){
            if(reInfo.restaurantInfo.intervalTime){
                intervalTime=reInfo.restaurantInfo.intervalTime;
            }
        }
        Date now=new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.MINUTE,intervalTime*(-1));
        Date date=calendar.getTime();
        def reserveList=ReserveOrderInfo.findAllByDinnerTimeLessThanAndValidLessThanEqualsAndStatusLessThan(date,OrderValid.EFFECTIVE_VALID.code,ReserveOrderStatus.REACHED_STATUS.code);
        if(reserveList){
             reserveList.each {
                 it.valid=OrderValid.EXPIRE_VALID.code;
                 it.save(flush: true);
                 log.info("过期预定订单->"+it);
             }
        }
    }
    //标注过期
    def markExpire(){
        markExpireOrder();
        markExpireReserve();
    }
}
