package lj.taos.order.staff

import lj.I18nError
import lj.Number
import lj.common.ValidationCode
import lj.data.DishesCollection
import lj.data.DishesInfo
import lj.data.FoodInfo
import lj.data.OrderInfo
import lj.data.ReserveOrderInfo
import lj.data.StaffInfo
import lj.data.TableInfo
import lj.enumCustom.*
import lj.taos.order.customer.CustomerOrderService

import java.text.SimpleDateFormat

class StaffOrderService {
    def webUtilService;
    def shopService;
//    MessageService messageService;

    def g = new org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib();

    def serviceMethod() {

    }

    /***********创建订单***********************
     * 参数和返回值同 customerOrderService.createOrder
     * ********/
    def createOrder(def params) {
        def session=webUtilService.getSession();
        StaffInfo staffInfo=webUtilService.getStaff();
        if(staffInfo){
            TableInfo tableInfo= TableInfo.findByCode(params.code);
            if(tableInfo==null){
                return [recode: ReCode.TABLE_NOT_EXIST];
            }
            OrderInfo orderInfo=OrderInfo.findByTableInfoAndValidAndStatusLessThan(tableInfo,OrderValid.EFFECTIVE_VALID.code,OrderStatus.CHECKOUTED_STATUS.code);
            if (orderInfo==null){//该桌上现在没有有效订单

                Date now = new Date();
                //检测桌位是否已经被预定
                //从店铺属性中获取订单直接间隔
                int intervalTime=60;//默认60分钟，单位分钟
                def reInfo=shopService.getShopEnabled();
                if(reInfo.recode!=ReCode.OK){
                    return reInfo;
                }
                if(reInfo.restaurantInfo.intervalTime){
                    intervalTime=reInfo.restaurantInfo.intervalTime;
                }
                Calendar calendar=Calendar.getInstance();
                calendar.setTime(now);
                calendar.add(Calendar.MINUTE,intervalTime*-1)
                Date lowerDateTime=calendar.getTime();
                println("lowerDateTime-->"+lowerDateTime.toLocaleString());
                calendar.setTime(now);
                calendar.add(Calendar.MINUTE,intervalTime)
                Date upperDateTime=calendar.getTime();
                println("upperDateTime-->"+upperDateTime.toLocaleString());
                ReserveOrderInfo reserveOrderInfo=ReserveOrderInfo.findByTableInfoAndDinnerTimeBetweenAndValidAndStatus(tableInfo,lowerDateTime,upperDateTime,OrderValid.EFFECTIVE_VALID.code,ReserveOrderStatus.ORIGINAL_STATUS.code);
                if(reserveOrderInfo){
                    return [recode: ReCode.TABLE_IS_RESERVED];
                }
                orderInfo=new OrderInfo();
                orderInfo.tableInfo=tableInfo;
                orderInfo.waiter=staffInfo;
                orderInfo.partakeCode=ValidationCode.getAuthCodeStr(4,ValidationCode.NUMBER);
                String orderNumStr=now.getTime()+ValidationCode.getAuthCodeStr(2,ValidationCode.NUMBER);
                long orderNum=Long.parseLong(orderNumStr);
                orderInfo.orderNum=orderNum;
                //获取订单店内编号最大值
                String sqlStr="select max(numInRestaurant) from OrderInfo";
                def result=OrderInfo.executeQuery(sqlStr);
                long numInRestaurant=1;
                if(result!=null&&result[0]!=null){
                    numInRestaurant=result[0]+1;
                }
                orderInfo.numInRestaurant=numInRestaurant;
                if(!orderInfo.save(flush: true)){
                    return [recode: ReCode.SAVE_FAILED, orderInfo: orderInfo,errors:I18nError.getMessage(g,orderInfo.errors.allErrors)];
                }
            }else{
                return [recode: ReCode.HAVE_ORDER_ALREADY];
            }
                return [recode: ReCode.OK,orderInfo:orderInfo];
        }
        else{
            return [recode: ReCode.NOT_LOGIN];
        }
    }

    /**********订单状态改变***********
     * params是传入的参数
     * 参数格式为：
     * orderId=Number.toLong(params.orderId);//订单号
     * statusCode=params.statusCode?:OrderStatus.ORDERED_STATUS.code;// 状态代码
     * statusCode的取值有：
     * ORDERED_STATUS(1,'点菜完成'),
     * VERIFY_ORDERED_STATUS(2,'确认点菜完成') ,SERVED_STATUS(3,'上菜完成'),
     * SHIPPING_STATUS(4,'运送中'),CHECKOUTED_STATUS(5,'结账完成')
     * //点菜列表参数,非必须
     * foodIds=12或foodIds=[112,231...]
     * counts=1或counts=[2,3...]
     * remarks=ddd或remarks=[dd,,dd...]
     *
     * 返回值
     * [recode: ReCode.SAVE_FAILED,errors:orderInfo.errors.allErrors];保存失败
     * [recode: ReCode.NO_ORDER];订单不存在
     * [recode:ReCode.NOT_LOGIN];用户没有登录
     * [recode: ReCode.OK];成功
     * **************/
    def orderStatusUpdate(def params){
        def session = webUtilService.getSession();
        //SimpleDateFormat sdfDate=new SimpleDateFormat("yyyy-MM-dd");
        //SimpleDateFormat sdfTime=new SimpleDateFormat("HH:mm:ss");

        //工作人员ID
        long staffId = Number.toLong(webUtilService.getStaffId());//工作人员ID
        StaffInfo staffInfo=webUtilService.getStaff();
        if (staffId) {
            //获取参数
            Long orderId = Number.toLong(params.orderId);//订单Id
            int statusCode=Number.toInteger(params.statusCode);//状态代码

            if (statusCode == OrderStatus.ORDERED_STATUS.code) { //如果是帮顾客做"点菜完成"操作则调用顾客方法就行了
                //如果是点菜完成的话且传入了点菜列表，则先做点菜
                if(statusCode==OrderStatus.ORDERED_STATUS.code&&params.foodIds){
                    def reObj=addDishes(params);
                    if(reObj.recode!=ReCode.OK){//点菜没有顺利完成则返回
                        return reObj;
                    }
                }
                if (statusCode == OrderStatus.VERIFY_ORDERED_STATUS.code) {// 确认点菜完成
                    //先修改订单中点菜的状态为状态为“0”有效性为“0”的菜的状态改为“1确定完成”，有效性改为“1有效”。
                    def reObj=dishConfirm(params);
                    if(reObj.recode!=ReCode.OK){
                        return reObj;
                    }
                }
                OrderInfo orderInfo=OrderInfo.findById(orderId);
                if(orderInfo){
                    orderInfo.status=statusCode;
                    if(!orderInfo.save(flush: true)){//保存数据库失败
                        return [recode: ReCode.SAVE_FAILED,errors:I18nError.getMessage(g,orderInfo.errors.allErrors)];
                    }
                    return [recode: ReCode.OK];
                }
                else{
                    return [recode: ReCode.ORDER_NOT_EXIST];
                }
            } else {
                if (statusCode == OrderStatus.VERIFY_ORDERED_STATUS.code) {// 确认点菜完成
                    //先修改订单中点菜的状态为状态为“0”有效性为“0”的菜的状态改为“1确定完成”，有效性改为“1有效”。
                    dishConfirm(params);
//                    //发送消息让厨师端，点菜列表进行更新
//                    //生成消息通知顾客
//                    def msgParams=[:];
//                    msgParams.orderId=orderId;
//                    msgParams.type=MessageType.UPDATE_DISH_LIST.code;
//                    msgParams.receiveId=staffId;
//                    msgParams.content="需要更新点菜列表";
//                    msgParams.sendType=MsgSendType.STAFF_TO_STAFF.code;
//                    msgParams.restaurantId=0;
//                    def reInfo=messageService.createMsg(msgParams);
//                    if(reInfo.recode!=ReCode.OK){
//                        println("保存消息失败，但对于订单的产生没有致命影响，故忽略此错误，请系统管理员注意查证："+",reInfo="+reInfo);
//                    }

                }
                //修改订单订单状态，如果有效性为初始态则更新有效性改为“1有效”
                OrderInfo orderInfo=OrderInfo.get(orderId);
                if(orderInfo){
                    orderInfo.status=statusCode;         //状态更新为需要的状态
                    if(orderInfo.valid==OrderValid.ORIGINAL_VALID.code)
                        orderInfo.valid=OrderValid.EFFECTIVE_VALID.code;//有效性更新为有效
                    if(statusCode == OrderStatus.CHECKOUTED_STATUS.code){//如果是算账的话更新收银员id为当前工作人员id
                        orderInfo.cashier=staffInfo;
                    }
                    if(orderInfo.save(flush: true)){

//                        //生成消息通知顾客
//                        def msgParams=[:];
//                        msgParams.orderId=orderInfo.id;
//                        msgParams.type=MessageType.ORDER_HANDLE_TYPE.code;
//                        msgParams.receiveId=orderInfo.clientId;
//                        msgParams.content="订单状态改变，"+OrderStatus.getLable(orderInfo.status);
//                        msgParams.sendType=MsgSendType.STAFF_TO_CUSTOMER.code;
//                        msgParams.restaurantId=orderInfo.restaurantId;
//                        def reInfo=messageService.createMsg(msgParams);
//                        if(reInfo.recode!=ReCode.OK){
//                            println("保存消息失败，但对于订单的产生没有致命影响，故忽略此错误，请系统管理员注意查证："+",reInfo="+reInfo);
//                        }

                        return [recode: ReCode.OK];
                    }
                    else{
                        return [recode: ReCode.SAVE_FAILED,errors:I18nError.getMessage(g,orderInfo.errors.allErrors)];
                    }
                }
                else{
                    return [recode: ReCode.NO_ORDER];
                }
            }
        } else {
            return [recode: ReCode.NOT_LOGIN];
        }
    }

