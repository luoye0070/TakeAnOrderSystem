package lj.enumCustom

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-6-24
 * Time: 下午11:42
 * To change this template use File | Settings | File Templates.
 */
public enum OrderType {
    NORMAL(0,"正常到店订单"),
    RESERVE(1,"预定订单"),
    TAKE_OUT(2,"外卖订单");

    public int code
    public String label

    OrderType(int code,String label){
        this.code=code
        this.label=label
    }

    public static def getCodeList(){
        return [
                NORMAL.code,
                RESERVE.code,
                TAKE_OUT.code
        ];
    }

    public static OrderType[] orderTypes=[NORMAL,RESERVE,TAKE_OUT];

}