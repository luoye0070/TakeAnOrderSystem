package lj.data

import lj.common.DesUtilGy

//工作人员
class StaffInfo {
    //登录用户名
    String loginName
    //名称
    String name
    //登录密码
    String passWord
    //是否在线
    Boolean isOnline=false;

    static constraints = {
        loginName(nullable: false,blank:false,maxSize:32);
        name(nullable: true, blank:true,maxSize:32);
        passWord(nullable: false,blank:false,maxSize:128);
        isOnline(nullable: false,blank:false)
    }

    def beforeInsert() {
        encodePassword()
    }

    def beforeUpdate() {
        if (isDirty('passWord')) {
            encodePassword()
        }
    }
    protected void encodePassword() {
        passWord =DesUtilGy.encryptDES(passWord,loginName);
    }
}
