package lj.taos.order

import grails.converters.JSON
import lj.common.StrCheckUtil
import lj.data.OrderInfo
import lj.enumCustom.DishesStatus
import lj.enumCustom.OrderStatus
import lj.enumCustom.OrderValid
import lj.enumCustom.ReCode
import lj.taos.order.staff.StaffOrderService
import lj.taos.shop.SearchService

import java.text.SimpleDateFormat

class StaffAjaxController {
    StaffOrderService staffOrderService;
    //StaffDishService staffDishService;
    SearchService searchService;
   // CustomerAppraiseService customerAppraiseService;
    //StaffCommonService staffCommonService;
    //StaffCartService staffCartService;

//创建订单
    def createOrder(){
        def reInfo=staffOrderService.createOrder(params);
        if(reInfo.recode!=ReCode.OK){
            if(reInfo.recode==ReCode.SAVE_FAILED){
                reInfo.errors=reInfo.errors;
            }
        }
        println("reInfo-->"+reInfo);
        render(reInfo as JSON);
    }
    //点菜界面数据
    def dataOfDish(){
        //查询订单信息
        OrderInfo orderInfoInstance=null;
        def reInfo=staffOrderService.orderInfo(params);
        if(reInfo.recode==ReCode.OK){
            orderInfoInstance=reInfo.orderInfoInstance;
            //判断订单当前状态是否能点菜
            if (orderInfoInstance.status >= OrderStatus.SERVED_STATUS.code||orderInfoInstance.valid>=OrderValid.USER_CANCEL_VALID.code) {//上菜完成不能再点菜了
                reInfo= [recode: ReCode.CANNOT_DISH];//不能点菜
                render(reInfo as JSON);
                return;
            }
            //查询菜谱
            def paramsT=[enabled:true,offset:params.offset,max:params.max];
            reInfo=searchService.searchFood(paramsT);
            reInfo<<[orderInfoInstance:orderInfoInstance];
        }
        render(reInfo as JSON);
    }
    //点菜
    def addDishes(){
        def reInfo=staffOrderService.addDishes(params);
        println("reInfo-->"+reInfo);
        render(reInfo as JSON);
    }

//    //预定桌位
//    def reserveTable(){
//        //获取参数
//        println("params-->"+params);
//        def restaurantId=params.restaurantId;
//        def date=params.date;
//        def reserveType=params.reserveType;
//        def tableId=params.tableId;
//        def phone=params.phone as String;
//
//        def reInfo=[:];
//        if(!restaurantId){
//            reInfo=[recode: ReCode.NO_RESERVE_RESTAURANT];
//            render(reInfo as JSON);
//            return ;
//        }
//        if(!date){
//            reInfo=[recode: ReCode.NO_RESERVE_DATE];
//            render(reInfo as JSON);
//            return ;
//        }
//        if(!reserveType){
//            reInfo=[recode: ReCode.NO_RESERVE_TYPE];
//            render(reInfo as JSON);
//            return ;
//        }
//        if(!phone){
//            reInfo=[recode: ReCode.NO_RIGHT_PHONE];
//            render(reInfo as JSON);
//            return ;
//        }else if(!StrCheckUtil.chkStrFormat(phone,"phone")){
//            reInfo=[recode: ReCode.NO_RIGHT_PHONE];
//            render(reInfo as JSON);
//            return ;
//        }
//        def errorList=[];
//        def okList=[];
//        params.remark="联系电话："+phone;//将联系电话写入备注信息中
//        if(tableId instanceof String){//预定了一个桌位
//            reInfo=staffOrderService.createOrder(params);
//            if(reInfo.recode!=ReCode.OK){
//                errorList.add([tableId:tableId,reInfo:reInfo]);
//            }
//            else{
//                okList.add([tableId:tableId])
//            }
//        }else if(tableId instanceof String[]){//预定了多个桌位
//            def paramsT=params.clone();
//            paramsT.remove("tableId");
//            for(int i=0;i<tableId.length;i++){
//                paramsT.tableId=tableId[i];
//                reInfo=staffOrderService.createOrder(paramsT);
//                if(reInfo.recode!=ReCode.OK){
//                    errorList.add([tableId:tableId[i]]<<reInfo);
//                }
//                else{
//                    okList.add([tableId:tableId[i]]);
//                }
//            }
//        }
//        else{//没有预定任何桌位
//            reInfo=[recode: ReCode.NO_RESERVE_TABLE];
//            render(reInfo as JSON);
//            return ;
//        }
//        //
//        if(errorList!=null&&errorList.size()>0){
//            for(int i=0;i<errorList.size();i++){
//                def reInfoTemp=errorList.get(i);
//                if(reInfoTemp.recode==ReCode.SAVE_FAILED){
//                    reInfoTemp=[tableId:reInfoTemp.tableId,recode:[code:ReCode.SAVE_FAILED.code,label:I118Error.getMessage(g,reInfoTemp.errors,0)]];
//                }
//            }
//            reInfo=[recode: ReCode.OK,errorList:errorList,okList:okList];
//        }
//        else{
//            //预定成功，跳转到订单详情页面
//            reInfo=[recode: ReCode.OK];
//        }
//        println("reInfo1-->"+reInfo);
//        render(reInfo as JSON);
//    }
    //订单列表
    def orderList(){
        def reInfo=staffOrderService.orderList(params);
        println("reInfo-->"+reInfo);
        render(reInfo as JSON);
    }
    //订单详细信息
    def orderShow(){
        //staffOrderService.castAccounts(params);//算下账，方便获取算账金额
        OrderInfo orderInfoInstance=null;
        def reInfo=staffOrderService.orderInfo(params);
        if(reInfo.recode==ReCode.OK){
            orderInfoInstance=reInfo.orderInfoInstance;
            //查询点菜
            def reInfo1=staffOrderService.dishList(params);
//            //查询评价
//            def reInfo2=customerAppraiseService.appraiseInfo(params,true);

            reInfo1<<[orderInfoInstance:orderInfoInstance]<<[params:params];
            reInfo=reInfo1;
        }
        //reInfo<<[orderInfoInstance:orderInfoInstance]<<[params:params];
        println("reInfo--->"+reInfo);
        render(reInfo as JSON);
    }

