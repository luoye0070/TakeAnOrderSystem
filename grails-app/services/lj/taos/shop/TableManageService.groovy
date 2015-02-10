package lj.taos.shop

import lj.I18nError
import lj.common.UniqueCode
import lj.data.TableInfo
import lj.enumCustom.ReCode

class TableManageService {
    def webUtilService;
    def shopService;
    def g = new org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib();

    def serviceMethod() {

    }

    /***************
     * 保存桌位信息
     *
     * params是传入的参数
     * 参数格式为：
     * id 桌位id，非必须，如果id传入了的话，下面的参数都是非必须的
     * name 桌名，必须
     * minPeople 最少人数，必须
     * maxPeople 最多人数，必须
     * canMultiOrder 是否多单共桌，非必须 ,默认不支持
     * canReserve 是否支持预订，非必须，默认是支持
     * description 详细描述，非必须
     * enabled 是否有效，非必须，默认有效
     *
     * 返回值
     * [recode: ReCode.SHOP_NOT_ENABLED];店铺是关闭状态，不能编辑菜单
     * [recode: ReCode.SHOP_WAIT_VERIFY];饭店正在审核中，不能编辑菜单
     * [recode: ReCode.SHOP_VERIFY_NOT_PASS];饭店审核没有通过
     * [recode: ReCode.NOT_REGISTER_RESTAURANT];用户没有注册店铺
     * [recode: ReCode.SAVE_FAILED,errors:foodInfo.errors.allErrors];保存餐桌信息失败
     * [recode:ReCode.NOT_LOGIN];用户没有登录
     * [recode: ReCode.OK];餐桌信息保存成功
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
            throw new RuntimeException(reInfo.recode.label);
        }

        TableInfo tableInfo = null;
        if (id) {
            tableInfo = TableInfo.get(id);
        }
        if (tableInfo) {
            tableInfo.setProperties(params);
        } else {
            tableInfo = new TableInfo(params);
        }

        if (!tableInfo.save(flush: true)) {
            throw new RuntimeException(I18nError.getMessage(g,tableInfo.errors.allErrors,0));
        }

        if(id==0){//新加桌位设置一个code
            String code=UniqueCode.getUniqueCode(tableInfo.id,32);//设置一个这么长的code是为了防止顾客随意输入
            tableInfo.code=code;
            if (!tableInfo.save(flush: true)) {
                throw new RuntimeException(I18nError.getMessage(g,tableInfo.errors.allErrors,0));
            }
        }


        return [recode: ReCode.OK, tableInfo: tableInfo];
    }

    //桌位查询
    def getTableInfo(def params) {
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
        TableInfo tableInfo = TableInfo.findById(id);
        if (tableInfo)
            return [recode: ReCode.OK, tableInfo: tableInfo];
        else
            return [recode: ReCode.NO_RESULT];
        //return searchService.searchTable(params);
    }

    //查询菜单信息
    def tableList(def params) {
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
        //查询条件
        def condition = {
            if (params.tableId) {
                eq("id", lj.Number.toLong(params.tableId));
            }
            if (params.peopleCount) { //就餐人数
                ge("maxPeople", lj.Number.toInteger(params.peopleCount));
                le("minPeople", lj.Number.toInteger(params.peopleCount));
            }
            if (params.canMultiOrder != null) {//是否支持多人共桌
                eq("canMultiOrder", params.canMultiOrder);
            }
            if (params.canReserve != null) { //是否支持预定
                eq("canReserve", params.canReserve);
            }
            if (params.enabled != null) { //是否有效
                eq("enabled", params.enabled);
            }
        }

        def tableList = TableInfo.createCriteria().list(params, condition);
        def totalCount = TableInfo.createCriteria().count(condition);

        return [recode: ReCode.OK, tableList: tableList, totalCount: totalCount, params: params];
    }

    /***************
     * 桌位删除
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
     * [recode: ReCode.OK];删除桌位成功
     * **********************/
    def deleteTable(def params) {
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
            TableInfo.executeUpdate("delete from TableInfo where id=" + id);
        } else if (params.ids instanceof String[]) {//传入id数组
            String sql_s = "delete from TableInfo where id in (0";
            for (int i = 0; i < params.ids.length; i++) {
                Long id = lj.Number.toLong(params.ids[i]);
                sql_s += "," + id;
            }
            sql_s += ")";
            TableInfo.executeUpdate(sql_s);
        }

        return [recode: ReCode.OK];
    }

}
