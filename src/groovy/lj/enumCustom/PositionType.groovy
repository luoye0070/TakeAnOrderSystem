package lj.enumCustom

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-7-28
 * Time: 下午6:51
 * To change this template use File | Settings | File Templates.
 */
//职位类别
public enum PositionType {
    SHOPKEEPER(0,'店主'),
    WAITER(1,'服务员'),
    WAITER_HEADER(2,'服务员总管'),
    COOK(3,'厨师'),
    BAD_TYPE(4,'收银员') ,
    ORDER_TAKER(5,"送餐员"),
    CLEANER(6,'清洁工')

    public Integer code
    public String label
    PositionType(Integer code,String label){
        this.code=code
        this.label=label
    }

    public static PositionType getPositionType(int code){
        switch (code){
            case 0:
                return SHOPKEEPER;
            case 1:
                return WAITER;
            case 2:
                return WAITER_HEADER;
            case 3:
                return COOK;
            case 4:
                return BAD_TYPE;
            case 5:
                return ORDER_TAKER;
            case 6:
                return CLEANER;
            default:
                return WAITER;
        }
    }

    public static PositionType [] positionTypes=[WAITER,WAITER_HEADER,COOK,BAD_TYPE,ORDER_TAKER,CLEANER];
}