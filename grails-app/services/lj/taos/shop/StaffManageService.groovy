package lj.taos.shop

import lj.I18nError
import lj.common.DesUtilGy
import lj.data.StaffInfo
import lj.data.StaffPositionInfo
import lj.enumCustom.PositionType
import lj.enumCustom.ReCode

class StaffManageService {
    def webUtilService;
    def searchService;
    def shopService;
    def g = new org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib();

    def serviceMethod() {

    }

    /***************
     * 保存工作人员信息
     *
     * params是传入的参数
     * 参数格式为：
     * id 工作人员id，非必须，如果id传入了的话，下面的参数都是非必须的
     * loginName 登录用户名，必须
     * name 工作人员名称，非必须
     * passWord 登录密码，必须
     * isOnline 是否在线，非必须 ,默认不在线
     *
     * 返回值
     * [recode: ReCode.SHOP_NOT_ENABLED];店铺是关闭状态，不能编辑菜单
     * [recode: ReCode.SHOP_WAIT_VERIFY];饭店正在审核中，不能编辑菜单
     * [recode: ReCode.SHOP_VERIFY_NOT_PASS];饭店审核没有通过
     * [recode: ReCode.NOT_REGISTER_RESTAURANT];用户没有注册店铺
     * [recode: ReCode.SAVE_FAILED,errors:staffInfo.errors.allErrors];保存工作人员信息失败
     * [recode:ReCode.NOT_LOGIN];用户没有登录
     * [recode: ReCode.OK];工作人员信息保存成功
     * **********************/
    def save(def params) {
        def session = webUtilService.getSession();
        //SimpleDateFormat sdfDate=new SimpleDateFormat("yyyy-MM-dd");
        //SimpleDateFormat sdfTime=new SimpleDateFormat("HH:mm:ss");

        //取参数
        Long id = lj.Number.toLong(params.id);
        def positionTypes = params.positionTypes;
        if (id == 0 && positionTypes == null) {// 如果是新增，必须设置职位
            return [recode: ReCode.NO_POSITION];
        }

        //检查店铺可用性
        def reInfo = shopService.getShopEnabled();
        if (reInfo.recode != ReCode.OK) {
            return reInfo;
        }

        if (id == 0) {
            //检查同店铺是否已经有相同的登录用户名
            StaffInfo staffInfo1 = StaffInfo.findByLoginName(params.loginName);
            if (staffInfo1) {//已经添加过相同登录名的工作人员
                return [recode: ReCode.EXIST_THE_SAME_STAFF];
            }
        } else {
            if (params.passWord == "") {
                params.remove("passWord");
            }
        }

        StaffInfo staffInfo = null;
        if (id) {
            staffInfo = StaffInfo.get(id);
        }
        if (staffInfo) {
            staffInfo.setProperties(params);
        } else {
            staffInfo = new StaffInfo(params);
        }
        def positionFullList = [];
        if (positionTypes instanceof String) {
            int positionType = lj.Number.toInteger(positionTypes);
            positionFullList.add([code: positionType, label: PositionType.getPositionType(positionType)]);
        } else if (positionTypes instanceof String[]) {
            for (int i = 0; i < positionTypes.length; i++) {
                int positionType = lj.Number.toInteger(positionTypes[i]);
                positionFullList.add([code: positionType, label: PositionType.getPositionType(positionType)]);
            }
        }

        if (staffInfo.save(flush: true)) {
            if (positionTypes) {//设置职位
                //删除原先的
                StaffPositionInfo.executeUpdate("delete from StaffPositionInfo where staffInfo.id=" + staffInfo.id);
                //保存新的职位
                if (positionTypes instanceof String) {
                    int positionType = lj.Number.toInteger(positionTypes);
                    StaffPositionInfo staffPositionInfo = new StaffPositionInfo();
                    staffPositionInfo.staffInfo = staffInfo;
                    staffPositionInfo.positionType = positionType;
                    staffPositionInfo.save(flush: true);
                } else if (positionTypes instanceof String[]) {
                    for (int i = 0; i < positionTypes.length; i++) {
                        int positionType = lj.Number.toInteger(positionTypes[i]);
                        StaffPositionInfo staffPositionInfo = new StaffPositionInfo();
                        staffPositionInfo.staffInfo = staffInfo;
                        staffPositionInfo.positionType = positionType;
                        staffPositionInfo.save(flush: true);
                    }
                }
            }
            return [recode: ReCode.OK, staffInfo: [staffInfo: staffInfo, positionFullList: positionFullList]];
        } else
            return [recode: ReCode.SAVE_FAILED, staffInfo: [staffInfo: staffInfo, positionFullList: positionFullList], errors: I18nError.getMessage(g, staffInfo.errors.allErrors)];

    }

