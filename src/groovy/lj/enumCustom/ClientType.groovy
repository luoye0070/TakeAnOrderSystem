package lj.enumCustom

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-7-4
 * Time: 上午1:02
 * To change this template use File | Settings | File Templates.
 */
public enum ClientType {
    PHONE(0,"手持设备"),
    WEB_PAGE(1,"网站网页"),
    USER_LOGIN(2,"用户登录"),
    WEIXIN(3,"微信");

    public int code
    public String label

    ClientType(int code,String label){
        this.code=code
        this.label=label
    }

    public static def getCodeList(){
        return [
                PHONE.code,
                WEB_PAGE.code,
                USER_LOGIN.code,
                WEIXIN.code
        ];
    }
}