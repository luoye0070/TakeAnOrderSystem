package lj.common

/**
 * 注册工具类
 * <p>注册时判断用户名、邮箱、手机号是否合法</p>
 * @author 赵建明
 * @version: 1.0
 * @Date: 13-10-12
 * @Time: 上午10:05
 */
class StrCheckUtil {

    /**
     * 验证字符串长度是否在合法区间内
     * @author
     * @param str 字符串
     * @param minLen:int 最小长度
     * @param maxLen:int 最大长度
     * @return boolean
     * @Date: 13-10-12
     * @Time: 上午11:26
     */
    public static boolean chkStrLen(String str, int minLen, int maxLen) {
        boolean flag = false;
        int strLen = StringUtil.getStrByteLen(str);
        if (strLen >= minLen && strLen <= maxLen) {
            flag = true;
        }
        return flag;
    }

    /**
     * 验证字符格式是否正确
     * @author
     * @param str 字符串
     * @param type 类型
     * @return boolean
     * @Date: 13-10-12
     * @Time: 下午17:01
     */
    public static boolean chkStrFormat(String str,String type) {      //输入数据正则判断
        def regex = /^[\u4e00-\u9fa5|A-Za-z]+\w*$/;
        if (type=='name')    {regex = /^[\u4e00-\u9fa5|A-Za-z|0-9]+\w*$/};
        if (type=='pwd')     {regex = /^[A-Za-z|\w]{6,20}$/};
        if (type=='email')   {regex = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/};
        if (type=='passport'){regex = /^\d{15}|\d{18}$/};
        if (type=='mobile')  {regex = /^((13)|(14)|(15)|(18))\d{9}$/};
        if (type=='money')   {regex = /^[1-9]\d*(\.\d{1,2})?$)|(^[0]{1}(\.\d{1,2})?$/};
        if (type=="phone")   {regex = /(^((13)|(14)|(15)|(18))\d{9}$)|(^(\d{3,4}|\d{3,4}(-)?)?\d{7,8}$)/};
        return (str =~ regex);
    }
}
