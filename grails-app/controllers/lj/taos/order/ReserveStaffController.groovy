package lj.taos.order

import grails.converters.JSON
import lj.enumCustom.OrderValid
import lj.enumCustom.ReCode
import lj.enumCustom.ReserveOrderStatus

class ReserveStaffController {
    def staffReserveOrderService;
    def searchService;
    def index() {
        redirect(action: "reserveDinnerTimeInput");
    }
    //预定第一步，输入预定时间
    def reserveDinnerTimeInput(){
        def errors = null;
        def msgs = null;
        render(view: "reserveDinnerTimeInput");
    }
    //预定第二步，查询出可用桌位
    def reserveTables(){
        def errors = null;
        def msgs = null;
        def reInfo=staffReserveOrderService.getReserveTable(params);
        if(reInfo.recode!=ReCode.OK){
            flash.errors=reInfo.recode.label;
            redirect(action: "reserveDinnerTimeInput",params: params);
            return;
        }
        println("reInfo->"+reInfo);
        render(view: "reserveTables",model: reInfo);
    }
    //预定第三步，创建预定订单
    def createReserveOrder(){
        println("params->"+params);
        def reInfo= staffReserveOrderService.createReserveOrder(params);
        if(reInfo.recode==ReCode.OK){ //跳转到点菜界面
            flash.message=reInfo.recode.label;
            redirect(action: "dishOfReserveOrder",params: [reserveOrderId:reInfo.reserveOrderInfo.id]);
        }else{//跳转到桌位选择界面
            if(reInfo.recode==ReCode.SAVE_FAILED){
                flash.errors=reInfo.errors;
            }
            else{
                flash.errors=reInfo.recode.label;
            }
            redirect(action: "reserveTables",params: params);
        }
       // render(view: "reserveTables");

    }
    //预定第四步，点菜
    def dishOfReserveOrder(){
        def errors = null;
        def msgs = null;
        //查询出相应预定订单
        def reInfo=staffReserveOrderService.getReserveOrderInfo(params);
        if(reInfo.recode!=ReCode.OK){
            errors=reInfo.recode.label;
            render(view: "dishOfReserveOrder",model: [errors:errors]);
            return;
        }
        if(reInfo.reserveOrderInfo.status==ReserveOrderStatus.REACHED_STATUS.code||reInfo.reserveOrderInfo.valid>=OrderValid.USER_CANCEL_VALID.code){ //已到店预定订单，不能再点菜了
            errors="取消订单和已到店订单，不能点菜了";
            render(view: "dishOfReserveOrder",model: [errors:errors]);
            return;
        }
        //查询出菜品和菜品类别
        //查询菜品
        params << [enabled: true];
        def reInfo1 = searchService.searchFood(params);
        reInfo << reInfo1;
        //查询菜品分类
        reInfo1 = searchService.searchFoodClass();
        reInfo << reInfo1;
        println("reInfo-->" + reInfo);
        println("orderInfo-->" + reInfo.orderInfo);
        render(view: "dishOfReserveOrder", model: reInfo);
    }
    //点菜方法,ajax方法
    def dishAjax(){
        try{
        def reInfo = staffReserveOrderService.addDish(params);
        println("reInfo-->" + reInfo);
        render(reInfo as JSON);
        }catch (Exception ex){
            def reInfo=[recode: [code:-39,label:ex.message]];
            render(reInfo as JSON);
        }
    }
    //删除点菜,ajax方法
    def delDishAjax(){
        def reInfo = staffReserveOrderService.delDish(params);
        println("reInfo-->" + reInfo);
        render(reInfo as JSON);
    }


    //预定订单列表
    def reserveOrderList(){
          def reInfo=staffReserveOrderService.reserveOrderList(params);
        println("reInfo-->" + reInfo);
        render(view: "reserveOrderList", model: reInfo);
    }
    //预定订单详情
    def reserveOrderDetail(){
        def reInfo=staffReserveOrderService.getReserveOrderInfo(params);
        println("reInfo-->" + reInfo);
        render(view: "reserveOrderDetail", model: reInfo);
    }
    //预定订单取消
    def reserveOrderCancel(){
        def reInfo=staffReserveOrderService.reserveOrderCancel(params);
        println("reInfo-->" + reInfo);
        if(params.backUrl){
            redirect(url: params.backUrl);
            return;
        }
        redirect(action: "reserveOrderList");
    }
    //顾客到店
    def reserveOrderReach(){
        try {
            def reInfo=staffReserveOrderService.reserveOrderReach(params);
            println("reInfo-->" + reInfo);
        }
         catch (Exception ex){
             println("reInfo-->" + ex.message);
         }
        String backUrl=params.backUrl;
        if(backUrl){
            redirect(url:backUrl);
            return;
        }
        redirect(action: "reserveOrderList");
    }

}
