package lj.taos.order

import grails.converters.JSON
import lj.common.StrCheckUtil
import lj.data.OrderInfo
import lj.data.StaffInfo
import lj.enumCustom.DishesStatus
import lj.enumCustom.OrderStatus
import lj.enumCustom.ReCode
import lj.taos.order.staff.StaffOrderService
import lj.taos.shop.SearchService
import lj.util.WebUtilService

import java.text.SimpleDateFormat

class StaffController {
    StaffOrderService staffOrderService;
    SearchService searchService;
//    CustomerAppraiseService customerAppraiseService;
    WebUtilService webUtilService;
    def jasperReportService
    def staffCartService;
//    CommonService commonService;
    def orderAndReserveService;
    def index() {//根据不同的职位跳转到不同界面
        redirect(action: "orderList");
    }

//    //预定的桌位列表
//    def tablesShow(){
//        //获取参数
////        String dateStr=params.date;//用餐日期
////        int reserveType=params.reserveType;//预定类型
////        long restaurantId=params.restaurantId;//饭店ID
//        def err=null;
//        def msg=null;
//        if(!params.date){
//            params.date=lj.FormatUtil.dateFormat(new Date());
//        }
//        if(!params.reserveType){
//            params.reserveType="1";
//        }
//        StaffInfo staffInfo=webUtilService.getStaff();
//        if(staffInfo){
//            params.restaurantId=staffInfo.restaurantId;
//        }
//        else{
//            params.restaurantId=-1;
//        }
//        params.enabled=true;
//        params.canReserve=true;
//        def tableId=params.tableId;
//        params.remove("tableId");
//        println("params-->"+params);
//        def reInfo=searchService.searchTable(params);
//        println("reInfo-->"+reInfo);
//        params.tableId=tableId;
//        reInfo<<[params:params];
//        render(view:"tablesShow",model: reInfo);
//
//    }

    //到店吃饭创建订单输入界面
    def orderInput() {
        //标注过期订单
        orderAndReserveService.markExpire();
        //查询出相应的桌位
        StaffInfo staffInfo = webUtilService.getStaff();
        params.enabled = true;
        params.canReserve = true;
        def tableId = params.tableId;
        params.remove("tableId");
        println("params-->" + params);
        def reInfo = searchService.searchTable(params);
        println("reInfo-->" + reInfo);
        params.tableId = tableId;
        reInfo << [params: params];
        render(view: webUtilService.getView("orderInput"), model: reInfo);
    }

//    //预定桌位
//    def reserveTable(){
//        //获取参数
//        println("params-->"+params);
//        StaffInfo staffInfo=webUtilService.getStaff();
//        if(staffInfo){
//            params.restaurantId=staffInfo.restaurantId;
//        }
//        def restaurantId=params.restaurantId;
//        def date=params.date;
//        def reserveType=params.reserveType;
//        def tableId=params.tableId;
//        def phone=params.phone as String;
//
//        if(!restaurantId){
//            flash.message="请选择饭店";
//            redirect(controller: "staff",action: "tablesShow",params: params);
//            return ;
//        }
//        if(!date){
//            flash.message="请选择预定日期";
//            redirect(controller: "staff",action: "tablesShow",params: params);
//            return ;
//        }
//        if(!reserveType){
//            flash.message="请选择预定的类型";
//            redirect(controller: "staff",action: "tablesShow",params: params);
//            return ;
//        }
//        if(!phone){
//            flash.message="请填写联系电话";
//            redirect(controller: "staff",action: "tablesShow",params: params);
//            return ;
//        }else if(!StrCheckUtil.chkStrFormat(phone,"phone")){
//            flash.message="填写的联系电话格式不正确";
//            redirect(controller: "staff",action: "tablesShow",params: params);
//            return ;
//        }
//
//        def reInfo=null;
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
//                    errorList.add([tableId:tableId[i],reInfo:reInfo]);
//                }
//                else{
//                    okList.add([tableId:tableId[i]]);
//                }
//            }
//        }
//        else{//没有预定任何桌位
//            flash.message="请选择预定的桌位";
//            redirect(controller: "staff",action: "tablesShow",params: params);
//            return ;
//        }
//
//        //返回桌位展示页
//        if(errorList!=null&&errorList.size()>0){
//            flash.message="";
//            for(int i=0;i<okList.size();i++){
//                flash.message+="桌位:"+okList.get(i).tableId+"，预定成功<br/>";
//            }
//
//            flash.error="";
//            for(int i=0;i<errorList.size();i++){
//                reInfo=errorList.get(i).reInfo;
//                flash.error+="桌位:"+errorList.get(i).tableId+"，预定不成功，失败原因是：";
//                if(reInfo.recode==ReCode.SAVE_FAILED){
//                    flash.error+= I118Error.getMessage(g,reInfo.errors,0)+"<br/>";
//                }
//                else{
//                    flash.error+=reInfo.recode.label+"<br/>";
//                }
//            }
//            redirect(controller: "staff",action: "tablesShow",params: params);
//
//        }
//        else{
//            //预定成功，跳转到订单详情页面
//            redirect(action: "orderList",params: [orderId:reInfo.orderInfo.id]);
//        }
//    }

