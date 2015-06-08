package lj.taos.shop

import grails.converters.JSON
import lj.common.DESUtil
import lj.util.WebUtilService

class StaffManageAjaxController {
    StaffManageService staffManageService;
    WebUtilService webUtilService;
    def index() {}
    //工作人员登录
    def staffLogin(){
        String USER_SECRET_KEY="MErhzSsJ";//用户中心需要的密钥
        String originalPassWord=params.passWord?:"";
        String passWord=DESUtil.decryptDES(params.passWord,USER_SECRET_KEY);
        println("passWord-->"+passWord);
        params.passWord=passWord;
        def reInfo=staffManageService.staffLogin(params)<<[originalPassWord:originalPassWord];
        println("reInfo-->"+reInfo);
        render(reInfo as JSON);
    }
}
