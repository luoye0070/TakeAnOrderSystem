package lj.common

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 15-2-4
 * Time: 下午11:21
 * To change this template use File | Settings | File Templates.
 */
class UniqueCode {
    /**
     * @param identifyNumber 标示号
     * @param digit 位数,最小值1
     * */
    public static String getUniqueCode(final long identifyNumber,final int digit,int codeType){
        int idlength=digit/2;
        if(digit<=1){
            idlength=1;
        }
        if(digit>38){
            idlength=19;//长整型最大值是19位
        }

        long identifyNumberTemp=identifyNumber;
        if(idlength<19){//长整型最大值是19位，所以只有标示数的位数小于19则去掉高位
            //去掉高位
            String s_divisor1="1";
            for(int n=0;n<idlength;n++){
                s_divisor1+="0";
            }
            long l_divisor1=Long.parseLong(s_divisor1);
            long result1=identifyNumber/l_divisor1;
            identifyNumberTemp=identifyNumber-l_divisor1*result1;
        }

        //高位补零
        String s_identify="";
        for(int i=(idlength-1);i>=0;i--){
             String s_divisor="1";
             for(int n=0;n<i;n++){
                 s_divisor+="0";
             }
             long l_divisor=Long.parseLong(s_divisor);
             long result=identifyNumberTemp/l_divisor;
             s_identify+=result;
            identifyNumberTemp-= l_divisor*result;
        }
        int balanceDigit=digit-idlength;
        String randomCode=ValidationCode.getAuthCodeStr(balanceDigit,codeType);

        //拼接串,随机数和标示数交替拼入这个唯一标示串
        StringBuffer temp=new StringBuffer("");
        int i=0;
        for(i=0;i<balanceDigit;i++){
            temp.append(randomCode.substring(i,i+1));
            if(s_identify.length()>i){
                temp.append(s_identify.substring(i,i+1));
            }
        }
        if(i<s_identify.length()){
            temp.append(s_identify.substring(i,s_identify.length()));
        }

        return temp.toString();
    }
    public static String getUniqueCode(final long identifyNumber,final int digit){
       return getUniqueCode(identifyNumber,digit,ValidationCode.NUMBER)
    }
}
