package lj.taos.tags

import lj.data.OrderInfo
import lj.data.ReserveOrderInfo
import lj.enumCustom.OrderStatus
import lj.enumCustom.OrderValid
import lj.enumCustom.ReCode
import lj.enumCustom.ReserveOrderStatus

class ReserveOrderOperationTagLib {
    def shopService;
    static namespace = "taos";
    //根据顾客根据预定订单状态和有效性对显示不同的操作按钮
    def customerReserveOrderOperation = { attr, body ->
        String htmlTag = "";
        try {
            long reserveOrderId = lj.Number.toLong(attr.reserveOrderId);
            String backUrl = attr.backUrl;
            //查询订单
            ReserveOrderInfo reserveOrderInfo = ReserveOrderInfo.get(reserveOrderId);
            if (reserveOrderInfo) {
                int intervalTime=60;//默认60分钟，单位分钟
                def reInfo=shopService.getShopEnabled();
                if(reInfo.recode!=ReCode.OK){
                    return reInfo;
                }
                if(reInfo.restaurantInfo.intervalTime){
                    intervalTime=reInfo.restaurantInfo.intervalTime;
                }
                //检查订单是否过期
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(reserveOrderInfo.dinnerTime);
//                calendar.set(Calendar.HOUR_OF_DAY, 23);
//                calendar.set(Calendar.MINUTE, 59);
//                calendar.set(Calendar.SECOND, 59);
                //calendar.add(Calendar.DATE,1);
                //calendar.add(Calendar.HOUR, 5);
                calendar.add(Calendar.MINUTE,intervalTime);
                println(calendar.getTime());
                if (calendar.getTime().before(new Date())) {//过期订单只能取消和删除
//                    //htmlTag += "<font color='RED'>订单已经过期，不能操作</font>";
//                    if (reserveOrderInfo.valid < OrderValid.USER_CANCEL_VALID.code&&reserveOrderInfo.status <= OrderStatus.ORDERED_STATUS.code) {
//                        htmlTag += "<a href='" + createLink(controller: "customer", action: "cancelOrder", params: [reserveOrderId: reserveOrderId,backUrl:backUrl]) + "'>取消</a>&nbsp;&nbsp;";
//                    } else if (reserveOrderInfo.valid == OrderValid.USER_CANCEL_VALID.code) {//取消的订单可以删除
//                        htmlTag += "<a href='" + createLink(controller: "customer", action: "delOrder", params: [reserveOrderId: reserveOrderId,backUrl:backUrl]) + "'>删除</a>"
//                    }
                    if (reserveOrderInfo.valid < OrderValid.USER_CANCEL_VALID.code && reserveOrderInfo.status < ReserveOrderStatus.REACHED_STATUS.code) {
                        htmlTag += "<font color='RED'>预定订单已过期</font>&nbsp;&nbsp;";
//                        htmlTag += "<a href='" + createLink(controller: "customer", action: "delOrder", params: [reserveOrderId: reserveOrderId, backUrl: backUrl]) + "'>删除</a>"

                        out << htmlTag;
                        //println("guoqu");
                        return;
                    }
                }
                if (reserveOrderInfo.valid < OrderValid.USER_CANCEL_VALID.code) { // 未取消的订单
                    if (reserveOrderInfo.status < ReserveOrderStatus.REACHED_STATUS.code) {
                        htmlTag += "<a href='" + createLink(controller: "reserveCustomer", action: "dishOfReserveOrder", params: [reserveOrderId: reserveOrderId, backUrl: backUrl]) + "'>点菜</a>&nbsp;&nbsp;";
                        htmlTag += "<a href='" + createLink(controller: "reserveCustomer", action: "reserveOrderCancel", params: [reserveOrderId: reserveOrderId, backUrl: backUrl]) + "' confirm='确定要取消吗？'>取消</a>&nbsp;&nbsp;";
                    }
                } else if (reserveOrderInfo.valid == OrderValid.USER_CANCEL_VALID.code) {//取消的订单可以删除
                    //htmlTag += "<a href='" + createLink(controller: "customer", action: "delOrder", params: [reserveOrderId: reserveOrderId, backUrl: backUrl]) + "'>删除</a>"
                }
            }
        }
        catch (Exception ex) {
            htmlTag += "<font color='RED'>" + ex.message + "</font>";
        }
//        htmlTag+="</div>";
        out << htmlTag;
    }

