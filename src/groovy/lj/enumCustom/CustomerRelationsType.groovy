package lj.enumCustom

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-8-6
 * Time: 上午10:58
 * To change this template use File | Settings | File Templates.
 */
public enum CustomerRelationsType {
    COMMON_CUSTOMER(0,"普通客户"),//普通客户
    BLACKLIST_CUSTOMER(-1,"黑名单客户"),     //黑名单客户
    VIP_CUSTOMER(1,"VIP客户"),     //VIP客户
    NOT_KNOW(-100,"未知的预定类型")

    public Integer code;
    public String label;
    CustomerRelationsType(Integer code,String label){
        this.code=code
        this.label=label
    }

    public static CustomerRelationsType getCustomerRelationsType(int code){
        switch (code){
            case 0:
                return COMMON_CUSTOMER;
            case -1:
                return BLACKLIST_CUSTOMER;
            case 1:
                return VIP_CUSTOMER;
            default:
                return NOT_KNOW;
        }
    }

    public static CustomerRelationsType [] customerRelationsTypes=[COMMON_CUSTOMER,BLACKLIST_CUSTOMER,VIP_CUSTOMER];
}