package lj.taos.shop

import lj.I18nError
import lj.data.FoodInfo
import lj.enumCustom.ReCode

class FoodManageService {

    def webUtilService;
    def searchService;
    def shopService;
    def g = new org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib();

    def serviceMethod() {

    }

    /***************
     * 保存菜单
     *
     * params是传入的参数
     * 参数格式为：
     * id 菜单id，非必须，如果id传入了的话，下面的参数都是非必须的
     * name 菜名，必须
     * price 价格，必须
     * originalPrice 原价，非必须
     * image 图片地址，非必须
     * canTakeOut 是否支持外卖，非必须，默认是不支持
     * description 菜单详细描述，非必须
     * enabled 菜单是否有效，非必须，默认有效
     * countLimit 每天限量，非必须，默认为0表示不限量
     * isSetMeal 是否是套餐，非必须，默认不是套餐
     * followFoodIds 套餐从属菜单数组，例如：12或[23,22,...]
     * sellCount 销量，必须没有
     *
     * 返回值
     * [recode: ReCode.SHOP_NOT_ENABLED];店铺是关闭状态，不能编辑菜单
     * [recode: ReCode.SHOP_WAIT_VERIFY];饭店正在审核中，不能编辑菜单
     * [recode: ReCode.SHOP_VERIFY_NOT_PASS];饭店审核没有通过
     * [recode: ReCode.NOT_REGISTER_RESTAURANT];用户没有注册店铺
     * [recode: ReCode.SAVE_FAILED,errors:foodInfo.errors.allErrors];保存菜单信息失败
     * [recode:ReCode.NOT_LOGIN];用户没有登录
     * [recode: ReCode.OK];菜单信息保存成功
     * **********************/
    def save(def params) {
        def session = webUtilService.getSession();
        //SimpleDateFormat sdfDate=new SimpleDateFormat("yyyy-MM-dd");
        //SimpleDateFormat sdfTime=new SimpleDateFormat("HH:mm:ss");
        //取参数
        Long id = lj.Number.toLong(params.id);

        //检查店铺可用性
        def reInfo = shopService.getShopEnabled();
        if (reInfo.recode != ReCode.OK) {
            return reInfo;
        }

        FoodInfo foodInfo = null;
        if (id) {
            foodInfo = FoodInfo.get(id);
        }
        if (foodInfo) {
            foodInfo.setProperties(params);
        } else {
            foodInfo = new FoodInfo(params);
        }
        if(params.get("foodClassInfo.id")=="0"){
            foodInfo.foodClassInfo=null;
        }
        if (foodInfo.save(flush: true)) {
            return [recode: ReCode.OK, foodInfo: foodInfo];
        } else
            return [recode: ReCode.SAVE_FAILED, foodInfo: foodInfo, errors: I18nError.getMessage(g, foodInfo.errors.allErrors)];
    }

    //查询菜单信息
    def getFoodInfo(def params) {
        def session = webUtilService.getSession();
        //SimpleDateFormat sdfDate=new SimpleDateFormat("yyyy-MM-dd");
        //SimpleDateFormat sdfTime=new SimpleDateFormat("HH:mm:ss");
        //取参数
        //Long id=lj.Number.toLong(params.id);

        //检查店铺可用性
        def reInfo = shopService.getShopEnabled();
        if (reInfo.recode != ReCode.OK) {
            return reInfo;
        }
        long id = lj.Number.toLong(params.id);
        FoodInfo foodInfo = FoodInfo.findById(id);
        if (foodInfo)
            return [recode: ReCode.OK, foodInfo: foodInfo];
        else
            return [recode: ReCode.NO_RESULT];

    }

    //查询菜单信息
    def foodList(def params) {
        def session = webUtilService.getSession();
        //SimpleDateFormat sdfDate=new SimpleDateFormat("yyyy-MM-dd");
        //SimpleDateFormat sdfTime=new SimpleDateFormat("HH:mm:ss");
        //取参数
        //Long id=lj.Number.toLong(params.id);

        //检查店铺可用性
        def reInfo = shopService.getShopEnabled();
        if (reInfo.recode != ReCode.OK) {
            return reInfo;
        }
        return searchService.searchFood(params);

    }

    /***************
     * 菜单删除
     *
     * params是传入的参数
     * 参数格式为：
     * ids 菜单id数组或单个id，必须，ids格式例如：12或者[1,23,...]
     *
     * 返回值
     * [recode: ReCode.SHOP_NOT_ENABLED];店铺是关闭状态，不能编辑菜单
     * [recode: ReCode.SHOP_WAIT_VERIFY];饭店正在审核中，不能编辑菜单
     * [recode: ReCode.SHOP_VERIFY_NOT_PASS];饭店审核没有通过
     * [recode: ReCode.NOT_REGISTER_RESTAURANT];用户没有注册店铺
     * [recode:ReCode.NOT_LOGIN];用户没有登录
     * [recode: ReCode.OK];删除菜单成功
     * **********************/
    def deleteFoodInfo(def params) {
        def session = webUtilService.getSession();
        //SimpleDateFormat sdfDate=new SimpleDateFormat("yyyy-MM-dd");
        //SimpleDateFormat sdfTime=new SimpleDateFormat("HH:mm:ss");
        //检查店铺可用性
        def reInfo = shopService.getShopEnabled();
        if (reInfo.recode != ReCode.OK) {
            return reInfo;
        }

        //根据ID删除记录
        if (params.ids instanceof String) { //传入id
            Long id = lj.Number.toLong(params.ids); //菜单id
            FoodInfo.executeUpdate("delete from FoodInfo where id=" + id);
        } else if (params.ids instanceof String[]) {//传入id数组
            String sql_s = "delete from FoodInfo where id in (0";
            for (int i = 0; i < params.ids.length; i++) {
                Long id = lj.Number.toLong(params.ids[i]);
                sql_s += "," + id;
            }
            sql_s += ")";
            FoodInfo.executeUpdate(sql_s);
        }

        return [recode: ReCode.OK];

    }
}
