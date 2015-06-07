package lj.taos.show;

import lj.data.FoodInfo
import lj.data.RestaurantInfo
import lj.enumCustom.ReCode
import lj.enumCustom.VerifyStatus

class InfoShowController {
    def shopService;
    def searchService;
    def webUtilService;

    def index() {}

    //店铺展示
    def shopShow() {
        def errors = null;
        def msgs = null;
        RestaurantInfo restaurantInfo = null;
        def reInfo = shopService.getShopEnabled();
        if (reInfo.recode == ReCode.OK) {
            restaurantInfo=reInfo.restaurantInfo;
            render(view: "shopShow", model: [restaurantInfo: restaurantInfo]);
        } else
            render(view: "/error", model: [errors: reInfo.recode.label]);
    }
    //菜谱展示
    def foodShow() {
        def errors = null;
        def msgs = null;
        FoodInfo foodInfo = null;
        RestaurantInfo restaurantInfo = null;
        def foodList = null;
        //查询菜单
        params.enabled = true;
        if (lj.Number.toLong(params.id) == 0) {
            params.foodId = -1;
        } else {
            params.foodId = params.id;
        }
        def reInfo = searchService.searchFood(params);
        if (reInfo.recode == ReCode.OK) {
            if (reInfo.foodList && reInfo.foodList.size() > 0) {
                foodInfo = reInfo.foodList.get(0);
                //查询饭店
                reInfo = shopService.getShopEnabled();
                if (reInfo.recode == ReCode.OK) {
                    restaurantInfo = reInfo.restaurantInfo;
                    //查询出饭店中5个菜
                    def paramsT = [:];
                    paramsT.enabled = true;
                    paramsT.max = 5;
                    reInfo = searchService.searchFood(paramsT);
                    if (reInfo.recode == ReCode.OK) {
                        foodList = reInfo.foodList;
                    } else {
                        foodInfo = null;
                    }
                }
            }
        }
        if (foodInfo)
            render(view: webUtilService.getView("foodShow"), model: [foodInfo: foodInfo, restaurantInfo: restaurantInfo, foodList: foodList]);
        else
            render(view: "/error", model: [errors: "菜谱不存在或已经下架了"]);
    }
}
