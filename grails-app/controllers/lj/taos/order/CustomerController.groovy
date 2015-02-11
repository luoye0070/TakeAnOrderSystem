package lj.taos.order

import lj.enumCustom.OrderStatus
import lj.enumCustom.ReCode

class CustomerController {
     def customerOrderService;
    def searchService;
    def index() {}
    def getOrCreateOrder(){
        def errors=null;
        def msgs=null;
        def reInfo=customerOrderService.getOrCreateOrder(params);
        if (reInfo.recode!=ReCode.OK){
            if(reInfo.recode==ReCode.SAVE_FAILED){
                 errors=reInfo.errors;

            } else {
               errors=reInfo.recode.label;
            }
            render(view: "originalStep",model: [errors:errors,msgs:msgs]);
            return ;
        }
        int orderStatus=reInfo.orderInfo.status;
        if(orderStatus==OrderStatus.ORIGINAL_STATUS.code){//初始状态，加载点菜界面
            //查询菜品
            params<<[enabled:true];
            def reInfo1=searchService.searchFood(params);
            reInfo<<reInfo1;
            //查询菜品分类
            reInfo1=searchService.searchFoodClass();
            reInfo<<reInfo1;
            render(view: "orderDish",model: reInfo);
            return;
        }else if(orderStatus==OrderStatus.ORDERED_STATUS.code){//点菜完成状态，加载点菜确认界面

//        }else if(orderStatus==OrderStatus.VERIFY_ORDERED_STATUS.code){//点菜确认完成状态，加载加菜界面

        }else{//其他状态，显示订单信息界面

        }


    }
}