    //创建订单
    def createOrder() {
        def reInfo = staffOrderService.createOrder(params);
        if (reInfo.recode == ReCode.OK) {
            //预定成功，跳转到订单详情页面
            redirect(action: "orderShow", params: [orderId: reInfo.orderInfo.id]);
        } else {//返回桌位展示页
            if (reInfo.recode == ReCode.SAVE_FAILED) {
                flash.errors = reInfo.errors;
            } else {
                flash.errors = reInfo.recode.label;
            }

            redirect(controller: "staff", action: "orderInput", params: params);
        }
    }
    //订单取消
    def cancelOrder() {
        def reInfo = staffOrderService.orderCancel(params);
        println("reInfo-->" + reInfo);
        //render(reInfo);
        if (reInfo.recode == ReCode.OK) {
            flash.message = reInfo.recode.label;
        } else {
            flash.errors = reInfo.recode.label;
        }
        if (params.backUrl) {
            redirect(url: params.backUrl);
            return;
        }
        redirect(action: "orderList");
    }
    //订单列表
    def orderList() {
        def reInfo = staffOrderService.orderList(params);
        println("reInfo-->" + reInfo);
        if(webUtilService.isMobileRequest()&&params.beginTime==null){//手机界面的特殊处理
            reInfo.orderList=null;
            reInfo<<[firstLoad:true];
        }
        render(view: webUtilService.getView("orderList"), model: reInfo);
    }
    //订单删除
    def delOrder() {
        try {
            def reInfo = staffOrderService.delOrder(params);
            println("reInfo-->" + reInfo);
            //render(reInfo);
            if (reInfo.recode == ReCode.OK) {
                flash.message = reInfo.recode.label;
            } else {
                flash.errors = reInfo.recode.label;
            }
        }
        catch (Exception ex) {
            flash.errors = ex.message;
        }
        if (params.backUrl) {
            redirect(url: params.backUrl);
            return;
        }
        redirect(action: "orderList");
    }
    //订单详细信息
    def orderShow() {

        OrderInfo orderInfoInstance = null;
        def reInfo = staffOrderService.orderInfo(params);
        if (reInfo.recode == ReCode.OK) {
            orderInfoInstance = reInfo.orderInfoInstance;
            //查询点菜
            def reInfo1 = staffOrderService.dishList(params);
//            //查询评价
//            def reInfo2=customerAppraiseService.appraiseInfo(params,true);

            reInfo1 << [orderInfoInstance: orderInfoInstance] << [params: params];
            reInfo = reInfo1;
        }
        //reInfo<<[orderInfoInstance:orderInfoInstance]<<[params:params];
        println("reInfo--->" + reInfo);
        render(view: webUtilService.getView("orderShow"), model: reInfo);
    }
    //进入点菜界面
    def doDish() {
        //查询订单信息
        def reInfo = staffOrderService.orderInfo(params);
        if (reInfo.recode == ReCode.OK) {
            //查询菜品
            params<<[enabled:true];
            def reInfo1=searchService.searchFood(params);
            reInfo<<reInfo1;
            //查询菜品分类
            reInfo1=searchService.searchFoodClass();
            reInfo<<reInfo1;

            //查询已有点菜
            reInfo1= staffOrderService.dishList([orderId:reInfo.orderInfoInstance?.id,max:1000]);
            reInfo<<[dishes:reInfo1];
        }
        reInfo<< [params: params];
        render(view: webUtilService.getView("doDish"), model: reInfo);
    }
    //完成点菜
    def completeDish() {
        def reInfo = staffOrderService.completeDish(params);
        println("reInfo-->" + reInfo);
        //render(reInfo);
        if (reInfo.recode == ReCode.OK) {
            flash.message = reInfo.recode.label;
        } else {
            flash.errors = reInfo.recode.label;
        }
        if (params.backUrl) {
            redirect(url: params.backUrl);
            return;
        }
        redirect(action: "orderShow", params: [orderId: params.orderId]);
    }