    //完成点菜
    def completeDish(){
        def reInfo=staffOrderService.completeDish(params);
        println("reInfo-->"+reInfo);
        render(reInfo as JSON);
    }
    //点菜取消
    def cancelDish(){
//        def orderId=params.orderId;
//        params.remove("orderId");
        def reInfo=staffOrderService.cancelDish(params);
        println("reInfo-->"+reInfo);
        render(reInfo as JSON);
    }
    //点菜删除
    def delDish(){
//        def orderId=params.orderId;
//        params.remove("orderId");
        def reInfo=staffOrderService.delDish(params);
        println("reInfo-->"+reInfo);
        render(reInfo as JSON);
    }
//    //取消并删除点菜
//    def cancelAndDelDish(){
//        def reInfo=null;
//        try{
//           reInfo=staffOrderService.cancelDish(params);
//        println("reInfo-->"+reInfo);
//        if(reInfo.recode!=ReCode.OK){
//            render(reInfo as JSON);
//            return;
//        }
//        reInfo=staffOrderService.delDish(params);
//        println("reInfo-->"+reInfo);
//        render(reInfo as JSON);
//        }catch (Exception ex){
//            println("ex-->"+ex.getMessage());
//            reInfo=[code:-5,label:ex.getMessage()];
//            render(reInfo as JSON);
//        }
//    }
    //订单取消
    def cancelOrder(){
        def reInfo=staffOrderService.orderCancel(params);
        println("reInfo-->"+reInfo);
        render(reInfo as JSON);
    }
    //订单删除
    def delOrder(){
        def reInfo=staffOrderService.delOrder(params);
        println("reInfo-->"+reInfo);
        render(reInfo as JSON);
    }
    //确认订单有效性
    def affirmValid(){
        def reInfo=staffOrderService.orderValidAffirm(params);
        println("reInfo-->"+reInfo);
        render(reInfo as JSON);
    }
    //确认点菜完成
    def completeAffirmDish(){
        params.statusCode=OrderStatus.VERIFY_ORDERED_STATUS.code;//更新订单的状态为确认点菜完成
//        def orderId=params.orderId;
        def reInfo=staffOrderService.orderStatusUpdate(params);
        println("reInfo-->"+reInfo);
        render(reInfo as JSON);
    }
    //算账
    def getAccounts(){
        def reInfo=staffOrderService.castAccounts(params);
        println("reInfo-->"+reInfo);
        render(reInfo as JSON);
        return ;
    }
    //结账
    def settleAccounts(){
//        flash.message=null;
//        flash.error=null;
//        def reInfo=null;
//        if(request.method=="GET"){
//            reInfo=staffOrderService.castAccounts(params);
//            render(view: "settleAccounts",model: reInfo);
//        }
        if(request.method=="POST"){//提交算账
            def reInfo=staffOrderService.submitCastAccounts(params);
            println("reInfo-->"+reInfo);
            render(reInfo as JSON);
            return ;
        }


    }
    //确认点菜
    def affirmDish(){
//        def orderId=params.orderId;
//        params.remove("orderId");
        def reInfo=staffOrderService.dishConfirm(params);
        println("reInfo-->"+reInfo);
        render(reInfo as JSON);
    }
    //完成上菜,对于每个点菜的完成上菜
    def completeServe(){
        params.statusCode=DishesStatus.SERVED_STATUS.code;//更新点菜的状态为上菜完成
//        def orderId=params.orderId;
//        params.remove("orderId");
        def reInfo=staffOrderService.dishStatusUpdate(params);
        println("reInfo-->"+reInfo);
        render(reInfo as JSON);
    }
    //开始做菜
    def beginCook(){
        params.statusCode=DishesStatus.COOKING_ORDERED_STATUS.code;//更新点菜的状态为做菜中
//        def orderId=params.orderId;
//        params.remove("orderId");
        def reInfo=staffOrderService.dishStatusUpdate(params);
        println("reInfo-->"+reInfo);
        render(reInfo as JSON);
    }
    //完成做菜
    def completeCook(){
        params.statusCode=DishesStatus.COOKED_STATUS.code;//更新点菜的状态为做菜完成
//        def orderId=params.orderId;
//        params.remove("orderId");
        def reInfo=staffOrderService.dishStatusUpdate(params);
        println("reInfo-->"+reInfo);
        render(reInfo as JSON);
    }
    //点菜列表
    def dishList(){ //厨师显示所有状态是1有效性是1的点菜
        params.statusGe=1;
        params.statusLe=2;
        params.valid=1;
        //params.date=new SimpleDateFormat("yyyy-MM-dd").format(new Date());//加上当天日期条件
        def reInfo=staffOrderService.dishList(params);
        println("reInfo-->"+reInfo);
        render(reInfo as JSON);
    }

//    //获取地址信息
//    def getAddress(){
//       def reInfo=staffCommonService.getAddress(params);
//        println("reInfo-->"+reInfo);
//        render(reInfo as JSON);
//    }

//    //***********************餐车相关*****************************
//    //添加食品到购餐车中
//    def addFoodToCart(){
//        def reInfo=[recode:ReCode.OTHER_ERROR];
//        try {
//            reInfo=staffCartService.addFoodToCart(params);
//        }catch (Exception ex){
//            reInfo=[recode:ReCode.OTHER_ERROR];
//            reInfo.recode.label=ex.message;
//        }
//        if(reInfo.recode!=ReCode.OK){
//            if(reInfo.recode==ReCode.SAVE_FAILED){
//                reInfo.recode.label=I118Error.getMessage(g,reInfo.errors,0);
//            }
//        }
//        println("reInfo-->"+reInfo);
//        render(reInfo as JSON);
//    }
//    //获取已经创建的购餐车对象列表和其中的点菜
//    def getCartsAndDishes(){
//        def reInfo=staffCartService.getCartsAndDishes();
//        println("reInfo-->"+reInfo);
//        render(reInfo as JSON);
//    }
//    //从购餐车中删除菜品
//    def delDishFromCart(){
//        def reInfo=staffCartService.delDish(params);
//        println("reInfo-->"+reInfo);
//        render(reInfo as JSON);
//    }
//    //更新餐车中菜品的数量
//    def updateDishOfCart(){
//        def reInfo=staffCartService.updateDish(params);
//        println("reInfo-->"+reInfo);
//        render(reInfo as JSON);
//    }


    //加菜
    def addDishAfterOrderConfirmAjax() {
        def reInfo =null;
        try {
            reInfo = staffOrderService.addDishAfterOrderConfirm(params);
        } catch (Exception ex){
            reInfo=[recode: [code:-1,label:ex.message]];
        }
        println("reInfo-->" + reInfo);
        render(reInfo as JSON);
    }
    //删除加菜
    def delDishAfterOrderConfirmAjax() {
        def reInfo = staffOrderService.delDishAfterOrderConfirm(params);
        println("reInfo-->" + reInfo);
        render(reInfo as JSON);
    }
}
