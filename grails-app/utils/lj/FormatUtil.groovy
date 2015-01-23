package lj

import java.text.SimpleDateFormat

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-11-2
 * Time: 下午4:04
 * To change this template use File | Settings | File Templates.
 */
class FormatUtil {
    //格式化时间
    public static String timeFormat(Date time){
        String timeFormat="00:00:00";
        try {
            SimpleDateFormat sdfTime=new SimpleDateFormat("HH:mm:ss");
            timeFormat=sdfTime.format(time);
        }
        catch (Exception ex){}
        return timeFormat;
    }
    //格式化日期
    public static String dateFormat(Date date){
        String dateFormat="1971-01-01";
        try {
            SimpleDateFormat sdfDate=new SimpleDateFormat("yyyy-MM-dd");
            dateFormat=sdfDate.format(date);
        }
        catch (Exception ex){}
        return dateFormat;
    }
    //格式化日期时间
    public static String dateTimeFormat(Date dateTime){
        String dateTimeFormat="1971-01-01 00:00:00";
        try {
            SimpleDateFormat sdfDate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dateTimeFormat=sdfDate.format(dateTime);
        }
        catch (Exception ex){}
        return dateTimeFormat;
    }
    //布尔类型格式化
    public static String boolFormat(boolean bool){
        if(bool)
            return "是";
        else
            return "否";
    }
    //byte数量转成KB
    public static String byteToKB(long byteCount){
        return ((long)(byteCount/1024))+"KB";
    }
    //byte数量转成MB
    public static String byteToMB(long byteCount){
        return ((long)(byteCount/(1024*1024)))+"MB";
    }
    //byte数量转成GB
    public static String byteToGB(long byteCount){
        return ((long)(byteCount/(1024*1024*1024)))+"GB";
    }
}
