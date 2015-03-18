package lj.taos.order.customer

import lj.I18nError
import lj.common.ValidationCode
import lj.data.ClientInfo
import lj.data.FoodInfo
import lj.data.OrderInfo
import lj.data.ReserveDishesInfo
import lj.data.ReserveOrderInfo
import lj.data.RestaurantInfo
import lj.data.TableInfo
import lj.enumCustom.OrderStatus
import lj.enumCustom.OrderValid
import lj.enumCustom.ReCode
import lj.enumCustom.ReserveOrderStatus
import lj.Number

import java.text.SimpleDateFormat

class CustomerReserveOrderService {
    def shopService;
    def clientService;

    def g = new org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib();

    //根据预定信息查询出相应的可预定桌位
    def getReserveTable(def params){
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
        reserveOrderInfoNew.clientInfo=clientService.getClient();
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

        ClientInfo clientInfo = clientService.getClient();
        if (clientInfo == null) {
            return [recode: ReCode.NO_CLIENT];
        }

        ReserveOrderInfo reserveOrderInfo = ReserveOrderInfo.get(reserveOrderId);
        if (reserveOrderInfo == null) {
            return [recode: ReCode.NO_ORDER];
        }
        if (reserveOrderInfo.clientInfo != clientInfo) { //非订单拥有者不能做加菜
            return [recode: ReCode.NOT_ORDER_OWNER];
        }

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

        ClientInfo clientInfo = clientService.getClient();
        if (clientInfo == null) {
            return [recode: ReCode.NO_CLIENT];
        }

        ReserveOrderInfo reserveOrderInfo = ReserveOrderInfo.get(reserveOrderId);
        if (reserveOrderInfo == null) {
            return [recode: ReCode.NO_ORDER];
        }
        if (reserveOrderInfo.clientInfo != clientInfo) { //非订单拥有者不能做加菜
            return [recode: ReCode.NOT_ORDER_OWNER];
        }
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

        ClientInfo clientInfo = clientService.getClient();
        if (clientInfo == null) {
            return [recode: ReCode.NO_CLIENT];
        }

        if (!params.max) {
            params.max = 10
        }
        if (!params.offset) {
            params.offset = 0;
        }

//             def cIds=userService.getIds(ClientInfo.get(clientId));
        def condition = {
            eq("clientInfo.id",clientInfo.id);
            if (reserveOrderId) {
                eq("id", reserveOrderId);//id条件
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
        ClientInfo clientInfo = clientService.getClient();
        if (clientInfo == null) {
            return [recode: ReCode.NO_CLIENT];
        }
        ReserveOrderInfo reserveOrderInfo=ReserveOrderInfo.findByIdAndClientInfo(reserveOrderId,clientInfo);
        if(reserveOrderInfo){
            return [recode: ReCode.OK,reserveOrderInfo:reserveOrderInfo];
        }else{
            return [recode: ReCode.NO_RESULT];
        }
    }
}