    //点菜
    def addDishes() {
        def reInfo = staffOrderService.addDishes(params);
        println("reInfo-->" + reInfo);
        if (reInfo.recode == ReCode.OK) {
            flash.message = reInfo.recode.label;
        } else {
            flash.errors = reInfo.recode.label;
        }
        redirect(action: "doDish", params: [orderId: params.orderId]);
    }
    //点菜取消
    def cancelDish() {
        def orderId = params.orderId;
        params.remove("orderId");
        def reInfo = staffOrderService.cancelDish(params);
        println("reInfo-->" + reInfo);
        if (reInfo.recode == ReCode.OK) {
            flash.message = reInfo.recode.label;
        } else {
            flash.errors = reInfo.recode.label;
        }
        if (params.backUrl) {
            redirect(url: params.backUrl);
            return;
        }
        redirect(action: "orderShow", params: [orderId: orderId]);
    }
    //点菜列表
    def dishList() { //厨师显示所有状态是1有效性是1的点菜
        //标注过期订单
        orderAndReserveService.markExpire();

        params.statusGe = 1;
        params.statusLe = 2;
        params.valid = 1;
//        params.date=new SimpleDateFormat("yyyy-MM-dd").format(new Date());//加上当天日期条件
//        println("params.date-->"+params.date);
        def reInfo = staffOrderService.dishList(params);
        println("reInfo-->" + reInfo);
        render(view: webUtilService.getView("dishList"), model: reInfo);
    }
    //点菜删除
    def delDish() {
        def orderId = params.orderId;
        params.remove("orderId");
        def reInfo = staffOrderService.delDish(params);
        println("reInfo-->" + reInfo);
        if (reInfo.recode == ReCode.OK) {
            flash.message = reInfo.recode.label;
        } else {
            flash.errors = reInfo.recode.label;
        }
        if (params.backUrl) {
            redirect(url: params.backUrl);
            return;
        }
        redirect(action: "orderShow", params: [orderId: orderId]);
    }