    /*******订单有效性确认，这里只是确认订单的有效性，这个时候点菜可能没完成，点菜确认也可能没完成**
     *  params是传入的参数
     * 参数格式为：
     * orderId=Number.toLong(params.orderId);//订单号
     *
     * 返回值
     * [recode: ReCode.SAVE_FAILED,errors:orderInfo.errors.allErrors];保存失败
     * [recode: ReCode.NO_ORDER];订单不存在
     * [recode:ReCode.NOT_LOGIN];用户没有登录
     * [recode: ReCode.ORDER_CANNOT_UPDATE_VALID];订单当前有效性下不能更改有效性
     * [recode: ReCode.OK];成功
     * **********/
    def orderValidAffirm(def params){
        def session = webUtilService.getSession();
        //SimpleDateFormat sdfDate=new SimpleDateFormat("yyyy-MM-dd");
        //SimpleDateFormat sdfTime=new SimpleDateFormat("HH:mm:ss");
        //工作人员ID
        long staffId = Number.toLong(webUtilService.getStaffId());//工作人员ID
        StaffInfo staffInfo=webUtilService.getStaff();
        if (staffId) {
            //获取参数
            Long orderId = Number.toLong(params.orderId);//订单Id

            //根据订单号查询出订单
            OrderInfo orderInfo=OrderInfo.get(orderId);
            if(orderInfo){
                if(orderInfo.valid==OrderValid.ORIGINAL_VALID.code){//当前有效性是可以更新有效性的
                    orderInfo.valid=OrderValid.EFFECTIVE_VALID.code;//更新订单有效性为有效
                    if(orderInfo.save(flush: true)){

//                        //生成消息通知顾客
//                        def msgParams=[:];
//                        msgParams.orderId=orderInfo.id;
//                        msgParams.type=MessageType.ORDER_HANDLE_TYPE.code;
//                        msgParams.receiveId=orderInfo.clientId;
//                        msgParams.content="你的订单'"+orderInfo.orderNum+"'已经确认为有效订单";
//                        msgParams.sendType=MsgSendType.STAFF_TO_CUSTOMER.code;
//                        msgParams.restaurantId=orderInfo.restaurantId;
//                        def reInfo=messageService.createMsg(msgParams);
//                        if(reInfo.recode!=ReCode.OK){
//                            println("保存消息失败，但对于订单的产生没有致命影响，故忽略此错误，请系统管理员注意查证："+",reInfo="+reInfo);
//                        }

                        return [recode: ReCode.OK];
                    }
                    else{
                        return [recode: ReCode.SAVE_FAILED,errors: I18nError.getMessage(g,orderInfo.errors.allErrors)];
                    }
                }
                else {//当前有效性下不能更改有效性
                    return [recode: ReCode.ORDER_CANNOT_UPDATE_VALID];
                }
            }
            else{
                return [recode: ReCode.NO_ORDER];
            }

        } else {
            return [recode: ReCode.NOT_LOGIN];
        }
    }

//    def customerReach(def params){
//        def session = webUtilService.getSession();
//        //SimpleDateFormat sdfDate=new SimpleDateFormat("yyyy-MM-dd");
//        //SimpleDateFormat sdfTime=new SimpleDateFormat("HH:mm:ss");
//        //工作人员ID
//        long staffId = Number.toLong(session.staffId);//工作人员ID
//
//        if (staffId) {
//            //获取参数
//            Long orderId = Number.toLong(params.orderId);//订单Id
//
//            //根据订单号查询出订单
//            OrderInfo orderInfo=OrderInfo.get(orderId);
//            if(orderInfo){
//                if(orderInfo.valid==OrderValid.ORIGINAL_VALID.code){//当前有效性是可以更新有效性的
//                    orderInfo.valid=OrderValid.EFFECTIVE_VALID.code;//更新订单有效性为有效
//                }
//                if(orderInfo.status < OrderStatus.SERVED_STATUS.code){
//                    orderInfo.reachRestaurant=true;
//                    if(orderInfo.save(flush: true)){
//
//                        //生成消息通知顾客
////                        def msgParams=[:];
////                        msgParams.orderId=orderInfo.id;
////                        msgParams.type=MessageType.ORDER_HANDLE_TYPE.code;
////                        msgParams.receiveId=orderInfo.clientId;
////                        msgParams.content="你的订单'"+orderInfo.orderNum+"'已经确认为有效订单";
////                        msgParams.sendType=MsgSendType.STAFF_TO_CUSTOMER.code;
////                        msgParams.restaurantId=orderInfo.restaurantId;
////                        def reInfo=messageService.createMsg(msgParams);
////                        if(reInfo.recode!=ReCode.OK){
////                            println("保存消息失败，但对于订单的产生没有致命影响，故忽略此错误，请系统管理员注意查证："+",reInfo="+reInfo);
////                        }
//
//                        return [recode: ReCode.OK];
//                    }
//                    else{
//                        return [recode: ReCode.SAVE_FAILED,errors: orderInfo.errors.allErrors];
//                    }
//                }
//                else {//当前有效性下不能更改有效性
//                    return [recode: ReCode.ORDER_CANNOT_UPDATE_VALID];
//                }
//            }
//            else{
//                return [recode: ReCode.NO_ORDER];
//            }
//
//        } else {
//            return [recode: ReCode.NOT_LOGIN];
//        }
//    }

