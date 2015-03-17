package lj.taos.order

import lj.enumCustom.ReCode

class ReserveCustomerController {
    def customerReserveOrderService;
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
        def reInfo=customerReserveOrderService.getReserveTable(params);
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
       // render(view: "reserveTables");
        redirect(action: "reserveTables",params: params);
    }
    //预定第四步，点菜
    def dishOfReserveOrder(){

    }
    //点菜方法,ajax方法
    def dishAjax(){

    }
    //删除点菜,ajax方法
    def delDishAjax(){

    }
}
