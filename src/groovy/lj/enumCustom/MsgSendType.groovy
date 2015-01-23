package lj.enumCustom

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-8-3
 * Time: 下午1:14
 * To change this template use File | Settings | File Templates.
 */
public enum MsgSendType {
    STAFF_TO_CUSTOMER(0,"工作人员到顾客"),
    CUSTOMER_TO_STAFF(1,"顾客到工作人员"),
    STAFF_TO_STAFF(2,"工作人员到工作人员"),
    CUSTOMER_TO_CUSTOMER(3,"顾客到顾客")

    public Integer code
    public String label

    MsgSendType(Integer code,String label){
    this.code=code
    this.label=label
    }

    public static def getCodeList(){
        return [
                STAFF_TO_CUSTOMER.code,
                CUSTOMER_TO_STAFF.code,
                STAFF_TO_STAFF.code,
                CUSTOMER_TO_CUSTOMER.code
        ];
    }
}