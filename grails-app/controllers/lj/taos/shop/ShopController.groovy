package lj.taos.shop

import lj.data.RestaurantInfo
import lj.data.StaffInfo
import lj.enumCustom.ReCode

//店铺相关控制器
class ShopController {
    ShopService shopService;

    def index() {
        redirect(action: "editShopInfo");
    }

    //店铺注册
    def shopCreate() {
        def errors = null;
        def msgs = null;
        def restaurantInfoInstance = new RestaurantInfo();
        def staffInfo=new StaffInfo();
        if (request.method == "POST") {//提交注册信息
            restaurantInfoInstance = new RestaurantInfo(params);
            staffInfo=new StaffInfo(params);
            staffInfo.name=params.staffName;
            try {
                def recode = shopService.createShop(params);//创建店铺
                println(recode);
                if (recode.recode == ReCode.OK) {//创建店铺成功,跳转到店铺信息显示页面
                    flash.message = "创建店铺成功";
                    redirect(action: "editShopInfo")
                    return
                } else {//创建店铺失败,显示错误信息
                    restaurantInfoInstance = recode.restaurantInfo;
                    errors = recode.errors;
                    println(errors);
                }
            } catch (Exception ex) {
                errors = ex.getMessage();
            }
        }
        render(view: "shopCreate", model: [restaurantInfoInstance: restaurantInfoInstance,staffInfo:staffInfo, errors: errors, msgs: msgs]);
    }

    //店铺基本信息设置
    def editShopInfo() {
        def errors = null;
        def msgs = null;
        def restaurantInfoInstance = null;
        if (request.method == "GET") {
            def reInfo = shopService.getShopInfo();
            if (reInfo.recode == ReCode.OK) {
                restaurantInfoInstance = reInfo.restaurantInfo;
            } else {
                redirect(action: "shopCreate");
                return;
            }
        } else if (request.method == "POST") {
            def reInfo = shopService.setShopInfo(params);
            if (reInfo.recode == ReCode.OK) {
                msgs = "保存成功";
            } else {
                errors = reInfo.errors;
            }
            restaurantInfoInstance = reInfo.restaurantInfo;
        }
        render(view: "editShopInfo", model: [restaurantInfoInstance: restaurantInfoInstance, errors: errors, msgs: msgs]);
    }
}