    //结账
    def settleAccounts() {
        flash.message = null;
        flash.errors = null;
        def reInfo = null;
        if (request.method == "GET") {
            reInfo = staffOrderService.castAccounts(params);
            if (reInfo.warning) {
                flash.warning = reInfo.warning;
            }
            println("reInfo-->" + reInfo);
            render(view: webUtilService.getView("settleAccounts"), model: reInfo);
        }
        if (request.method == "POST") {//提交算账
            reInfo = staffOrderService.submitCastAccounts(params);
            println("reInfo-->" + reInfo);
            if (reInfo.recode == ReCode.OK) { //结账成功
                flash.message = reInfo.recode.label;
                if (params.backUrl) {
                    redirect(url: params.backUrl);
                    return;
                }
                redirect(action: "orderShow", params: [orderId: params.orderId]);
                return;
            } else {
                flash.errors = reInfo.recode.label;
            }
            render(view: webUtilService.getView("settleAccounts"), model: reInfo);
            return;
        }


    }
    //确认点菜完成
    def completeAffirmDish() {
        params.statusCode = OrderStatus.VERIFY_ORDERED_STATUS.code;//更新订单的状态为确认点菜完成
        def orderId = params.orderId;
        def reInfo = staffOrderService.orderStatusUpdate(params);
        println("reInfo-->" + reInfo);
        if (reInfo.recode == ReCode.OK) {
            flash.message = reInfo.recode.label;
        } else {
            flash.errors = reInfo.recode.label;
        }
        if (params.backUrl) {
            redirect(url: params.backUrl);
            return;
        }
        if (params.backUrl) {
            redirect(url: params.backUrl);
            return;
        }
        redirect(action: "orderShow", params: [orderId: orderId]);
    }
    //确认订单有效性
    def affirmValid() {
        def reInfo = staffOrderService.orderValidAffirm(params);
        println("reInfo-->" + reInfo);
        //render(reInfo);
        if (reInfo.recode == ReCode.OK) {
            flash.message = reInfo.recode.label;
        } else {
            flash.errors = reInfo.recode.label;
        }
        if (params.backUrl) {
            redirect(url: params.backUrl);
            return;
        }
        redirect(action: "orderList");
    }
    //确认点菜
    def affirmDish() {
        def orderId = params.orderId;
        params.remove("orderId");
        def reInfo = staffOrderService.dishConfirm(params);
        println("reInfo-->" + reInfo);
        if (reInfo.recode == ReCode.OK) {
            flash.message = reInfo.recode.label;
        } else {
            flash.errors = reInfo.recode.label;
        }
        if (params.backUrl) {
            redirect(url: params.backUrl);
            return;
        }
        redirect(action: "orderShow", params: [orderId: orderId]);
    }
    //开始做菜
    def beginCook() {
        params.statusCode = DishesStatus.COOKING_ORDERED_STATUS.code;//更新点菜的状态为做菜中
        def orderId = params.orderId;
        params.remove("orderId");
        def reInfo = staffOrderService.dishStatusUpdate(params);
        println("reInfo-->" + reInfo);
        if (reInfo.recode == ReCode.OK) {
            flash.message = reInfo.recode.label;
        } else {
            flash.errors = reInfo.recode.label;
        }
        if (params.backUrl) {
            redirect(url: params.backUrl);
            return;
        }
        redirect(action: "orderShow", params: [orderId: orderId]);
    }
    //完成做菜
    def completeCook() {
        params.statusCode = DishesStatus.COOKED_STATUS.code;//更新点菜的状态为做菜完成
        def orderId = params.orderId;
        params.remove("orderId");
        def reInfo = staffOrderService.dishStatusUpdate(params);
        println("reInfo-->" + reInfo);
        if (reInfo.recode == ReCode.OK) {
            flash.message = reInfo.recode.label;
        } else {
            flash.errors = reInfo.recode.label;
        }
        if (params.backUrl) {
            redirect(url: params.backUrl);
            return;
        }
        redirect(action: "orderShow", params: [orderId: orderId]);
    }
    //完成上菜,对于每个点菜的完成上菜
    def completeServe() {
        params.statusCode = DishesStatus.SERVED_STATUS.code;//更新点菜的状态为上菜完成
        def orderId = params.orderId;
        params.remove("orderId");
        def reInfo = staffOrderService.dishStatusUpdate(params);
        println("reInfo-->" + reInfo);
        if (reInfo.recode == ReCode.OK) {
            flash.message = reInfo.recode.label;
        } else {
            flash.errors = reInfo.recode.label;
        }
        if (params.backUrl) {
            redirect(url: params.backUrl);
            return;
        }
        redirect(action: "orderShow", params: [orderId: orderId]);
    }
//    //顾客到店
//    def customerReach(){
//        def reInfo=staffOrderService.customerReach(params);
//        println("reInfo-->"+reInfo);
//
//        //render(reInfo);
//        if(reInfo.recode==ReCode.OK){
//            flash.message=reInfo.recode.label;
//        }
//        else{
//            flash.error=reInfo.recode.label;
//        }
//        if(params.backUrl){
//            redirect(url: params.backUrl);
//            return ;
//        }
//        redirect(action: "orderList");
//    }

    //完成上菜,对于每个点菜的完成上菜
    def completePackage() {
        params.statusCode = DishesStatus.SERVED_STATUS.code;//更新点菜的状态为上菜完成
        def orderId = params.orderId;
        //params.remove("orderId");
        def reInfo = staffOrderService.dishStatusUpdate(params);
        println("reInfo-->" + reInfo);
        if (reInfo.recode == ReCode.OK) {
            params.statusCode = OrderStatus.SERVED_STATUS.code;//更新订单的状态为上菜完成/打包完成
            reInfo = staffOrderService.orderStatusUpdate(params);
            println("reInfo-->" + reInfo);
            if (reInfo.recode == ReCode.OK) {
                flash.message = reInfo.recode.label;
            } else {
                flash.errors = reInfo.recode.label;
            }
        } else {
            flash.errors = reInfo.recode.label;
        }
        if (params.backUrl) {
            redirect(url: params.backUrl);
            return;
        }
        redirect(action: "orderShow", params: [orderId: orderId]);
    }

