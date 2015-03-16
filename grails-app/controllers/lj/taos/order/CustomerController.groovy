package lj.taos.order

import grails.converters.JSON
import lj.data.OrderInfo
import lj.enumCustom.OrderStatus
import lj.enumCustom.ReCode

class CustomerController {
    def customerOrderService;
    def searchService;

    def index() {}

    def getOrCreateOrder() {
        def errors = null;
        def msgs = null;
        def reInfo = customerOrderService.getOrCreateOrder(params);
        if (reInfo.recode != ReCode.OK) {
            boolean isNeedPartakeCode = false;
            if (reInfo.recode == ReCode.SAVE_FAILED) {
                errors = reInfo.errors;
            } else {
                errors = reInfo.recode.label;
                if (reInfo.recode == ReCode.NO_PARTAKECODE || reInfo.recode == ReCode.WRONG_PARTAKECODE) {
                    isNeedPartakeCode = true;
                }
            }
            render(view: "originalStep", model: [errors: errors, msgs: msgs, isNeedPartakeCode: isNeedPartakeCode]);
            return;
        }
        int orderStatus = reInfo.orderInfo.status;
        if (orderStatus == OrderStatus.ORIGINAL_STATUS.code) {//初始状态，加载点菜界面
            //查询菜品
            params << [enabled: true];
            def reInfo1 = searchService.searchFood(params);
            reInfo << reInfo1;
            //查询菜品分类
            reInfo1 = searchService.searchFoodClass();
            reInfo << reInfo1;

            //查询已有点菜
            reInfo1 = customerOrderService.dishList([orderId: reInfo.orderInfo?.id, max: 1000]);
            reInfo << [dishes: reInfo1];

            println("reInfo-->" + reInfo);
            println("orderInfo-->" + reInfo.orderInfo);
            render(view: "orderDish", model: reInfo);
            return;
        } else if (orderStatus == OrderStatus.ORDERED_STATUS.code) {//点菜完成状态，加载点菜确认界面
            //查询已有点菜
            def reInfo1 = customerOrderService.dishList([orderId: reInfo.orderInfo?.id, max: 1000]);
            reInfo << [dishes: reInfo1];

            println("reInfo-->" + reInfo);
            println("orderInfo-->" + reInfo.orderInfo);
            render(view: "orderConfirm", model: reInfo);
            return;
//        }else if(orderStatus==OrderStatus.VERIFY_ORDERED_STATUS.code){//点菜确认完成状态，加载加菜界面

        } else {//其他状态，显示订单信息界面
            //查询菜品
            params << [enabled: true];
            def reInfo1 = searchService.searchFood(params);
            reInfo << reInfo1;
            //查询菜品分类
            reInfo1 = searchService.searchFoodClass();
            reInfo << reInfo1;

            //查询已有点菜
            reInfo1 = customerOrderService.dishList([orderId: reInfo.orderInfo?.id, max: 1000]);
            reInfo << [dishes: reInfo1];

            println("reInfo-->" + reInfo);
            println("orderInfo-->" + reInfo.orderInfo);
            render(view: "orderInfo", model: reInfo);
            return;
        }


    }

    //点菜
    def addDishesAjax() {
        def reInfo = customerOrderService.addDishes(params);
        println("reInfo-->" + reInfo);
        render(reInfo as JSON);
    }

