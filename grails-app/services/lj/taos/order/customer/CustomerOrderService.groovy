package lj.taos.order.customer

import lj.I18nError
import lj.Number
import lj.common.Distance
import lj.common.ValidationCode
import lj.data.ClientInfo
import lj.data.DishesCollection
import lj.data.DishesInfo
import lj.data.FoodInfo
import lj.data.OrderInfo
import lj.data.ReserveOrderInfo
import lj.data.RestaurantInfo
import lj.data.TableInfo
import lj.enumCustom.*
import lj.mina.server.MinaServer
import lj.util.WebUtilService

import java.text.SimpleDateFormat


class CustomerOrderService {
    WebUtilService webUtilService;
    //CustomerDishService customerDishService;
    //MessageService messageService;
    //def userService;
    def clientService;
    def shopService;

    def g = new org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib();
    /**
     * 根据传人的桌位号来获取或创建一个订单
     *
     *  params是传入的参数
     * 参数格式为：
     * code=params.code;//桌位code,必须
     * */
    def getOrCreateOrder(def params) {
        ClientInfo clientInfo = clientService.getClient();
        if (clientInfo == null) {
            return [recode: ReCode.NO_CLIENT];
        }
        if (params.code) {
            webUtilService.setTableCode(params.code);
        }
        String code = webUtilService.getTableCode();
        TableInfo tableInfo = TableInfo.findByCodeAndEnabled(code,true);
        if (tableInfo == null) {
            return [recode: ReCode.TABLE_NOT_EXIST];
        }
        OrderInfo orderInfo = OrderInfo.findByTableInfoAndValidAndStatusLessThan(tableInfo, OrderValid.EFFECTIVE_VALID.code, OrderStatus.CHECKOUTED_STATUS.code);
        if (orderInfo == null) {
            //检测是否已经有其他桌的有效订单
            OrderInfo orderInfo1 = OrderInfo.findByValidAndStatusLessThanAndClientInfoAndTableInfoNotEqual(OrderValid.EFFECTIVE_VALID.code, OrderStatus.CHECKOUTED_STATUS.code, clientInfo, tableInfo);
            if (orderInfo1) {
                if (orderInfo1.status <= OrderStatus.ORDERED_STATUS.code) {
                    //可以取消订单，自动取消该订单
                    def reInfo = cancelOrder([orderId: orderInfo1.id]);
                    if (reInfo.recode != ReCode.OK) {
                        return reInfo;
                    }
                } else {//返回错误
                    return [recode: ReCode.CAN_NOT_CHANGE_TABLE];
                }
            }

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

            orderInfo = new OrderInfo();
            orderInfo.clientInfo = clientInfo;
            orderInfo.tableInfo = tableInfo;
            orderInfo.partakeCode = ValidationCode.getAuthCodeStr(4, ValidationCode.NUMBER);

            String orderNumStr = now.getTime() + ValidationCode.getAuthCodeStr(2, ValidationCode.NUMBER);
            long orderNum = Long.parseLong(orderNumStr);
            orderInfo.orderNum = orderNum;
            //获取订单店内编号最大值
            String sqlStr = "select max(numInRestaurant) from OrderInfo";
            def result = OrderInfo.executeQuery(sqlStr);
            long numInRestaurant = 1;
            if (result != null && result[0] != null) {
                numInRestaurant = result[0] + 1;
            }
            orderInfo.numInRestaurant = numInRestaurant;
            if (!orderInfo.save(flush: true)) {
                return [recode: ReCode.SAVE_FAILED, orderInfo: orderInfo, errors: I18nError.getMessage(g, orderInfo.errors.allErrors)];
            }
        }else if (orderInfo.clientInfo == null) {
            orderInfo.clientInfo = clientInfo;
            if (!orderInfo.save(flush: true)) {
                return [recode: ReCode.SAVE_FAILED, orderInfo: orderInfo, errors: I18nError.getMessage(g, orderInfo.errors.allErrors)];
            }
        } else {//获取订单，需要验证订单参与码
            if (orderInfo.clientInfo != clientInfo) {
                String partakeCode = params.partakeCode;
                if (partakeCode) {
                    if (partakeCode == orderInfo.partakeCode) {
                        webUtilService.setPartakeCode(partakeCode);
                    }
                }
                partakeCode = webUtilService.getPartakeCode();
                if (partakeCode == null || partakeCode == "") {//返回点菜参与码错误
                    return [recode: ReCode.NO_PARTAKECODE];
                }
                if (partakeCode != orderInfo.partakeCode) {
                    return [recode: ReCode.WRONG_PARTAKECODE];
                }
            }
        }

        boolean isOwner = false;
        if (orderInfo.clientInfo == clientInfo) {
            isOwner = true;
        }
        return [recode: ReCode.OK, orderInfo: orderInfo, isOwner: isOwner];
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
        int status = DishesStatus.ORIGINAL_STATUS.code; ;
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
                    int foodCount = Number.toInteger(params.get("counts" + foodId));
                    String remark = params.get("remarks" + foodId);
                    def dishMap = [foodId: foodId, count: foodCount, remark: remark];
                    dishList.add(dishMap);
                } else if (foodIds instanceof String[]) {//传入一组ID
                    for (int i = 0; i < foodIds.length; i++) {
                        long foodId = Number.toLong(foodIds[i]);
//                        int foodCount = Number.toInteger(counts[i]);
//                        String remark=remarks[i];
                        int foodCount = Number.toInteger(params.get("counts" + foodId));
                        String remark = params.get("remarks" + foodId);
                        def dishMap = [foodId: foodId, count: foodCount, remark: remark];
                        dishList.add(dishMap);
                    }
                }
                for (int i = 0; i < dishList.size(); i++) {
                    def it = dishList.get(i);
                    //更新菜品数量
                    FoodInfo foodInfo = FoodInfo.get(it.foodId);
                    if (foodInfo) {
                        if (foodInfo.enabled) {//在售
                            if (foodInfo.countLimit != 0 && (foodInfo.countLimit >= it.count + foodInfo.sellCount)) {//数量足够
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
                    dishesInfo.num = it.count;
                    dishesInfo.remark = it.remark;//备注
                    dishesInfo.foodPrice = foodInfo.price;//价格
                    dishesInfo.foodName = foodInfo.name;
                    dishesInfo.orderTime = orderInfo.createTime;
                    dishesInfo.foodImg = foodInfo.image;
                    dishesInfo.tableName = orderInfo.tableName;
                    dishesInfo.orderType = orderInfo.orderType;
                    if (!dishesInfo.save(flush: true)) {//保存数据失败输出日志
                        println("保存点菜记录失败:" + dishesInfo);
                        failedList.add([foodId: it.foodId, msg: "保存点菜记录失败", errors: I18nError.getMessage(g, dishesInfo.errors.allErrors)]);
                    }
                }
//                //点菜完成后，根据订单状态来确定是否，在饭店的点菜列表中更新
//                if (orderInfo.status >= OrderStatus.VERIFY_ORDERED_STATUS.code) {//订单的状态是点菜确认完成后,这时点的菜默认设置点菜的有效性和状态为1和1
//                    //生成消息通知顾客
//                    def msgParams=[:];
//                    msgParams.orderId=orderId;
//                    msgParams.type=MessageType.UPDATE_DISH_LIST.code;
//                    msgParams.receiveId=0;
//                    msgParams.content="需要更新点菜列表";
//                    msgParams.sendType=MsgSendType.STAFF_TO_STAFF.code;
//                    msgParams.restaurantId=orderInfo.restaurantId;
//                    def reInfo=messageService.createMsg(msgParams);
//                    if(reInfo.recode!=ReCode.OK){
//                        println("保存消息失败，但对于订单的产生没有致命影响，故忽略此错误，请系统管理员注意查证："+",reInfo="+reInfo);
//                    }
//                }

                if (failedList.size() > 0) {
                    return [recode: ReCode.DISH_HAVEERROR, failedList: failedList];
                } else {
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
     * 传入订单Id则按订单ID取消
     * *****************/
    def cancelDish(def params) {
        def session = webUtilService.getSession();
        //SimpleDateFormat sdfDate=new SimpleDateFormat("yyyy-MM-dd");
        //SimpleDateFormat sdfTime=new SimpleDateFormat("HH:mm:ss");
        //取出用户ID
        //long userId = Number.toLong(session.userId);//用户ID
//        long clientId=webUtilService.getClientId();
//        if(clientId==0){
//            //没登录
//            return [recode: ReCode.NOT_LOGIN];
//        }

        //取参数
        long orderId = Number.toLong(params.orderId);//订单ID
        def dishIds = params.dishIds;//点菜Id列表

        def dishList = null;
        if (orderId) {//订单ID存在则按订单ID取消
            dishList = DishesInfo.findAllByOrder(OrderInfo.get(orderId));
        } else if (dishIds) {//不然如果按点菜Id列表取消
            def dishIdList = [];
            if (dishIds instanceof String) {
                dishIdList.add(Number.toLong(dishIds));
            } else if (dishIds instanceof String[]) {
                for (int i = 0; i < dishIds.length; i++) {
                    dishIdList.add(Number.toLong(dishIds[i]));
                }
            }
            dishList = DishesInfo.findAllByIdInList(dishIdList);
        } else {
            return [recode: ReCode.ERROR_PARAMS];//参数错误
        }
        if (dishList) {
            dishList.each {
                if (it.valid == DishesValid.ORIGINAL_VALID.code || orderId != 0) {//有效性为初始状态下能取消或者对应的订单取消了能取消
                    it.valid = DishesValid.USER_CANCEL_VALID.code;
                    FoodInfo.executeUpdate("update FoodInfo set sellCount=sellCount-" + it.num + " where id=" + it.foodId);//更新菜的销量
                }
            }
        }
        return [recode: ReCode.OK];//成功
    }

    /***************
     * 点菜删除
     * params是传入的参数
     * 参数格式为：
     * orderId=12
     * dishIds=12或dishIds=[112,231...]
     * *****************/
    def delDish(def params) {
        def session = webUtilService.getSession();
        //取出用户ID
        //long userId = Number.toLong(session.userId);//用户ID
//        long clientId=webUtilService.getClientId();
//        if(clientId==0){
//            //没登录
//            return [recode: ReCode.NOT_LOGIN];
//        }

        //取参数
        long orderId = Number.toLong(params.orderId);//订单ID
        def dishIds = params.dishIds;//点菜Id列表

        def dishList = null;
        if (orderId) {//订单ID存在则按订单ID取消
            dishList = DishesInfo.findAllByOrder(OrderInfo.get(orderId));
        } else if (dishIds) {//不然如果按点菜Id列表删除
            def dishIdList = [];
            if (dishIds instanceof String) {
                dishIdList.add(Number.toLong(dishIds));
            } else if (dishIds instanceof String[]) {
                for (int i = 0; i < dishIds.length; i++) {
                    dishIdList.add(Number.toLong(dishIds[i]));
                }
            }
            dishList = DishesInfo.findAllByIdInList(dishIdList);
        } else {
            return [recode: ReCode.ERROR_PARAMS];//参数错误
        }
        if (dishList) {
            dishList.each {
                if (it.valid == DishesValid.USER_CANCEL_VALID.code || orderId != 0) {//有效性为用户取消下能删除或者对应的订单删除了能删除
                    it.delete(flush: true);
                } else if (it.valid == DishesValid.ORIGINAL_VALID.code) { //点菜时直接删除,需要恢复菜品销量
                    FoodInfo.executeUpdate("update FoodInfo set sellCount=sellCount-" + it.num + " where id=" + it.foodId);//更新菜的销量
                    it.delete(flush: true);
                }
            }
        }
        return [recode: ReCode.OK];//成功
    }

    //点菜查询
    def dishList(def params) {
        def session = webUtilService.getSession();
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
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
        long dishId = Number.toLong(params.dishId);//点菜ID
        int status = -1;//状态
        if (params.status != null) {
            status = Number.toInteger(params.status);
        }
        int valid = -1;//有效性
        if (params.valid != null) {
            valid = Number.toInteger(params.valid);
        }

        int statusGe = -1;
        if (params.statusGe != null)
            statusGe = Number.toInteger(params.statusGe);//状态
        int validGe = -1;
        if (params.validGe != null)
            validGe = Number.toInteger(params.validGe);//有效性
        int statusLe = -1;
        if (params.statusLe != null)
            statusLe = Number.toInteger(params.statusLe);//状态
        int validLe = -1;
        if (params.validLe != null)
            validLe = Number.toInteger(params.validLe);//有效性

        String orderTimeStr = params.orderTime;//开始日期
        //println("dateStr-->"+dateStr);
        Date orderTime = null;
        try { orderTime = sdfDate.parse(orderTimeStr); } catch (Exception ex) {}
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

        def condition = {
            if (orderId) {
                eq("order.id", orderId);
            }
            if (dishId) {
                eq("id", dishId);
            }
            if (status >= 0) {
                eq("status", status);
            }
            if (valid >= 0) {
                eq("valid", valid);
            }
            if (orderTime) {
                eq("orderTime", orderTime);
            }
            if (statusGe >= 0) {
                ge("status", statusGe);//订单状态条件
            }
            if (validGe >= 0) {
                ge("valid", validGe);//订单有效性条件
            }
            if (statusLe >= 0) {
                le("status", statusLe);//订单状态条件
            }
            if (validLe >= 0) {
                le("valid", validLe);//订单有效性条件
            }
        }

        if (!params.sort) {//如果没有排序，则按ID倒排序
            params.sort = "id";
            params.order = "desc";
        }

        def dishList = DishesInfo.createCriteria().list(params, condition);
        def totalCount = DishesInfo.createCriteria().count(condition);

        return [recode: ReCode.OK, totalCount: totalCount, dishList: dishList];

    }

    //根据ID获取一个点菜
    //参数 dishId
    def getDish(def params) {
        def session = webUtilService.getSession();
        //取出用户ID
        //long userId = Number.toLong(session.userId);//用户ID
//        long clientId=webUtilService.getClientId();
//        if(clientId==0){
//            //没登录
//            return [recode: ReCode.NOT_LOGIN];
//        }

        //取参数
        long dishId = Number.toLong(params.dishId);//点菜Id
        DishesInfo dishesInfo = DishesInfo.get(dishId);
        if (dishesInfo) {
            return [recode: ReCode.OK, dishesInfo: dishesInfo];//成功
        } else {
            return [recode: ReCode.NO_RESULT];
        }

    }

    /*********************订单取消**********************
     * params是传入的参数
     * 参数格式为：
     * orderId=Number.toLong(params.orderId);//订单号
     *
     * 返回值
     * [recode: ReCode.ORDER_CANNOT_CANCEL];订单当前状态不能被取消
     * [recode: ReCode.SAVE_FAILED];保存状态失败
     * [recode: ReCode.ORDER_NOT_EXIST];订单不存在
     * [recode:ReCode.NOT_LOGIN];没有登录
     * [recode: ReCode.OK];成功
     ********************************************************/
    def cancelOrder(def params) {
        def session = webUtilService.getSession();
        //SimpleDateFormat sdfDate=new SimpleDateFormat("yyyy-MM-dd");
        //SimpleDateFormat sdfTime=new SimpleDateFormat("HH:mm:ss");
        //取出用户ID
        //long userId=Number.toLong(session.userId);//用户ID
//        long clientId=webUtilService.getClientId();
//        if(clientId){
        ClientInfo clientInfo = clientService.getClient();
        //获取参数
        long orderId = Number.toLong(params.orderId);//订单号
        OrderInfo orderInfo = OrderInfo.findByIdAndClientInfo(orderId, clientInfo);
        if (orderInfo) {
            //检查订单是否可以取消
            if (orderInfo.valid > OrderValid.EFFECTIVE_VALID.code || orderInfo.status > OrderStatus.ORDERED_STATUS.code) {//订单不能取消
                return [recode: ReCode.ORDER_CANNOT_CANCEL];
            }
            //取消订单
            orderInfo.valid = OrderValid.USER_CANCEL_VALID.code;
            if (!orderInfo.save(flush: true)) {//保存更改失败
                return [recode: ReCode.SAVE_FAILED, errors: I18nError.getMessage(g, orderInfo.errors.allErrors)];
            }
            //取消订单中点菜
            cancelDish(params);

//                //调用消息服务创建消息
//                def msgParams=[:];
//                msgParams.orderId=orderInfo.id;
//                msgParams.type=MessageType.ORDER_HANDLE_TYPE.code;
//                msgParams.receiveId=orderInfo.listenWaiterId;
//                msgParams.content="顾客已经取消订单id是"+orderInfo.id+"的订单，赶快点击去查看详情吧";
//                msgParams.sendType=MsgSendType.CUSTOMER_TO_STAFF.code;
//                msgParams.restaurantId=orderInfo.restaurantId;
//                def reInfo=messageService.createMsg(msgParams);
//                if(reInfo.recode!=ReCode.OK){
//                    println("保存消息失败，但对于订单的产生没有致命影响，故忽略此错误，请系统管理员注意查证："+",reInfo="+reInfo);
//                }

            return [recode: ReCode.OK];
        } else {
            return [recode: ReCode.ORDER_NOT_EXIST];
        }
//        }
//        else{
//            return [recode:ReCode.NOT_LOGIN];
//        }
    }

    /********************订单删除******
     * params是传入的参数
     * 参数格式为：
     * orderId=Number.toLong(params.orderId);//订单号
     *
     * 返回值
     * [recode: ReCode.ORDER_CANNOT_DELETE];订单不能删除
     * [recode: ReCode.ORDER_NOT_EXIST];订单不存在
     * [recode:ReCode.NOT_LOGIN];用户没有登录
     * [recode: ReCode.OK];成功
     * ****************/
    def delOrder(def params) {
        def session = webUtilService.getSession();
        //SimpleDateFormat sdfDate=new SimpleDateFormat("yyyy-MM-dd");
        //SimpleDateFormat sdfTime=new SimpleDateFormat("HH:mm:ss");
        //取出用户ID
        //long userId=Number.toLong(session.userId);//用户ID
//        long clientId=webUtilService.getClientId();
//        if(clientId){
        ClientInfo clientInfo = clientService.getClient();
        //获取参数
        long orderId = Number.toLong(params.orderId);//订单号
        OrderInfo orderInfo = OrderInfo.findByIdAndClientInfo(orderId, clientInfo);
        if (orderInfo) {
            //检查订单是否可以删除
            if (orderInfo.valid != OrderValid.USER_CANCEL_VALID.code) {//订单不能删除
                return [recode: ReCode.ORDER_CANNOT_DELETE];
            }
            //删除订单中点菜
            delDish(params);
            orderInfo.delete(flush: true);
            return [recode: ReCode.OK];
        } else {
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
    def dishConfirm(def params) {
        def session = webUtilService.getSession();
        //SimpleDateFormat sdfDate=new SimpleDateFormat("yyyy-MM-dd");
        //SimpleDateFormat sdfTime=new SimpleDateFormat("HH:mm:ss");

        //获取参数
        Long orderId = Number.toLong(params.orderId);//订单Id
        def dishIds = params.dishIds;//点菜列表

        def dishList = null;
        if (orderId) {//订单ID存在则按订单ID取消
            dishList = DishesInfo.findAllByOrder(OrderInfo.get(orderId));
        } else if (dishIds) {//不然如果按点菜Id列表取消
            def dishIdList = [];
            if (dishIds instanceof String) {
                dishIdList.add(Number.toLong(dishIds));
            } else if (dishIds instanceof String[]) {
                for (int i = 0; i < dishIds.length; i++) {
                    dishIdList.add(Number.toLong(dishIds[i]));
                }
            }
            dishList = DishesInfo.findAllByIdInList(dishIdList);
        } else {
            return [recode: ReCode.ERROR_PARAMS];//参数错误
        }
        if (dishList) {
            dishList.each {
                if (it.valid == DishesValid.ORIGINAL_VALID.code) {//有效性为初始状态下且状态也为初始态时更新有效性和状态分别为1有效和1确认完成
                    it.valid = DishesValid.EFFECTIVE_VALID.code;
                }
                if (it.status == DishesStatus.ORIGINAL_STATUS.code) {
                    it.status = DishesStatus.VERIFYING_STATUS.code;
                }
            }
        }
        return [recode: ReCode.OK];//成功
    }
    /************************订单状态改变,这里主要是点菜完成******
     * params是传入的参数
     * 参数格式为：
     * orderId=Number.toLong(params.orderId);//订单号
     * statusCode=params.statusCode?:OrderStatus.ORDERED_STATUS.code;// 状态代码
     * //点菜列表参数,非必须
     * foodIds=12或foodIds=[112,231...]
     * counts=1或counts=[2,3...]
     * remarks=ddd或remarks=[dd,,dd...]
     * byWaiter是标示是否是服务员帮忙操作
     *
     * 返回值
     * [recode: ReCode.SAVE_FAILED];保存失败
     * [recode: ReCode.ORDER_NOT_EXIST];订单不存在
     * [recode:ReCode.NOT_LOGIN];用户没有登录
     * [recode: ReCode.OK];成功
     * ************************/
    def orderStatusUpdate(def params) {
        def session = webUtilService.getSession();
        //SimpleDateFormat sdfDate=new SimpleDateFormat("yyyy-MM-dd");
        //SimpleDateFormat sdfTime=new SimpleDateFormat("HH:mm:ss");
        //取出用户ID
        //long userId=Number.toLong(session.userId);//用户ID
//        long clientId=webUtilService.getClientId();
//        if (byWaiter) {//如果是服务员帮助更新订单状态为点菜完成，则取服务员ID作为用户ID
//            clientId = Number.toLong(session.staffId);
//        }
//
//        if(clientId){
        //获取参数
        long orderId = Number.toLong(params.orderId);//订单号
        int statusCode = params.statusCode ?: OrderStatus.ORIGINAL_STATUS.code;// 状态代码

        //如果是点菜完成的话且传入了点菜列表，则先做点菜
        if (statusCode == OrderStatus.ORDERED_STATUS.code && params.foodIds) {
            def reObj = addDishes(params);
            if (reObj.recode != ReCode.OK) {//点菜没有顺利完成则返回
                return reObj;
            }
        }

        if (statusCode == OrderStatus.VERIFY_ORDERED_STATUS.code) {// 确认点菜完成
            //先修改订单中点菜的状态为状态为“0”有效性为“0”的菜的状态改为“1确定完成”，有效性改为“1有效”。
            def reObj = dishConfirm(params);
            if (reObj.recode != ReCode.OK) {
                return reObj;
            }
        }
        OrderInfo orderInfo = OrderInfo.findById(orderId);
        if (orderInfo) {
//                if(orderInfo.status<statusCode){
            orderInfo.status = statusCode;
            if (!orderInfo.save(flush: true)) {//保存数据库失败
                return [recode: ReCode.SAVE_FAILED, errors: I18nError.getMessage(g, orderInfo.errors.allErrors)];
            }
//                    if(!byWaiter){
//                        //调用消息服务创建消息
//                        def msgParams=[:];
//                        msgParams.orderId=orderInfo.id;
//                        msgParams.type=MessageType.ORDER_HANDLE_TYPE.code;
//                        msgParams.receiveId=orderInfo.listenWaiterId;
//                        msgParams.content="订单id是"+orderInfo.id+"的订单状态已改为"+OrderStatus.getLable(orderInfo.status)+"，赶快点击去处理吧";
//                        msgParams.sendType=MsgSendType.CUSTOMER_TO_STAFF.code;
//                        msgParams.restaurantId=orderInfo.restaurantId;
//                        def reInfo=messageService.createMsg(msgParams);
//                        if(reInfo.recode!=ReCode.OK){
//                            println("保存消息失败，但对于订单的产生没有致命影响，故忽略此错误，请系统管理员注意查证："+",reInfo="+reInfo);
//                        }
//                    }
//                }
            return [recode: ReCode.OK];
        } else {
            return [recode: ReCode.ORDER_NOT_EXIST];
        }
//        }
//        else{
//            return [recode:ReCode.NOT_LOGIN];
//        }
    }

    /**********************
     * 查询
     *************************/
    def orderList(def params) {
        def session = webUtilService.getSession();
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
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
        long orderId = Number.toLong(params.orderId);//订单ID
        long restaurantId = Number.toLong(params.restaurantId);//饭店ID
        long tableId = Number.toLong(params.tableId);//桌位ID
        String beginTimeStr = params.beginTime;//开始时间
        Date beginTime = null;
        try { beginTime = sdfDate.parse(beginTimeStr); } catch (Exception ex) {}
        String endTimeStr = params.endTime;//截止时间
        Date endTime = null;
        try { endTime = sdfDate.parse(endTimeStr); } catch (Exception ex) {}
        int reserveType = -1;
        if (params.reserveType != null)
            reserveType = Number.toInteger(params.reserveType);// 用餐类别（早餐、午餐、晚餐）
        int status = -1;
        if (params.status != null)
            status = Number.toInteger(params.status);//订单状态
        int valid = -1;
        if (params.valid != null)
            valid = Number.toInteger(params.valid);//有效性
        String orderNum = params.orderNum;//订单流水号

        int statusGe = -1;
        if (params.statusGe != null)
            statusGe = Number.toInteger(params.statusGe);//订单状态
        int validGe = -1;
        if (params.validGe != null)
            validGe = Number.toInteger(params.validGe);//有效性
        int statusLe = -1;
        if (params.statusLe != null)
            statusLe = Number.toInteger(params.statusLe);//订单状态
        int validLe = -1;
        if (params.validLe != null)
            validLe = Number.toInteger(params.validLe);//有效性

        int orderType = -1;
        if (params.orderType != null)
            orderType = Number.toInteger(params.orderType);//订单类型

        if (!params.max) {
            params.max = 10
        }
        if (!params.offset) {
            params.offset = 0;
        }

//             def cIds=userService.getIds(ClientInfo.get(clientId));
        def condition = {
            if (orderId) {
                eq("id", orderId);//id条件
            }
            if (tableId) {
                eq("tableInfo.id", tableId);//桌位ID条件
            }
            if (beginTime) {
                ge("createTime", beginTime);//日期条件
            }
            if (endTime) {
                le("createTime", endTime);//日期条件
            }
            if (reserveType >= 0) {
                eq("reserveType", reserveType);//用餐类别（早餐、午餐、晚餐）条件
            }
            if (status >= 0) {
                eq("status", status);//订单状态条件
            }
            if (valid >= 0) {
                eq("valid", valid);//订单有效性条件
            }
            if (statusGe >= 0) {
                ge("status", statusGe);//订单状态条件
            }
            if (validGe >= 0) {
                ge("valid", validGe);//订单有效性条件
            }
            if (statusLe >= 0) {
                le("status", statusLe);//订单状态条件
            }
            if (validLe >= 0) {
                le("valid", validLe);//订单有效性条件
            }
            if (orderNum) {
                eq("orderNum", orderNum);//订单流水号条件
            }
            if (orderType >= 0) {
                eq("orderType", orderType);//订单类型条件
            }
        }

        if (!params.sort) {//如果没有排序，则按ID倒排序
            params.sort = "id";
            params.order = "desc";
        }

        def orderList = OrderInfo.createCriteria().list(params, condition);
        def totalCount = OrderInfo.createCriteria().count(condition);

        return [recode: ReCode.OK, totalCount: totalCount, orderList: orderList];
//         }
//         else{
//             return [recode:ReCode.NOT_LOGIN];
//         }
    }

    /********************订单完成点菜******
     * params是传入的参数
     * 参数格式为：
     * orderId=Number.toLong(params.orderId);//订单号
     * //点菜列表参数,非必须
     * foodIds=12或foodIds=[112,231...]
     * counts=1或counts=[2,3...]
     * remarks=ddd或remarks=[dd,,dd...]
     *
     * 返回值
     * [recode: ReCode.ORDER_CANNOT_DELETE];订单不能删除
     * [recode: ReCode.ORDER_NOT_EXIST];订单不存在
     * [recode:ReCode.NOT_LOGIN];用户没有登录
     * [recode: ReCode.OK];成功
     * ****************/
    def completeDish(def params) {
        def session = webUtilService.getSession();
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
        long orderId = Number.toLong(params.orderId);//订单号
        OrderInfo orderInfo = null;
        ClientInfo clientInfo = clientService.getClient();
        orderInfo = OrderInfo.findByIdAndClientInfo(orderId, clientInfo);
        if (orderInfo) {
            //检查订单是否可以做完成点菜操作
            if (orderInfo.valid > OrderValid.EFFECTIVE_VALID.code || orderInfo.status > OrderStatus.ORIGINAL_STATUS.code) {//订单不能完成点菜
                return [recode: ReCode.ORDER_CANNOT_COMPLETE_DISH];
            }
            //完成点菜
            //statusCode=params.statusCode?:OrderStatus.ORDERED_STATUS.code;// 状态代码
            params.statusCode = OrderStatus.ORDERED_STATUS.code;
            return orderStatusUpdate(params);
        } else {
            return [recode: ReCode.ORDER_NOT_EXIST];
        }
//        }
//        else{
//            return [recode:ReCode.NOT_LOGIN];
//        }
    }

    /********************订单完成点菜******
     * params是传入的参数
     * 参数格式为：
     * orderId=Number.toLong(params.orderId);//订单号
     * //点菜列表参数,非必须
     * foodIds=12或foodIds=[112,231...]
     * counts=1或counts=[2,3...]
     * remarks=ddd或remarks=[dd,,dd...]
     *
     * 返回值
     * [recode: ReCode.ORDER_CANNOT_DELETE];订单不能删除
     * [recode: ReCode.ORDER_NOT_EXIST];订单不存在
     * [recode:ReCode.NOT_LOGIN];用户没有登录
     * [recode: ReCode.OK];成功
     * ****************/
    def backToDish(def params) {
        def session = webUtilService.getSession();
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
        long orderId = Number.toLong(params.orderId);//订单号
        OrderInfo orderInfo = null;
        ClientInfo clientInfo = clientService.getClient();
        orderInfo = OrderInfo.findByIdAndClientInfo(orderId, clientInfo);
        if (orderInfo) {
            //检查订单是否可以做完成点菜操作
            if (orderInfo.valid > OrderValid.EFFECTIVE_VALID.code || orderInfo.status != OrderStatus.ORDERED_STATUS.code) {//订单不能退回点菜
                return [recode: ReCode.ORDER_CANNOT_COMPLETE_DISH];
            }
            //完成点菜
            //statusCode=params.statusCode?:OrderStatus.ORDERED_STATUS.code;// 状态代码
            params.statusCode = OrderStatus.ORIGINAL_STATUS.code;
            return orderStatusUpdate(params);
        } else {
            return [recode: ReCode.ORDER_NOT_EXIST];
        }
//        }
//        else{
//            return [recode:ReCode.NOT_LOGIN];
//        }
    }

    /********************订单完成点菜******
     * params是传入的参数
     * 参数格式为：
     * orderId=Number.toLong(params.orderId);//订单号
     * //点菜列表参数,非必须
     * foodIds=12或foodIds=[112,231...]
     * counts=1或counts=[2,3...]
     * remarks=ddd或remarks=[dd,,dd...]
     *
     * 返回值
     * [recode: ReCode.ORDER_CANNOT_DELETE];订单不能删除
     * [recode: ReCode.ORDER_NOT_EXIST];订单不存在
     * [recode:ReCode.NOT_LOGIN];用户没有登录
     * [recode: ReCode.OK];成功
     * ****************/
    def orderConfirm(def params) {
        def session = webUtilService.getSession();
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
        long orderId = Number.toLong(params.orderId);//订单号
        OrderInfo orderInfo = null;
        ClientInfo clientInfo = clientService.getClient();
        orderInfo = OrderInfo.findByIdAndClientInfo(orderId, clientInfo);
        if (orderInfo) {
            //检查订单是否可以做完成点菜操作
            if (orderInfo.valid > OrderValid.EFFECTIVE_VALID.code || orderInfo.status != OrderStatus.ORDERED_STATUS.code) {//订单不能退回点菜
                return [recode: ReCode.ORDER_CANNOT_COMPLETE_DISH];
            }
            //完成点菜
            //statusCode=params.statusCode?:OrderStatus.ORDERED_STATUS.code;// 状态代码
            params.statusCode = OrderStatus.VERIFY_ORDERED_STATUS.code;
            def reInfo = orderStatusUpdate(params);
            if (reInfo.recode == ReCode.OK) {//打印厨房做菜单子

            }
            return reInfo;
        } else {
            return [recode: ReCode.ORDER_NOT_EXIST];
        }
//        }
//        else{
//            return [recode:ReCode.NOT_LOGIN];
//        }
    }

    //订单详情
    def orderInfo(def params) {
        def session = webUtilService.getSession();
        //SimpleDateFormat sdfDate=new SimpleDateFormat("yyyy-MM-dd");
        //SimpleDateFormat sdfTime=new SimpleDateFormat("HH:mm:ss");

        //获取参数
        long orderId = Number.toLong(params.orderId);//订单号
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
        OrderInfo orderInfo = OrderInfo.findById(orderId);
        if (orderInfo) {
//            //点菜参与码是否正确
//            if (clientId == orderInfo.clientId || byWaiter) {//用户登录切是订单创建的用户或者是服务员帮忙点菜，则不需要参与验证码
//
//            } else {//检查点菜参与验证码是否正确
//                if (partakeCode != orderInfo.partakeCode) { //点菜参与码不正确
//                    return [recode: ReCode.ERROR_PARTAKECODE];
//                }
//            }
            return [recode: ReCode.OK, orderInfoInstance: orderInfo];
        } else {
            return [recode: ReCode.ORDER_NOT_EXIST];
        }
    }

    //加菜
    def addDishAfterOrderConfirm(def params) {
        //获取参数
        long orderId = Number.toLong(params.orderId);//订单ID
        def foodIds = params.foodIds;//菜品Id列表

        ClientInfo clientInfo = clientService.getClient();
        if (clientInfo == null) {
            return [recode: ReCode.NO_CLIENT];
        }
        if (params.code) {
            webUtilService.setTableCode(params.code);
        }
        String code = webUtilService.getTableCode();
        TableInfo tableInfo = TableInfo.findByCode(code);

        OrderInfo orderInfo = OrderInfo.get(orderId);
        if (orderInfo == null) {
            orderInfo = OrderInfo.findByTableInfo(tableInfo);
            if (orderInfo == null) {
                return [recode: ReCode.NO_ORDER];
            }
        } else {
            if (orderInfo.tableInfo != null) {
                tableInfo = orderInfo.tableInfo;
                webUtilService.setTableCode(orderInfo.tableInfo.code);
            }
        }
        if (tableInfo == null) {
            return [recode: ReCode.TABLE_NOT_EXIST];
        }
        if (orderInfo.clientInfo != clientInfo) { //非订单拥有者不能做加菜
            return [recode: ReCode.NOT_ORDER_OWNER];
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

        ClientInfo clientInfo = clientService.getClient();
        if (clientInfo == null) {
            return [recode: ReCode.NO_CLIENT];
        }
        if (params.code) {
            webUtilService.setTableCode(params.code);
        }
        String code = webUtilService.getTableCode();
        TableInfo tableInfo = TableInfo.findByCode(code);

        OrderInfo orderInfo = OrderInfo.get(orderId);
        if (orderInfo == null) {
            orderInfo = OrderInfo.findByTableInfo(tableInfo);
            if (orderInfo == null) {
                return [recode: ReCode.NO_ORDER];
            }
        } else {
            if (orderInfo.tableInfo != null) {
                tableInfo = orderInfo.tableInfo;
                webUtilService.setTableCode(orderInfo.tableInfo.code);
            }
        }
        if (tableInfo == null) {
            return [recode: ReCode.TABLE_NOT_EXIST];
        }
        if (orderInfo.clientInfo != clientInfo) { //非订单拥有者不能做加菜
            return [recode: ReCode.NOT_ORDER_OWNER];
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

        ClientInfo clientInfo = clientService.getClient();
        if (clientInfo == null) {
            return [recode: ReCode.NO_CLIENT];
        }
        if (params.code) {
            webUtilService.setTableCode(params.code);
        }
        String code = webUtilService.getTableCode();
        TableInfo tableInfo = TableInfo.findByCode(code);

        OrderInfo orderInfo = OrderInfo.get(orderId);
        if (orderInfo == null) {
            orderInfo = OrderInfo.findByTableInfo(tableInfo);
            if (orderInfo == null) {
                return [recode: ReCode.NO_ORDER];
            }
        } else {
            if (orderInfo.tableInfo != null) {
                tableInfo = orderInfo.tableInfo;
                webUtilService.setTableCode(orderInfo.tableInfo.code);
            }
        }
        if (tableInfo == null) {
            return [recode: ReCode.TABLE_NOT_EXIST];
        }
        if (orderInfo.clientInfo != clientInfo) { //非订单拥有者不能做加菜
            return [recode: ReCode.NOT_ORDER_OWNER];
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

        ClientInfo clientInfo = clientService.getClient();
        if (clientInfo == null) {
            return [recode: ReCode.NO_CLIENT];
        }
        if (params.code) {
            webUtilService.setTableCode(params.code);
        }
        String code = webUtilService.getTableCode();
        TableInfo tableInfo = TableInfo.findByCode(code);

        OrderInfo orderInfo = OrderInfo.get(orderId);
        if (orderInfo == null) {
            orderInfo = OrderInfo.findByTableInfo(tableInfo);
            if (orderInfo == null) {
                return [recode: ReCode.NO_ORDER];
            }
        } else {
            if (orderInfo.tableInfo != null) {
                tableInfo = orderInfo.tableInfo;
                webUtilService.setTableCode(orderInfo.tableInfo.code);
            }
        }
        if (tableInfo == null) {
            return [recode: ReCode.TABLE_NOT_EXIST];
        }
        if (orderInfo.clientInfo != clientInfo) { //非订单拥有者不能做加菜
            return [recode: ReCode.NOT_ORDER_OWNER];
        }
        //先查询是否存在初始态的点菜集合
        DishesCollection dishesCollection = DishesCollection.findByOrderInfoAndStatus(orderInfo, DishesCollectionStatus.ORIGINAL_STATUS.code);
//        if (dishesCollection == null) {
//            return [recode: ReCode.NO_ADDITION_DISH];
//        }
        return [recode: ReCode.OK,orderInfo:orderInfo, dishList: dishesCollection?.dishesInfos];
    }

}