    /**********订单取消
     *  params是传入的参数
     * 参数格式为：
     * orderId=Number.toLong(params.orderId);//订单号
     * cancelReason=params.cancelReason;//订单取消原因
     *
     * 返回值
     * [recode: ReCode.SAVE_FAILED,errors:orderInfo.errors.allErrors];保存失败
     * [recode: ReCode.NO_ORDER];订单不存在
     * [recode:ReCode.NOT_LOGIN];用户没有登录
     * [recode: ReCode.ORDER_CANNOT_UPDATE_VALID];订单当前有效性下不能更改有效性
     * [recode: ReCode.OK];成功
     * **********/
    def orderCancel(def params){
        def session = webUtilService.getSession();
        //SimpleDateFormat sdfDate=new SimpleDateFormat("yyyy-MM-dd");
        //SimpleDateFormat sdfTime=new SimpleDateFormat("HH:mm:ss");
        //工作人员ID
        long staffId = Number.toLong(webUtilService.getStaffId());//工作人员ID
        StaffInfo staffInfo=webUtilService.getStaff();
        if (staffId) {
            //获取参数
            Long orderId = Number.toLong(params.orderId);//订单Id
            String cancelReason=params.cancelReason;//订单取消原因

            //根据订单号查询出订单
            OrderInfo orderInfo=OrderInfo.get(orderId);
            if(orderInfo){
                //检查订单当前状态是否能取消
                if(orderInfo.status<=OrderStatus.VERIFY_ORDERED_STATUS.code&&orderInfo.valid<=OrderValid.EFFECTIVE_VALID.code){ //订单能取消
//                    if(){//如果有状态大于等于做菜中的点菜，则订单不能取消??,这个地方让服务员也可以取消吧，不然出现正在做菜的过程中，顾客走掉的话，可以取消订单来阻止厨师继续为该订单做菜
//
//                    }
                    //调用点菜服务方法取消订单所有点菜
                    cancelDish(params);
                    //更新订单有效性为饭店取消
                    orderInfo.valid=OrderValid.RESTAURANT_CANCEL_VALID.code;
                    orderInfo.cancelReason=cancelReason;
                    if(orderInfo.save(flush: true)){

//                        //生成消息通知顾客
//                        def msgParams=[:];
//                        msgParams.orderId=orderInfo.id;
//                        msgParams.type=MessageType.ORDER_HANDLE_TYPE.code;
//                        msgParams.receiveId=orderInfo.clientId;
//                        msgParams.content="你的订单'"+orderInfo.orderNum+"'已经被饭店取消，取消原因是："+orderInfo.cancelReason;
//                        msgParams.sendType=MsgSendType.STAFF_TO_CUSTOMER.code;
//                        msgParams.restaurantId=orderInfo.restaurantId;
//                        def reInfo=messageService.createMsg(msgParams);
//                        if(reInfo.recode!=ReCode.OK){
//                            println("保存消息失败，但对于订单的产生没有致命影响，故忽略此错误，请系统管理员注意查证："+",reInfo="+reInfo);
//                        }

                        return [recode: ReCode.OK];
                    }
                    else{
                        return [recode: ReCode.SAVE_FAILED,errors: I18nError.getMessage(g,orderInfo.errors.allErrors)];
                    }
                }
                else{//订单不能取消
                    return [recode: ReCode.ORDER_CANNOT_UPDATE_VALID];
                }
            }
            else{
                return [recode: ReCode.NO_ORDER];
            }
        } else {
            return [recode: ReCode.NOT_LOGIN];
        }
    }

    /*******************订单列表*
     *  参数和返回值同 customerOrderService.orderList
     * ***********/
    def orderList(def params){
        def session=webUtilService.getSession();
        SimpleDateFormat sdfDate=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        //SimpleDateFormat sdfTime=new SimpleDateFormat("HH:mm:ss");
        //取出用户ID
        //long userId=Number.toLong(session.userId);//用户ID
//         long clientId=webUtilService.getClientId();
//         if(byWaiter){//如果是服务员帮助创建订单，则取服务员ID作为用户ID
//             clientId=Number.toLong(session.staffId);
//         }
//         println("clientId-->"+clientId);
//         if(clientId){
        //获取参数
        long orderId=Number.toLong(params.orderId);//订单ID
        long restaurantId=Number.toLong(params.restaurantId);//饭店ID
        long tableId=Number.toLong(params.tableId);//桌位ID
        String beginTimeStr=params.beginTime;//开始时间
        Date beginTime=null;
        try{beginTime=sdfDate.parse(beginTimeStr);}catch (Exception ex){}
        String endTimeStr=params.endTime;//截止时间
        Date endTime=null;
        try{endTime=sdfDate.parse(endTimeStr);}catch (Exception ex){}
        int reserveType=-1;
        if(params.reserveType!=null)
            reserveType=Number.toInteger(params.reserveType);// 用餐类别（早餐、午餐、晚餐）
        int status=-1;
        if(params.status!=null)
            status=Number.toInteger(params.status);//订单状态
        int valid=-1;
        if (params.valid!=null)
            valid=Number.toInteger(params.valid);//有效性
        String orderNum=params.orderNum;//订单流水号

        int statusGe=-1;
        if(params.statusGe!=null)
            statusGe=Number.toInteger(params.statusGe);//订单状态
        int validGe=-1;
        if (params.validGe!=null)
            validGe=Number.toInteger(params.validGe);//有效性
        int statusLe=-1;
        if(params.statusLe!=null)
            statusLe=Number.toInteger(params.statusLe);//订单状态
        int validLe=-1;
        if (params.validLe!=null)
            validLe=Number.toInteger(params.validLe);//有效性

        int orderType=-1;
        if (params.orderType!=null)
            orderType=Number.toInteger(params.orderType);//订单类型

        if (!params.max) {
            params.max = 10
        }
        if (!params.offset) {
            params.offset = 0;
        }

//             def cIds=userService.getIds(ClientInfo.get(clientId));
        def condition={
            if(orderId){
                eq("id",orderId);//id条件
            }
            if(tableId){
                eq("tableInfo.id",tableId);//桌位ID条件
            }
            if(beginTime){
                ge("createTime",beginTime);//日期条件
            }
            if(endTime){
                le("createTime",endTime);//日期条件
            }
//            if(reserveType>=0){
//                eq("reserveType",reserveType);//用餐类别（早餐、午餐、晚餐）条件
//            }
            if(status>=0){
                eq("status",status);//订单状态条件
            }
            if(valid>=0){
                eq("valid",valid);//订单有效性条件
            }
            if(statusGe>=0){
                ge("status",statusGe);//订单状态条件
            }
            if(validGe>=0){
                ge("valid",validGe);//订单有效性条件
            }
            if(statusLe>=0){
                le("status",statusLe);//订单状态条件
            }
            if(validLe>=0){
                le("valid",validLe);//订单有效性条件
            }
            if(orderNum){
                eq("orderNum",orderNum);//订单流水号条件
            }
            if(orderType>=0){
                eq("orderType",orderType);//订单类型条件
            }
        }

        if(!params.sort){//如果没有排序，则按ID倒排序
            params.sort="id";
            params.order="desc";
        }

        def orderList=OrderInfo.createCriteria().list(params,condition);
        def totalCount=OrderInfo.createCriteria().count(condition);

        return [recode:ReCode.OK,totalCount:totalCount,orderList:orderList];
//         }
//         else{
//             return [recode:ReCode.NOT_LOGIN];
//         }
    }