    //完成点菜
    def completeDish() {
        def reInfo = customerOrderService.completeDish(params);
        if (reInfo.recode != ReCode.OK) {
            flash.errors = reInfo.recode.label;
            if (reInfo.recode == ReCode.SAVE_FAILED) {
                flash.errors = reInfo.errors;
            }
        } else {
            flash.message = reInfo.recode.label;
        }
        redirect(action: "getOrCreateOrder", params: params);
    }
    //退回点菜
    def backToDish() {
        def reInfo = customerOrderService.backToDish(params);
        if (reInfo.recode != ReCode.OK) {
            flash.errors = reInfo.recode.label;
            if (reInfo.recode == ReCode.SAVE_FAILED) {
                flash.errors = reInfo.errors;
            }
        } else {
            flash.message = reInfo.recode.label;
        }
        redirect(action: "getOrCreateOrder", params: params);
    }
    //删除点菜
    def delDishesAjax() {
        def reInfo = customerOrderService.delDish(params);
        println("reInfo-->" + reInfo);
        render(reInfo as JSON);
    }
    //确认点菜
    def orderConfirm() {
        def reInfo = customerOrderService.orderConfirm(params);
        if (reInfo.recode != ReCode.OK) {
            flash.errors = reInfo.recode.label;
            if (reInfo.recode == ReCode.SAVE_FAILED) {
                flash.errors = reInfo.errors;
            }
        } else {
            flash.message = reInfo.recode.label;
        }
        redirect(action: "getOrCreateOrder", params: params);
    }
    //订单取消
    def orderCancel() {
        def errors;
        def msgs;
        def reInfo = customerOrderService.cancelOrder(params);
        if (reInfo.recode != ReCode.OK) {
            flash.errors = reInfo.recode.label;
            if (reInfo.recode == ReCode.SAVE_FAILED) {
                flash.errors = reInfo.errors;
            }
            redirect(action: "getOrCreateOrder", params: params);
            return;
        } else {
            msgs = reInfo.recode.label;
        }
        render(view: "originalStep", model: [msgs: msgs]);
    }

    //加菜
    def addDishAfterOrderConfirmView() {
        def errors = null;
        def msgs = null;
        def reInfo = customerOrderService.dishListAfterOrderConfirm(params);
        if (reInfo.recode != ReCode.OK) {
            if (reInfo.recode == ReCode.SAVE_FAILED) {
                errors = reInfo.errors;
            } else {
                errors = reInfo.recode.label;
            }
            render(view: "addDishAfterOrderConfirm", model: [errors: errors, msgs: msgs]);
            return;
        }
        //查询菜品
        params << [enabled: true];
        def reInfo1 = searchService.searchFood(params);
        reInfo << reInfo1;
        //查询菜品分类
        reInfo1 = searchService.searchFoodClass();
        reInfo << reInfo1;
        println("reInfo-->" + reInfo);
        println("orderInfo-->" + reInfo.orderInfo);
        render(view: "addDishAfterOrderConfirm", model: reInfo);
    }

    //加菜
    def addDishAfterOrderConfirmAjax() {
        def reInfo =null;
        try {
            reInfo = customerOrderService.addDishAfterOrderConfirm(params);
        } catch (Exception ex){
           reInfo=[recode: [code:-1,label:ex.message]];
        }
        println("reInfo-->" + reInfo);
        render(reInfo as JSON);
    }
    //删除加菜
    def delDishAfterOrderConfirmAjax() {
        def reInfo = customerOrderService.delDishAfterOrderConfirm(params);
        println("reInfo-->" + reInfo);
        render(reInfo as JSON);
    }
    //加菜确认
    def orderConfirmAfterOrderConfirm() {
        def errors = null;
        def msgs = null;
        def reInfo = customerOrderService.orderConfirmAfterOrderConfirm(params);
        println("reInfo-->" + reInfo);
        if (reInfo.recode != ReCode.OK) {
            if (reInfo.recode == ReCode.SAVE_FAILED) {
                flash.errors = reInfo.errors;
            } else {
                flash.errors = reInfo.recode.label;
            }
            //render(view: "addDishAfterOrderConfirm", model: [errors: errors, msgs: msgs]);
            redirect(action: "addDishAfterOrderConfirmView",params: [orderId: params.orderId]);
            return;
        }else{
            flash.message=reInfo.recode.label;
            redirect(action: "getOrCreateOrder");
            return;
        }
    }
}
