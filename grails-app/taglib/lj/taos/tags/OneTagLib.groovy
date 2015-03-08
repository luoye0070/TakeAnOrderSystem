package lj.taos.tags

import lj.data.DishesInfo
import lj.data.OrderInfo
import lj.data.StaffInfo
import lj.data.StaffPositionInfo
import lj.enumCustom.*

class OneTagLib {
    static namespace = "taos"
    def checkBoxGroup = { attr, body ->
        String htmlTag = "";
        try {
            def checkGroup = attr.checkGroup as List;
            def checkValues = attr.checkValues as List;
            def name = attr.name ?: "";
//            def id=attr.id;
//            if(id){
//                htmlTag += "<div id='"+id+"'>"
//            }
//            else{
//                htmlTag += "<div>"
//            }
            if (checkGroup) {
                checkGroup.each {
                    boolean inValues = false;
                    if (checkValues) {
                        for (int i = 0; i < checkValues.size(); i++) {
                            if (checkValues.get(i).code == it.code) {
                                inValues = true;
                            }
                        }
                    }
                    if (inValues)
                        htmlTag += "<input type='checkBox' name='" + name + "' value='" + it.code + "' checked='checked' />" + it.label + "&nbsp;";
                    else
                        htmlTag += "<input type='checkBox' name='" + name + "' value='" + it.code + "'  />" + it.label + "&nbsp;";
                }
            }
        }
        catch (Exception ex) {
            htmlTag += "<font color='RED'>" + ex.message + "</font>";
        }
//        htmlTag+="</div>";
        out << htmlTag;
    }
    def radioGroupCustom = { attr, body ->
        String htmlTag = "";
        try {
            def radioGroup = attr.radioGroup as List;
            int value = lj.Number.toInteger(attr.value);
            def name = attr.name ?: "";
            if (radioGroup) {
                radioGroup.each {
                    if (value == it.code)
                        htmlTag += "<input type='radio' value='" + it.code + "' name='" + name + "'  checked='checked'/>" + it.label + "&nbsp;";
                    else
                        htmlTag += "<input type='radio' value='" + it.code + "' name='" + name + "'/>" + it.label + "&nbsp;";
                }
            }
        }
        catch (Exception ex) {
            htmlTag += "<font color='RED'>" + ex.message + "</font>";
        }
//        htmlTag+="</div>";
        out << htmlTag;
    }