    /***********************算账,这里只是计算出费用结果并将算账结果更新到订单中去,更新订单状态为算账完成用更新订单状态的方法
     *
     * *****************/
     def castAccounts(def params){
         def session = webUtilService.getSession();
         //SimpleDateFormat sdfDate=new SimpleDateFormat("yyyy-MM-dd");
         //SimpleDateFormat sdfTime=new SimpleDateFormat("HH:mm:ss");
         //工作人员ID
         long staffId = Number.toLong(webUtilService.getStaffId());//工作人员ID
         StaffInfo staffInfo=webUtilService.getStaff();
         if (staffId) {
             //获取参数
             Long orderId = Number.toLong(params.orderId);//订单Id

             //根据订单号查询出订单
             OrderInfo orderInfo=OrderInfo.get(orderId);
             if(orderInfo){
                 // 订单有效且没有到已算账状态才能算账
                 if(orderInfo.valid==OrderValid.EFFECTIVE_VALID.code&&orderInfo.status<OrderStatus.CHECKOUTED_STATUS.code&&orderInfo.status>=OrderStatus.VERIFY_ORDERED_STATUS.code){
                     //如果已经计算出来了的则直接返回
//                     if (orderInfo.totalAccount != null && orderInfo.realAccount != null) {
//                         return [recode: ReCode.OK, orderInfo: orderInfo];
//                     }
                     //根据订单号查出点菜总金额
                     String sqlStr = "select sum(foodPrice*num) from DishesInfo where order=" + orderId +
                             " and status=" + DishesStatus.SERVED_STATUS.code + " and valid=" + DishesValid.EFFECTIVE_VALID.code;
                     def totalAccounts = DishesInfo.executeQuery(sqlStr);
                     def totalAccount = 0d;
                     if (totalAccounts) {
                         totalAccount = totalAccounts[0];
                     }
                     def postage=0d;
//                     //如果是外卖需要加上运费
//                     if(orderInfo.tableId==0){
//                        RestaurantInfo restaurantInfo=RestaurantInfo.get(orderInfo.restaurantId);
//                        if(restaurantInfo){
//                            if(restaurantInfo.freeFreight==0||totalAccount<restaurantInfo.freeFreight){
//                                postage=restaurantInfo.freight;
//                            }
//                        }
//                     }

                     //这里暂时实付金额就是总金额加运费
                     if(!totalAccount){
                         totalAccount=0;
                     }
                     if(!postage){
                         postage=0;
                     }
                     Double realAccount=totalAccount+postage;

                     //保存数据到数据库
                     orderInfo.totalAccount=totalAccount;
//                     orderInfo.postage=postage;
                     orderInfo.realAccount=realAccount;
                     if(orderInfo.save(flush: true)){
                         //查询是否存在做菜中或做菜完成的点菜，有则需提醒收银员
                         DishesInfo dishesInfo=DishesInfo.findByStatusBetweenAndValidAndOrder(DishesStatus.COOKING_ORDERED_STATUS.code,DishesStatus.COOKED_STATUS.code,DishesValid.EFFECTIVE_VALID.code,OrderInfo.get(orderId));
                         if(dishesInfo){
                             return [recode: ReCode.OK, orderInfo: orderInfo,warning:"还有做菜中或做菜完成的点菜因未上菜而未将费用计入到消费费用中，请注意查看确定后再做结账操作！"];
                         }else{
                            return [recode: ReCode.OK, orderInfo: orderInfo];
                         }
                     }
                     else{
                         return [recode: ReCode.SAVE_FAILED,errors: I18nError.getMessage(g,orderInfo.errors.allErrors)];
                     }
                 }
                 else{//订单不能算账
                     return [recode: ReCode.ORDER_CANNOT_CAST_ACCOUNT];
                 }
             }
             else{
                 return [recode: ReCode.NO_ORDER];
             }
         } else {
             return [recode: ReCode.NOT_LOGIN];
         }
     }
    /*******算账，更新状态和更新实收金额**
     *  params是传入的参数
     * 参数格式为：
     * orderId=Number.toLong(params.orderId);//订单号
     * realAccount=Number.toDouble(params.realAccount);//实收金额
     *
     * 返回值
     * [recode: ReCode.SAVE_FAILED,errors:orderInfo.errors.allErrors];保存失败
     * [recode: ReCode.NO_ORDER];订单不存在
     * [recode:ReCode.NOT_LOGIN];用户没有登录
     * [recode: ReCode.ORDER_CANNOT_UPDATE_VALID];订单当前有效性下不能更改有效性
     * [recode: ReCode.OK];成功
     * **********/
    def submitCastAccounts(def params){
        def session = webUtilService.getSession();
        //SimpleDateFormat sdfDate=new SimpleDateFormat("yyyy-MM-dd");
        //SimpleDateFormat sdfTime=new SimpleDateFormat("HH:mm:ss");
        //工作人员ID
        long staffId = Number.toLong(webUtilService.getStaffId());//工作人员ID
        StaffInfo staffInfo=webUtilService.getStaff();
        if (staffId) {
            //获取参数
            Long orderId = Number.toLong(params.orderId);//订单Id
            double realAccount=Number.toDouble(params.realAccount);//实收金额

            //根据订单号查询出订单
            OrderInfo orderInfo=OrderInfo.get(orderId);
            if(orderInfo){
                if(orderInfo.valid==OrderValid.EFFECTIVE_VALID.code&&orderInfo.status<OrderStatus.CHECKOUTED_STATUS.code){// 订单有效且没有到已算账状态才能算账
                    orderInfo.status=OrderStatus.CHECKOUTED_STATUS.code;//更新订单状态为算账完成
                    if(realAccount>0)
                        orderInfo.realAccount=realAccount;
                    if(orderInfo.save(flush: true)){
                        //未做的有效的菜改为取消，并给厨师端点菜列表发消息刷新列表
                        def dishesInfos=DishesInfo.findAllByValidAndStatus(DishesValid.EFFECTIVE_VALID.code,DishesStatus.VERIFYING_STATUS.code);
                        if(dishesInfos){
                            dishesInfos.each {
                                it.valid=DishesValid.RESTAURANT_AFTER_VERIFYED_CANCEL_VALID.code;
                                it.cancelReason="结账完成";
                            }
//                            //发送消息让厨师端，点菜列表进行更新
//                            def msgParams=[:];
//                            msgParams.orderId=orderId;
//                            msgParams.type=MessageType.UPDATE_DISH_LIST.code;
//                            msgParams.receiveId=staffId;
//                            msgParams.content="需要更新点菜列表";
//                            msgParams.sendType=MsgSendType.STAFF_TO_STAFF.code;
//                            msgParams.restaurantId=0;
//                            def reInfo=messageService.createMsg(msgParams);
//                            if(reInfo.recode!=ReCode.OK){
//                                println("保存消息失败，但对于订单的产生没有致命影响，故忽略此错误，请系统管理员注意查证："+",reInfo="+reInfo);
//                            }
                        }

//                        //生成消息通知顾客
//                        def msgParams=[:];
//                        msgParams.orderId=orderInfo.id;
//                        msgParams.type=MessageType.ORDER_HANDLE_TYPE.code;
//                        msgParams.receiveId=orderInfo.clientId;
//                        msgParams.content="你的订单'"+orderInfo.orderNum+"'已经算账完成，你可以对本次用餐进行评价";
//                        msgParams.sendType=MsgSendType.STAFF_TO_CUSTOMER.code;
//                        msgParams.restaurantId=orderInfo.restaurantId;
//                        def reInfo=messageService.createMsg(msgParams);
//                        if(reInfo.recode!=ReCode.OK){
//                            println("保存消息失败，但对于订单的产生没有致命影响，故忽略此错误，请系统管理员注意查证："+",reInfo="+reInfo);
//                        }

                        return [recode: ReCode.OK,orderInfo:orderInfo];
                    }
                    else{
                        return [recode: ReCode.SAVE_FAILED,orderInfo:orderInfo,errors:I18nError.getMessage(g, orderInfo.errors.allErrors)];
                    }
                }
                else {//当前有效性下不能更改有效性
                    return [recode: ReCode.ORDER_CANNOT_UPDATE_VALID];
                }
            }
            else{
                return [recode: ReCode.NO_ORDER];
            }

        } else {
            return [recode: ReCode.NOT_LOGIN];
        }
    }

