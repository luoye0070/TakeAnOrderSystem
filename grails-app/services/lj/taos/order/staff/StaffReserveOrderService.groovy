package lj.taos.order.staff

import lj.I18nError
import lj.Number
import lj.common.ValidationCode
import lj.data.*
import lj.enumCustom.DishesStatus
import lj.enumCustom.DishesValid
import lj.enumCustom.OrderStatus
import lj.enumCustom.OrderValid
import lj.enumCustom.ReCode
import lj.enumCustom.ReserveOrderStatus

import java.text.SimpleDateFormat

class StaffReserveOrderService {
    def shopService;
//    def clientService;
    def webUtilService;
    def staffOrderService;
    def orderAndReserveService;
    def g = new org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib();

    //根据预定信息查询出相应的可预定桌位
    def getReserveTable(def params){
        //标注过期订单
        //orderAndReserveService.markExpire();

        SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String dinnerTimeStr=params.dinnerTime;
        Date dinnerTime=new Date();
        try { dinnerTime = sdfDateTime.parse(dinnerTimeStr); } catch (Exception ex) {}

        //查询出所有桌位
        List<TableInfo> tableInfoListTemp=TableInfo.findAllByEnabled(true);
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
        Calendar calendar1=Calendar.getInstance();
        calendar1.setTime(new Date());
        if(calendar.before(calendar1)){
            return [recode:ReCode.BEFORE_NOW];
        }
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

    //创建预定订单
    def createReserveOrder(def params){
        StaffInfo staffInfo=webUtilService.getStaff();
        /**************取参数***************/
        SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String dinnerTimeStr=params.dinnerTime;
        Date dinnerTime=new Date();//到店时间
        try { dinnerTime = sdfDateTime.parse(dinnerTimeStr); } catch (Exception ex) {}
        String phone=params.phone;//联系电话
        String customerName=params.customerName;//联系人
        long tableId=lj.Number.toLong(params.tableId);
        int personCount=lj.Number.toInteger(params.personCount);
        String remark=params.remark;

        TableInfo tableInfo=TableInfo.get(tableId);
        if(tableInfo==null){
            return [recode: ReCode.TABLE_NOT_EXIST];
        }

        if(tableInfo.maxPeople<personCount){
            return [recode: ReCode.PERSONCOUNT_MORE_THAN_TABLE_MAX];
        }
        if(tableInfo.minPeople>personCount){
            return [recode: ReCode.PERSONCOUNT_LESS_THAN_TABLE_MAN];
        }

        /**********************查询是否桌位已经被占或被预定*******************************/
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
        Calendar calendar1=Calendar.getInstance();
        calendar1.setTime(new Date());
        if(calendar.before(calendar1)){
            return [recode:ReCode.BEFORE_NOW];
        }
        calendar.add(Calendar.MINUTE,intervalTime*-1)
        Date lowerDateTime=calendar.getTime();
        println("lowerDateTime-->"+lowerDateTime.toLocaleString());
        calendar.setTime(dinnerTime);
        calendar.add(Calendar.MINUTE,intervalTime)
        Date upperDateTime=calendar.getTime();
        println("upperDateTime-->"+upperDateTime.toLocaleString());
        //查询是否有预定
        ReserveOrderInfo reserveOrderInfo=ReserveOrderInfo.findByTableInfoAndDinnerTimeBetweenAndValidAndStatus(tableInfo,lowerDateTime,upperDateTime,OrderValid.EFFECTIVE_VALID.code,ReserveOrderStatus.ORIGINAL_STATUS.code);
        if(reserveOrderInfo){
            return [recode: ReCode.TABLE_IS_RESERVED];
        }
        //查询是否有订单
        OrderInfo orderInfo=OrderInfo.findByTableInfoAndCreateTimeBetweenAndValidAndStatusLessThan(tableInfo,lowerDateTime,upperDateTime,OrderValid.EFFECTIVE_VALID.code, OrderStatus.CHECKOUTED_STATUS.code);
        if(orderInfo){
            return [recode: ReCode.TABLE_HOLDED];
        }

        /*********************创建预定订单***********************/
        ReserveOrderInfo reserveOrderInfoNew=new ReserveOrderInfo();
        reserveOrderInfoNew.tableInfo=tableInfo;
        //reserveOrderInfoNew.clientInfo=clientService.getClient();
        reserveOrderInfoNew.waiter=webUtilService.getStaff();
        reserveOrderInfoNew.dinnerTime=dinnerTime;
        Date now=new Date();
        String orderNumStr = now.getTime() + ValidationCode.getAuthCodeStr(2, ValidationCode.NUMBER);
        long orderNum = Long.parseLong(orderNumStr);
        reserveOrderInfoNew.orderNum= orderNum;
        //获取订单店内编号最大值
        String sqlStr = "select max(numInRestaurant) from ReserveOrderInfo";
        def result = OrderInfo.executeQuery(sqlStr);
        long numInRestaurant = 1;
        if (result != null && result[0] != null) {
            numInRestaurant = result[0] + 1;
        }
        reserveOrderInfoNew.numInRestaurant=numInRestaurant;
        reserveOrderInfoNew.personCount=personCount;
        reserveOrderInfoNew.remark=remark;
        reserveOrderInfoNew.phone=phone;
        reserveOrderInfoNew.customerName=customerName;
        reserveOrderInfoNew.restaurantName=restaurantInfo.name;
        reserveOrderInfoNew.tableName=tableInfo.name;
        reserveOrderInfoNew.waiter=staffInfo;

        if(!reserveOrderInfoNew.save(flush: true)){
            return [recode: ReCode.SAVE_FAILED, reserveOrderInfo: reserveOrderInfoNew, errors: I18nError.getMessage(g, reserveOrderInfoNew.errors.allErrors)];
        }

        return [recode: ReCode.OK,reserveOrderInfo: reserveOrderInfoNew];
    }

    //点菜
    def addDish(def params){
        //获取参数
        long reserveOrderId = lj.Number.toLong(params.reserveOrderId);//订单ID
        def foodIds = params.foodIds;//菜品Id列表

//        ClientInfo clientInfo = clientService.getClient();
//        if (clientInfo == null) {
//            return [recode: ReCode.NO_CLIENT];
//        }

        ReserveOrderInfo reserveOrderInfo = ReserveOrderInfo.get(reserveOrderId);
        if (reserveOrderInfo == null) {
            return [recode: ReCode.NO_ORDER];
        }
//        if (reserveOrderInfo.clientInfo != clientInfo) { //非订单拥有者不能做加菜
//            return [recode: ReCode.NOT_ORDER_OWNER];
//        }

        //检查定当前状态是否能点菜
        if (reserveOrderInfo.status == ReserveOrderStatus.REACHED_STATUS.code||reserveOrderInfo.valid>=OrderValid.USER_CANCEL_VALID.code) {//取消订单和已到店订单不能再点菜
            return [recode: ReCode.CANNOT_DISH];//不能点菜
        }

        //点菜
        long foodId = lj.Number.toLong(foodIds);
        int foodCount = Number.toInteger(params.get("counts" + foodId));
        String remark = params.get("remarks" + foodId);
        //更新菜品数量
        FoodInfo foodInfo = FoodInfo.get(foodId);
        if (foodInfo) {
            if (foodInfo.enabled) {//在售
                if (foodInfo.countLimit == 0 || (foodInfo.countLimit >= foodCount + foodInfo.sellCount)) {//数量足够
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

        //创建点菜记录
        ReserveDishesInfo reserveDishesInfo = new ReserveDishesInfo();
        //reserveDishesInfo.reserveOrderInfo = reserveOrderInfo;
        reserveDishesInfo.food = foodInfo;
        reserveDishesInfo.num = foodCount;
        reserveDishesInfo.remark = remark;//备注
        reserveDishesInfo.foodPrice = foodInfo.price;//价格
        reserveDishesInfo.foodName = foodInfo.name;
        reserveDishesInfo.orderTime = reserveOrderInfo.createTime;
        reserveDishesInfo.foodImg = foodInfo.image;
        reserveDishesInfo.tableName = reserveOrderInfo.tableName;
        if (!reserveDishesInfo.save(flush: true)) {//保存数据失败输出日志
            println("保存点菜记录失败:" + reserveDishesInfo);
            throw new RuntimeException(I18nError.getMessage(g, reserveDishesInfo.errors.allErrors));
        }

        //添加到点菜集合
        if(reserveOrderInfo.dishes==null){
            reserveOrderInfo.dishes=new ArrayList<ReserveDishesInfo>();
        }
        reserveOrderInfo.dishes.add(reserveDishesInfo);
        if (!reserveOrderInfo.save(flush: true)) {
            throw new RuntimeException(I18nError.getMessage(g, reserveOrderInfo.errors.allErrors));
        }
        return [recode: ReCode.OK, reserveOrderInfo: reserveOrderInfo, reserveDishesInfo: reserveDishesInfo];
    }

    //删除点菜
    def delDish(def params){
        //获取参数
        long reserveOrderId = lj.Number.toLong(params.reserveOrderId);//订单ID
        def dishIds = params.dishIds;//点菜Id列表

//        ClientInfo clientInfo = clientService.getClient();
//        if (clientInfo == null) {
//            return [recode: ReCode.NO_CLIENT];
//        }

        ReserveOrderInfo reserveOrderInfo = ReserveOrderInfo.get(reserveOrderId);
        if (reserveOrderInfo == null) {
            return [recode: ReCode.NO_ORDER];
        }
//        if (reserveOrderInfo.clientInfo != clientInfo) { //非订单拥有者不能做加菜
//            return [recode: ReCode.NOT_ORDER_OWNER];
//        }
        //检查定当前状态是否能点菜
        if (reserveOrderInfo.status == ReserveOrderStatus.REACHED_STATUS.code||reserveOrderInfo.valid>=OrderValid.USER_CANCEL_VALID.code) {//取消订单和已到店订单不能再点菜
            return [recode: ReCode.CANNOT_DELETE_DISH];//不能删除点菜
        }

        ReserveDishesInfo reserveDishesInfo = null;
        if (dishIds&&dishIds instanceof String) {//不然如果按点菜Id列表删除
            reserveDishesInfo = ReserveDishesInfo.get(Number.toLong(dishIds));
        } else {
            return [recode: ReCode.ERROR_PARAMS];//参数错误
        }
        if (reserveDishesInfo) {
            FoodInfo.executeUpdate("update FoodInfo set sellCount=sellCount-" + reserveDishesInfo.num + " where id=" + reserveDishesInfo.foodId);//更新菜的销量
            reserveOrderInfo.dishes.remove(reserveDishesInfo);
            reserveOrderInfo.save(flush: true);
            reserveDishesInfo.delete(flush: true);
        }
        return [recode: ReCode.OK];//成功
    }
    //预定订单列表
    def reserveOrderList(def params){
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        //获取参数
        long reserveOrderId = Number.toLong(params.reserveOrderId);//订单ID
        long tableId = Number.toLong(params.tableId);//桌位ID
        String beginTimeStr = params.beginTime;//开始时间
        Date beginTime = null;
        try { beginTime = sdfDate.parse(beginTimeStr); } catch (Exception ex) {}
        String endTimeStr = params.endTime;//截止时间
        Date endTime = null;
        try { endTime = sdfDate.parse(endTimeStr); } catch (Exception ex) {}
        String phone=params.phone;
        int status=-1;
        if(params.status!=null)
            status=Number.toInteger(params.status);//订单状态
        int valid=-1;
        if (params.valid!=null)
            valid=Number.toInteger(params.valid);//有效性
//        ClientInfo clientInfo = clientService.getClient();
//        if (clientInfo == null) {
//            return [recode: ReCode.NO_CLIENT];
//        }

        if (!params.max) {
            params.max = 10
        }
        if (!params.offset) {
            params.offset = 0;
        }

//             def cIds=userService.getIds(ClientInfo.get(clientId));
        def condition = {
//            eq("clientInfo.id",clientInfo.id);
            if (reserveOrderId) {
                eq("id", reserveOrderId);//id条件
            }
            if (tableId) {
                eq("tableInfo.id", tableId);//桌位ID条件
            }
            if (beginTime) {
                ge("dinnerTime", beginTime);//日期条件
            }
            if (endTime) {
                le("dinnerTime", endTime);//日期条件
            }
            if(phone){
                eq("phone",phone);//手机号条件
            }
            if(status>=0){
                eq("status",status);//订单状态条件
            }
            if(valid>=0){
                eq("valid",valid);//订单有效性条件
            }
        }

        if (!params.sort) {//如果没有排序，则按ID倒排序
            params.sort = "id";
            params.order = "desc";
        }

        def reserveOrderInfoList = ReserveOrderInfo.createCriteria().list(params, condition);
        def totalCount = ReserveOrderInfo.createCriteria().count(condition);

        return [recode: ReCode.OK, totalCount: totalCount, reserveOrderInfoList: reserveOrderInfoList];
    }
    //预定订单详情
    def getReserveOrderInfo(def params){
        //获取参数
        long reserveOrderId = Number.toLong(params.reserveOrderId);//订单ID
//        ClientInfo clientInfo = clientService.getClient();
//        if (clientInfo == null) {
//            return [recode: ReCode.NO_CLIENT];
//        }
        ReserveOrderInfo reserveOrderInfo=ReserveOrderInfo.findById(reserveOrderId);
        if(reserveOrderInfo){
            return [recode: ReCode.OK,reserveOrderInfo:reserveOrderInfo];
        }else{
            return [recode: ReCode.NO_RESULT];
        }
    }
    //订单取消
    def reserveOrderCancel(def params){
        //获取参数
        long reserveOrderId = Number.toLong(params.reserveOrderId);//订单ID
        ReserveOrderInfo reserveOrderInfo=ReserveOrderInfo.findById(reserveOrderId);
        if(reserveOrderInfo){
            if(reserveOrderInfo.status==ReserveOrderStatus.REACHED_STATUS.code){//到店订单不能取消
                return [recode: ReCode.RESERVE_ORDER_CANNOT_CANCEL];
            }
            if(reserveOrderInfo.valid<=OrderValid.EFFECTIVE_VALID.code){
                reserveOrderInfo.valid=OrderValid.RESTAURANT_CANCEL_VALID.code;
                if(!reserveOrderInfo.save(flush: true)){
                    return [recode: ReCode.SAVE_FAILED, reserveOrderInfo: reserveOrderInfo, errors: I18nError.getMessage(g, reserveOrderInfo.errors.allErrors)];
                }
            }
            return [recode: ReCode.OK,reserveOrderInfo:reserveOrderInfo];
        }else{
            return [recode: ReCode.NO_RESULT];
        }
    }
    //顾客到店
    def reserveOrderReach(def params){
        //获取参数
        long reserveOrderId = Number.toLong(params.reserveOrderId);//订单ID
        ReserveOrderInfo reserveOrderInfo=ReserveOrderInfo.findById(reserveOrderId);
        if(reserveOrderInfo){
            if(reserveOrderInfo.valid>=OrderValid.USER_CANCEL_VALID.code){//到店订单不能
                return [recode: ReCode.RESERVE_ORDER_CANNOT_REACH];
            }

            OrderInfo orderInfo=null;
            //设置状态和初始值
            int status = DishesStatus.ORIGINAL_STATUS.code;;
            int valid = DishesValid.ORIGINAL_VALID.code;

            if(reserveOrderInfo.status==ReserveOrderStatus.ORIGINAL_STATUS.code){
                reserveOrderInfo.status=ReserveOrderStatus.REACHED_STATUS.code;
                if(!reserveOrderInfo.save(flush: true)){
                    println("保存记录失败:" + reserveOrderInfo);
                    throw new RuntimeException(I18nError.getMessage(g, reserveOrderInfo.errors.allErrors));
                }
//                //根据预定订单创建订单
//                OrderInfo orderInfo=new OrderInfo();
//                orderInfo.tableInfo=reserveOrderInfo.tableInfo;
//                orderInfo.waiter=reserveOrderInfo.waiter;
//                orderInfo.partakeCode=ValidationCode.getAuthCodeStr(4,ValidationCode.NUMBER);
//                Date now=new Date();
//                String orderNumStr=now.getTime()+ValidationCode.getAuthCodeStr(2,ValidationCode.NUMBER);
//                long orderNum=Long.parseLong(orderNumStr);
//                orderInfo.orderNum=orderNum;
//                //获取订单店内编号最大值
//                String sqlStr="select max(numInRestaurant) from OrderInfo";
//                def result=OrderInfo.executeQuery(sqlStr);
//                long numInRestaurant=1;
//                if(result!=null&&result[0]!=null){
//                    numInRestaurant=result[0]+1;
//                }
//                orderInfo.numInRestaurant=numInRestaurant;
//                if(!orderInfo.save(flush: true)){
//                    throw new RuntimeException(I18nError.getMessage(g,orderInfo.errors.allErrors));
//                }
//                if(reserveOrderInfo.dishes){
//                    def failedList = [];//点菜失败的菜列表
//                    int size=reserveOrderInfo.dishes.size();
//                    for (int i=0;i<size;i++){
//                        ReserveDishesInfo rdishInfo=reserveOrderInfo.dishes.get(i);
//                        if(rdishInfo.food?.isReady){
//                            status=DishesStatus.COOKED_STATUS.code;
//                        }
//                        //创建点菜记录
//                        DishesInfo dishesInfo = new DishesInfo();
//                        dishesInfo.order = orderInfo;
//                        dishesInfo.food = rdishInfo.food;
//                        dishesInfo.status = status;
//                        dishesInfo.valid = valid;
//                        dishesInfo.numInRestaurant = 0;//先不要店内编号
//                        dishesInfo.num = rdishInfo.count;
//                        dishesInfo.remark=rdishInfo.remark;//备注
//                        dishesInfo.foodPrice= rdishInfo.food?.price;//价格
//                        dishesInfo.foodName=rdishInfo.food?.name;
//                        dishesInfo.orderTime=orderInfo.createTime;
//                        dishesInfo.foodImg=rdishInfo.food?.image;
//                        dishesInfo.tableName=orderInfo.tableName;
//                        dishesInfo.orderType=orderInfo.orderType;
//                        if (!dishesInfo.save(flush: true)) {//保存数据失败输出日志
//                            println("保存点菜记录失败:" + dishesInfo);
//                            failedList.add([foodId: it.foodId, msg: "保存点菜记录失败",errors:I18nError.getMessage(g,dishesInfo.errors.allErrors)]);
//                        }
//                    }
//                    if(failedList.size()>0){
//                        return [recode: ReCode.DISH_HAVEERROR,failedList: failedList];
//                    }
//                }
                //创建订单
                def reInfo=staffOrderService.createOrder([code: reserveOrderInfo.tableInfo.code]);
                if(reInfo.recode!=ReCode.OK){
                    throw new RuntimeException(reInfo.recode.label);
                }else{
                    orderInfo=reInfo.orderInfo;
                }
                //点菜
                if(reserveOrderInfo.dishes){
                    def failedList = [];//点菜失败的菜列表
                    int size=reserveOrderInfo.dishes.size();
                    for (int i=0;i<size;i++){
                        ReserveDishesInfo rdishInfo=reserveOrderInfo.dishes.get(i);
                        if(rdishInfo.food?.isReady){
                            status=DishesStatus.COOKED_STATUS.code;
                        }
                        //创建点菜记录
                        DishesInfo dishesInfo = new DishesInfo();
                        dishesInfo.order = orderInfo;
                        dishesInfo.food = rdishInfo.food;
                        dishesInfo.status = status;
                        dishesInfo.valid = valid;
                        dishesInfo.numInRestaurant = 0;//先不要店内编号
                        dishesInfo.num = rdishInfo.num;
                        dishesInfo.remark=rdishInfo.remark;//备注
                        dishesInfo.foodPrice= rdishInfo.food?.price;//价格
                        dishesInfo.foodName=rdishInfo.food?.name;
                        dishesInfo.orderTime=orderInfo.createTime;
                        dishesInfo.foodImg=rdishInfo.food?.image;
                        dishesInfo.tableName=orderInfo.tableName;
                        dishesInfo.orderType=orderInfo.orderType;
                        if (!dishesInfo.save(flush: true)) {//保存数据失败输出日志
                            println("保存点菜记录失败:" + dishesInfo);
                            failedList.add([foodId: rdishInfo.food?.id, msg: "保存点菜记录失败",errors:I18nError.getMessage(g,dishesInfo.errors.allErrors)]);
                        }
                    }
                    if(failedList.size()>0){
                        return [recode: ReCode.DISH_HAVEERROR,failedList: failedList];
                    }
                }
            }
            return [recode: ReCode.OK,reserveOrderInfo:reserveOrderInfo,orderInfo:orderInfo];
        }else{
            return [recode: ReCode.NO_RESULT];
        }
    }
}
