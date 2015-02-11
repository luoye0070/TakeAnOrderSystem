package lj.taos.order.customer

import lj.I18nError
import lj.Number
import lj.common.Distance
import lj.common.ValidationCode
import lj.data.ClientInfo
import lj.data.OrderInfo
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

    def g = new org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib();
    /**
     * 根据传人的桌位号来获取或创建一个订单
     *
     *  params是传入的参数
     * 参数格式为：
     *code=params.code;//桌位code,必须
     * */
    def getOrCreateOrder(def params){
        ClientInfo clientInfo=clientService.getClient();
        if(clientInfo==null){
            return [recode: ReCode.NO_CLIENT];
        }
        TableInfo tableInfo= TableInfo.findByCode(params.code);
        if(tableInfo==null){
            return [recode: ReCode.TABLE_NOT_EXIST];
        }
        OrderInfo orderInfo=OrderInfo.findByTableInfoAndValidAndStatusLessThan(tableInfo,OrderValid.EFFECTIVE_VALID.code,OrderStatus.CHECKOUTED_STATUS.code);
        if (orderInfo==null){
            //检测桌位是否已经被预定

            orderInfo=new OrderInfo();
            orderInfo.clientInfo=clientInfo;
            orderInfo.tableInfo=tableInfo;
            Date now=new Date();
            String orderNumStr=now.getTime()+ValidationCode.getAuthCodeStr(2,ValidationCode.NUMBER);
            long orderNum=Long.parseLong(orderNumStr);
            orderInfo.orderNum=orderNum;
            if(!orderInfo.save(flush: true)){
                return [recode: ReCode.SAVE_FAILED, orderInfo: orderInfo,errors:I18nError.getMessage(g,orderInfo.errors.allErrors)];
            }
        }
        if(orderInfo.clientInfo==null){
            orderInfo.clientInfo=clientInfo;
            if(!orderInfo.save(flush: true)){
                return [recode: ReCode.SAVE_FAILED, orderInfo: orderInfo,errors:I18nError.getMessage(g,orderInfo.errors.allErrors)];
            }
        }
        boolean isOwner=false;
        if(orderInfo.clientInfo==clientInfo){
            isOwner=true;
        }
        return [recode: ReCode.OK,orderInfo:orderInfo,isOwner:isOwner];
    }