    /********************订单删除******
     *params是传入的参数
     * 参数格式为：
     * orderId=Number.toLong(params.orderId);//订单号
     *
     * 返回值
     * [recode: ReCode.ORDER_CANNOT_DELETE];订单不能删除
     * [recode: ReCode.ORDER_NOT_EXIST];订单不存在
     * [recode:ReCode.NOT_LOGIN];用户没有登录
     * [recode: ReCode.DENIED_USER] ;//非法用户
     * [recode: ReCode.OK];成功
     * ****************/
    def delOrder(def params){
        def session=webUtilService.getSession();
        //SimpleDateFormat sdfDate=new SimpleDateFormat("yyyy-MM-dd");
        //SimpleDateFormat sdfTime=new SimpleDateFormat("HH:mm:ss");
        //取出用户ID
        long staffId = Number.toLong(webUtilService.getStaffId());//工作人员ID
        StaffInfo staffInfo=webUtilService.getStaff();
        if(staffId){
            //获取参数
            long orderId=Number.toLong(params.orderId);//订单号

            //获取饭店ID
            if(!staffInfo){
                return [recode: ReCode.DENIED_USER] ;
            }

            OrderInfo orderInfo=OrderInfo.findById(orderId);
            if(orderInfo){
                //检查订单是否可以删除
                //if(orderInfo.valid!=OrderValid.RESTAURANT_CANCEL_VALID.code){//订单不能删除
                if(orderInfo.valid!=OrderValid.RESTAURANT_CANCEL_VALID.code&&orderInfo.valid!=OrderValid.USER_CANCEL_VALID.code){//订单不能删除
                    return [recode: ReCode.ORDER_CANNOT_DELETE];
                }
                //删除订单中点菜
                delDish(params);
                orderInfo.delete(flush: true);
                return [recode: ReCode.OK];
            }
            else{
                return [recode: ReCode.ORDER_NOT_EXIST];
            }
        }
        else{
            return [recode:ReCode.NOT_LOGIN];
        }
    }
    def orderInfo(def params){
        def session=webUtilService.getSession();
        //SimpleDateFormat sdfDate=new SimpleDateFormat("yyyy-MM-dd");
        //SimpleDateFormat sdfTime=new SimpleDateFormat("HH:mm:ss");

        //获取参数
        long orderId=Number.toLong(params.orderId);//订单号
        //long userId=Number.toLong(session.userId);//用户ID
//        long clientId=webUtilService.getClientId();
//        String partakeCode = params.partakeCode;//点菜参与码
//        if(!partakeCode){ //没有点菜验证码则需要验证用户是否登录
//            if (byWaiter) {//如果是服务员帮助，则取服务员ID作为用户ID
//                clientId = Number.toLong(session.staffId);
//                if (clientId == 0) { //没登录
//                    return [recode: ReCode.NOT_LOGIN];
//                }
//            }
//        }
        OrderInfo orderInfo=OrderInfo.findById(orderId);
        if(orderInfo){
//            //点菜参与码是否正确
//            if (clientId == orderInfo.clientId || byWaiter) {//用户登录切是订单创建的用户或者是服务员帮忙点菜，则不需要参与验证码
//
//            } else {//检查点菜参与验证码是否正确
//                if (partakeCode != orderInfo.partakeCode) { //点菜参与码不正确
//                    return [recode: ReCode.ERROR_PARTAKECODE];
//                }
//            }
            return [recode: ReCode.OK,orderInfoInstance:orderInfo];
        }
        else{
            return [recode: ReCode.ORDER_NOT_EXIST];
        }
    }
    def completeDish(def params){
        def session=webUtilService.getSession();
        //SimpleDateFormat sdfDate=new SimpleDateFormat("yyyy-MM-dd");
        //SimpleDateFormat sdfTime=new SimpleDateFormat("HH:mm:ss");
        //取出用户ID
        //long userId=Number.toLong(session.userId);//用户ID
//        long clientId=webUtilService.getClientId();
//        if (byWaiter) {//如果是服务员帮助更新订单状态为点菜完成，则取服务员ID作为用户ID
//            clientId = Number.toLong(session.staffId);
//        }
//        if(clientId){
        //获取参数
        long orderId=Number.toLong(params.orderId);//订单号
        OrderInfo orderInfo=null;
        orderInfo=OrderInfo.findById(orderId);
        if(orderInfo){
            //检查订单是否可以做完成点菜操作
            if(orderInfo.valid>OrderValid.EFFECTIVE_VALID.code||orderInfo.status>OrderStatus.ORIGINAL_STATUS.code){//订单不能完成点菜
                return [recode: ReCode.ORDER_CANNOT_COMPLETE_DISH];
            }
            //完成点菜
            //statusCode=params.statusCode?:OrderStatus.ORDERED_STATUS.code;// 状态代码
            params.statusCode= OrderStatus.ORDERED_STATUS.code;
            return orderStatusUpdate(params);
        }
        else{
            return [recode: ReCode.ORDER_NOT_EXIST];
        }
//        }
//        else{
//            return [recode:ReCode.NOT_LOGIN];
//        }
    }


    /**************点菜确认*****
     * params是传入的参数
     * 参数格式为：
     * orderId=Number.toLong(params.orderId);//订单Id
     * dishIds=12或dishIds=[112,231...] //点菜列表参数
     * 以上参数任选一个传入，传入订单ID则按订单ID操作
     *
     * 返回值
     * [recode: ReCode.ERROR_PARAMS];//参数错误
     * [recode:ReCode.NOT_LOGIN];用户没有登录
     * [recode: ReCode.OK];成功
     * ********/
    def dishConfirm(def params){
        def session=webUtilService.getSession();
        //SimpleDateFormat sdfDate=new SimpleDateFormat("yyyy-MM-dd");
        //SimpleDateFormat sdfTime=new SimpleDateFormat("HH:mm:ss");

        //获取参数
        Long orderId=Number.toLong(params.orderId);//订单Id
        def dishIds=params.dishIds;//点菜列表

        def dishList=null;
        if(orderId){//订单ID存在则按订单ID取消
            dishList=DishesInfo.findAllByOrder(OrderInfo.get(orderId));
        }
        else if(dishIds){//不然如果按点菜Id列表取消
            def dishIdList=[];
            if(dishIds instanceof String){
                dishIdList.add(Number.toLong(dishIds));
            }
            else if(dishIds instanceof String[]){
                for(int i=0;i<dishIds.length;i++){
                    dishIdList.add(Number.toLong(dishIds[i]));
                }
            }
            dishList=DishesInfo.findAllByIdInList(dishIdList);
        }
        else {
            return [recode: ReCode.ERROR_PARAMS];//参数错误
        }
        if(dishList){
            dishList.each {
                if(it.valid==DishesValid.ORIGINAL_VALID.code)
                {//有效性为初始状态下且状态也为初始态时更新有效性和状态分别为1有效和1确认完成
                    it.valid=DishesValid.EFFECTIVE_VALID.code;
                }
                if(it.status==DishesStatus.ORIGINAL_STATUS.code){
                    it.status=DishesStatus.VERIFYING_STATUS.code;
                }
            }
        }
        return [recode: ReCode.OK];//成功
    }

