package lj.common

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-8-11
 * Time: 下午8:07
 * To change this template use File | Settings | File Templates.
 */
class DesUtilGy {
    public final static String SECRET_KEY="MErhzSsJ";//需要的密钥

    public static String getSecretKey(String encryptKey){
        if(encryptKey==null){
            return SECRET_KEY;
        }
        if(encryptKey.length()>8){
            encryptKey=encryptKey.substring(0,8);
        }
        else{
            encryptKey+=SECRET_KEY.substring(0,8-encryptKey.length());
        }
        println(encryptKey);
        return encryptKey;
    }
    //加密
    public static String encryptDES(String encryptString, String encryptKey) throws Exception{
        return DESUtil.encryptDES(encryptString,getSecretKey(encryptKey));
    }
    //解密
    public static String decryptDES(String decryptString, String decryptKey) throws Exception{
        return DESUtil.decryptDES(decryptString,getSecretKey(decryptKey));
    }
}