    //根据顾客根据预定订单状态和有效性对显示不同的操作按钮
    def staffReserveOrderOperation = { attr, body ->
        String htmlTag = "";
        try {
            long reserveOrderId = lj.Number.toLong(attr.reserveOrderId);
            String backUrl = attr.backUrl;
            //查询订单
            ReserveOrderInfo reserveOrderInfo = ReserveOrderInfo.get(reserveOrderId);
            if (reserveOrderInfo) {
                int intervalTime=60;//默认60分钟，单位分钟
                def reInfo=shopService.getShopEnabled();
                if(reInfo.recode!=ReCode.OK){
                    return reInfo;
                }
                if(reInfo.restaurantInfo.intervalTime){
                    intervalTime=reInfo.restaurantInfo.intervalTime;
                }
                //检查订单是否过期
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(reserveOrderInfo.dinnerTime);
//                calendar.set(Calendar.HOUR_OF_DAY, 23);
//                calendar.set(Calendar.MINUTE, 59);
//                calendar.set(Calendar.SECOND, 59);
                //calendar.add(Calendar.DATE,1);
                //calendar.add(Calendar.HOUR, 5);
                calendar.add(Calendar.MINUTE,intervalTime);
                println(calendar.getTime());
                if (calendar.getTime().before(new Date())) {//过期订单只能取消和删除
//                    //htmlTag += "<font color='RED'>订单已经过期，不能操作</font>";
//                    if (reserveOrderInfo.valid < OrderValid.USER_CANCEL_VALID.code&&reserveOrderInfo.status <= OrderStatus.ORDERED_STATUS.code) {
//                        htmlTag += "<a href='" + createLink(controller: "customer", action: "cancelOrder", params: [reserveOrderId: reserveOrderId,backUrl:backUrl]) + "'>取消</a>&nbsp;&nbsp;";
//                    } else if (reserveOrderInfo.valid == OrderValid.USER_CANCEL_VALID.code) {//取消的订单可以删除
//                        htmlTag += "<a href='" + createLink(controller: "customer", action: "delOrder", params: [reserveOrderId: reserveOrderId,backUrl:backUrl]) + "'>删除</a>"
//                    }
                    if (reserveOrderInfo.valid < OrderValid.USER_CANCEL_VALID.code && reserveOrderInfo.status < ReserveOrderStatus.REACHED_STATUS.code) {
                        htmlTag += "<font color='RED'>预定订单已过期</font>&nbsp;&nbsp;";
//                        htmlTag += "<a href='" + createLink(controller: "customer", action: "delOrder", params: [reserveOrderId: reserveOrderId, backUrl: backUrl]) + "'>删除</a>"

                        out << htmlTag;
                        //println("guoqu");
                        return;
                    }
                }
                if (reserveOrderInfo.valid < OrderValid.USER_CANCEL_VALID.code) { // 未取消的订单
                    if (reserveOrderInfo.status < ReserveOrderStatus.REACHED_STATUS.code) {
                        htmlTag += "<a href='" + createLink(controller: "reserveStaff", action: "dishOfReserveOrder", params: [reserveOrderId: reserveOrderId, backUrl: backUrl]) + "'>点菜</a>&nbsp;&nbsp;";
                        htmlTag += "<a href='" + createLink(controller: "reserveStaff", action: "reserveOrderCancel", params: [reserveOrderId: reserveOrderId, backUrl: backUrl]) + "' confirm='确定要取消该预定订单吗？'>取消</a>&nbsp;&nbsp;";
                        htmlTag += "<a href='" + createLink(controller: "reserveStaff", action: "reserveOrderReach", params: [reserveOrderId: reserveOrderId, backUrl: backUrl]) + "' confirm='确定顾客到店吗？'>顾客到店</a>&nbsp;&nbsp;";
                    }
                } else if (reserveOrderInfo.valid == OrderValid.RESTAURANT_CANCEL_VALID.code) {//取消的订单可以删除
                    //htmlTag += "<a href='" + createLink(controller: "customer", action: "delOrder", params: [reserveOrderId: reserveOrderId, backUrl: backUrl]) + "'>删除</a>"
                }
            }
        }
        catch (Exception ex) {
            htmlTag += "<font color='RED'>" + ex.message + "</font>";
        }
//        htmlTag+="</div>";
        out << htmlTag;
    }

}