    /*******
     * 点菜
     * params是传入的参数
     * 参数格式为：
     * orderId=12
     * foodIds=12或foodIds=[112,231...]
     * counts12=1,counts112=2,counts231=1...
     * remarks12=ddd,remarks112=ddd,remarks231=dfddd....
     * partakeCode=3454,点菜参与验证码,这个暂时不要
     * ************/
    def addDishes(def params) {
        def failedList = [];//点菜失败的菜列表
        //println(session.userId)


        //获取参数
        //long userId = Number.toLong(session.userId);//用户ID
        //long clientId=webUtilService.getClientId();
        long orderId = Number.toLong(params.orderId);//订单ID
        def foodIds = params.foodIds;//菜品Id列表
//        def counts=params.counts;// 数量列表
//        def remarks=params.remarks;//备注列表
//        String partakeCode = params.partakeCode;//点菜参与码
//        if(!partakeCode){ //没有点菜验证码则需要验证用户是否登录
//
//        }
        //设置状态和初始值
        int status = DishesStatus.ORIGINAL_STATUS.code;;
        int valid = DishesValid.ORIGINAL_VALID.code;

        if (orderId) {
            OrderInfo orderInfo = OrderInfo.get(orderId);
            if (orderInfo) {
                //检查定当前状态是否能点菜
                if (orderInfo.status == OrderStatus.SERVED_STATUS.code) {//上菜完成不能再点菜了
                    return [recode: ReCode.CANNOT_DISH];//不能点菜
                }
                if (orderInfo.valid <= OrderValid.EFFECTIVE_VALID.code) { //初始态或者有效态
                    if (orderInfo.status >= OrderStatus.VERIFY_ORDERED_STATUS.code) {//订单的状态是点菜确认完成后,这时点的菜默认设置点菜的有效性和状态为1和1
                        status = DishesStatus.VERIFYING_STATUS.code;
                        valid = DishesValid.EFFECTIVE_VALID.code;
                    }
                } else {//订单无效不能点菜
                    return [recode: ReCode.CANNOT_DISH];//不能点菜
                }
                //检查点菜参与验证码是否正确
//                    if (partakeCode != orderInfo.partakeCode) { //点菜参与码不正确
//                        return [recode: ReCode.ERROR_PARTAKECODE];
//                    }
            } else {
                return [recode: ReCode.NO_ORDER];//订单不存在
            }
            //点菜
            if (foodIds) {
                def dishList = [];
                if (foodIds instanceof String) {//如果只传入了一个Id
                    long foodId = Number.toLong(foodIds);
//                    int foodCount = Number.toInteger(counts);
//                    String remark=remarks;
                    int foodCount = Number.toInteger(params.get("counts"+foodId));
                    String remark=params.get("remarks"+foodId);
                    def dishMap = [foodId: foodId, count: foodCount,remark:remark];
                    dishList.add(dishMap);
                } else if (foodIds instanceof String[]) {//传入一组ID
                    for (int i = 0; i<foodIds.length; i++) {
                        long foodId = Number.toLong(foodIds[i]);
//                        int foodCount = Number.toInteger(counts[i]);
//                        String remark=remarks[i];
                        int foodCount = Number.toInteger(params.get("counts"+foodId));
                        String remark=params.get("remarks"+foodId);
                        def dishMap = [foodId: foodId, count: foodCount,remark:remark];
                        dishList.add(dishMap);
                    }
                }
                for (int i = 0; i < dishList.size(); i++) {
                    def it = dishList.get(i);
                    //更新菜品数量
                    FoodInfo foodInfo = FoodInfo.get(it.foodId);
                    if (foodInfo) {
                        if (foodInfo.enabled) {//在售
                            if (foodInfo.countLimit!=0&&(foodInfo.countLimit >= it.count+foodInfo.sellCount)) {//数量足够
                                foodInfo.sellCount += it.count;//当日销售量加上点菜数量
                                if (!foodInfo.save(flush: true)) {//保存数据失败
                                    failedList.add([foodId: it.foodId, msg: "更新所点菜的限额失败"]);
                                    continue;
                                }
                            } else {//数量不够
                                failedList.add([foodId: it.foodId, msg: "所点的菜超出今日限额了"]);
                                continue;
                            }
                        } else {//下架
                            failedList.add([foodId: it.foodId, msg: "所点的菜已经下架"]);
                            continue;
                        }
                    } else {//不存在
                        failedList.add([foodId: it.foodId, msg: "所点的菜不存在"]);
                        continue;
                    }
                    if(foodInfo.isReady){
                        status=DishesStatus.COOKED_STATUS.code;
                    }
                    //创建点菜记录
                    DishesInfo dishesInfo = new DishesInfo();
                    dishesInfo.order = orderInfo;
                    dishesInfo.food = foodInfo;
                    dishesInfo.status = status;
                    dishesInfo.valid = valid;
                    dishesInfo.numInRestaurant = 0;//先不要店内编号
                    dishesInfo.num = it.count;
                    dishesInfo.remark=it.remark;//备注
                    dishesInfo.foodPrice= foodInfo.price;//价格
                    dishesInfo.foodName=foodInfo.name;
                    dishesInfo.orderTime=orderInfo.createTime;
                    dishesInfo.foodImg=foodInfo.image;
                    dishesInfo.tableName=orderInfo.tableName;
                    dishesInfo.orderType=orderInfo.orderType;
                    if (!dishesInfo.save(flush: true)) {//保存数据失败输出日志
                        println("保存点菜记录失败:" + dishesInfo);
                        failedList.add([foodId: it.foodId, msg: "保存点菜记录失败",errors:I18nError.getMessage(g,dishesInfo.errors.allErrors)]);
                    }
                }
                if(failedList.size()>0){
                    return [recode: ReCode.DISH_HAVEERROR,failedList: failedList];
                }
                else{
                    return [recode: ReCode.OK];
                }
            } else {
                return [recode: ReCode.NO_ENOUGH_PARAMS];
            }

        } else {
            return [recode: ReCode.ERROR_PARAMS];//参数错误
        }
    }

    /***************
     * 点菜取消
     * params是传入的参数
     * 参数格式为：
     * orderId=12 //订单Id
     * dishIds=12或dishIds=[112,231...] //点菜ID列表
     * cancelReason=ddd //点菜取消原因
     * 传入订单Id则按订单ID取消
     * *****************/
    def cancelDish(def params){
        def session=webUtilService.getSession();
        //SimpleDateFormat sdfDate=new SimpleDateFormat("yyyy-MM-dd");
        //SimpleDateFormat sdfTime=new SimpleDateFormat("HH:mm:ss");
        //工作人员ID
        long staffId = Number.toLong(webUtilService.getStaffId());//工作人员ID
        StaffInfo staffInfo=webUtilService.getStaff();
        if(staffId){
            //获取参数
            Long orderId=Number.toLong(params.orderId);//订单Id
            def dishIds=params.dishIds;//点菜列表
            String cancelReason=params.cancelReason;//订单取消原因

            def dishList=null;
            if(orderId){//订单ID存在则按订单ID取消
                dishList=DishesInfo.findAllByOrder(OrderInfo.get(orderId));
            }
            else if(dishIds){//不然如果按点菜Id列表取消
                def dishIdList=[];
                if(dishIds instanceof String){
                    dishIdList.add(Number.toLong(dishIds));
                }
                else if(dishIds instanceof String[]){
                    for(int i=0;i<dishIds.length;i++){
                        dishIdList.add(Number.toLong(dishIds[i]));
                    }
                }
                dishList=DishesInfo.findAllByIdInList(dishIdList);
            }
            else {
                return [recode: ReCode.ERROR_PARAMS];//参数错误
            }
            if(dishList){
                dishList.each {
                    if((it.valid<=DishesValid.EFFECTIVE_VALID.code&&it.status<=DishesStatus.VERIFYING_STATUS.code)
                            ||(it.orderType==OrderType.TAKE_OUT.code&&it.valid<=DishesValid.EFFECTIVE_VALID.code&&it.status<DishesStatus.SERVED_STATUS.code))//外卖可以在打包前取消
                    {//有效性小于1且状态小于1可以取消
                        //根据订单状态确定3或4
                        OrderInfo orderInfo=OrderInfo.get(it.orderId);
                        if(orderInfo){//订单存在
                            if(orderInfo.valid==OrderValid.ORIGINAL_VALID.code){//订单还没有确认
                                it.cancelReason=cancelReason;
                                it.valid=DishesValid.RESTAURANT_BEFORE_VERIFYED_CANCEL_VALID.code;
                            }
                            else if (orderInfo.valid==OrderValid.EFFECTIVE_VALID.code){ //订单已经确认
                                it.cancelReason=cancelReason;
                                it.valid=DishesValid.RESTAURANT_AFTER_VERIFYED_CANCEL_VALID.code;
                            }
                            FoodInfo.executeUpdate("update FoodInfo set sellCount=sellCount-"+it.num+" where id="+it.foodId);//更新菜的销量
                        }
                    }
//                    else{
//                        throw new RuntimeException("当前状态下点菜不能取消!");
//                    }
                }
            }
            return [recode: ReCode.OK];//成功
        }
        else{
            return [recode:ReCode.NOT_LOGIN];
        }
    }

