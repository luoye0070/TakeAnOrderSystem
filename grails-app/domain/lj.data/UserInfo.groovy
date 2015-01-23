package lj.data

import lj.common.DesUtilGy

//用户信息
class UserInfo {

    //用户名
    String userName
    //密码
    String passWord
    //邮箱
    String email
    //邮箱是否通过验证
    Boolean emailEnabled
    //手机号码
    String phone
    //手机号是否通过验证
    Boolean phoneEnabled
    //最近登录时间
    Date loginTime
    //密码找回问题
    String question
    //密码找回问题回答
    String answer
    //最近登录ip
    String loginIp
    //用户有效标志，true有效，false冻结
    Boolean enabled=true

    static hasMany = [clientInfos:ClientInfo];

    static constraints = {
        userName blank:false,maxSize:32,unique:true
        passWord blank:false,maxSize:128
        email nullable:true,maxSize:64
        emailEnabled nullable:true
        phone nullable:true,maxSize:16
        phoneEnabled nullable:true
        loginTime nullable:true
        question blank:true,maxSize:64,nullable:true
        answer nullable:true,maxSize:64,blank:true
        loginIp blank:true,maxSize:32,nullable:true
        enabled()
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
        passWord =DesUtilGy.encryptDES(passWord,userName);
    }
}
