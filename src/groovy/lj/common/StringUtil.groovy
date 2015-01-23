package lj.common

/**
 * 字符串处理工具类
 * @author
 * @version: 1.0
 * @Date: 13-10-12
 * @Time: 上午11:10
 */
class StringUtil {

    /**
     * 获取字符串占几个字节
     * @author
     * @param str 字符串
     * @return int
     * @Date: 13-10-12
     * @Time: 下午13:02
     */
    public static int getStrByteLen(String str) {
        return str.getBytes("GBK").length;
    }
}