    //点菜列表,参数同 customerDishService.dishList(params,true);
    def dishList(def params){
        def session=webUtilService.getSession();
        //SimpleDateFormat sdfDate=new SimpleDateFormat("yyyy-MM-dd");
        //SimpleDateFormat sdfTime=new SimpleDateFormat("HH:mm:ss");
        //工作人员ID
        StaffInfo staffInfo=webUtilService.getStaff();//工作人员ID
        if(staffInfo){
            SimpleDateFormat sdfDate=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            //SimpleDateFormat sdfTime=new SimpleDateFormat("HH:mm:ss");
            //取出用户ID
            //long userId=Number.toLong(session.userId);//用户ID
//        long clientId=webUtilService.getClientId();
//        if(clientId==0){
//            //没登录
//            return [recode: ReCode.NOT_LOGIN];
//        }

            //取参数
            long orderId = Number.toLong(params.orderId);//订单ID
            long dishId=Number.toLong(params.dishId);//点菜ID
            int status=-1;//状态
            if(params.status!=null){
                status=Number.toInteger(params.status);
            }
            int valid=-1;//有效性
            if(params.valid!=null){
                valid=Number.toInteger(params.valid);
            }

            int statusGe=-1;
            if(params.statusGe!=null)
                statusGe=Number.toInteger(params.statusGe);//状态
            int validGe=-1;
            if (params.validGe!=null)
                validGe=Number.toInteger(params.validGe);//有效性
            int statusLe=-1;
            if(params.statusLe!=null)
                statusLe=Number.toInteger(params.statusLe);//状态
            int validLe=-1;
            if (params.validLe!=null)
                validLe=Number.toInteger(params.validLe);//有效性

            String orderTimeStr=params.orderTime;//开始日期
            //println("dateStr-->"+dateStr);
            Date orderTime=null;
            try{orderTime=sdfDate.parse(orderTimeStr);}catch (Exception ex){}
            //println("date-->"+date);
            //如果是用户查询的话必须传入订单ID，且订单是该用户所有 ,这里不能加用户限定，因为有可能是用户创建的订单但是服务员帮忙点菜
//        OrderInfo orderInfo=OrderInfo.get(orderId);
//        if(orderInfo){
////            if(orderInfo.clientId!=clientId){//不属于该用户的订单
////                return [recode: ReCode.ERROR_PARAMS] ;
////            }
//        }
//        else{ //订单不存在
//            return [recode: ReCode.ERROR_PARAMS] ;
//        }

            if (!params.max) {
                params.max = 10
            }
            if (!params.offset) {
                params.offset = 0;
            }

            def condition={
                if(orderId){
                    eq("order.id",orderId);
                }
                if(dishId){
                    eq("id",dishId);
                }
                if(status>=0){
                    eq("status",status);
                }
                if(valid>=0){
                    eq("valid",valid);
                }
                if(orderTime){
                    eq("orderTime",orderTime);
                }
                if(statusGe>=0){
                    ge("status",statusGe);//订单状态条件
                }
                if(validGe>=0){
                    ge("valid",validGe);//订单有效性条件
                }
                if(statusLe>=0){
                    le("status",statusLe);//订单状态条件
                }
                if(validLe>=0){
                    le("valid",validLe);//订单有效性条件
                }
                //加上order有效性为有效
                //eq("order.valid",OrderValid.EFFECTIVE_VALID.code);
            }

//            if(!params.sort){//如果没有排序，则按ID倒排序
//                params.sort="id";
//                params.order="desc";
//            }

            def dishList=DishesInfo.createCriteria().list(params,condition);
            def totalCount=DishesInfo.createCriteria().count(condition);

            return [recode: ReCode.OK,totalCount:totalCount,dishList:dishList];

        }
        else{
            return [recode:ReCode.NOT_LOGIN];
        }
    }

    //点菜状态改变
    def dishStatusUpdate(def params){
        def session=webUtilService.getSession();
        //SimpleDateFormat sdfDate=new SimpleDateFormat("yyyy-MM-dd");
        //SimpleDateFormat sdfTime=new SimpleDateFormat("HH:mm:ss");
        //工作人员ID
        long staffId = Number.toLong(webUtilService.getStaffId());//工作人员ID
        StaffInfo staffInfo=webUtilService.getStaff();
        if(staffId){
            //获取参数
            Long orderId=Number.toLong(params.orderId);//订单Id
            def dishIds=params.dishIds;//点菜列表
            int statusCode=Number.toInteger(params.statusCode);//状态代码

            def dishList=null;
            if(orderId){//订单ID存在则按订单ID更新点菜状态
                dishList=DishesInfo.findAllByOrder(OrderInfo.get(orderId));
            }
            else if(dishIds){//不然如果按点菜Id列表更新点菜状态
                def dishIdList=[];
                if(dishIds instanceof String){
                    dishIdList.add(Number.toLong(dishIds));
                }
                else if(dishIds instanceof String[]){
                    for(int i=0;i<dishIds.length;i++){
                        dishIdList.add(Number.toLong(dishIds[i]));
                    }
                }
                dishList=DishesInfo.findAllByIdInList(dishIdList);
            }
            else {
                return [recode: ReCode.ERROR_PARAMS];//参数错误
            }
            if(dishList){
                dishList.each {
                    if(it.status<=statusCode&&it.valid==DishesValid.EFFECTIVE_VALID.code)
                    {//点菜状态小于当前要更新为的状态才更新
                        it.status=statusCode;
                        if(statusCode==DishesStatus.COOKING_ORDERED_STATUS.code){// 更新为做菜中，则更新厨师ID
                            it.cook=staffInfo;
                        }
                        if(statusCode==DishesStatus.COOKED_STATUS.code){//做菜完成，通知服务员上菜
//                            //生成消息通知服务员
//                            def msgParams=[:];
//                            msgParams.orderId=it.orderId;
//                            msgParams.type=MessageType.SERVED_FOOD.code;
//                            msgParams.receiveId=0;
//                            msgParams.content="桌位“"+it.tableName+"”点的菜“"+it.foodName+"”已做好，请上菜。";
//                            msgParams.sendType=MsgSendType.STAFF_TO_STAFF.code;
//                            msgParams.restaurantId=it.restaurantId;
//                            def reInfo=messageService.createMsg(msgParams);
//                            if(reInfo.recode!=ReCode.OK){
//                                println("保存消息失败，但对于订单的产生没有致命影响，故忽略此错误，请系统管理员注意查证："+",reInfo="+reInfo);
//                            }
                        }
                    }
                }
            }
            return [recode: ReCode.OK];//成功
        }
        else{
            return [recode:ReCode.NOT_LOGIN];
        }
    }

    /***************
     * 点菜删除
     * params是传入的参数
     * 参数格式为：
     * orderId=12
     * dishIds=12或dishIds=[112,231...]
     * *****************/
    def delDish(def params){
        def session = webUtilService.getSession();
        //取出用户ID
        long staffId = Number.toLong(webUtilService.getStaffId());//工作人员ID
        StaffInfo staffInfo=webUtilService.getStaff();
        if(staffId==0){
            //没登录
            return [recode: ReCode.NOT_LOGIN];
        }

        //取参数
        long orderId = Number.toLong(params.orderId);//订单ID
        def dishIds = params.dishIds;//点菜Id列表

        def dishList=null;
        if(orderId){//订单ID存在则按订单ID取消
            dishList=DishesInfo.findAllByOrder(OrderInfo.get(orderId));
        }
        else if(dishIds){//不然如果按点菜Id列表取消
            def dishIdList=[];
            if(dishIds instanceof String){
                dishIdList.add(Number.toLong(dishIds));
            }
            else if(dishIds instanceof String[]){
                for(int i=0;i<dishIds.length;i++){
                    dishIdList.add(Number.toLong(dishIds[i]));
                }
            }
            dishList=DishesInfo.findAllByIdInList(dishIdList);
        }
        else {
            return [recode: ReCode.ERROR_PARAMS];//参数错误
        }
        if(dishList){
            dishList.each {
                //if(it.valid==DishesValid.RESTAURANT_AFTER_VERIFYED_CANCEL_VALID.code||it.valid==DishesValid.RESTAURANT_BEFORE_VERIFYED_CANCEL_VALID.code)
                if(it.valid==DishesValid.RESTAURANT_AFTER_VERIFYED_CANCEL_VALID.code||it.valid==DishesValid.RESTAURANT_BEFORE_VERIFYED_CANCEL_VALID.code||it.valid==DishesValid.USER_CANCEL_VALID.code)
                {//有效性为饭店取消下能删除
                    it.delete(flush: true);
                }else if (it.valid == DishesValid.ORIGINAL_VALID.code) { //点菜时直接删除,需要恢复菜品销量
                    FoodInfo.executeUpdate("update FoodInfo set sellCount=sellCount-" + it.num + " where id=" + it.foodId);//更新菜的销量
                    it.delete(flush: true);
                }
            }
        }
        return [recode: ReCode.OK];//成功
    }



