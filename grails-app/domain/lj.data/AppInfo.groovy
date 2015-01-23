package lj.data

class AppInfo {
    String appName;//应用名称
    String appKey;//应用key
    String appSecret;//秘钥
    String appDescription;//应用描述
    static constraints = {
        appName(nullable: false,blank: false,maxSize: 32);
        appKey(nullable: false,blank: false,maxSize: 16);
        appSecret(nullable: false,blank: false,maxSize: 16);
        appDescription(nullable: false,blank: false,maxSize: 256);
    }
}