    //根据顾客根据订单状态和有效性对显示不同的操作按钮
    def customerOrderOperation = { attr, body ->
        String htmlTag = "";
        try {
            long orderId = lj.Number.toLong(attr.orderId);
            String backUrl = attr.backUrl;
            //查询订单
            OrderInfo orderInfo = OrderInfo.get(orderId);
            if (orderInfo) {
                //检查订单是否过期
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(orderInfo.date);
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                calendar.set(Calendar.SECOND, 59);
                //calendar.add(Calendar.DATE,1);
                calendar.add(Calendar.HOUR, 5);
                println(calendar.getTime());
                if (calendar.getTime().before(new Date())) {//过期订单只能取消和删除
//                    //htmlTag += "<font color='RED'>订单已经过期，不能操作</font>";
//                    if (orderInfo.valid < OrderValid.USER_CANCEL_VALID.code&&orderInfo.status <= OrderStatus.ORDERED_STATUS.code) {
//                        htmlTag += "<a href='" + createLink(controller: "customer", action: "cancelOrder", params: [orderId: orderId,backUrl:backUrl]) + "'>取消</a>&nbsp;&nbsp;";
//                    } else if (orderInfo.valid == OrderValid.USER_CANCEL_VALID.code) {//取消的订单可以删除
//                        htmlTag += "<a href='" + createLink(controller: "customer", action: "delOrder", params: [orderId: orderId,backUrl:backUrl]) + "'>删除</a>"
//                    }
                    if (orderInfo.valid < OrderValid.USER_CANCEL_VALID.code && orderInfo.status < OrderStatus.VERIFY_ORDERED_STATUS.code) {
                        htmlTag += "<font color='RED'>订单已过期</font>&nbsp;&nbsp;";
                        htmlTag += "<a href='" + createLink(controller: "customer", action: "delOrder", params: [orderId: orderId, backUrl: backUrl]) + "'>删除</a>"

                        out << htmlTag;
                        //println("guoqu");
                        return;
                    }
                }
                if (orderInfo.valid < OrderValid.USER_CANCEL_VALID.code) { // 未取消的订单
                    if (orderInfo.status <= OrderStatus.VERIFY_ORDERED_STATUS.code) {
                        htmlTag += "<a href='" + createLink(controller: "customer", action: "doDish", params: [orderId: orderId, backUrl: backUrl]) + "'>点菜</a>&nbsp;&nbsp;";
                    }
                    if (orderInfo.status <= OrderStatus.ORDERED_STATUS.code) {
                        htmlTag += "<a href='" + createLink(controller: "customer", action: "cancelOrder", params: [orderId: orderId, backUrl: backUrl]) + "'>取消</a>&nbsp;&nbsp;";
                    }
                    if (orderInfo.status <= OrderStatus.ORIGINAL_STATUS.code) {
                        htmlTag += "<a href='" + createLink(controller: "customer", action: "completeDish", params: [orderId: orderId, backUrl: backUrl]) + "'>完成点菜</a>&nbsp;&nbsp;";
                    }
                    if (orderInfo.status == OrderStatus.CHECKOUTED_STATUS.code) {//结账完成，可以评价
                        htmlTag += "<a href='" + createLink(controller: "customer", action: "appraiseOrder", params: [orderId: orderId, backUrl: backUrl]) + "'>评价</a>&nbsp;&nbsp;";
                    }
                } else if (orderInfo.valid == OrderValid.USER_CANCEL_VALID.code) {//取消的订单可以删除
                    htmlTag += "<a href='" + createLink(controller: "customer", action: "delOrder", params: [orderId: orderId, backUrl: backUrl]) + "'>删除</a>"
                }
            }
        }
        catch (Exception ex) {
            htmlTag += "<font color='RED'>" + ex.message + "</font>";
        }
//        htmlTag+="</div>";
        out << htmlTag;
    }

    //根据顾客根据点菜状态和有效性对显示不同的操作按钮
    def customerDishesOperation = { attr, body ->
        String htmlTag = "";
        try {
            long dishesId = lj.Number.toLong(attr.dishesId);
            String backUrl = attr.backUrl;
            //查询点菜
            DishesInfo dishesInfo = DishesInfo.get(dishesId);
            if (dishesInfo) {
                if (dishesInfo.valid == DishesValid.ORIGINAL_VALID.code && dishesInfo.status == DishesStatus.ORIGINAL_STATUS.code) {//初始态可以取消
                    htmlTag += "<a href='" + createLink(controller: "customer", action: "cancelDish", params: [dishIds: dishesInfo.id, orderId: dishesInfo.orderId, backUrl: backUrl]) + "'>取消</a>&nbsp;&nbsp;";
                }
                if (dishesInfo.valid == DishesValid.USER_CANCEL_VALID.code) {//用户取消的点菜可以删除
                    htmlTag += "<a href='" + createLink(controller: "customer", action: "delDish", params: [dishIds: dishesInfo.id, orderId: dishesInfo.orderId, backUrl: backUrl]) + "'>删除</a>&nbsp;&nbsp;";
                }
            }
        }
        catch (Exception ex) {
            htmlTag += "<font color='RED'>" + ex.message + "</font>";
        }
//        htmlTag+="</div>";
        out << htmlTag;
    }