    //加菜
    def addDishAfterOrderConfirm(def params) {
        //获取参数
        long orderId = Number.toLong(params.orderId);//订单ID
        def foodIds = params.foodIds;//菜品Id列表

        OrderInfo orderInfo = OrderInfo.get(orderId);
        if (orderInfo == null) {
            return [recode: ReCode.NO_ORDER];
        }
        //先查询是否存在初始态的点菜集合
        DishesCollection dishesCollection = DishesCollection.findByOrderInfoAndStatus(orderInfo, DishesCollectionStatus.ORIGINAL_STATUS.code);
        if (dishesCollection == null) {
            dishesCollection = new DishesCollection();
            dishesCollection.orderInfo = orderInfo;
        }
        if (dishesCollection.dishesInfos == null) {
            dishesCollection.dishesInfos = new ArrayList<DishesInfo>();
        }
        //点菜
        if (orderInfo) {
            //检查定当前状态是否能点菜
            if (orderInfo.status == OrderStatus.SERVED_STATUS.code) {//上菜完成不能再点菜了
                return [recode: ReCode.CANNOT_DISH];//不能点菜
            }
        } else {
            return [recode: ReCode.NO_ORDER];//订单不存在
        }
        //点菜
        long foodId = Number.toLong(foodIds);
//                    int foodCount = Number.toInteger(counts);
//                    String remark=remarks;
        int foodCount = Number.toInteger(params.get("counts" + foodId));
        String remark = params.get("remarks" + foodId);
        //设置状态和初始值
        int status = DishesStatus.ORIGINAL_STATUS.code; ;
        int valid = DishesValid.ORIGINAL_VALID.code;
        //更新菜品数量
        FoodInfo foodInfo = FoodInfo.get(foodId);
        if (foodInfo) {
            if (foodInfo.enabled) {//在售
                if (foodInfo.countLimit != 0 && (foodInfo.countLimit >= foodCount + foodInfo.sellCount)) {//数量足够
                    foodInfo.sellCount += foodCount;//当日销售量加上点菜数量
                    if (!foodInfo.save(flush: true)) {//保存数据失败
                        throw new RuntimeException(I18nError.getMessage(g, foodInfo.errors.allErrors));
                    }
                } else {//数量不够
                    throw new RuntimeException("所点的菜超出今日限额了");
                }
            } else {//下架
                throw new RuntimeException("所点的菜已经下架");
            }
        } else {//不存在
            throw new RuntimeException("所点的菜不存在");
        }
        if (foodInfo.isReady) {
            status = DishesStatus.COOKED_STATUS.code;
        }
        //创建点菜记录
        DishesInfo dishesInfo = new DishesInfo();
        dishesInfo.order = orderInfo;
        dishesInfo.food = foodInfo;
        dishesInfo.status = status;
        dishesInfo.valid = valid;
        dishesInfo.numInRestaurant = 0;//先不要店内编号
        dishesInfo.num = foodCount;
        dishesInfo.remark = remark;//备注
        dishesInfo.foodPrice = foodInfo.price;//价格
        dishesInfo.foodName = foodInfo.name;
        dishesInfo.orderTime = orderInfo.createTime;
        dishesInfo.foodImg = foodInfo.image;
        dishesInfo.tableName = orderInfo.tableName;
        dishesInfo.orderType = orderInfo.orderType;
        if (!dishesInfo.save(flush: true)) {//保存数据失败输出日志
            println("保存点菜记录失败:" + dishesInfo);
            throw new RuntimeException(I18nError.getMessage(g, dishesInfo.errors.allErrors));
        }

        //添加到点菜集合
        dishesCollection.dishesInfos.add(dishesInfo);
        if (!dishesCollection.save(flush: true)) {
            throw new RuntimeException(I18nError.getMessage(g, dishesCollection.errors.allErrors));
        }
        return [recode: ReCode.OK, orderInfo: orderInfo, dishesCollection: dishesCollection, dishesInfo: dishesInfo];
    }
    //删除加菜
    def delDishAfterOrderConfirm(def params) {
        //取参数
        long orderId = Number.toLong(params.orderId);//订单ID
        def dishIds = params.dishIds;//点菜Id列表

        OrderInfo orderInfo = OrderInfo.get(orderId);
        if (orderInfo == null) {
            return [recode: ReCode.NO_ORDER];
        }
        //先查询是否存在初始态的点菜集合
        DishesCollection dishesCollection = DishesCollection.findByOrderInfoAndStatus(orderInfo, DishesCollectionStatus.ORIGINAL_STATUS.code);
        if (dishesCollection == null) {
            return [recode: ReCode.NO_ADDITION_DISH];
        }

        DishesInfo dishesInfo = null;
        if (dishIds&&dishIds instanceof String) {//不然如果按点菜Id列表删除
            dishesInfo = DishesInfo.get(Number.toLong(dishIds));
        } else {
            return [recode: ReCode.ERROR_PARAMS];//参数错误
        }
        if (dishesInfo) {
            FoodInfo.executeUpdate("update FoodInfo set sellCount=sellCount-" + dishesInfo.num + " where id=" + dishesInfo.foodId);//更新菜的销量
            dishesCollection.dishesInfos.remove(dishesInfo);
            dishesCollection.save(flush: true);
            dishesInfo.delete(flush: true);
        }
        return [recode: ReCode.OK];//成功
    }
    //加菜确认
    def orderConfirmAfterOrderConfirm(def params) {
        //获取参数
        long orderId = Number.toLong(params.orderId);//订单ID

        OrderInfo orderInfo = OrderInfo.get(orderId);
        if (orderInfo == null) {
            return [recode: ReCode.NO_ORDER];
        }
        //先查询是否存在初始态的点菜集合
        DishesCollection dishesCollection = DishesCollection.findByOrderInfoAndStatus(orderInfo, DishesCollectionStatus.ORIGINAL_STATUS.code);
        if (dishesCollection == null) {
            return [recode: ReCode.NO_ADDITION_DISH];
        }
        //确认点菜
        dishConfirm(params);
        //更新点菜集合状态
        dishesCollection.status=DishesCollectionStatus.VERIFYING_STATUS.code;
        if(!dishesCollection.save(flush: true)){
            return [recode: ReCode.SAVE_FAILED, errors: I18nError.getMessage(g, dishesCollection.errors.allErrors)];
        }
        //打印加菜信息

        return [recode: ReCode.OK];
    }
    //加菜列表
    def dishListAfterOrderConfirm(def params) {
        //获取参数
        long orderId = Number.toLong(params.orderId);//订单ID

        OrderInfo orderInfo = OrderInfo.get(orderId);
        if (orderInfo == null) {
            return [recode: ReCode.NO_ORDER];
        }
        //先查询是否存在初始态的点菜集合
        DishesCollection dishesCollection = DishesCollection.findByOrderInfoAndStatus(orderInfo, DishesCollectionStatus.ORIGINAL_STATUS.code);
//        if (dishesCollection == null) {
//            return [recode: ReCode.NO_ADDITION_DISH];
//        }
        return [recode: ReCode.OK,orderInfo:orderInfo, dishList: dishesCollection?.dishesInfos];
    }

}
