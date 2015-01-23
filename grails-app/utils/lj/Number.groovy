/**
 *Title:        数字处理函数库
 *Description:  其它类型转换到数字等功能
 *@author:      杨飞
 *@create:      2011-12-2
 *@edit:
 */

package lj

class Number {

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
    /**********
     * 创建一个数据记录的编号num，由时间加4位随机数组成
     * ***********/
    public static Long makeNum(){
        Date date=new Date();
        long time = new Date().getTime() ;
        StringBuffer temp=new StringBuffer("");
        for(int i=0;i<4;i++){
            temp.append(new Random().nextInt(10).toString());
        }
        String timestr=time;
        String numStr = timestr+temp.toString();
        long num = numStr.toLong();
        return num;
    }
}