    //根据工作人员根据订单状态和有效性对显示不同的操作按钮
    def staffOrderOperation = { attr, body ->
        String htmlTag = "";
        try {

            //获取工作人员信息
            StaffInfo staffInfo = StaffInfo.get(session.staffId);
            if (staffInfo) {
                //查询工作人员职务
                def staffPositionInfoList = StaffPositionInfo.findAllByStaffInfo(staffInfo);
                if (staffPositionInfoList) {
                    long orderId = lj.Number.toLong(attr.orderId);
                    String backUrl = attr.backUrl;
                    //查询订单
                    OrderInfo orderInfo = OrderInfo.get(orderId);
                    def positionList = staffPositionInfoList.collect { it.positionType };
                    //println("positionList-->"+positionList);
//                    if(orderInfo.orderType==OrderType.TAKE_OUT.code){
//                        //if ((PositionType.SHOPKEEPER.code in positionList) || (PositionType.WAITER.code in positionList) || (PositionType.WAITER_HEADER.code in positionList)) {   //服务员
//                            if ((PositionType.SHOPKEEPER.code in positionList) || (PositionType.WAITER.code in positionList)) {   //服务员
//                                if (orderInfo) {
//                                if (orderInfo.valid < OrderValid.USER_CANCEL_VALID.code) { // 未取消的订单
//                                    if (orderInfo.status < OrderStatus.SERVED_STATUS.code) {
//                                        htmlTag += "<a href='" + createLink(controller: "staff", action: "cancelOrder", params: [orderId: orderId,backUrl:backUrl]) + "'>取消</a>&nbsp;&nbsp;";
//                                        htmlTag += "<a href='" + createLink(controller: "staff", action: "doDish", params: [orderId: orderId],backUrl:backUrl) + "'>点菜</a>&nbsp;&nbsp;";
//                                    }
//                                    if (orderInfo.status == OrderStatus.ORIGINAL_STATUS.code) {
//                                        htmlTag += "<a href='" + createLink(controller: "staff", action: "completeDish", params: [orderId: orderId,backUrl:backUrl]) + "'>完成点菜</a>&nbsp;&nbsp;";
//                                        if (orderInfo.valid == OrderValid.ORIGINAL_VALID.code) {
//                                            htmlTag += "<a href='" + createLink(controller: "staff", action: "affirmValid", params: [orderId: orderId,backUrl:backUrl]) + "'>确认有效</a>&nbsp;&nbsp;";
//                                        }
//                                    }
//                                    if (orderInfo.status == OrderStatus.ORDERED_STATUS.code) {
//                                        htmlTag += "<a href='" + createLink(controller: "staff", action: "completeAffirmDish", params: [orderId: orderId,backUrl:backUrl]) + "'>确认完成</a>&nbsp;&nbsp;";
//                                    }
//                                    if(orderInfo.status == OrderStatus.VERIFY_ORDERED_STATUS.code){
//                                        htmlTag += "<a href='" + createLink(controller: "staff", action: "completePackage", params: [orderId: orderId,backUrl:backUrl]) + "'>打包完成</a>&nbsp;&nbsp;";
//                                    }
//                                }
//                            }
//                        }
//                        if ((PositionType.SHOPKEEPER.code in positionList) || (PositionType.ORDER_TAKER.code in positionList)) {
//                            //送餐员
//                            if (orderInfo) {
//                                if (orderInfo.valid == OrderValid.EFFECTIVE_VALID.code &&orderInfo.status==OrderStatus.SERVED_STATUS.code) {//有效且打包完成的订单可以运送
//                                    htmlTag += "<a href='" + createLink(controller: "staff", action: "beginShip", params: [orderId: orderId,backUrl:backUrl]) + "'>运送</a>"
//                                }
//                                if (orderInfo.valid == OrderValid.EFFECTIVE_VALID.code &&orderInfo.status==OrderStatus.SHIPPING_STATUS.code) {//有效且运送中的订单可以结账
//                                    htmlTag += "<a href='" + createLink(controller: "staff", action: "settleAccounts", params: [orderId: orderId,backUrl:backUrl]) + "'>结账</a>"
//                                }
//                            }
//                        }
//                    }else{
                    //if ((PositionType.SHOPKEEPER.code in positionList) || (PositionType.WAITER.code in positionList) || (PositionType.WAITER_HEADER.code in positionList)) {   //服务员
                    if ((PositionType.SHOPKEEPER.code in positionList) || (PositionType.WAITER.code in positionList)) {   //服务员
                        if (orderInfo) {
                            if (orderInfo.valid < OrderValid.USER_CANCEL_VALID.code) { // 未取消的订单
                                if (orderInfo.status <= OrderStatus.SERVED_STATUS.code) {
                                    htmlTag += "<a href='" + createLink(controller: "staff", action: "cancelOrder", params: [orderId: orderId, backUrl: backUrl]) + "'>取消</a>&nbsp;&nbsp;";
                                    htmlTag += "<a href='" + createLink(controller: "staff", action: "doDish", params: [orderId: orderId], backUrl: backUrl) + "'>点菜</a>&nbsp;&nbsp;";
                                }
                                if (orderInfo.status == OrderStatus.ORIGINAL_STATUS.code) {
                                    htmlTag += "<a href='" + createLink(controller: "staff", action: "completeDish", params: [orderId: orderId, backUrl: backUrl]) + "'>完成点菜</a>&nbsp;&nbsp;";
                                    if (orderInfo.valid == OrderValid.ORIGINAL_VALID.code) {
                                        htmlTag += "<a href='" + createLink(controller: "staff", action: "affirmValid", params: [orderId: orderId, backUrl: backUrl]) + "'>确认有效</a>&nbsp;&nbsp;";
                                    }
                                }
                                if (orderInfo.status == OrderStatus.ORDERED_STATUS.code) {
                                    htmlTag += "<a href='" + createLink(controller: "staff", action: "completeAffirmDish", params: [orderId: orderId, backUrl: backUrl]) + "'>确认点菜完成</a>&nbsp;&nbsp;";
                                }
//                                    if(!orderInfo.reachRestaurant&&orderInfo.orderType==OrderType.RESERVE.code){
//                                        htmlTag += "<a href='" + createLink(controller: "staff", action: "customerReach", params: [orderId: orderId],backUrl:backUrl) + "'>顾客到店</a>&nbsp;&nbsp;";
//                                    }
                            }else{//取消订单，可以删除掉
                                htmlTag += "<a href='" + createLink(controller: "staff", action: "delOrder", params: [orderId: orderId], backUrl: backUrl) + "'>删除</a>&nbsp;&nbsp;";
                            }
                        }
                    }
                    if ((PositionType.SHOPKEEPER.code in positionList) || (PositionType.BAD_TYPE.code in positionList)) {
                        //收银员
                        if (orderInfo) {
                            if (orderInfo.valid == OrderValid.EFFECTIVE_VALID.code && orderInfo.status < OrderStatus.CHECKOUTED_STATUS.code && orderInfo.status >= OrderStatus.VERIFY_ORDERED_STATUS.code) {//有效且确认点菜完成的订单可以结账
                                htmlTag += "<a href='" + createLink(controller: "staff", action: "settleAccounts", params: [orderId: orderId, backUrl: backUrl]) + "'>结账</a>"
                            }
                        }
                    }
//                    }


                }
            }
        }
        catch (Exception ex) {
            htmlTag += "<font color='RED'>" + ex.message + "</font>";
        }
//        htmlTag+="</div>";
        out << htmlTag;
    }

