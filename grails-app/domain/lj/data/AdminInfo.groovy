package lj.data
//管理员
class AdminInfo {
    String username;//用户名
    String password;//密码
    int authority=0;//权限
    static constraints = {
        username(blank:false,maxSize:32);//用户名
        password(blank:false,maxSize:128);//密码
        authority(blank:false,min:0);//权限
    }
}
