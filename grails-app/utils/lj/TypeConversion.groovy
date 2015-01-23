package lj

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-11-7
 * Time: 下午11:03
 * To change this template use File | Settings | File Templates.
 */
class TypeConversion {
    // 任意类型转整形
    public static Integer toInteger(def n) {
        if (n) {
            String x = "${n}".trim()
            return x.isInteger() ? x.toInteger() : 0
        } else
            return 0
    }

    // 任意类型转长整形
    public static Long toLong(def n) {
        if (n) {
            String x = "${n}".trim()
            return x.isLong() ? x.toLong() : 0
        } else
            return 0
    }
    //任意类型转换为double
    public static Double toDouble(def n) {
        if (n) {
            String x = "${n}".trim()
            return x.isDouble() ? x.toDouble() : 0
        } else
            return 0
    }
    //转换为布尔型
    public static boolean toBoolean(def bool){
        if(bool==false){
            return false;
        }
        else if(bool=="true"){
            return true;
        }
        else if(bool=="false"){
            return false;
        }
        else if(bool==0){
            return false;
        }
        else if(bool==null){
            return false;
        }
        else {
            return true;
        }
    }
}