    //根据工作人员根据点菜状态和有效性对显示不同的操作按钮
    def staffDishesOperation = { attr, body ->
        String htmlTag = "";
        try {
            //获取工作人员信息
            StaffInfo staffInfo = StaffInfo.get(session.staffId);
            if (staffInfo) {
                //查询工作人员职务
                def staffPositionInfoList = StaffPositionInfo.findAllByStaffInfo(staffInfo);
                if (staffPositionInfoList) {
                    long dishesId = lj.Number.toLong(attr.dishesId);
                    String backUrl = attr.backUrl;
                    //查询点菜
                    DishesInfo dishesInfo = DishesInfo.get(dishesId);
                    //查询相应的订单的状态
//                    OrderInfo orderInfo = OrderInfo.get(dishesInfo.orderId);
//                    if(orderInfo.valid>=OrderValid.USER_CANCEL_VALID.code||orderInfo.status>=OrderStatus.SHIPPING_STATUS.code){
//                        out << htmlTag;
//                        return ;
//                    }
                    //OrderInfo orderInfo = OrderInfo.get(dishesInfo.orderId);

                    def positionList = staffPositionInfoList.collect { it.positionType };
                    println("positionList-->" + positionList);
                    //if ((PositionType.SHOPKEEPER.code in positionList) || (PositionType.WAITER.code in positionList) || (PositionType.WAITER_HEADER.code in positionList)) {   //服务员
                    if ((PositionType.SHOPKEEPER.code in positionList) || (PositionType.WAITER.code in positionList)) {   //服务员
                        if (dishesInfo.orderType == OrderType.TAKE_OUT.code) {
                            println("orderType-->" + dishesInfo.orderType);
                            println("valid+status-->" + dishesInfo.valid + "+" + dishesInfo.status);
                            if (dishesInfo.valid < DishesValid.USER_CANCEL_VALID.code && dishesInfo.status < DishesStatus.SERVED_STATUS.code) {//打包前都可以取消点菜
                                println("valid+status-->" + dishesInfo.valid + "+" + dishesInfo.status);
                                htmlTag += "<a href='" + createLink(controller: "staff", action: "cancelDish", params: [dishIds: dishesInfo.id, orderId: dishesInfo.orderId, backUrl: backUrl]) + "'>取消</a>&nbsp;&nbsp;";
                            }
                            if (dishesInfo.valid == DishesValid.ORIGINAL_VALID.code) {//初始态可以取消和确认点菜
                                htmlTag += "<a href='" + createLink(controller: "staff", action: "affirmDish", params: [dishIds: dishesInfo.id, orderId: dishesInfo.orderId, backUrl: backUrl]) + "'>确认点菜</a>&nbsp;&nbsp;";
                            }
                        } else {
                            if (dishesInfo.valid == DishesValid.ORIGINAL_VALID.code && dishesInfo.status == DishesStatus.ORIGINAL_STATUS.code) {//初始态可以取消和确认点菜
                                htmlTag += "<a href='" + createLink(controller: "staff", action: "cancelDish", params: [dishIds: dishesInfo.id, orderId: dishesInfo.orderId, backUrl: backUrl]) + "'>取消</a>&nbsp;&nbsp;";
                            }
                            if (dishesInfo.valid == DishesValid.ORIGINAL_VALID.code && dishesInfo.status == DishesStatus.ORIGINAL_STATUS.code) {//初始态可以取消和确认点菜
                                htmlTag += "<a href='" + createLink(controller: "staff", action: "affirmDish", params: [dishIds: dishesInfo.id, orderId: dishesInfo.orderId, backUrl: backUrl]) + "'>确认点菜</a>&nbsp;&nbsp;";
                            }
                        }
                        if (dishesInfo.valid == DishesValid.EFFECTIVE_VALID.code && dishesInfo.status == DishesStatus.COOKED_STATUS.code) {//做菜完成可以上菜
                            htmlTag += "<a href='" + createLink(controller: "staff", action: "completeServe", params: [dishIds: dishesInfo.id, orderId: dishesInfo.orderId, backUrl: backUrl]) + "'>上菜完成</a>&nbsp;&nbsp;";
                        }
                    }
                    if ((PositionType.SHOPKEEPER.code in positionList) || (PositionType.COOK.code in positionList)) {
                        //厨师
                        println("chushi");
                        if (dishesInfo.valid == DishesValid.EFFECTIVE_VALID.code && dishesInfo.status == DishesStatus.VERIFYING_STATUS.code) {//初始态可以取消和开始做菜
                            htmlTag += "<a href='" + createLink(controller: "staff", action: "cancelDish", params: [dishIds: dishesInfo.id, orderId: dishesInfo.orderId, backUrl: backUrl]) + "'>取消</a>&nbsp;&nbsp;";
                            htmlTag += "<a href='" + createLink(controller: "staff", action: "beginCook", params: [dishIds: dishesInfo.id, orderId: dishesInfo.orderId, backUrl: backUrl]) + "'>开始做菜</a>&nbsp;&nbsp;";
                        }
                        if (dishesInfo.valid == DishesValid.EFFECTIVE_VALID.code && dishesInfo.status == DishesStatus.COOKING_ORDERED_STATUS.code) {//做菜中的有效的点菜可以做菜完成操作
                            htmlTag += "<a href='" + createLink(controller: "staff", action: "completeCook", params: [dishIds: dishesInfo.id, orderId: dishesInfo.orderId, backUrl: backUrl]) + "'>做菜完成</a>&nbsp;&nbsp;";
                        }
                    }
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