    /***************
     * 工作人员删除
     *
     * params是传入的参数
     * 参数格式为：
     * ids 工作人员id数组或单个id，必须，ids格式例如：12或者[1,23,...]
     *
     * 返回值
     * [recode: ReCode.SHOP_NOT_ENABLED];店铺是关闭状态，不能编辑菜单
     * [recode: ReCode.SHOP_WAIT_VERIFY];饭店正在审核中，不能编辑菜单
     * [recode: ReCode.SHOP_VERIFY_NOT_PASS];饭店审核没有通过
     * [recode: ReCode.NOT_REGISTER_RESTAURANT];用户没有注册店铺
     * [recode:ReCode.NOT_LOGIN];用户没有登录
     * [recode: ReCode.OK];删除工作人员成功
     * **********************/
    def delete(def params) {
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
            Long id = lj.Number.toLong(params.ids); //工作人员id
            //删除职位
            StaffPositionInfo.executeUpdate("delete from StaffPositionInfo where staffInfo.id=" + id);
            //删除工作人员
            StaffInfo.executeUpdate("delete from StaffInfo where id=" + id);
        } else if (params.ids instanceof String[]) {//传入id数组
            //删除职位
            String sql_s = "delete from StaffPositionInfo where staffInfo.id in (0";
            for (int i = 0; i < params.ids.length; i++) {
                Long id = lj.Number.toLong(params.ids[i]);
                sql_s += "," + id;
            }
            sql_s += ")";
            StaffInfo.executeUpdate(sql_s);
            //删除工作人员
            sql_s = "delete from StaffInfo where id in (0";
            for (int i = 0; i < params.ids.length; i++) {
                Long id = lj.Number.toLong(params.ids[i]);
                sql_s += "," + id;
            }
            sql_s += ")";
            StaffInfo.executeUpdate(sql_s);
        }

        return [recode: ReCode.OK];

    }

    /***************
     * 工作人员查找
     *
     * params是传入的参数
     * 参数格式为：
     * id 工作人员id
     *
     * 返回值
     * [recode: ReCode.SHOP_NOT_ENABLED];店铺是关闭状态，不能编辑菜单
     * [recode: ReCode.SHOP_WAIT_VERIFY];饭店正在审核中，不能编辑菜单
     * [recode: ReCode.SHOP_VERIFY_NOT_PASS];饭店审核没有通过
     * [recode: ReCode.NOT_REGISTER_RESTAURANT];用户没有注册店铺
     * [recode:ReCode.NOT_LOGIN];用户没有登录
     * [recode: ReCode.OK,staffCount:staffCount,staffFullList:staffFullList];查询工作人员成功 ，其中staffFull为[staffInfo:it,positionFullList:positionFullList];
     * **********************/
    def search(def params) {
        def session = webUtilService.getSession();
        //SimpleDateFormat sdfDate=new SimpleDateFormat("yyyy-MM-dd");
        //SimpleDateFormat sdfTime=new SimpleDateFormat("HH:mm:ss");

        //取参数
        Long id = lj.Number.toLong(params.id); //工作人员ID

        //检查店铺可用性
        def reInfo = shopService.getShopEnabled();
        if (reInfo.recode != ReCode.OK) {
            return reInfo;
        }

        //查询条件
        def condition = {
            if (id) {
                eq("id", id);//根据ID查询
            }
            if (params.loginName) {
                eq("loginName", params.loginName);
            }
        }

        def staffList = StaffInfo.createCriteria().list(params, condition);
        def staffCount = StaffInfo.createCriteria().count(condition);

        //查询职位列表
        if (staffList) {
            def staffFullList = staffList.collect {
                def positionList = StaffPositionInfo.findAllByStaffInfo(it);
                def positionFullList = positionList.collect {
                    [code: it.positionType, label: PositionType.getPositionType(it.positionType).label];
                }
                [staffInfo: it, positionFullList: positionFullList];
            }

            //返回结果
            return [recode: ReCode.OK, totalCount: staffCount, staffFullList: staffFullList];

        } else {
            return [recode: ReCode.NO_RESULT];
        }

    }

    //根据ID获取一个工作人员信息
    def getStaffInfo(def params) {
        def reInfo = search(params);
        if (reInfo.recode == ReCode.OK) {
            if (reInfo.staffFullList?.size() > 0) {
                return [recode: ReCode.OK, staffInfo: reInfo.staffFullList.get(0)];
            } else {
                return [recode: ReCode.NO_RESULT];
            }
        } else {
            return reInfo;
        }
    }

    //登录
    def staffLogin(def params) {
        //获取参数
        long restaurantId = lj.Number.toLong(params.restaurantId);//饭店ＩＤ
        String loginName = params.loginName;//登录名
        String passWord = params.passWord;//密码

        StaffInfo staffInfo = StaffInfo.findByLoginName(loginName);
        if (staffInfo) {
            passWord = DesUtilGy.encryptDES(passWord, loginName);
            if (staffInfo.passWord == passWord) {
                webUtilService.setStaff(staffInfo);
                //设置工作人员在线状态为在线
                staffInfo.isOnline = true;
                if (!staffInfo.save(flush: true)) {
                    println("更新工作人员在线状态为在线错误，请查证：");
                    staffInfo.errors.allErrors.each {
                        println(it);
                    }
                }
                return [recode: ReCode.OK] << getStaffWithPosition([id: staffInfo.id]);
            } else {
                return [recode: ReCode.PASSWORD_INCORRECT];
            }
        } else {
            return [recode: ReCode.STAFF_LOGINNAME_NOT_EXIST];
        }
    }
    //根据ID获取一个工作人员信息
    def getStaffWithPosition(def params) {
        long id = lj.Number.toLong(params.id);
        StaffInfo staffInfo = StaffInfo.findById(id);
        if (staffInfo) {
            def positionList = StaffPositionInfo.findAllByStaffInfo(staffInfo);
            return [recode: ReCode.OK, staffInfo: staffInfo, positionList: positionList];
        } else {
            return [recode: ReCode.NO_RESULT];
        }
    }
}
