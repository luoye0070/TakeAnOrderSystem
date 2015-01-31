package lj.taos.shop

import lj.I18nError
import lj.data.RestaurantInfo
import lj.enumCustom.ReCode

import java.text.SimpleDateFormat

/**
 * ShopService
 * <p>店铺相关的服务</p>
 * @author 刘兆国
 * @version 1.0
 */
class ShopService {
    def webUtilService;

    def g = new org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib();
    /***************
     * 创建店铺
     *
     * 返回值
     * [recode: ReCode.SAVE_FAILED,errors:restaurantInfo.errors.allErrors];保存数据失败
     * [recode: ReCode.OK];创建店铺成功
     * **********************/
    def createShop(def params) {

        def session = webUtilService.getSession();
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");

        //初始参数处理
        String shopHoursBeginTimeStr = params.shopHoursBeginTime; //营业时间起,HH:mm:ss
        try { params.shopHoursBeginTime = sdfTime.parse(shopHoursBeginTimeStr); } catch (Exception ex) { ex.printStackTrace(); }
        String shopHoursEndTimeStr = params.shopHoursEndTime; //营业时间止,HH:mm:ss
        try { params.shopHoursEndTime = sdfTime.parse(shopHoursEndTimeStr); } catch (Exception ex) { ex.printStackTrace(); }
        long imageSpaceSize=0;
        try {imageSpaceSize=lj.Number.toLong(params.imageSpaceSize);}catch (Exception ex) { ex.printStackTrace(); }
        params.imageSpaceSize=imageSpaceSize;
        //手机号正确性验证

        RestaurantInfo restaurantInfo = new RestaurantInfo(params);
        if (restaurantInfo.save(flush: true)) {
            return [recode: ReCode.OK, restaurantInfo: restaurantInfo];
        } else
            return [recode: ReCode.SAVE_FAILED, restaurantInfo: restaurantInfo, errors: I18nError.getMessage(g,restaurantInfo.errors.allErrors)];
    }

    //店铺信息查询
    def getShopInfo() {
        def session = webUtilService.getSession();
        //查找店铺信息
        RestaurantInfo restaurantInfo = RestaurantInfo.last();
        if (restaurantInfo) {
            return [recode: ReCode.OK, restaurantInfo: restaurantInfo];
        } else {//还没有注册饭店
            return [recode: ReCode.NOT_CREATE_RESTAURANT];
        }
    }

    //店铺属性信息设置
    def setShopInfo(def params) {
        def session = webUtilService.getSession();
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
        if (params.shopHoursBeginTime) {
            String shopHoursBeginTimeStr = params.shopHoursBeginTime; //营业时间起,HH:mm:ss
            try { params.shopHoursBeginTime = sdfTime.parse(shopHoursBeginTimeStr); } catch (Exception ex) { ex.printStackTrace(); }
        }
        if (params.shopHoursEndTime) {
            String shopHoursEndTimeStr = params.shopHoursEndTime; //营业时间止,HH:mm:ss
            try { params.shopHoursEndTime = sdfTime.parse(shopHoursEndTimeStr); } catch (Exception ex) { ex.printStackTrace(); }
        }
        long imageSpaceSize=0;
        try {imageSpaceSize=lj.Number.toLong(params.imageSpaceSize);}catch (Exception ex) { ex.printStackTrace(); }
        params.imageSpaceSize=imageSpaceSize;
        //手机号正确性验证

        //查找店铺信息
        RestaurantInfo restaurantInfo = RestaurantInfo.last();

        if (restaurantInfo) {
            //设置属性值
            restaurantInfo.setProperties(params);
            //保存数据
            if (restaurantInfo.save(flush: true))
                return [recode: ReCode.OK, restaurantInfo: restaurantInfo];
            else
                return [recode: ReCode.SAVE_FAILED, restaurantInfo: restaurantInfo, errors: I18nError.getMessage(g,restaurantInfo.errors.allErrors)];
        } else {//还没有注册饭店
            return [recode: ReCode.NOT_CREATE_RESTAURANT];
        }
    }

    //获取店铺的可用性
    def getShopEnabled() {
        def session = webUtilService.getSession();
            //初始化一些参数
            RestaurantInfo restaurantInfo = RestaurantInfo.last();
            if (restaurantInfo) {
                if (restaurantInfo.enabled == false) {//店铺关闭了
                    return [recode: ReCode.SHOP_NOT_ENABLED];
                }
                return [recode: ReCode.OK, restaurantInfo: restaurantInfo];
            } else {//还没有注册店铺
                return [recode: ReCode.NOT_CREATE_RESTAURANT];
            }
    }
}