//    /***************
//     * 创建订单
//     *
//     * params是传入的参数
//     * 参数格式为：
//     *tableId=Number.toLong(params.tableId);//桌位ID
//     *restaurantId=Number.toLong(params.restaurantId);//饭店ID,必须
//     *dateStr=params.date; //日期,yyyy-MM-dd ,到店吃饭可以不要
//     *timeStr=params.time; //时间,HH:mm:ss ，到店吃饭可以不要
//     *reserveType=Number.toInteger(params.reserveType);//预定类别 ,非预定可以不要
//     *addressId=Number.toLong(params.addressId);//外卖地址ID ,非外卖可以不要
//     * int personCount=Number.toInteger(params.personCount);//用餐人数,非必须
//     * phone;//联系电话，非必须
//     * //点菜列表参数,非必须
//     * foodIds=12或foodIds=[112,231...]
//     * counts=1或counts=[2,3...]
//     * remarks=ddd或remarks=[dd,,dd...]
//     *
//     * 返回值
//     * [recode: ReCode.BLACKLIST_CUSTOMER];用户在饭店的黑名单内不能完成创建订单
//     * [recode: ReCode.OUT_RANGE];外卖送的地址超出了饭店的配送范围，不能完成订单创建
//     * [recode:ReCode.OUT_SHOPHOURS];超出了饭店的营业时间，不能完成订单创建
//     * [recode:ReCode.TABLE_HOLDED];桌位被占，不能完成订单的创建
//     * [recode:ReCode.SAVE_FAILED];订单保存失败
//     * [recode:ReCode.NOT_LOGIN];用户没有登录不能完成订单创建
//     * [recode: ReCode.OK];订单创建成功
//     * **********************/
//    def createOrder(def params,boolean byWaiter){
//        def session=webUtilService.getSession();
//        SimpleDateFormat sdfDate=new SimpleDateFormat("yyyy-MM-dd");
//        SimpleDateFormat sdfTime=new SimpleDateFormat("HH:mm:ss");
//        //取出用户ID
//        //long userId=Number.toLong(session.userId);//用户ID
//        long clientId=webUtilService.getClientId();//客户ID
//        //UserInfo userInfo=session.user;
//        if(byWaiter){//如果是服务员帮助创建订单，则取服务员ID作为用户ID
//            clientId=Number.toLong(session.staffId);
//        }
//        if(clientId){
//            //获取参数
//            long tableId=Number.toLong(params.tableId);//桌位ID
//            long restaurantId=Number.toLong(params.restaurantId);//饭店ID
//            String dateStr=params.date; //日期,yyyy-MM-dd
//            String timeStr=params.time; //时间,HH:mm:ss
//            int reserveType=Number.toInteger(params.reserveType);//预定类别
//            long addressId=Number.toLong(params.addressId);//外卖地址ID
////            boolean has2Code=params.has2Code;//是否扫描了2维码
//            String remark=params.remark;//备注信息，记录客户的联系电话等信息
//            int personCount=Number.toInteger(params.personCount);//用餐人数
//            //检查用户是否在黑名单之内
//            if(!byWaiter){
//                CustomerRelations customerRelations=CustomerRelations.findByRestaurantIdAndCustomerClientIdAndType(restaurantId,clientId,CustomerRelationsType.BLACKLIST_CUSTOMER.code);
//                if(customerRelations){//用户在黑名单中
//                    return [recode: ReCode.BLACKLIST_CUSTOMER];
//                }
//            }
//
//            RestaurantInfo restaurantInfo=RestaurantInfo.findByIdAndEnabledAndVerifyStatus(restaurantId,true, VerifyStatus.PASS.code);
//            if(!restaurantInfo){ //饭店不存在
//                return [recode: ReCode.NO_RESTAURANTINFO];
//            }
//
//            //检查外卖地址是否在放到的配送范围内
//            if(tableId==0){
//                AddressInfo addressInfo=AddressInfo.get(addressId);
//                if(addressInfo==null){//地址不能为空
//                    return [recode:ReCode.NO_VALID_ADDRESS];
//                }
//                if(addressInfo.latitude&&addressInfo.longitude&&restaurantInfo.latitude&&restaurantInfo.longitude){
//                    //通过经纬度用勾股定理计算距离
//                    double distance=Distance.GetDistance(addressInfo.latitude,addressInfo.longitude,restaurantInfo.latitude,restaurantInfo.longitude);
//                    if(distance>restaurantInfo.deliverRange){
//                        //超出范围
//                        return [recode: ReCode.OUT_RANGE];
//                    }
//                }
//            }
//
//            //参数预处理
//            Date date=null;//日期
//            try{date=sdfDate.parse(dateStr);}catch (Exception ex){}
//            Date time=null;//时间
//            try{time=sdfTime.parse(timeStr);}catch (Exception ex){}
//            println("time-->"+timeStr+"----"+time);
//            if(reserveType!=0||tableId==0){ //检查日期时间参数是否存在
//                if(date==null){
//                    return [recode: ReCode.NEED_RESERVE_DATE];
//                }
//                if(time==null){
//                    return [recode: ReCode.NEED_RESERVE_TIME];
//                }
//            }
//
//            long numInRestaurant=0;//店内编号
//            def maxNumInRestaurant=OrderInfo.executeQuery("select max(numInRestaurant) from OrderInfo where restaurantId="+restaurantId);
//            println("maxNumInRestaurant-->"+maxNumInRestaurant);
//            if(maxNumInRestaurant&&maxNumInRestaurant[0]){
//                numInRestaurant=maxNumInRestaurant[0]+1;
//            }
//            String partakeCode="1111";//参与验证码
//            StringBuffer temp=new StringBuffer("");
//            for(int i = 0 ; i<4 ; i++){
//                temp.append(new Random().nextInt(10).toString());
//            }
//            partakeCode=temp.toString();
//
//            /***********分配跟进服务员**********/
//            long waiterId=0; // 服务员ID
//            if(byWaiter)
//            {
//                waiterId=clientId;
//            }
//            else{
//                //查出饭店总订单数量
//                int orderCount=OrderInfo.createCriteria().count(){
//                    ne("valid",2)
//                    eq("restaurantId",restaurantId)
//                    lt("status",OrderStatus.CHECKOUTED_STATUS.code);
//                    lt("valid",OrderValid.USER_CANCEL_VALID.code);
//                }
//                def waiterIds=StaffPositionInfo.findAllByPositionTypeAndRestaurantId(PositionType.WAITER.code,restaurantId).collect{it.staffId};
//                println("waiterIds-->"+waiterIds);
//                if(!waiterIds||waiterIds.size()==0){//系统错误，没有服务员 ,分配给店主
//                    StaffPositionInfo staffPositionInfo=StaffPositionInfo.findByRestaurantIdAndPositionType(restaurantId,PositionType.SHOPKEEPER.code);
//                    if(staffPositionInfo)
//                    {
//                        waiterId=staffPositionInfo.staffId;
//                    }
//                    else {
//                        return [recode: ReCode.SYSTEM_ERROR];
//                    }
//                }
//                else {
//                    //分配服务员Id
//                    ArrayList<StaffInfo> waiterList=StaffInfo.createCriteria().list(){
//                        eq("restaurantId",restaurantId)
//                        inList("id",waiterIds);
//                    }
//                    if(waiterList!=null&&waiterList.size()>0){
//                        int waiterCount=waiterList.size();//查出服务员数量
//                        int idx= orderCount%waiterCount;
//                        //if(waiterList.get(idx).isOnline){
//                        if(MinaServer.isOnline(waiterList.get(idx).id,0)){
//                            waiterId=waiterList.get(idx).id;
//                        }
//                    }
//                    if(waiterId==0){//服务员不在线则分配给主管
//                        def waiterHeaderIds=StaffPositionInfo.findAllByPositionTypeAndRestaurantId(PositionType.WAITER_HEADER.code,restaurantId).collect{it.staffId};
//                        if(waiterHeaderIds){
//                            //分配服务员Id
//                            ArrayList<StaffInfo> waiterHeaderList=StaffInfo.createCriteria().list(){
//                                eq("restaurantId",restaurantId)
//                                inList("id",waiterHeaderIds);
//                                //eq("isOnline",true);
//                            }
//                            if(waiterHeaderList!=null&&waiterHeaderList.size()>0){
//                                int waiterCount=waiterHeaderList.size();//查出服务员数量
//                                int idx= orderCount%waiterCount;
//                                if(MinaServer.isOnline(waiterList.get(idx).id,0)){
//                                    waiterId=waiterList.get(idx).id;
//                                }
//                                //waiterId=waiterHeaderList.get(idx).id;
//                            }
//                        }
//                    }
//                    if(waiterId==0){//没有在线工作人员，则分配给店主
//                        StaffPositionInfo staffPositionInfo=StaffPositionInfo.findByRestaurantIdAndPositionType(restaurantId,PositionType.SHOPKEEPER.code);
//                        if(staffPositionInfo)
//                        {
//                            waiterId=staffPositionInfo.staffId;
//                        }
//                        else {
//                            return [recode: ReCode.SYSTEM_ERROR];
//                        }
//                    }
//                }
//            }
//            TableInfo tableInfo=null;
//            if(tableId){//非外卖
//                int reserveTypeTemp=reserveType;
//                if(reserveType==0){//到店吃饭
////                    if(!byWaiter){//如果不是服务员帮忙创建订单的话需要检查是否需要扫描二维码
////                        if(restaurantInfo){
////                            if(restaurantInfo.need2Code&&!has2Code){//需要扫描2维码而客户没有扫描2维码
////                                return [recode:ReCode.NEED_2CODE];
////                            }
////                        }
////                    }
//                    //日期时间为当前日期时间
//                    date=new Date();
//                    date=sdfDate.parse(sdfDate.format(date));
//                    time =new Date();
//                    time=sdfTime.parse(sdfTime.format(time));
//                    //构造日期和预定类型（早中晚饭）
//                    def reserveTypeInfos=ReserveTypeInfo.findAllByRestaurantId(restaurantId);
//                    if(reserveTypeInfos){
//                        for(int i=0;i<reserveTypeInfos.size();i++) {
//                            def it=reserveTypeInfos.get(i);
//                            Calendar calendar=Calendar.getInstance();
//                            calendar.setTime(time);
//                            //if((calendar.after(it.beginTime)&&calendar.before(it.endTime))||calendar.equals(it.endTime)||calendar.equals(it.beginTime)){
//                            if((it.beginTime.before(time)&&it.endTime.after(time))||it.endTime.equals(time)||it.beginTime.equals(time)){
//                                reserveTypeTemp=it.reserveType;
//                                break;
//                            }
//                        }
//                    }
//                }
//                else{//预定到店吃饭
//                    //检查预定日期是否合法
//                    Date now=sdfDate.parse(sdfDate.format(new Date()));
//                    if(date.before(now)){
//                        return [recode: ReCode.OVER_RESERVE_DATE];
//                    }
//                    //检查到店时间是否在营业时间内
//                    Calendar calendar=Calendar.getInstance();
//                    calendar.setTime(time);
//                    //if(calendar.after(restaurantInfo.shopHoursEndTime)||calendar.before(restaurantInfo.shopHoursBeginTime)){//在营业时间之外
//                    if(restaurantInfo.shopHoursEndTime.before(time)||restaurantInfo.shopHoursBeginTime.after(time)){//在营业时间之外
//                            return [recode:ReCode.OUT_SHOPHOURS]
//                    }
//                    //检查到店时间是否在预定类型的时间内
//                    ReserveTypeInfo reserveTypeInfo=ReserveTypeInfo.findByRestaurantIdAndReserveType(restaurantId,reserveType);
//                    if(reserveTypeInfo){
//                        println("reserveTypeInfo--->"+reserveTypeInfo.beginTime+"---"+reserveTypeInfo.endTime+"--"+time);
//                        if((reserveTypeInfo.endTime.before(time)||reserveTypeInfo.beginTime.after(time))){
//                            return [recode:ReCode.OUT_RESERVETYPE_TIME];
//                        }
//                    }
//                }
//                //检查桌号是否支持多单共桌
//                tableInfo=TableInfo.get(tableId);
//                if(tableInfo){
//                ReserveTypeInfo reserveTypeInfo=ReserveTypeInfo.findByRestaurantIdAndReserveType(restaurantId,reserveTypeTemp);
//                if(!tableInfo.canMultiOrder){//不支持多单共桌
//                    //检查桌号是否被占
//                    int orderCount=OrderInfo.createCriteria().count(){
//                        eq("restaurantId",restaurantId);
//                        eq("tableId",tableId);
//                        eq("date",date);
//                        if(reserveTypeInfo){
//                            le("time",reserveTypeInfo.endTime);
//                            ge("time",reserveTypeInfo.beginTime);
//                        }
//                        //eq("reserveType", reserveType);
//                        lt("status",OrderStatus.CHECKOUTED_STATUS.code);
//                        lt("valid",OrderValid.USER_CANCEL_VALID.code);
//                    }//findByRestaurantIdAndTableIdAndDateAndReserveTypeIdAndStatus(restaurantId,tableId,date,reserveType,OrderStatus.CHECKOUTED_STATUS.code);
//                    if(orderCount){
//                        return [recode:ReCode.TABLE_HOLDED];
//                    }
//                }
//                }
//                else{
//                    return [recode: ReCode.TABLE_NOT_EXIST];
//                }
//            }
//
//            /********************创建订单*****************************/
//            if(byWaiter){
//                clientId=0;
//            }
//            OrderInfo orderInfo=new OrderInfo();
//            //设置域类的值
//            orderInfo.restaurantId=restaurantId;//饭店ID
//            orderInfo.clientId=clientId;//用户ID
//            orderInfo.tableId=tableId;//桌号
//            orderInfo.date=date;//日期
//            orderInfo.time=time;//时间
//            orderInfo.reserveType=reserveType;//预定类型
//            orderInfo.addressId=addressId;//外卖地址
//            orderInfo.waiterId=waiterId;//服务员ID
//            orderInfo.listenWaiterId=waiterId;//接收消息服务员ID
//            orderInfo.numInRestaurant=numInRestaurant;//店内编号
//            orderInfo.partakeCode=partakeCode;//参与验证码
//            orderInfo.orderNum=Number.makeNum();//订单号
//            if(params.phone){ //联系电话
//                orderInfo.phone=params.phone;
//            }
//            if(params.customerName){
//                orderInfo.customerName=params.customerName;
//            }
//            orderInfo.remark=remark;//备注信息，客户联系电话等
//            orderInfo.restaurantName=restaurantInfo?.name;//饭店名
//            if(!byWaiter){
//                ClientInfo clientInfo=webUtilService.getClient();
//                orderInfo.userName=clientInfo?.userName;//用户名
//            }
//            orderInfo.tableName=tableInfo?.name;//桌位名
//            if(personCount)
//                orderInfo.personCount=personCount;//用餐人数
//            //设置状态初始值
//            if(tableId==0){//外卖
//                orderInfo.status=OrderStatus.ORDERED_STATUS.code;//点菜完成
//                orderInfo.orderType=OrderType.TAKE_OUT.code;
//                if(byWaiter){
//                    orderInfo.status=OrderStatus.VERIFY_ORDERED_STATUS.code;//确认点菜完成
//                    orderInfo.valid=OrderValid.EFFECTIVE_VALID.code; //确认有效
//                }
//            }
//            else{
//                if(reserveType==0){//到店吃饭
//                    orderInfo.orderType=OrderType.NORMAL.code;
//                    orderInfo.reachRestaurant=true;//到店
//                }else{
//                    orderInfo.orderType=OrderType.RESERVE.code;
//                    orderInfo.reachRestaurant=false;//没到店
//                }
//            }
//            //保存订单
//            if(orderInfo.save(flush: true)){ //保存成功
////                //生成订单流水号
////                String timestr      =(new Date().getTime())+"";
////                String idstr        =orderInfo.id+"";
////                if(idstr.length()>4){
////                    idstr=idstr.substring(idstr.length()-4,idstr.length());
////                }
////                else{
////                    idstr=String.format("%1\$04d",idstr);
////                }
////                orderInfo.orderNum=timestr+idstr;//订单流水号
////                if(orderInfo.save(flush: true)){ //保存成功
////                }
////                else{
////                    return [recode:ReCode.SAVE_FAILED,errors:orderInfo.errors.allErrors];
////                }
//            }
//            else{
//                return [recode:ReCode.SAVE_FAILED,orderInfo:orderInfo,errors:orderInfo.errors.allErrors];
//            }
//            println("params.foodIds-->"+params.foodIds);
//            if(params.foodIds){//如果有传点菜列表，则点菜
//                params.orderId=orderInfo.id;//传入订单ID
//                def reInfo=customerDishService.addDishes(params,byWaiter);
//                println("customerDishService.addDishes.reInfo-->"+reInfo);
//            }
//
//            if(!byWaiter){
//                //调用消息服务创建消息
//                def msgParams=[:];
//                msgParams.orderId=orderInfo.id;
//                msgParams.type=MessageType.ORDER_HANDLE_TYPE.code;
//                msgParams.receiveId=waiterId;
//                msgParams.content="有新的订单需要处理,订单id是"+orderInfo.id+",赶快点击去处理吧";
//                msgParams.sendType=MsgSendType.CUSTOMER_TO_STAFF.code;
//                msgParams.restaurantId=orderInfo.restaurantId;
//                def reInfo=messageService.createMsg(msgParams);
//                if(reInfo.recode!=ReCode.OK){
//                    println("保存消息失败，但对于订单的产生没有致命影响，故忽略此错误，请系统管理员注意查证："+",reInfo="+reInfo);
//                }
//            }
//            return [recode: ReCode.OK,orderInfo:orderInfo];
//        }
//        else{
//            return [recode:ReCode.NOT_LOGIN];
//        }
//    }

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
    def cancelOrder(def params){
        def session=webUtilService.getSession();
        //SimpleDateFormat sdfDate=new SimpleDateFormat("yyyy-MM-dd");
        //SimpleDateFormat sdfTime=new SimpleDateFormat("HH:mm:ss");
        //取出用户ID
        //long userId=Number.toLong(session.userId);//用户ID
        long clientId=webUtilService.getClientId();
        if(clientId){
            //获取参数
            long orderId=Number.toLong(params.orderId);//订单号
            OrderInfo orderInfo=OrderInfo.findByIdAndClientId(orderId,clientId);
            if(orderInfo){
                //检查订单是否可以取消
                if(orderInfo.valid>OrderValid.EFFECTIVE_VALID.code||orderInfo.status>OrderStatus.ORDERED_STATUS.code){//订单不能取消
                    return [recode: ReCode.ORDER_CANNOT_CANCEL];
                }
                //取消订单
                orderInfo.valid=OrderValid.USER_CANCEL_VALID.code;
                if(!orderInfo.save(flush: true)){//保存更改失败
                    return [recode: ReCode.SAVE_FAILED];
                }
                //取消订单中点菜
                customerDishService.cancelDish(params);

                //调用消息服务创建消息
                def msgParams=[:];
                msgParams.orderId=orderInfo.id;
                msgParams.type=MessageType.ORDER_HANDLE_TYPE.code;
                msgParams.receiveId=orderInfo.listenWaiterId;
                msgParams.content="顾客已经取消订单id是"+orderInfo.id+"的订单，赶快点击去查看详情吧";
                msgParams.sendType=MsgSendType.CUSTOMER_TO_STAFF.code;
                msgParams.restaurantId=orderInfo.restaurantId;
                def reInfo=messageService.createMsg(msgParams);
                if(reInfo.recode!=ReCode.OK){
                    println("保存消息失败，但对于订单的产生没有致命影响，故忽略此错误，请系统管理员注意查证："+",reInfo="+reInfo);
                }

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

    /********************订单删除******
     *params是传入的参数
     * 参数格式为：
     * orderId=Number.toLong(params.orderId);//订单号
     *
     * 返回值
     * [recode: ReCode.ORDER_CANNOT_DELETE];订单不能删除
     * [recode: ReCode.ORDER_NOT_EXIST];订单不存在
     * [recode:ReCode.NOT_LOGIN];用户没有登录
     * [recode: ReCode.OK];成功
     * ****************/
    def delOrder(def params){
        def session=webUtilService.getSession();
        //SimpleDateFormat sdfDate=new SimpleDateFormat("yyyy-MM-dd");
        //SimpleDateFormat sdfTime=new SimpleDateFormat("HH:mm:ss");
        //取出用户ID
        //long userId=Number.toLong(session.userId);//用户ID
        long clientId=webUtilService.getClientId();
        if(clientId){
            //获取参数
            long orderId=Number.toLong(params.orderId);//订单号
            OrderInfo orderInfo=OrderInfo.findByIdAndClientId(orderId,clientId);
            if(orderInfo){
                //检查订单是否可以删除
                if(orderInfo.valid!=OrderValid.USER_CANCEL_VALID.code){//订单不能删除
                    return [recode: ReCode.ORDER_CANNOT_DELETE];
                }
                //删除订单中点菜
                customerDishService.delDish(params);
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

    /************************订单状态改变,这里主要是点菜完成******
     * params是传入的参数
     * 参数格式为：
     * orderId=Number.toLong(params.orderId);//订单号
     * statusCode=params.statusCode?:OrderStatus.ORDERED_STATUS.code;// 状态代码
     * //点菜列表参数,非必须
     * foodIds=12或foodIds=[112,231...]
     * counts=1或counts=[2,3...]
     * remarks=ddd或remarks=[dd,,dd...]
     *byWaiter是标示是否是服务员帮忙操作
     *
     * 返回值
     * [recode: ReCode.SAVE_FAILED];保存失败
     * [recode: ReCode.ORDER_NOT_EXIST];订单不存在
     * [recode:ReCode.NOT_LOGIN];用户没有登录
     * [recode: ReCode.OK];成功
     * ************************/
    def orderStatusUpdate(def params, boolean byWaiter ){
        def session=webUtilService.getSession();
        //SimpleDateFormat sdfDate=new SimpleDateFormat("yyyy-MM-dd");
        //SimpleDateFormat sdfTime=new SimpleDateFormat("HH:mm:ss");
        //取出用户ID
        //long userId=Number.toLong(session.userId);//用户ID
        long clientId=webUtilService.getClientId();
        if (byWaiter) {//如果是服务员帮助更新订单状态为点菜完成，则取服务员ID作为用户ID
            clientId = Number.toLong(session.staffId);
        }

        if(clientId){
            //获取参数
            long orderId=Number.toLong(params.orderId);//订单号
            int statusCode=params.statusCode?:OrderStatus.ORDERED_STATUS.code;// 状态代码

            //如果是点菜完成的话且传入了点菜列表，则先做点菜
            if(statusCode==OrderStatus.ORDERED_STATUS.code&&params.foodIds){
                def reObj=customerDishService.addDishes(params,false);
                if(reObj.recode!=ReCode.OK){//点菜没有顺利完成则返回
                    return reObj;
                }
            }

            OrderInfo orderInfo=OrderInfo.findById(orderId);
            if(orderInfo){
                if(orderInfo.status<statusCode){
                    orderInfo.status=statusCode;
                    if(!orderInfo.save(flush: true)){//保存数据库失败
                        return [recode: ReCode.SAVE_FAILED];
                    }
                    if(!byWaiter){
                        //调用消息服务创建消息
                        def msgParams=[:];
                        msgParams.orderId=orderInfo.id;
                        msgParams.type=MessageType.ORDER_HANDLE_TYPE.code;
                        msgParams.receiveId=orderInfo.listenWaiterId;
                        msgParams.content="订单id是"+orderInfo.id+"的订单状态已改为"+OrderStatus.getLable(orderInfo.status)+"，赶快点击去处理吧";
                        msgParams.sendType=MsgSendType.CUSTOMER_TO_STAFF.code;
                        msgParams.restaurantId=orderInfo.restaurantId;
                        def reInfo=messageService.createMsg(msgParams);
                        if(reInfo.recode!=ReCode.OK){
                            println("保存消息失败，但对于订单的产生没有致命影响，故忽略此错误，请系统管理员注意查证："+",reInfo="+reInfo);
                        }
                    }
                }
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

    /**********************
     *查询
     *************************/
     def orderList(def params,boolean byWaiter){
         def session=webUtilService.getSession();
         SimpleDateFormat sdfDate=new SimpleDateFormat("yyyy-MM-dd");
         //SimpleDateFormat sdfTime=new SimpleDateFormat("HH:mm:ss");
         //取出用户ID
         //long userId=Number.toLong(session.userId);//用户ID
         long clientId=webUtilService.getClientId();
         if(byWaiter){//如果是服务员帮助创建订单，则取服务员ID作为用户ID
             clientId=Number.toLong(session.staffId);
         }
         println("clientId-->"+clientId);
         if(clientId){
             //获取参数
             long orderId=Number.toLong(params.orderId);//订单ID
             long restaurantId=Number.toLong(params.restaurantId);//饭店ID
             long tableId=Number.toLong(params.tableId);//桌位ID
             String beginDateStr=params.beginDate;//开始日期
             Date beginDate=null;
             try{beginDate=sdfDate.parse(beginDateStr);}catch (Exception ex){}
             String endDateStr=params.endDate;//截止日期
             Date endDate=null;
             try{endDate=sdfDate.parse(endDateStr);}catch (Exception ex){}
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

             //如果工作人员查询的话必须有饭店ID
             if(byWaiter) {
//                 StaffInfo staffInfo=webUtilService.getStaff();
                 if(staffInfo)
                    restaurantId=staffInfo.restaurantId;
                 else
                    return [recode: ReCode.NOT_LOGIN] ;
             }
             def cIds=userService.getIds(ClientInfo.get(clientId));
             def condition={
                if(!byWaiter){ //非工作人员查询必须加上用户ID条件
                    'in'("clientId",cIds);
                }
                if(orderId){
                    eq("id",orderId);//id条件
                }
                if(restaurantId){
                    eq("restaurantId",restaurantId);//饭店ID条件
                }
                if(tableId){
                    eq("tableId",tableId);//桌位ID条件
                }
                if(beginDate){
                    ge("date",beginDate);//日期条件
                }
                 if(endDate){
                     le("date",endDate);//日期条件
                 }
                if(reserveType>=0){
                    eq("reserveType",reserveType);//用餐类别（早餐、午餐、晚餐）条件
                }
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
         }
         else{
             return [recode:ReCode.NOT_LOGIN];
         }
     }

    /********************订单完成点菜******
     *params是传入的参数
     * 参数格式为：
     * orderId=Number.toLong(params.orderId);//订单号
     * //点菜列表参数,非必须
     * foodIds=12或foodIds=[112,231...]
     * counts=1或counts=[2,3...]
     * remarks=ddd或remarks=[dd,,dd...]
     *byWaiter是标示是否是服务员帮忙操作
     *
     * 返回值
     * [recode: ReCode.ORDER_CANNOT_DELETE];订单不能删除
     * [recode: ReCode.ORDER_NOT_EXIST];订单不存在
     * [recode:ReCode.NOT_LOGIN];用户没有登录
     * [recode: ReCode.OK];成功
     * ****************/
    def completeDish(def params,boolean byWaiter){
        def session=webUtilService.getSession();
        //SimpleDateFormat sdfDate=new SimpleDateFormat("yyyy-MM-dd");
        //SimpleDateFormat sdfTime=new SimpleDateFormat("HH:mm:ss");
        //取出用户ID
        //long userId=Number.toLong(session.userId);//用户ID
        long clientId=webUtilService.getClientId();
        if (byWaiter) {//如果是服务员帮助更新订单状态为点菜完成，则取服务员ID作为用户ID
            clientId = Number.toLong(session.staffId);
        }
        if(clientId){
            //获取参数
            long orderId=Number.toLong(params.orderId);//订单号
            OrderInfo orderInfo=null;
            if(byWaiter){
                orderInfo=OrderInfo.findById(orderId);
            }
            else{
                orderInfo=OrderInfo.findByIdAndClientId(orderId,clientId);
            }
            if(orderInfo){
                //检查订单是否可以做完成点菜操作
                if(orderInfo.valid>OrderValid.EFFECTIVE_VALID.code||orderInfo.status>OrderStatus.ORIGINAL_STATUS.code){//订单不能完成点菜
                    return [recode: ReCode.ORDER_CANNOT_COMPLETE_DISH];
                }
                //完成点菜
                //statusCode=params.statusCode?:OrderStatus.ORDERED_STATUS.code;// 状态代码
                params.statusCode= OrderStatus.ORDERED_STATUS.code;
                return orderStatusUpdate(params,byWaiter);
            }
            else{
                return [recode: ReCode.ORDER_NOT_EXIST];
            }
        }
        else{
            return [recode:ReCode.NOT_LOGIN];
        }
    }

    //订单详情
    def orderInfo(def params,boolean byWaiter){
        def session=webUtilService.getSession();
        //SimpleDateFormat sdfDate=new SimpleDateFormat("yyyy-MM-dd");
        //SimpleDateFormat sdfTime=new SimpleDateFormat("HH:mm:ss");

        //获取参数
        long orderId=Number.toLong(params.orderId);//订单号
        //long userId=Number.toLong(session.userId);//用户ID
        long clientId=webUtilService.getClientId();
        String partakeCode = params.partakeCode;//点菜参与码
        if(!partakeCode){ //没有点菜验证码则需要验证用户是否登录
            if (byWaiter) {//如果是服务员帮助，则取服务员ID作为用户ID
                clientId = Number.toLong(session.staffId);
                if (clientId == 0) { //没登录
                    return [recode: ReCode.NOT_LOGIN];
                }
            }
        }
        OrderInfo orderInfo=OrderInfo.findById(orderId);
        if(orderInfo){
            //点菜参与码是否正确
            if (clientId == orderInfo.clientId || byWaiter) {//用户登录切是订单创建的用户或者是服务员帮忙点菜，则不需要参与验证码

            } else {//检查点菜参与验证码是否正确
                if (partakeCode != orderInfo.partakeCode) { //点菜参与码不正确
                    return [recode: ReCode.ERROR_PARTAKECODE];
                }
            }
            return [recode: ReCode.OK,orderInfoInstance:orderInfo];
        }
        else{
            return [recode: ReCode.ORDER_NOT_EXIST];
        }
    }


}