    //完成上菜,对于每个点菜的完成上菜
    def beginShip() {
        params.statusCode = OrderStatus.SHIPPING_STATUS.code;//更新订单的状态为运送中
        def reInfo = staffOrderService.orderStatusUpdate(params);
        println("reInfo-->" + reInfo);
        if (reInfo.recode == ReCode.OK) {
            flash.message = reInfo.recode.label;
        } else {
            flash.errors = reInfo.recode.label;
        }
        if (params.backUrl) {
            redirect(url: params.backUrl);
            return;
        }
        redirect(action: "orderShow", params: [orderId: params.orderId]);
    }


    def exportOrderList() {

        params.locale = request.locale
        params.reportName = "orderList"
        generateResponse(jasperReportService.exportOrderList(params), "订单列表")
    }

    //输出流
    def generateResponse = { reportDef, expName ->
        def name = URLEncoder.encode(expName, "UTF-8")
        if (name.length() > 150) {
            name = new String(expName.getBytes("UTF-8"), "GB2312");
        }

        if (!reportDef.fileFormat.inline && !reportDef.parameters._inline) {
            response.setHeader("Content-disposition", "attachment; filename=" + (name ?: (reportDef.name)) + "." + reportDef.fileFormat.extension);
            response.contentType = reportDef.fileFormat.mimeTyp
            response.characterEncoding = "UTF-8"
            response.outputStream << reportDef.contentStream.toByteArray()
        } else {
            render(text: reportDef.contentStream, contentType: reportDef.fileFormat.mimeTyp, encoding: 'UTF-8');
        }
    }

//    //创建外卖订单
//    def makeTakeOutOrder(){
//        //查询菜谱
//        StaffInfo staffInfo=webUtilService.getStaff();
//        if(staffInfo){
//            if(request.method=="POST"){//提交数据创建订单
//                //转换地址
//                commonService.transformAddress(params);
//
//                def reInfo=staffCartService.makeOrderFromCart(params);
//                if(reInfo.recode==ReCode.OK){
//                    redirect(action: "orderShow",params: [orderId:reInfo.orderInfo?.id?:0]);
//                    return;
//                } else if (reInfo.recode==ReCode.SAVE_FAILED){
//                    flash.error=reInfo.errorstr;
//                }else{
//                    flash.error=reInfo.recode.label;
//                }
//            }
//            def paramsT=[restaurantId:staffInfo.restaurantId,canTakeOut:true]
//            def reInfo=searchService.searchFood(paramsT);
//            def provinceList=[];
//            provinceList.add([id:0,province:"请选择"]);
//            def provinceListTemp=areaParamService.getProvinceList().provinceList;
//            if(provinceListTemp){
//                provinceListTemp.each {
//                    provinceList.add([id:it.id,province:it.province]);
//                }
//            }
//            reInfo<<[provinces:provinceList]<<[params:params];
//            println("reInfo-->"+reInfo);
//            render(view: "makeTakeOutOrder",model: reInfo);
//        }else{
//             render("staff not login");
//        }
//    }



    //加菜
    def addDishAfterOrderConfirmView() {
        def errors = null;
        def msgs = null;
        def reInfo = staffOrderService.dishListAfterOrderConfirm(params);
        if (reInfo.recode != ReCode.OK) {
            if (reInfo.recode == ReCode.SAVE_FAILED) {
                errors = reInfo.errors;
            } else {
                errors = reInfo.recode.label;
            }
            render(view: webUtilService.getView("addDishAfterOrderConfirm"), model: [errors: errors, msgs: msgs]);
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
        render(view: webUtilService.getView("addDishAfterOrderConfirm"), model: reInfo);
    }

    //加菜确认
    def orderConfirmAfterOrderConfirm() {
        def errors = null;
        def msgs = null;
        def reInfo = staffOrderService.orderConfirmAfterOrderConfirm(params);
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
            redirect(action: "orderShow",params: [orderId: params.orderId]);
            return;
        }
    }
}
